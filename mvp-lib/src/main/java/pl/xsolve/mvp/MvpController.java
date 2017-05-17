package pl.xsolve.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.HashSet;
import java.util.Set;

public class MvpController {

    private final Set<PresenterHolder> holders;
    private boolean isInitialized = false;

    public MvpController() {
        holders = new HashSet<>();
    }

    public <VIEW> PresenterHolder<VIEW> managePresenter(Presenter<VIEW> presenter, Class<VIEW> typeOfView) {
        PresenterHolder<VIEW> presenterHolder = new PresenterHolder<>(presenter, typeOfView);
        holders.add(presenterHolder);
        return presenterHolder;
    }

    public void onCreate(Bundle savedState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.viewState.onCreate(savedState));
    }

    public void onStart(Activity activityContext) {
        holdersStream().forEach(presenterHolder -> {
            presenterHolder.presenter.onStart();
            presenterHolder.viewState.onStart(activityContext);
        });
    }

    public void onStop() {
        holdersStream().forEach(presenterHolder -> presenterHolder.viewState.onStop());
    }

    public void onSaveInstanceState(Bundle outState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.viewState.onSaveInstanceState(outState));
    }

    public void onRestoreInstanceState(Bundle savedState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.viewState.onRestoreInstanceState(savedState));
    }

    public boolean onBackPressed() {
        return holdersStream().anyMatch(
                presenterHolder -> presenterHolder.presenter.onBackPressed()
        );
    }

    public void onDestroy() {
        holdersStream().forEach(presenterHolder -> presenterHolder.viewState.removeView());
    }

    @NonNull
    private Stream<PresenterHolder> holdersStream() {
        return Stream.of(holders);
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized() {
        isInitialized = true;
    }

    public static class PresenterHolder<VIEW> {
        private final Presenter<VIEW> presenter;
        private final Class<VIEW> typeOfView;
        private ViewState<VIEW> viewState;

        public PresenterHolder(Presenter<VIEW> presenter, Class<VIEW> typeOfView) {
            this.presenter = presenter;
            this.typeOfView = typeOfView;
        }

        public void withViewState(ViewState<VIEW> viewState) throws WrongViewException {
            if (typeOfView.isInstance(viewState)) {
                this.viewState = viewState;
                presenter.setView((VIEW) viewState);
            } else {
                String description = String.format("viewState %s for %s must implement %s",
                        viewState.getClass().getCanonicalName(),
                        presenter.getClass().getCanonicalName(),
                        typeOfView.getCanonicalName()
                );
                throw new WrongViewException(description);
            }
        }
    }

    public static class WrongViewException extends RuntimeException {
        public WrongViewException(String description) {
            super(description);
        }
    }
}
