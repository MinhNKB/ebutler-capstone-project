package com.guardian.ebutler.displaydata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Tabuzaki IA on 12/22/2015.
 */
public class TaskList {
    private static TaskList instance = null;

    public static TaskList getInstance() {
        if (instance == null) {
            instance = new TaskList();
        }
        return instance;
    }

    public ArrayList<Task> tasks;
    private TaskList() {
        tasks = new ArrayList<Task>();
        tasks.add(new Task("Đóng tiền điện", ScheduleHelper.addHours(new Date(),2), false));
        tasks.add(new Task("Đóng tiền nước", ScheduleHelper.addHours(new Date(),3), false));
        tasks.add(new Task("Lau nhà", ScheduleHelper.addHours(new Date(),1), true));
        tasks.add(new Task("Tập thể dục", ScheduleHelper.addHours(new Date(),-1), true));
        tasks.add(new Task("Đánh răng", ScheduleHelper.addHours(new Date(),-2), false));
    }

    public void sortTasks() {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                if (lhs.date.before(rhs.date)) {
                    return -1;
                } else if (lhs.date.after(rhs.date)) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public ArrayList<Task> getTasksChronoOrder() {
        sortTasks();
        return tasks;
    }
}
