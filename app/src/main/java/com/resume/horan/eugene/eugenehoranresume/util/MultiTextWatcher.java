package com.resume.horan.eugene.eugenehoranresume.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MultiTextWatcher {
    private MultiTextWatcherInterface callback;

    public interface MultiTextWatcherInterface {
        void onTextChanged(EditText editText, CharSequence s, int start, int before, int count);
    }

    public MultiTextWatcher setCallback(MultiTextWatcherInterface callback) {
        this.callback = callback;
        return this;
    }

    public MultiTextWatcher registerEditText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                callback.beforeTextChanged(editText, s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callback.onTextChanged(editText, s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                callback.afterTextChanged(editText, editable);
            }
        });

        return this;
    }
}
