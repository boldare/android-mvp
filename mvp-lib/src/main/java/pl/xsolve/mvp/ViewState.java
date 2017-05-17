package pl.xsolve.mvp;

import android.app.Activity;
import android.os.Bundle;

public abstract class ViewState<VIEW> {

    protected VIEW view;
    protected Activity activityContext;

    public void setView(VIEW view) {
        this.view = view;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onStart(Activity activity) {
        if (!hasView()) {
            throw new RuntimeException("Trying to start a ViewState without a view");
        }
        activityContext = activity;
    }

    protected boolean hasView() {
        return view != null;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStop() {
    }

    public void removeView() {
        view = null;
        activityContext = null;
    }
}
