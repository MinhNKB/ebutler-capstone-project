package com.guardian.ebutler.world;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.TaskList;
import com.guardian.ebutler.ebutler.dataclasses.Location;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskChecklist;
import com.guardian.ebutler.ebutler.dataclasses.TaskNote;
import com.guardian.ebutler.ebutler.dataclasses.TaskOneTimeReminder;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;

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

    public TaskType pubTaskType;
    public Location pubTaskLocation;

    public int getCategoryColor(Context iContext, String iCategory){
        switch (iCategory){
            case "Hóa đơn":
                return iContext.getResources().getColor(R.color.red_900);
            case "Thiết bị":
                return iContext.getResources().getColor(R.color.purple_900);
            case "Sức khỏe":
                return iContext.getResources().getColor(R.color.lightBlue_900);
            case "Việc cần làm":
                return iContext.getResources().getColor(R.color.green_900);
            case "Khác":
                return iContext.getResources().getColor(R.color.yellow_900);
            default:
                return iContext.getResources().getColor(R.color.transparent);
        }
    }

    public int getTaskTypeDrawable(Context iContext, TaskType iTaskType){
        switch (iTaskType){
            case Note:
                return iContext.getResources().getColor(R.color.red_900);
            case OneTimeReminder:
                return iContext.getResources().getColor(R.color.purple_900);
            case PeriodicReminder:
                return iContext.getResources().getColor(R.color.lightBlue_900);
            case CheckList:
                return iContext.getResources().getColor(R.color.green_900);
            default:
                return iContext.getResources().getColor(R.color.transparent);
        }
    }

    public int getTaskTypeDrawable(Context iContext, Task iTask) {
        switch (iTask.pubTaskType){
            case OneTimeReminder:
                return R.mipmap.ic_alarm;
            case CheckList:
                return R.mipmap.ic_playlist_add_check;
            case Note:
                return R.mipmap.ic_note;
            default:
                return -1;
        }
    }

    public TaskType getTaskTypeEnum(Task iTask){
        return iTask.pubTaskType;
    }
}

