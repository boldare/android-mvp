package pl.xsolve.mvp.dagger;

import android.app.Application;

public class BaseComponentFactory {
    private static BaseComponent sBaseComponent;

    public BaseComponent create(Application app) {
        if (sBaseComponent == null) {
            sBaseComponent = DaggerBaseComponent.builder()
                    .appModule(new AppModule(app.getApplicationContext()))
                    .build();
        }
        return sBaseComponent;
    }

    public BaseComponent get() {
        if (sBaseComponent == null) {
            throw new RuntimeException("call create from application before calling get()");
        }
        return sBaseComponent;
    }
}
