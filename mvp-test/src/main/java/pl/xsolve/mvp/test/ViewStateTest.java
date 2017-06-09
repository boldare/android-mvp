package pl.xsolve.mvp.test;

import android.app.Activity;
import android.os.Bundle;

import java.lang.reflect.Constructor;

import pl.xsolve.mvp.ViewState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ViewStateTest {

    public static <V, VS extends ViewState<V>> VS simulateRecreatedFromBackStack(VS viewState, V viewMock)
            throws Exception {
        Bundle savedState = simulateStartConfigChange(viewState, viewMock);

        VS newViewState = createNew(viewState);
        reset(viewMock);

        simulateFinishConfigChange(newViewState, viewMock, savedState);
        return newViewState;
    }


    public static <V> void simulateStart(ViewState<V> viewState, V viewMock) {
        viewState.onCreate(null);
        viewState.setView(viewMock);
        viewState.onStart(mock(Activity.class));
    }

    public static <V> void simulateConfigurationChange(ViewState<V> viewState, V viewMock) {
        Bundle savedState = simulateStartConfigChange(viewState, viewMock);

        simulateFinishConfigChange(viewState, viewMock, savedState);
    }

    public static <V> Bundle simulateStartConfigChange(ViewState<V> viewState, V viewMock) {
        Bundle outState = BundleMock.mockBundle();
        viewState.onSaveInstanceState(outState);
        viewState.onStop();
        viewState.removeView();
        return outState;
    }

    public static <V> void simulateFinishConfigChange(ViewState<V> viewState, V viewMock, Bundle savedState) {
        reset(viewMock);
        viewState.onCreate(savedState);
        viewState.setView(viewMock);
        viewState.onStart(mock(Activity.class));
        viewState.onRestoreInstanceState(savedState);
    }

    public static <T> T createNew(T object) throws Exception {
        Constructor<?> constructor = object.getClass().getDeclaredConstructor();
        constructor.setAccessible(true);
        return (T) constructor.newInstance();
    }

    public static <V> void verifySingleCallOnView(V view, Action action) {
        action.call();
        verifyNoMoreInteractions(view);
        reset(view);
    }


    public interface Action {
        void call();
    }
}
