package pl.xsolve.mvp.sample;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import pl.xsolve.mvp.Presenter;
import pl.xsolve.mvp.dagger.scope.ScreenScope;


@ScreenScope
public class SamplePresenter extends Presenter<SampleView> {
    private static final CharSequence LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam porta, eros vel ulrices congue, tellus urna aliquam velit, non faucibus velit libero et odio. Phasellus dictum lectus lorem, quis vulputate tellus scelerisque in. Sed lobortis sem nec libero bibendum, et congue ex sagittis. Aliquam laoreet mi et tincidunt finibus. Donec sed interdum arcu. Sed ut dolor sapien. In placerat felis sit amet finibus sagittis. Praesent ut lorem iaculis, bibendum dolor id, mollis tellus. Nam ullamcorper massa ac elit dapibus, sit amet dapibus diam mollis. In sed arcu a odio accumsan porta a tempus nisi\n\nPellentesque efficitur sem velit, at fringilla metus fringilla gravida. Nullam ut elit massa. Etiam elementum diam ex, vel vehicula dolor feugiat ac. In ut ex nec diam egestas pretium. Maecenas tincidunt semper dui, quis rutrum mi pellentesque malesuada. Vestibulum ex ante, porttitor in lorem et, hendrerit tincidunt nisl. Nam vitae eleifend mi, sed gravida libero. Nunc dignissim mattis turpis ac posuere. Nullam sodales condimentum tempor. Pellentesque ac justo ac ex cursus pharetra vitae eget ante. Proin sed magna eu diam dapibus porta quis sollicitudin massa";

    @Inject
    public SamplePresenter() {
    }

    @Override
    public void setView(SampleView sampleView) {
        super.setView(sampleView);
        view.setListener(this::requestData);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void requestData() {
        view.displayProgress();
        new Handler().postDelayed(() -> view.displayData(LOREM_IPSUM),5000);
    }
}
