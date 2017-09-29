package com.resume.horan.eugene.eugenehoranresume.util.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

public class TextInputView extends TextInputLayout {

    public TextInputView(Context context) {
        this(context, null);
    }

    public TextInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        boolean isErrorEnabled = error != null;
        setErrorEnabled(isErrorEnabled);
        super.setError(error);
    }
}