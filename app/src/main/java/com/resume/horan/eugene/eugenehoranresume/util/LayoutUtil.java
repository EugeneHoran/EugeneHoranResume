package com.resume.horan.eugene.eugenehoranresume.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class LayoutUtil {

    /**
     * Hide keyboard
     *
     * @param v View focus
     */
    public static void hideKeyboard(View v) {
        Context context = v.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Used to measure changes to Activity rootView
     *
     * @param context   Context
     * @param valueInDp height or width in dp
     * @return valueInPx
     */
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
