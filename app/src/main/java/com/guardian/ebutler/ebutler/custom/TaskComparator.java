package com.guardian.ebutler.ebutler.custom;

import com.guardian.ebutler.ebutler.dataclasses.Task;

import java.util.Comparator;

/**
 * Created by Duy on 4/3/2016.
 */

public enum TaskComparator implements Comparator<Task> {
    DATE_SORT {
        public int compare(Task o1, Task o2) {
            return o2.pubTime.compareTo(o1.pubTime);
        }},
    NAME_SORT {
        public int compare(Task o1, Task o2) {
            return o1.pubName.compareTo(o2.pubName);
        }};

    public static Comparator<Task> acending(final Comparator<Task> other) {
        return new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return other.compare(o1, o2);
            }
        };
    }
    public static Comparator<Task> decending(final Comparator<Task> other) {
        return new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Task> getComparator(final TaskComparator... multipleOptions) {
        return new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                for (TaskComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}