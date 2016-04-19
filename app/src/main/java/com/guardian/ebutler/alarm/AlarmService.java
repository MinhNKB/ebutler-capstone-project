package com.guardian.ebutler.alarm;

/**
 * Created by Duy on 4/14/2016.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;

import java.util.Date;
import java.util.List;

public class AlarmService extends Service
{
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        DatabaseHelper iHelper = new DatabaseHelper(this);
        List<Task> lTaskList = iHelper.GetAllTasks();
        for (Task lTask: lTaskList) {
            if (lTask.pubTaskType == TaskType.OneTimeReminder && lTask.pubTime.getTime() > (new Date()).getTime()) {
                Alarm lAlarm = new Alarm();
                lAlarm.SetAlarm(this, lTask.pubTime.getTime());
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
