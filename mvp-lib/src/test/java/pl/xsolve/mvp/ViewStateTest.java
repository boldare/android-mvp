package pl.xsolve.mvp;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ViewStateTest {
    private TestViewState systemUnderTest;

    @Before
    public void setUp() throws Exception {
        systemUnderTest = new TestViewState();
    }

    @Test
    public void shouldAllowToSetView() throws Exception {
        systemUnderTest.setView(mock(TestViewInterface.class));
    }

    @Test
    public void shouldAllowToStartViewState() throws Exception {
        systemUnderTest.setView(mock(TestViewInterface.class));
        systemUnderTest.onStart(mock(Activity.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowToStartViewStateWithoutView() throws Exception {
        systemUnderTest.onStart(mock(Activity.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowToStartViewStateAfterRemovingView() throws Exception {
        systemUnderTest.setView(mock(TestViewInterface.class));
        systemUnderTest.removeView();
        systemUnderTest.onStart(mock(Activity.class));
    }

    private interface TestViewInterface {
    }

    private static class TestViewState extends ViewState<TestViewInterface> {
    }

}