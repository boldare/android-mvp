package pl.xsolve.mvp;

public abstract class Presenter<VIEW> {

    protected VIEW view;

    public void setView(VIEW view) {
        this.view = view;
    }

    public void onStart() {
        if (!hasView()) {
            throw new RuntimeException("Trying to start a pilot without a view");
        }
    }

    private boolean hasView() {
        return view != null;
    }

    /**
     * @return Returns true if onBackPressed should be consumed
     */
    public boolean onBackPressed() {
        return false;
    }

}
