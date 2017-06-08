package pl.xsolve.mvp.compiler;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class MvpProcessorTest {
    private JavaFileObject dummySource;
    private JavaFileObject boringSource;
    private JavaFileObject dummyGeneratedSource;
    private JavaFileObject boringGeneratedResult;
    private JavaFileObject bindingsGeneratedResult;

    @Test
    public void generateFile() throws Exception {
        simulateSourceFilesExist();

        prepareExpectedResults();

        assertAbout(javaSources())
                .that(getSourceFiles())
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(new MvpProcessor())
                .compilesWithoutWarnings()
                .and()
                .generatesSources(
                        dummyGeneratedSource,
                        boringGeneratedResult,
                        bindingsGeneratedResult);
    }

    private Iterable<? extends JavaFileObject> getSourceFiles() {
        return Arrays.asList(dummySource, boringSource);
    }

    private void simulateSourceFilesExist() {
        dummySource = forSourceString("test.dummy.DummyActivity", "" +
                "package test.dummy;\n" +
                "import pl.xsolve.mvp.MvpActivity;\n" +
                "import pl.xsolve.mvp.Presenter;\n" +
                "import pl.xsolve.mvp.ViewState;\n" +
                "import pl.xsolve.mvp.api.MvpPresenter;\n" +
                "import pl.xsolve.mvp.api.MvpViewState;\n" +
                "public abstract class DummyActivity extends MvpActivity{\n" +
                "\n" +
                "    @MvpViewState\n" +
                "    DummyViewState dummyViewState;\n" +
                "    @MvpPresenter( viewState = \"dummyViewState\" )\n" +
                "    DummyPresenter dummyPresenter;\n" +
                "\n" +
                "    @MvpViewState\n" +
                "    SillyViewState sillyViewState;\n" +
                "    @MvpPresenter( viewState = \"sillyViewState\" )\n" +
                "    SillyPresenter sillyPresenter;\n" +
                "\n" +
                "    public static class DummyPresenter extends Presenter<DummyView>{}\n" +
                "    public static class DummyViewState extends ViewState<DummyView> implements DummyView{}\n" +
                "    public interface DummyView{}\n" +
                "\n" +
                "    public static class SillyPresenter extends Presenter<SillyView>{}\n" +
                "    public static class SillyViewState extends ViewState<SillyView> implements SillyView{}\n" +
                "    public interface SillyView{}\n" +
                "}"
        );

        boringSource = forSourceString("test.boring.EnclosingClass", "" +
                "package test.boring;\n" +
                "import pl.xsolve.mvp.MvpActivity;\n" +
                "import pl.xsolve.mvp.Presenter;\n" +
                "import pl.xsolve.mvp.ViewState;\n" +
                "import pl.xsolve.mvp.api.MvpPresenter;\n" +
                "import pl.xsolve.mvp.api.MvpViewState;\n" +
                "public class EnclosingClass{\n" +
                "    public static abstract class BoringActivity extends MvpActivity{\n" +
                "\n" +
                "        @MvpViewState\n" +
                "        BoringViewState boringViewState;\n" +
                "        @MvpPresenter( viewState = \"boringViewState\" )\n" +
                "        BoringPresenter boringPresenter;\n" +
                "    }\n" +
                "\n" +
                "    public static class BoringPresenter extends Presenter<BoringView>{}\n" +
                "    public static class BoringViewState extends ViewState<BoringView> implements BoringView{}\n" +
                "    public interface BoringView{}\n" +
                "}"
        );
    }

    private void prepareExpectedResults() {
        dummyGeneratedSource = forSourceString("test.dummy.DummyActivity$MvpActivityBinder", "" +
                "package test.dummy;\n" +
                "import java.lang.Override;\n" +
                "import pl.xsolve.mvp.MvpActivity;\n" +
                "import pl.xsolve.mvp.MvpBinder;\n" +
                "public class DummyActivity$MvpActivityBinder extends MvpBinder.ActivityBinder{\n" +
                "    @Override\n" +
                "    public void bind(MvpActivity mvpActivity) {\n" +
                "        DummyActivity activity = (DummyActivity) mvpActivity;\n" +
                "        getController(activity)\n" +
                "                .managePresenter(activity.dummyPresenter, DummyActivity.DummyView.class)\n" +
                "                .withViewState(activity.dummyViewState);\n" +
                "        getController(activity)\n" +
                "                .managePresenter(activity.sillyPresenter, DummyActivity.SillyView.class)\n" +
                "                .withViewState(activity.sillyViewState);\n" +
                "    }\n" +
                "}"
        );

        boringGeneratedResult = forSourceString("test.boring.EnclosingClass$BoringActivity$MvpActivityBinder", "" +
                "package test.boring;\n" +
                "import java.lang.Override;\n" +
                "import pl.xsolve.mvp.MvpActivity;\n" +
                "import pl.xsolve.mvp.MvpBinder;\n" +
                "public class EnclosingClass$BoringActivity$MvpActivityBinder extends MvpBinder.ActivityBinder{\n" +
                "    @Override\n" +
                "    public void bind(MvpActivity mvpActivity) {\n" +
                "        EnclosingClass.BoringActivity activity = (EnclosingClass.BoringActivity) mvpActivity;\n" +
                "        getController(activity)\n" +
                "                .managePresenter(activity.boringPresenter, EnclosingClass.BoringView.class)\n" +
                "                .withViewState(activity.boringViewState);\n" +
                "    }\n" +
                "}"
        );

        bindingsGeneratedResult = forSourceString("pl.xsolve.mvp.MvpBinder$StaticBindings", "" +
                "package pl.xsolve.mvp;\n" +
                "\n" +
                "import test.boring.EnclosingClass;\n" +
                "import test.boring.EnclosingClass$BoringActivity$MvpActivityBinder;\n" +
                "import test.dummy.DummyActivity;\n" +
                "import test.dummy.DummyActivity$MvpActivityBinder;\n" +
                "\n" +
                "public class MvpBinder$StaticBindings {\n" +
                "    static {\n" +
                "        MvpBinder.\n" +
                "                addBinder(EnclosingClass.BoringActivity.class,\n" +
                "                        new EnclosingClass$BoringActivity$MvpActivityBinder());\n" +
                "        MvpBinder.\n" +
                "                addBinder(DummyActivity.class,\n" +
                "                        new DummyActivity$MvpActivityBinder());\n" +
                "    }\n" +
                "}"
        );
    }
}
