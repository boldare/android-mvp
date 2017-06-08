package pl.xsolve.mvp.sample.dagger;

import dagger.Component;
import pl.xsolve.mvp.dagger.BaseActivityComponent;
import pl.xsolve.mvp.dagger.BaseActivityModule;
import pl.xsolve.mvp.dagger.BaseComponent;
import pl.xsolve.mvp.dagger.scope.ScreenScope;
import pl.xsolve.mvp.sample.SampleActivity;

@ScreenScope
@Component(
        dependencies = {
                BaseComponent.class
        },
        modules = {
                BaseActivityModule.class
        }
)
public interface SampleComponent extends BaseActivityComponent {
    void inject(SampleActivity activity);
}
