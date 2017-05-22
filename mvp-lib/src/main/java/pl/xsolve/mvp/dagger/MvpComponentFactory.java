package pl.xsolve.mvp.dagger;

import android.app.Application;


public class MvpComponentFactory {
    private static MvpComponent sMvpComponent;

    public MvpComponent create(Application app) {
        if (sMvpComponent == null) {
            sMvpComponent = DaggerMvpComponent.builder()
                    .mvpAppModule(new MvpAppModule(app.getApplicationContext()))
                    .build();
        }
        return sMvpComponent;
    }

    public MvpComponent get() {
        if (sMvpComponent == null) {
            throw new RuntimeException("call create from application before calling get()");
        }
        return sMvpComponent;
    }
}
