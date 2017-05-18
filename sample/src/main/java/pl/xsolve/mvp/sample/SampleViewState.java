package pl.xsolve.mvp.sample;

import android.os.Bundle;
import android.os.Debug;

import javax.inject.Inject;

import pl.xsolve.mvp.ViewState;
import pl.xsolve.mvp.dagger.scope.ScreenScope;

@ScreenScope
public class SampleViewState extends ViewState<SampleView> implements SampleView {

    private static final int STATE_INITIAL = 0;
    private static final int STATE_PROGRESS = 1;
    private static final int STATE_DATA = 2;
    private static final String STATE = "SimpleViewState.STATE";
    private static final String DATA = "SimpleViewState.DATA";

    private ActionListener listener;
    private int state = STATE_INITIAL;
    private CharSequence data;

    @Inject
    public SampleViewState() {
    }

    @Override
    public void setView(SampleView sampleView) {
        super.setView(sampleView);
        updateView();
    }

    @Override
    public void displayData(CharSequence someData) {
        state = STATE_DATA;
        data = someData;
        callOnView(() -> view.displayData(someData));
    }

    @Override
    public void displayProgress() {
        state = STATE_PROGRESS;
        callOnView(view::displayProgress);
    }

    @Override
    public void setListener(ActionListener listener) {
        this.listener = listener;
        callOnView(() -> view.setListener(listener));
    }

    private void updateView() {
        view.setListener(listener);
        switch (state) {
            case STATE_PROGRESS:
                view.displayProgress();
                return;
            case STATE_DATA:
                view.displayData(data);
                return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE, state);
        outState.putCharSequence(DATA, data);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        state = savedInstanceState.getInt(STATE,state);
        data = savedInstanceState.getCharSequence(DATA,data);
        updateView();
    }

    private void callOnView(Action action) {
        if (hasView()) {
            action.run();
        }
    }

    private interface Action {
        void run();
    }
}
