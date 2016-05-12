package com.guardian.ebutler.ebutler.dataclasses;

import java.util.Date;
import java.util.List;

/**
 * Created by nkbmi on 3/10/2016.
 */
public class Task {
    public String pubName;
    public String pubRepeat;
    public TaskType pubTaskType;
    public String pubDescription;
    public Date pubTime;
    public List<Location> pubLocation;
    public Priority pubPriority;
    public Status pubStatus;
    public List<Plugin> pubPlugins;
    public int pubId;

    public Task() {
    }
}

