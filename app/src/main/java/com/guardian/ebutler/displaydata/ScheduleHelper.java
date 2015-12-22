package com.guardian.ebutler.displaydata;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tabuzaki IA on 12/22/2015.
 */
public class ScheduleHelper {
    static public Date addHours(Date date, int hrs) {
        Calendar cal = Calendar.getInstance();
        //in below line of code, date is in which which you want to add 7 number of days
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hrs);
        Date newDate = cal.getTime();
        return newDate;
    }
}
