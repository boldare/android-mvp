package pl.xsolve.mvp.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                MvpAppModule.class,
        }
)
public interface MvpComponent {
    Context appContext();
}
