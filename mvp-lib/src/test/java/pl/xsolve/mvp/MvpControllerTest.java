package pl.xsolve.mvp;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MvpControllerTest {
    @Before
    public void setUp() throws Exception {
        systemUnderTest = new MvpController();
    }

    private MvpController systemUnderTest;

    @Test
    public void shouldAllowToManagePresenter() throws Exception {
        TestPresenter somePresenter = mock(TestPresenter.class);
        systemUnderTest.managePresenter(somePresenter, TestViewInterface.class);
    }

    @Test
    public void shouldAllowToPassViewToPresenter() throws Exception {
        TestPresenter somePresenter = mock(TestPresenter.class);
        TestViewState someView = mock(TestViewState.class);
        systemUnderTest.managePresenter(somePresenter, TestViewInterface.class).withViewState(someView);

        verify(somePresenter).setView(someView);
    }

    @Test(expected = MvpController.WrongViewException.class)
    public void shouldRequireViewToExtendViewState() throws Exception {
        TestPresenter somePresenter = mock(TestPresenter.class);
        ViewState<TestViewInterface> someView = mock(ViewState.class);
        systemUnderTest.managePresenter(somePresenter, TestViewInterface.class).withViewState(someView);
    }

    @Test
    public void shouldPassLifecycleMethodsToViewStateAndPresenter() throws Exception {
        TestPresenter somePresenter = mock(TestPresenter.class);
        TestViewState someViewState = mock(TestViewState.class);
        systemUnderTest.managePresenter(somePresenter, TestViewInterface.class).withViewState(someViewState);

        Bundle bundle = mock(Bundle.class);
        systemUnderTest.onCreate(bundle);
        verify(someViewState).onCreate(bundle);

        Activity activity = mock(Activity.class);
        systemUnderTest.onStart(activity);
        verify(someViewState).onStart(activity);
        verify(somePresenter).onStart();

        systemUnderTest.onStop();
        verify(someViewState).onStop();

        bundle = mock(Bundle.class);
        systemUnderTest.onSaveInstanceState(bundle);
        verify(someViewState).onSaveInstanceState(bundle);

        bundle = mock(Bundle.class);
        systemUnderTest.onRestoreInstanceState(bundle);
        verify(someViewState).onRestoreInstanceState(bundle);

        systemUnderTest.onBackPressed();
        verify(somePresenter).onBackPressed();
    }

    @Test
    public void shouldReturnBackConsumptionResultIfNoneHasConsumed() throws Exception {

        TestPresenter firstPresenter = mock(TestPresenter.class);
        TestPresenter secondPresenter = mock(TestPresenter.class);
        TestPresenter thirdPresenter = mock(TestPresenter.class);
        when(firstPresenter.onBackPressed()).thenReturn(false);
        when(secondPresenter.onBackPressed()).thenReturn(false);
        when(thirdPresenter.onBackPressed()).thenReturn(false);

        systemUnderTest
                .managePresenter(firstPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));
        systemUnderTest
                .managePresenter(secondPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));
        systemUnderTest
                .managePresenter(thirdPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));

        boolean result = systemUnderTest.onBackPressed();

        verify(firstPresenter).onBackPressed();
        verify(secondPresenter).onBackPressed();
        verify(thirdPresenter).onBackPressed();
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnBackConsumptionResultIfAtLeaseOneHasConsumed() throws Exception {

        TestPresenter firstPresenter = mock(TestPresenter.class);
        TestPresenter secondPresenter = mock(TestPresenter.class);
        TestPresenter thirdPresenter = mock(TestPresenter.class);
        when(firstPresenter.onBackPressed()).thenReturn(false);
        when(secondPresenter.onBackPressed()).thenReturn(true);
        when(thirdPresenter.onBackPressed()).thenReturn(false);

        systemUnderTest
                .managePresenter(firstPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));
        systemUnderTest
                .managePresenter(secondPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));
        systemUnderTest
                .managePresenter(thirdPresenter, TestViewInterface.class)
                .withViewState(mock(TestViewState.class));

        boolean result = systemUnderTest.onBackPressed();

        verify(secondPresenter).onBackPressed();
        assertThat(result).isTrue();
    }

    @Test
    public void shouldRemoveViewsFromViewStates() throws Exception {
        TestPresenter somePresenter = mock(TestPresenter.class);
        TestViewState someViewState = mock(TestViewState.class);
        systemUnderTest.managePresenter(somePresenter, TestViewInterface.class).withViewState(someViewState);

        systemUnderTest.onDestroy();
        verify(someViewState).removeView();
    }

    private interface TestViewInterface {
    }

    private static class TestPresenter extends Presenter<TestViewInterface> {
    }

    private static class TestViewState extends ViewState<TestViewInterface> implements TestViewInterface {
    }

}