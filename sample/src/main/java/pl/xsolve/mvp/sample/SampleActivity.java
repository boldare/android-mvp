package pl.xsolve.mvp.sample;

import android.os.Bundle;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.xsolve.mvp.BaseActivity;
import pl.xsolve.mvp.R;
import pl.xsolve.mvp.api.MvpPresenter;
import pl.xsolve.mvp.api.MvpViewState;
import pl.xsolve.mvp.dagger.BaseActivityComponent;
import pl.xsolve.mvp.dagger.BaseComponentFactory;
import pl.xsolve.mvp.sample.dagger.DaggerSampleComponent;
import pl.xsolve.mvp.sample.dagger.SampleComponent;

public class SampleActivity extends BaseActivity {

    @Inject
    @MvpPresenter(viewState = "sampleViewState")
    protected SamplePresenter samplePresenter;

    @Inject
    @MvpViewState
    protected SampleViewState sampleViewState;

    @BindView(R.id.root)
    protected ViewGroup root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflate();

        sampleViewState.setView(new SampleViewImpl(root));
    }

    private void inflate() {
        setContentView(R.layout.sample_layout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected BaseActivityComponent createComponent() {
        return DaggerSampleComponent.builder()
                .baseComponent(new BaseComponentFactory().get())
                .build();
    }

    @Override
    protected void inject(BaseActivityComponent component) {
        ((SampleComponent)component).inject(this);
    }
}
