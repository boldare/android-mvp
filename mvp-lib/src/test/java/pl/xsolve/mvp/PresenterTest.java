package pl.xsolve.mvp;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class PresenterTest {
    private TestPresenter systemUnderTest;

    @Before
    public void setUp() throws Exception {
        systemUnderTest = new TestPresenter();
    }

    @Test
    public void shouldAllowToSetView() throws Exception {
        systemUnderTest.setView(mock(TestViewInterface.class));
    }

    @Test
    public void shouldAllowToStartPresenter() throws Exception {
        systemUnderTest.setView(mock(TestViewInterface.class));
        systemUnderTest.onStart();
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowToStartPresenterWithoutView() throws Exception {
        systemUnderTest.onStart();
    }

    private interface TestViewInterface {
    }
    private static class TestPresenter extends Presenter<TestViewInterface> {}
}