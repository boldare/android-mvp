package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

class MvpBinderWriter extends AbstractWriter<Stream<MvpClassData>> {
    private static final String BINDER_CLASS_NAME = "MvpBinder$StaticBindings";
    private static final String BINDER_PACKAGE_NAME = "pl.xsolve.mvp";

    MvpBinderWriter(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    protected String getPackage(Stream<MvpClassData> mvpClassDataStream) {
        return BINDER_PACKAGE_NAME;
    }

    @Override
    protected TypeSpec getTypeSpec(Stream<MvpClassData> mvpClassDataStream) {
        CodeBlock bindingCode = generateBindingCode(mvpClassDataStream);

        return generateTypeSpec(bindingCode);
    }

    private CodeBlock generateBindingCode(Stream<MvpClassData> mvpClassDataStream) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        mvpClassDataStream.forEach(
                mvpClassData -> generateBindingFor(mvpClassData, codeBlockBuilder)
        );
        return codeBlockBuilder
                .build();
    }

    private void generateBindingFor(MvpClassData mvpClassData, CodeBlock.Builder codeBlockBuilder) {
        ClassName activity = ClassName.bestGuess(mvpClassData.getPackageName() + "." + mvpClassData.getActivityClassName());

        ClassName activityBinder = ClassName.get(mvpClassData.getPackageName(), mvpClassData.getBinderClassName());
        codeBlockBuilder.addStatement("" +
                        "MvpBinder.addBinder(\n" +
                        "  $T.class,\n" +
                        "  new $T())",
                activity,
                activityBinder
        );
    }

    private TypeSpec generateTypeSpec(CodeBlock bindingCode) {
        return TypeSpec.classBuilder(BINDER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(bindingCode)
                .build();
    }
}
