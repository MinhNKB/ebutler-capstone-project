package com.guardian.ebutler.screenhelper;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Tabuzaki IA on 4/12/2016.
 */
public class DimensionHelper {
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
}
