package com.guardian.ebutler.displaydata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tabuzaki IA on 12/22/2015.
 */
public class Task {
    public String name = "";
    public Date date = new Date();
    public boolean isDone = false;

    public Task(String _name) {
        this.name = _name;
    }

    public Task(String _name, Date _date) {
        this(_name);
        this.date = _date;
    }

    public Task(String _name, Date _date, boolean _isDone) {
        this(_name, _date);
        this.isDone = _isDone;
    }

    public String formattedDate(String format) {
        // MM/dd/yyyy HH:mm:ss
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
}
