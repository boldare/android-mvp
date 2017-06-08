package pl.xsolve.mvp.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
        }
)
public interface BaseComponent {
    Context appContext();
}
