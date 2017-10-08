package com.resume.horan.eugene.eugenehoranresume.util;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.login.LoginActivity;


public class LayoutUtil {

    public static boolean isM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param context
     * @param view
     */
    public static void getStatusBarHeight(Context context, View view) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        view.setPadding(0, result, 0, 0);
    }


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
