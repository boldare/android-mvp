package pl.xsolve.mvp;

public class BaseActivityTest$TestBaseActivity$TestBinder extends MvpBinder.ActivityBinder{

    @Override
    public void bind(BaseActivity baseActivity) {
        BaseActivityTest.TestBaseActivity activity = (BaseActivityTest.TestBaseActivity) baseActivity;
        activity.mvpController
                .managePresenter(activity.presenter, BaseActivityTest.TestViewInterface.class)
                .withViewState(activity.viewState);
    }
}
