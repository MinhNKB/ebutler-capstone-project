package com.guardian.ebutler.screenhelper;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Tabuzaki IA on 12/24/2015.
 */
public class FullScreenHelper {
    public static void setFullScreen(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
