package com.example.f.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Administrator on 2017/11/9.
 */

public class SnackbarUtils {

    public static void show(Activity activity, int resId) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(Activity activity, CharSequence text) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
