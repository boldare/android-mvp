package org.robolectric;

import android.app.Activity;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(Activity.class)
public class ShadowActivity extends org.robolectric.shadows.ShadowActivity {

  private boolean isChangingConfig;

    public void changeConfigurations() {
        isChangingConfig = true;
    }

    public void resetIsChangingConfigurations() {
        isChangingConfig = false;
    }

    @Implementation
    public boolean isChangingConfigurations() {
        return isChangingConfig;
    }
}
