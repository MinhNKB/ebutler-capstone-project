package com.guardian.ebutler.resourcehelper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by Tabuzaki IA on 3/17/2016.
 */
public class EnumDisplayStringHelper {
    public static ArrayList<String> map(Context rContext, ArrayList<String> rEnumList) {
        ArrayList<String> lReturnValue = new ArrayList<>();
        for (Object lEnum:
             rEnumList) {
            int lResourceId = ResourceFromStringHelper.getResourceId(rContext, "enum_value_" + lEnum, "string", rContext.getPackageName());
            if (lResourceId != -1) {
                lReturnValue.add(rContext.getResources().getString(lResourceId));
            } else {
                lReturnValue.add("");
            }
        }
        return lReturnValue;
    }
}
