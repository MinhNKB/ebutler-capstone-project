package com.guardian.ebutler.resourcehelper;

import android.content.Context;

/**
 * Created by Tabuzaki IA on 3/17/2016.
 */
public class ResourceFromStringHelper {
    public static int getResourceId(Context rContext, String iVariableName, String iResourcename, String iPackageName)
    {
        try {
            return rContext.getResources().getIdentifier(iVariableName, iResourcename, iPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
