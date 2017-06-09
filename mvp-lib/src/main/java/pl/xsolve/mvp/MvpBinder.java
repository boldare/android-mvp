package pl.xsolve.mvp;

import java.util.HashMap;
import java.util.Map;

public class MvpBinder {

    public static final String BINDER_CANONICAL_NAME = "pl.xsolve.mvp.MvpBinder$StaticBindings";

    static {
        try {
            //initialize static generated class
            Class.forName(BINDER_CANONICAL_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static Map<String, ActivityBinder> binders;

    public static void bindMvp(MvpActivity activity) {
        ActivityBinder binder = findBinderFor(getKeyFor(activity));
        binder.bind(activity);
    }

    private static String getKeyFor(Object object) {
        return object.getClass().getCanonicalName();
    }
    private static String getKeyFor(Class aClass) {
        return aClass.getCanonicalName();
    }

    private static ActivityBinder findBinderFor(String key) {
        initializeBindersMap();
        if (binders.containsKey(key)) {
            return binders.get(key);
        } else {
            return ActivityBinder.EMPTY;
        }
    }

    public static void addBinder(Class aClass, ActivityBinder binder) {
        initializeBindersMap();
        binders.put(getKeyFor(aClass), binder);
    }

    private static void initializeBindersMap() {
        if (binders == null) {
            binders = new HashMap<>();
        }
    }

    public abstract static class ActivityBinder {
        public abstract void bind(MvpActivity activity);
        protected MvpController getController(MvpActivity activity) {
            return activity.mvpController;
        }

        private static final ActivityBinder EMPTY = new ActivityBinder() {
            @Override
            public void bind(MvpActivity activity) {
            }
        };
    }
}
