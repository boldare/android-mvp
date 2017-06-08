package pl.xsolve.mvp;

import android.app.Application;

import pl.xsolve.mvp.dagger.BaseComponentFactory;


public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new BaseComponentFactory().create(this);
    }
}
