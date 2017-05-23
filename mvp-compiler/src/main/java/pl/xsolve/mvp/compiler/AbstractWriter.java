package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;

abstract class AbstractWriter<T> {
    protected final ProcessingEnvironment processingEnvironment;

    AbstractWriter(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    void write(T classData) {
        JavaFile javaFile = generateJavaFile(getPackage(classData), getTypeSpec(classData));

        writeFile(javaFile);
    }

    private JavaFile generateJavaFile(String packageName, TypeSpec typeSpec) {
        return JavaFile.builder(packageName, typeSpec)
                .addFileComment("Generated code from MvpProcessor. Do not modify!")
                .build();
    }

    protected abstract String getPackage(T classData);

    protected abstract TypeSpec getTypeSpec(T classData);

    private void writeFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(processingEnvironment.getFiler());
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }
}
