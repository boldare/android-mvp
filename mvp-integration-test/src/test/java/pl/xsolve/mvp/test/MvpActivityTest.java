package pl.xsolve.mvp.test;

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.ShadowActivity;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import javax.inject.Inject;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import pl.xsolve.mvp.MvpActivity;
import pl.xsolve.mvp.Presenter;
import pl.xsolve.mvp.ViewState;
import pl.xsolve.mvp.api.MvpPresenter;
import pl.xsolve.mvp.api.MvpViewState;
import pl.xsolve.mvp.dagger.MvpActivityComponent;
import pl.xsolve.mvp.dagger.MvpActivityModule;
import pl.xsolve.mvp.dagger.MvpComponent;
import pl.xsolve.mvp.dagger.MvpComponentFactory;
import pl.xsolve.mvp.dagger.scope.ScreenScope;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = pl.xsolve.mvp_lib_test.BuildConfig.class,
        sdk = 21,
        shadows = {ShadowActivity.class}

)
public class MvpActivityTest {
    private TestMvpActivity systemUnderTest;
    private ActivityController<TestMvpActivity> activityController;

    @Inject
    TestViewState viewState;

    @Inject
    TestPresenter presenter;

    private static MockComponent sComponent;

    @Before
    public void setUp() throws Exception {
        createComponent();
        sComponent.inject(this);
        activityController = Robolectric.buildActivity(TestMvpActivity.class);
        systemUnderTest = activityController.get();
    }

    private void createComponent() {
        new MvpComponentFactory().create(RuntimeEnvironment.application);
        sComponent = DaggerMvpActivityTest_MockComponent
                .builder()
                .mvpComponent(new MvpComponentFactory().get())
                .mockModule(new MockModule(mock(TestPresenter.class), mock(TestViewState.class)))
                .build();
    }


    //region injection tests
    @Test
    public void shouldInjectPresenterAndViewState() throws Exception {
        launchActivity();

        assertThat(systemUnderTest.presenter).isSameAs(presenter);
        assertThat(systemUnderTest.viewState).isSameAs(viewState);
    }

    @Test
    public void mvpShouldBeKeptAfterConfigurationChange() throws Exception {
        launchActivity();


        MvpActivity oldActivity = systemUnderTest;
        TestPresenter oldPresenter = systemUnderTest.presenter;
        TestViewState oldView = systemUnderTest.viewState;

        changeConfiguration();

        TestMvpActivity newActivity = systemUnderTest;
        TestPresenter newPresenter = systemUnderTest.presenter;
        TestViewState newView = systemUnderTest.viewState;

        assertThat(newActivity).isNotSameAs(oldActivity);
        assertThat(newView).isSameAs(oldView);
        assertThat(newPresenter).isSameAs(oldPresenter);
    }
    //endregion

    //region lifecycle tests
    @Test
    public void shouldCallMvpLifecycleMethods() throws Exception {
        InOrder presenterVerify = inOrder(presenter);
        InOrder viewStateVerify = inOrder(viewState);

        reset(presenter);
        reset(viewState);

        launchActivity();

        presenterVerify.verify(presenter).setView(viewState);
        presenterVerify.verify(presenter).onStart();
        verifyNoMoreInteractions(presenter);

        viewStateVerify.verify(viewState).onCreate(null);
        viewStateVerify.verify(viewState).onStart(systemUnderTest);
        verifyNoMoreInteractions(viewState);

        reset(viewState);
        reset(presenter);

        finishActivity();

        viewStateVerify.verify(viewState).onStop();
        viewStateVerify.verify(viewState).removeView();
        viewStateVerify.verify(viewState).onFinish();
        verifyNoMoreInteractions(viewState);
        presenterVerify.verify(presenter).onFinish();
        verifyNoMoreInteractions(presenter);
    }

    @Test
    public void shouldCallMvpLifecycleMethodsOnConfigurationChange() throws Exception {
        InOrder inOrder = inOrder(viewState);
        ArgumentCaptor<Bundle> argumentCaptor = ArgumentCaptor.forClass(Bundle.class);

        launchActivity();

        reset(presenter);
        reset(viewState);

        changeConfiguration();

        verify(presenter).onStart();
        verify(presenter, never()).onFinish();

        inOrder.verify(viewState).onSaveInstanceState(argumentCaptor.capture());
        inOrder.verify(viewState).onStop();
        inOrder.verify(viewState).removeView();
        inOrder.verify(viewState, never()).onFinish();

        Bundle savedState = argumentCaptor.getValue();
        inOrder.verify(viewState).onStart(systemUnderTest);
        inOrder.verify(viewState).onRestoreInstanceState(savedState);
        verifyNoMoreInteractions(viewState);

        verifyNoMoreInteractions(presenter);
    }

    @Test
    public void shouldCallMvpLifecycleMethodsOnMovedToAndFromBackStack() throws Exception {
        InOrder viewStateVerify = inOrder(viewState);
        ArgumentCaptor<Bundle> argumentCaptor = ArgumentCaptor.forClass(Bundle.class);

        launchActivity();

        reset(viewState);
        reset(presenter);

        moveToAndFromBackStack();

        viewStateVerify.verify(viewState).onSaveInstanceState(argumentCaptor.capture());
        viewStateVerify.verify(viewState).onStop();

        Bundle savedState = argumentCaptor.getValue();
        viewStateVerify.verify(viewState).onStart(systemUnderTest);
        viewStateVerify.verify(viewState).onRestoreInstanceState(savedState);
        verifyNoMoreInteractions(viewState);

        verify(presenter).onStart();
        verifyNoMoreInteractions(presenter);
    }

    @Test
    public void shouldCallMvpLifecycleMethodsWhenDestroyingActivityInBackStack() throws Exception {
        InOrder inOrder = inOrder(viewState);

        launchActivity();

        reset(viewState);

        moveActivityToBackStack();

        inOrder.verify(viewState, never()).removeView();
        inOrder.verify(viewState).onSaveInstanceState(any(Bundle.class));
        inOrder.verify(viewState).onStop();
        verifyNoMoreInteractions(viewState);

        reset(presenter);
        reset(viewState);

        destroyActivity();

        inOrder.verify(viewState).removeView();
        inOrder.verify(viewState).onFinish();
        verifyNoMoreInteractions(viewState);
        verify(presenter).onFinish();
        verifyNoMoreInteractions(presenter);
    }

    @Test
    public void shouldRestartKilledMvpWithSavedState() throws Exception {
        launchActivity();

        TestMvpActivity oldActivity = systemUnderTest;
        TestPresenter oldPresenter = systemUnderTest.presenter;
        ViewState oldView = systemUnderTest.viewState;

        Bundle savedState = moveActivityToBackStack();

        destroyActivity();

        setUp();

        assertThat(systemUnderTest).isNotSameAs(oldActivity);
        assertThat(systemUnderTest.viewState).isNotSameAs(oldView);
        assertThat(systemUnderTest.presenter).isNotSameAs(oldPresenter);

        InOrder presenterVerify = inOrder(presenter);
        InOrder viewStateVerify = inOrder(viewState);

        reset(presenter);
        reset(viewState);

        launchActivityFromSavedState(savedState);

        presenterVerify.verify(presenter).setView(viewState);
        presenterVerify.verify(presenter).onStart();
        verifyNoMoreInteractions(presenter);

        viewStateVerify.verify(viewState).onCreate(savedState);
        viewStateVerify.verify(viewState).onStart(systemUnderTest);
        viewStateVerify.verify(viewState).onRestoreInstanceState(savedState);
        verifyNoMoreInteractions(viewState);
    }
    //endregion

    //region back button tests
    @Test
    public void shouldFinishActivityIfPilotDoesNotConsumeBackPress() throws Exception {
        launchActivity();
        when(presenter.onBackPressed()).thenReturn(false);
        systemUnderTest.onBackPressed();
        assertThat(systemUnderTest.isFinishing()).isTrue();
    }

    @Test
    public void shouldNotFinishActivityIfPilotDoesConsumeBackPress() throws Exception {
        launchActivity();
        when(presenter.onBackPressed()).thenReturn(true);
        systemUnderTest.onBackPressed();
        assertThat(systemUnderTest.isFinishing()).isFalse();
    }
    //endregion

    //region Test classes
    public interface TestViewInterface {
    }

    public static class TestPresenter extends Presenter<TestViewInterface> {
    }

    public static class TestViewState extends ViewState<TestViewInterface> implements TestViewInterface {
    }

    @SuppressLint("Registered")
    public static class TestMvpActivity extends MvpActivity {
        @Inject
        @MvpPresenter(viewState = "viewState")
        TestPresenter presenter;
        @Inject
        @MvpViewState
        TestViewState viewState;

        @Override
        protected MvpActivityComponent createComponent() {
            return sComponent;
        }

        @Override
        protected void inject(MvpActivityComponent component) {
            ((MockComponent) component).inject(this);
        }
    }
    //endregion

    //region Dependency Injection
    @Module
    public static class MockModule extends MvpActivityModule {
        private final TestViewState viewState;
        private final TestPresenter presenter;

        public MockModule(TestPresenter presenter, TestViewState viewState) {
            this.presenter = presenter;
            this.viewState = viewState;
        }

        @Provides
        @ScreenScope
        public TestViewState provideState() {
            return viewState;
        }

        @Provides
        @ScreenScope
        public TestPresenter providePresenter() {
            return presenter;
        }
    }

    @ScreenScope
    @Component(
            dependencies = MvpComponent.class,
            modules = MockModule.class
    )
    public interface MockComponent extends MvpActivityComponent {
        void inject(TestMvpActivity activity);

        void inject(MvpActivityTest activity);
    }
    //endregion

    //region Activity Controlling
    private void launchActivity() {
        activityController
                .create()
                .start()
                .resume();
    }

    private void changeConfiguration() {
        Bundle outState = new Bundle();
        ShadowActivity shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.changeConfigurations();
        activityController
                .pause()
                .saveInstanceState(outState)
                .stop();
        Object nci = systemUnderTest.onRetainNonConfigurationInstance();
        activityController.destroy();

        activityController = Robolectric.buildActivity(TestMvpActivity.class);
        systemUnderTest = activityController.get();
        shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.setLastNonConfigurationInstance(nci);
        activityController.create(outState).start().restoreInstanceState(outState).resume();
    }

    private void launchActivityFromSavedState(Bundle savedState) {
        activityController
                .create(savedState)
                .start()
                .restoreInstanceState(savedState)
                .resume();
    }

    private void finishActivity() {
        ShadowActivity shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        shadowActivity.finish();
        activityController
                .pause()
                .stop()
                .destroy();
    }

    private Bundle moveActivityToBackStack() {
        Bundle outState = new Bundle();
        ShadowActivity shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        shadowActivity.resetIsFinishing();
        activityController
                .pause()
                .saveInstanceState(outState)
                .stop();

        return outState;
    }

    private void moveToAndFromBackStack() {
        Bundle outState = new Bundle();
        ShadowActivity shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        activityController
                .pause()
                .saveInstanceState(outState)
                .stop()
                .start()
                .restoreInstanceState(outState);
    }

    private void destroyActivity() {
        ShadowActivity shadowActivity = Shadow.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        activityController
                .destroy();
    }
    //endregion
}
