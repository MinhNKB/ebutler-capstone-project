package com.guardian.ebutler.ebutler.dataclasses;

import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tabuzaki IA on 3/28/2016.
 */
public class TaskChecklist extends Task {
    public TaskChecklist() {
        pubTaskType = TaskType.CheckList;
    }

    public void setDescription(List<String> iChecklistItems) {
        pubDescription = TextUtils.join(", ", iChecklistItems);
    }

    public List<String> getDescription() {
        List<String> lReturnValue = Arrays.asList(TextUtils.split(pubDescription, " "));
        return lReturnValue;
    }
}
