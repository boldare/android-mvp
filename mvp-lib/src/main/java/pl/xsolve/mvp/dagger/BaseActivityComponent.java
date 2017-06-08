package pl.xsolve.mvp.dagger;

import dagger.Component;
import pl.xsolve.mvp.BaseActivity;
import pl.xsolve.mvp.dagger.scope.ScreenScope;

@ScreenScope
@Component(
        dependencies = {
                BaseComponent.class
        },
        modules = {
                BaseActivityModule.class
        }
)
public interface BaseActivityComponent {
    void inject(BaseActivity activity);
}
