package com.guardian.ebutler.resourcehelper;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import com.guardian.ebutler.ebutler.R;

/**
 * Created by Tabuzaki IA on 4/20/2016.
 */
public class ResourceGetter {
    public static ColorDrawable getColorFromId (int iId, Context rContext) {
        ColorDrawable lReturnValue;
        if (Build.VERSION.SDK_INT > 23) {
            lReturnValue = new ColorDrawable(rContext.getResources().getColor(R.color.transparent, rContext.getTheme()));
        } else {
            lReturnValue = new ColorDrawable(rContext.getResources().getColor(R.color.transparent));
        }
        return lReturnValue;
    }
}
