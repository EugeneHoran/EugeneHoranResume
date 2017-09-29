package com.resume.horan.eugene.eugenehoranresume.base;


public interface BaseView<P> {
    void setPresenter(P presenter);

    void showLoading(boolean showLoading);
}
