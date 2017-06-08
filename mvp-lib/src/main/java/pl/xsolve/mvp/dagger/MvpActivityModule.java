package pl.xsolve.mvp.dagger;

import dagger.Module;
import dagger.Provides;
import pl.xsolve.mvp.MvpController;
import pl.xsolve.mvp.dagger.scope.ScreenScope;

@Module
public class MvpActivityModule {
    @ScreenScope
    @Provides
    public MvpController provideMvpController() {
        return new MvpController();
    }
}
