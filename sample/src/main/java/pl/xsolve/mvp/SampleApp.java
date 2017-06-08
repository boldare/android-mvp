package pl.xsolve.mvp;

import android.app.Application;

import pl.xsolve.mvp.dagger.MvpComponentFactory;


public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new MvpComponentFactory().create(this);
    }
}
