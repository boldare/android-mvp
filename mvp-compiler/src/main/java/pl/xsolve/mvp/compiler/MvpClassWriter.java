package pl.xsolve.mvp.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

class MvpClassWriter {
    private final ProcessingEnvironment processingEnv;

    public MvpClassWriter(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
    }

    void write(MvpClassData mvpClassData) {

        MethodSpec bindMethod = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("pl.xsolve.mvp", "BaseActivity"),"activity")
                .build();
        TypeName superclass = ClassName.bestGuess(
                MvpProcessor.BINDER_SUPERCLASS_CANONICAL_NAME);
        TypeSpec typeSpec = TypeSpec.classBuilder(mvpClassData.getClassName())
                .superclass(superclass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(bindMethod)
                .build();
        JavaFile javaFile = JavaFile.builder(mvpClassData.getPackageName(), typeSpec)
                .addFileComment("Generated code from MvpProcessor. Do not modify!")
                .build();
        try { // write the file
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }
}
