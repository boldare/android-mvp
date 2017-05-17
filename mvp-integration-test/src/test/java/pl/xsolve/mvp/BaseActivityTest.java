package pl.xsolve.mvp;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.ShadowActivity;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = pl.xsolve.mvp_lib_test.BuildConfig.class,
        sdk = 21,
        shadows = {ShadowActivity.class}

)
public class BaseActivityTest {
    private TestBaseActivity systemUnderTest;
    private ActivityController<TestBaseActivity> activityController;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(TestBaseActivity.class);
        systemUnderTest = activityController.get();
    }

    @Test
    public void shouldLaunchActivity() throws Exception {
        launchActivity();
    }

    @Test
    public void shouldChangeConfig() throws Exception {
        launchActivity();

        changeConfiguration();
    }

    //region Test classes
    public static class TestBaseActivity extends BaseActivity {
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
        ShadowActivity shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
        shadowActivity.changeConfigurations();
        activityController
                .pause()
                .saveInstanceState(outState)
                .stop();
        Object nci = systemUnderTest.onRetainNonConfigurationInstance();
        activityController.destroy();

        activityController = Robolectric.buildActivity(TestBaseActivity.class);
        systemUnderTest = activityController.get();
        shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
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
        ShadowActivity shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        shadowActivity.finish();
        activityController
                .pause()
                .stop()
                .destroy();
    }

    private Bundle moveActivityToBackStack() {
        Bundle outState = new Bundle();
        ShadowActivity shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
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
        ShadowActivity shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        activityController
                .pause()
                .saveInstanceState(outState)
                .stop()
                .start()
                .restoreInstanceState(outState);
    }

    private void destroyActivity() {
        ShadowActivity shadowActivity = (ShadowActivity) ShadowExtractor.extract(systemUnderTest);
        shadowActivity.resetIsChangingConfigurations();
        activityController
                .destroy();
    }
    //endregion
}