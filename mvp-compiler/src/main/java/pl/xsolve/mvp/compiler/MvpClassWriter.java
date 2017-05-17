package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

class MvpClassWriter {
    static final String BINDER_SUPERCLASS_CANONICAL_NAME = "pl.xsolve.mvp.MvpBinder.ActivityBinder";

    private final ProcessingEnvironment processingEnvironment;

    public MvpClassWriter(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    void write(MvpClassData mvpClassData) {
        MethodSpec bindMethod = generateBindMethod(mvpClassData);

        TypeName superclass = generateSuperClass();

        TypeSpec typeSpec = generateTypeSpec(mvpClassData, bindMethod, superclass);

        JavaFile javaFile = generateJavaFile(mvpClassData, typeSpec);

        writeFile(javaFile);
    }

    private MethodSpec generateBindMethod(MvpClassData mvpClassData) {
        return MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("pl.xsolve.mvp", "BaseActivity"), "baseActivity")
                .addCode(generateBindingsCodeBlock(mvpClassData))
                .build();
    }

    private CodeBlock generateBindingsCodeBlock(MvpClassData mvpClassData) {
        ClassName activity = ClassName.get(mvpClassData.getPackageName(), mvpClassData.getActivityClassName());

        CodeBlock.Builder codeBlock = CodeBlock.builder()
                .addStatement("$T activity = ($T) baseActivity", activity, activity);

        mvpClassData.getBindings().stream()
                .forEach(mvpBinding -> {

                    codeBlock.addStatement("getController(activity)\n" +
                                    "  .managePresenter(activity.$L, $T.class)\n" +
                                    "  .withViewState(activity.$L)",
                            mvpBinding.presenter.getSimpleName(),
                            mvpBinding.getViewType(),
                            mvpBinding.viewState.getSimpleName());
                });

        return codeBlock.build();
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

    private JavaFile generateJavaFile(MvpClassData mvpClassData, TypeSpec typeSpec) {
        return JavaFile.builder(mvpClassData.getPackageName(), typeSpec)
                .addFileComment("Generated code from MvpProcessor. Do not modify!")
                .build();
    }

    private void writeFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(processingEnvironment.getFiler());
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }
}
