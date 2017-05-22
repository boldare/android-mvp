package pl.xsolve.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.xsolve.mvp.dagger.MvpActivityComponent;
import pl.xsolve.mvp.dagger.MvpComponentFactory;
import pl.xsolve.mvp.dagger.DaggerMvpActivityComponent;

public abstract class MvpActivity extends AppCompatActivity {
    @Inject
    MvpController mvpController;

    private MvpActivityComponent daggerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(getComponent());
        if (!mvpController.isInitialized()) {
            initializeMvp();
            mvpController.setInitialized();
        }
        mvpController.onCreate(savedInstanceState);
    }

    /**
     * Implementing activity *must* call the inject(this) on the component to initialize
     * Dependency injection
     *
     * @param component - Dagger component returned previously in createComponent method
     */
    protected void inject(MvpActivityComponent component) {
        component.inject(this);
    }

    private MvpActivityComponent getComponent() {
        if (daggerComponent == null) {
            Object nci = getLastCustomNonConfigurationInstance();

            if (nci != null && nci instanceof MvpActivityComponent) {
                daggerComponent = (MvpActivityComponent) nci;
            } else {
                daggerComponent = createComponent();
            }
        }
        return daggerComponent;
    }

    protected MvpActivityComponent createComponent() {
        return DaggerMvpActivityComponent.builder()
                .mvpComponent(new MvpComponentFactory().get())
                .build();
    }

    private void initializeMvp() {
        MvpBinder.bindMvp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvpController.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mvpController.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvpController.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpController.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mvpController.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return daggerComponent;
    }

    @Override
    public void onBackPressed() {
        if (!mvpController.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
