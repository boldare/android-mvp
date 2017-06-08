package pl.xsolve.mvp.dagger;

import dagger.Component;
import pl.xsolve.mvp.MvpActivity;
import pl.xsolve.mvp.dagger.scope.ScreenScope;

@ScreenScope
@Component(
        dependencies = {
                MvpComponent.class
        },
        modules = {
                MvpActivityModule.class
        }
)
public interface MvpActivityComponent {
    void inject(MvpActivity activity);
}
