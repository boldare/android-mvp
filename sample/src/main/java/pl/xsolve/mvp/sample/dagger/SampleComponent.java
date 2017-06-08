package pl.xsolve.mvp.sample.dagger;

import dagger.Component;
import pl.xsolve.mvp.dagger.MvpActivityComponent;
import pl.xsolve.mvp.dagger.MvpActivityModule;
import pl.xsolve.mvp.dagger.MvpComponent;
import pl.xsolve.mvp.dagger.scope.ScreenScope;
import pl.xsolve.mvp.sample.SampleActivity;

@ScreenScope
@Component(
        dependencies = {
                MvpComponent.class
        },
        modules = {
                MvpActivityModule.class
        }
)
public interface SampleComponent extends MvpActivityComponent {
    void inject(SampleActivity activity);
}
