package com.guardian.ebutler.alarm;

/**
 * Created by Duy on 4/14/2016.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;
import com.guardian.ebutler.world.Global;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlarmService extends Service
{
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try
        {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            List<Task> lTaskList = iHelper.GetAllTasks();
            for (Task lTask: lTaskList) {
                if (lTask.pubTaskType == TaskType.OneTimeReminder)
                {
                    Log.w("wel", "checking one time reminder");
                    if (lTask.pubTime.getTime() > (new Date()).getTime()) {
                        Alarm lAlarm = new Alarm();
                        lAlarm.SetAlarm(this, lTask.pubTime.getTime());
                    }
                }
                else if (lTask.pubTaskType == TaskType.PeriodicReminder && lTask.pubRepeat != null){
                    Log.w("wel", "checking repeatable reminder");
                    Alarm lAlarm = new Alarm();
                    long repeat = 0;
                    switch (lTask.pubRepeat) {
                        case "Mỗi ngày":
                            repeat = getDateDuration();
                            break;
                        case "Mỗi tuần":
                            repeat = getWeekDuration();
                            break;
                        case "Mỗi tháng":
                            repeat = getMonthDuration();
                            break;
                        case "Mỗi năm":
                            repeat = getYearDuration();
                            break;
                    }
                    while (lTask.pubTime.getTime() < (new Date()).getTime()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lTask.pubTime);
                        switch (lTask.pubRepeat) {
                            case "Mỗi ngày":
                                cal.add(Calendar.DATE, 1);
                                break;
                            case "Mỗi tuần":
                                cal.add(Calendar.WEEK_OF_YEAR, 1);
                                break;
                            case "Mỗi tháng":
                                cal.add(Calendar.MONTH, 1);
                                break;
                            case "Mỗi năm":
                                cal.add(Calendar.YEAR, 1);
                                break;
                        }
                        lTask.pubTime = new Date(cal.getTimeInMillis());
                        iHelper.UpdateATask(lTask);
                    }
                    lAlarm.SetAlarm(this, lTask.pubTime.getTime(), repeat);
                }
            }
            iHelper.close();

//            Calendar cal = Calendar.getInstance();
//            cal.set(Calendar.HOUR, 8);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            Alarm alarm = new Alarm();
//            alarm.SetAlarm(this, cal.getTimeInMillis(), getDateDuration());

        }
        catch (Exception ex){
            Log.w("wel", ex.toString());
        }

        return START_STICKY;
    }

    private long getYearDuration() {
        Date current = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, (int) current.getTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTimeInMillis() - current.getTime();
    }

    private long getWeekDuration() {
        return getDateDuration() * 7;
    }

    private long getDateDuration() {
        return TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    }

    private long getMonthDuration(){
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead


        return (thenTime); // this is what you set as trigger point time i.e one month after

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
