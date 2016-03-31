package com.guardian.ebutler.fragments.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Task;

public class AlarmFragment extends AbstractTaskFragment {
    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void getValues(Task rNewTask) {

    }
}
