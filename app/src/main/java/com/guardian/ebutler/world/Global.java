package com.guardian.ebutler.world;

import android.content.Context;
import android.util.DisplayMetrics;

import com.guardian.ebutler.ebutler.dataclasses.ScriptManager;
import com.guardian.ebutler.ebutler.dataclasses.Task;

import java.util.HashMap;

/**
 * Created by Tabuzaki IA on 12/24/2015.
 */
public class Global {
    private static Global instance = null;
    private Global() {

    }

    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public Task pubNewTask;

    public int dpToPx(Context iContext, int idp) {
        DisplayMetrics displayMetrics = iContext.getResources().getDisplayMetrics();
        int px = Math.round(idp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}

