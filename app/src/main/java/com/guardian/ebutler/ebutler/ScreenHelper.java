package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Tabuzaki IA on 12/19/2015.
 */
public class ScreenHelper {
    public static void setFullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
