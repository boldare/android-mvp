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

    void onCreate(Bundle savedState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.onCreate(savedState));
    }

    void onStart(Activity activityContext) {
        holdersStream().forEach(presenterHolder -> presenterHolder.onStart(activityContext));
    }

    void onStop() {
        holdersStream().forEach(PresenterHolder::onStop);
    }

    void onSaveInstanceState(Bundle outState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.onSaveInstanceState(outState));
    }

    void onRestoreInstanceState(Bundle savedState) {
        holdersStream().forEach(presenterHolder -> presenterHolder.onRestoreInstanceState(savedState));
    }

    boolean onBackPressed() {
        return holdersStream().anyMatch(
                PresenterHolder::onBackPressed
        );
    }

    void onDestroy() {
        holdersStream().forEach(PresenterHolder::onDestroy);
    }

    void onFinish() {
        holdersStream().forEach(PresenterHolder::onFinish);
    }

    @NonNull
    private Stream<PresenterHolder> holdersStream() {
        return Stream.of(holders);
    }

    boolean isInitialized() {
        return isInitialized;
    }

    void setInitialized() {
        isInitialized = true;
    }

    public static class PresenterHolder<VIEW> {
        private final Presenter<VIEW> presenter;
        private final Class<VIEW> typeOfView;
        private ViewState<VIEW> viewState;
        private boolean created = false;


        PresenterHolder(Presenter<VIEW> presenter, Class<VIEW> typeOfView) {
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

        public void onCreate(Bundle savedState) {
            if (!created) {
                created = true;
                viewState.onCreate(savedState);
            }
        }

        public void onStart(Activity activityContext) {
            presenter.onStart();
            viewState.onStart(activityContext);
        }

        public void onStop() {
            viewState.onStop();
        }

        public void onSaveInstanceState(Bundle outState) {
            viewState.onSaveInstanceState(outState);
        }

        public void onRestoreInstanceState(Bundle savedState) {
            viewState.onRestoreInstanceState(savedState);
        }

        public boolean onBackPressed() {
            return presenter.onBackPressed();
        }

        public void onDestroy() {
            viewState.removeView();
        }

        public void onFinish() {
            viewState.onFinish();
            presenter.onFinish();
        }
    }

    static class WrongViewException extends RuntimeException {
        WrongViewException(String description) {
            super(description);
        }
    }
}
