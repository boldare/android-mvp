package pl.xsolve.mvp.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class MvpAppModule {
    private final Context context;

    public MvpAppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return context;
    }
}
