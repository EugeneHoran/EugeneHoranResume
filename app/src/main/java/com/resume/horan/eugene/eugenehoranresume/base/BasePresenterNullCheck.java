package com.resume.horan.eugene.eugenehoranresume.base;

import android.util.Log;


/**
 * Return a dummy view if the view is null
 * <p>
 * https://medium.com/@giorgio.npli/android-mvp-avoid-null-checks-35467cd1a03f
 *
 * @param <T>
 */
public abstract class BasePresenterNullCheck<T> {
    private T mView;
    private T mNullView;

    public BasePresenterNullCheck() {
        mNullView = createNullView();
    }

    public void onAttachView(T view) {
        this.mView = view;
    }

    public void onDetachView() {
        this.mView = null;
    }

    public abstract T createNullView();

    public T getView() {
        if (mView == null) {
            Log.e(BasePresenterNullCheck.class.getSimpleName(), "getView: view is null, returning dummy view.");
            return mNullView;
        } else {
            return mView;
        }
    }
}
