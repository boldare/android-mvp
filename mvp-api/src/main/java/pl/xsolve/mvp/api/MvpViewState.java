package pl.xsolve.mvp.api;


import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target(value = FIELD)
public @interface MvpViewState {
}
