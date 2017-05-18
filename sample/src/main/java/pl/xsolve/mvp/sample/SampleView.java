package pl.xsolve.mvp.sample;

public interface SampleView {
    void displayData(CharSequence someData);
    void displayProgress();

    void setListener(ActionListener listener);

    interface ActionListener  {
        void onDataRequested();
    }
}
