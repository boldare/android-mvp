package pl.xsolve.mvp.sample;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.xsolve.mvp.R;

public class SampleViewImpl implements SampleView {
    @BindView(R.id.fab)
    View fab;

    @BindView(R.id.text)
    TextView text;


    private ActionListener listener = () -> {
    };

    public SampleViewImpl(ViewGroup root) {
        ButterKnife.bind(this, root);
    }

    @Override
    public void displayData(CharSequence someData) {
        text.setText("data received:\n" + someData);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void displayProgress() {
        text.setText("loading data - feel free to rotate the device");
        fab.setVisibility(View.GONE);
    }

    @Override
    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.fab)
    void fabClick() {
        listener.onDataRequested();
    }
}
