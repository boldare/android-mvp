package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class MvpBinderWriter {
    public static final String BINDER_CLASS_NAME = "MvpBinder$StaticBindings";
    public static final String BINDER_PACKAGE_NAME = "pl.xsolve.mvp";
    private final ProcessingEnvironment processingEnvironment;

    public MvpBinderWriter(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public void write(Stream<MvpClassData> mvpClassData) {
        CodeBlock bindingCode = generateBindingCode(mvpClassData);

        TypeSpec typeSpec = generateTypeSpec(bindingCode);

        JavaFile javaFile = generateJavaFile(typeSpec);

        writeFile(javaFile);
    }

    private CodeBlock generateBindingCode(Stream<MvpClassData> mvpClassDataStream) {
        CodeBlock.Builder builder = CodeBlock.builder();
        mvpClassDataStream.forEach(
                mvpClassData -> {
                    ClassName activity = ClassName.bestGuess(mvpClassData.getPackageName() + "." + mvpClassData.getActivityClassName());

                    ClassName activityBinder = ClassName.get(mvpClassData.getPackageName(), mvpClassData.getBinderClassName());
                    builder.addStatement("" +
                                    "MvpBinder.addBinder(\n" +
                                    "  $T.class,\n" +
                                    "  new $T())",
                            activity,
                            activityBinder
                    );
                }
        );
        return builder
                .build();
    }

    private TypeSpec generateTypeSpec(CodeBlock bindingCode) {
        return TypeSpec.classBuilder(BINDER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(bindingCode)
                .build();
    }

    private JavaFile generateJavaFile(TypeSpec typeSpec) {
        return JavaFile.builder(BINDER_PACKAGE_NAME, typeSpec)
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
