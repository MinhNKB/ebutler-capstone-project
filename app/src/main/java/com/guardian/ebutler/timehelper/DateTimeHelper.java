package com.guardian.ebutler.timehelper;

import android.os.Build;
import android.widget.DatePicker;
import android.widget.TimePicker;

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
}
