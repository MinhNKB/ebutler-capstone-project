package com.guardian.ebutler.timehelper;

import android.os.Build;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {
    public static String getTimeFromTimePicker(TimePicker rTimePicker) {
        String lResult = "";
        if (Build.VERSION.SDK_INT >= 23 )
            lResult += String.format("%02d", rTimePicker.getHour());
        else
            lResult += String.format("%02d", rTimePicker.getCurrentHour());
        lResult += ":";
        if (Build.VERSION.SDK_INT >= 23 )
            lResult += String.format("%02d", rTimePicker.getMinute());
        else
            lResult += String.format("%02d", rTimePicker.getCurrentMinute());
        return lResult;
    }

    public static void setTimeToTimePicker(TimePicker rTimerPicker, int iHour, int iMinute) {
        if(Build.VERSION.SDK_INT >= 23) {
            rTimerPicker.setHour(iHour);
            rTimerPicker.setMinute(iMinute);
        } else {
            rTimerPicker.setCurrentHour(iHour);
            rTimerPicker.setCurrentMinute(iMinute);
        }
    }

    public static String getDateFromDatePicker(DatePicker rDatePicker) {
        String lResult = "";
        lResult += String.format("%02d", rDatePicker.getDayOfMonth());
        lResult += "-";
        lResult += String.format("%02d", rDatePicker.getMonth() + 1);
        lResult += "-";
        lResult += String.format("%04d", rDatePicker.getYear());
        return lResult;
    }

    public static String getDateStringFromDate(Date rDate) {
        String lResult;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rDate);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        lResult = String.format("%02d", hours) + ":" + String.format("%02d", minutes) +","
        + " ngày " + day + "/" + (month + 1) + "/" + (year);
        return lResult;
    }
}
