package com.guardian.ebutler.fragments.tasks;

import android.view.View;

import com.guardian.ebutler.ebutler.dataclasses.Task;

/**
 * Created by Tabuzaki IA on 3/27/2016.
 */
public interface TaskFragmentInterface {
    void setValuesToView(View view);
    void getValues(Task rTask);
}
