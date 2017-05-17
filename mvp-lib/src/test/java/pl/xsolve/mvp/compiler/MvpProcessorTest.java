package pl.xsolve.mvp.compiler;


import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class MvpProcessorTest {
    @Test
    public void generateFile() throws Exception {

        JavaFileObject source = JavaFileObjects.forSourceString("test.DummyActivity", "" +
                "package test;\n" +
                "import pl.xsolve.mvp.api.MvpPresenter;\n" +
                "import pl.xsolve.mvp.api.MvpViewState;\n" +
                "public class DummyActivity {\n" +
                "    @MvpViewState\n" +
                "    DummyViewState dummyViewState;\n" +
                "    @MvpPresenter( viewState = \"dummyViewState\" )\n" +
                "    DummyPresenter dummyPresenter;" +
                "    public static class DummyPresenter{}" +
                "    public static class DummyViewState{}\n" +
                "}"
        );

        JavaFileObject bindingSource = JavaFileObjects.forSourceString("test.DummyActivity$MvpActivityBinder", "" +
                "package test;\n" +
                "import java.lang.Override\n" +
                "import pl.xsolve.mvp.BaseActivity\n" +
                "import pl.xsolve.mvp.MvpBinder;\n" +
                "public class DummyActivity$MvpActivityBinder extends MvpBinder.ActivityBinder{" +
                "  @Override\n" +
                "  public void bind(BaseActivity activity) {\n" +
                "  }\n" +
                "}"
        );
        assertAbout(javaSource()).that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(new MvpProcessor())
                .compilesWithoutWarnings()
                .and()
                .generatesSources(bindingSource);
    }
}