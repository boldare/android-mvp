package pl.xsolve.mvp.sample;

import android.os.Bundle;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.xsolve.mvp.MvpActivity;
import pl.xsolve.mvp.R;
import pl.xsolve.mvp.api.MvpPresenter;
import pl.xsolve.mvp.api.MvpViewState;
import pl.xsolve.mvp.dagger.MvpActivityComponent;
import pl.xsolve.mvp.dagger.MvpComponentFactory;
import pl.xsolve.mvp.sample.dagger.DaggerSampleComponent;
import pl.xsolve.mvp.sample.dagger.SampleComponent;

public class SampleActivity extends MvpActivity {

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
    protected MvpActivityComponent createComponent() {
        return DaggerSampleComponent.builder()
                .mvpComponent(new MvpComponentFactory().get())
                .build();
    }

    @Override
    protected void inject(MvpActivityComponent component) {
        ((SampleComponent) component).inject(this);
    }
}
