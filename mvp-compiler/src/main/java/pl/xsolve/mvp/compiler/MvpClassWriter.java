package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

class MvpClassWriter extends AbstractWriter<MvpClassData> {
    private static final String BINDER_SUPERCLASS_CANONICAL_NAME = "pl.xsolve.mvp.MvpBinder.ActivityBinder";

    MvpClassWriter(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    protected String getPackage(MvpClassData mvpClassData) {
        return mvpClassData.getPackageName();
    }

    @Override
    protected TypeSpec getTypeSpec(MvpClassData mvpClassData) {
        MethodSpec bindMethod = generateBindMethod(mvpClassData);

        TypeName superclass = generateSuperClass();

        return generateTypeSpec(mvpClassData, bindMethod, superclass);
    }

    private MethodSpec generateBindMethod(MvpClassData mvpClassData) {
        return MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("pl.xsolve.mvp", "MvpActivity"), "mvpActivity")
                .addCode(generateBindingsCodeBlock(mvpClassData))
                .build();
    }

    private CodeBlock generateBindingsCodeBlock(MvpClassData mvpClassData) {
        ClassName activity = ClassName.get(mvpClassData.getPackageName(), mvpClassData.getActivityClassName());

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder()
                .addStatement("$T activity = ($T) mvpActivity", activity, activity);

        mvpClassData.getBindings()
                .stream()
                .forEach(mvpBinding -> generateBindingStatement(mvpBinding, codeBlockBuilder));

        return codeBlockBuilder.build();
    }

    private void generateBindingStatement(MvpClassData.MvpBinding mvpBinding, CodeBlock.Builder codeBlockBuilder) {
        codeBlockBuilder.addStatement("getController(activity)\n" +
                        "  .managePresenter(activity.$L, $T.class)\n" +
                        "  .withViewState(activity.$L)",
                mvpBinding.presenter.getSimpleName(),
                mvpBinding.getViewType(),
                mvpBinding.viewState.getSimpleName());
    }

    private ClassName generateSuperClass() {
        return ClassName.bestGuess(
                BINDER_SUPERCLASS_CANONICAL_NAME);
    }

    private TypeSpec generateTypeSpec(MvpClassData mvpClassData, MethodSpec bindMethod, TypeName superclass) {
        return TypeSpec.classBuilder(mvpClassData.getBinderClassName())
                .superclass(superclass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(bindMethod)
                .build();
    }
}
