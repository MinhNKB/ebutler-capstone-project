package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";

    public TimeFragment() {
        proFragmentId = R.layout.fragment_time;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lConditionName Parameter 1.
     * @return A new instance of fragment TimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeFragment newInstance(String lConditionName) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, lConditionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String lString = getArguments().getString(ARG_PARAM1);
            if (lString.length() > 0) {
                priConditionName = lString;
            }
        }
    }

    @Override
    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "time";
        TimePicker lTimePicker = (TimePicker)(getView().findViewById(R.id.fragment_time_TimePicker));
        lTimePicker.clearFocus();
        String result = "";
        if (Build.VERSION.SDK_INT >= 23 )
            result += String.format("%02d", lTimePicker.getHour());
        else
            result += String.format("%02d", lTimePicker.getCurrentHour());
        result += ":";
        if (Build.VERSION.SDK_INT >= 23 )
            result += String.format("%02d", lTimePicker.getMinute());
        else
            result += String.format("%02d", lTimePicker.getCurrentMinute());
        lReturnValue.pubValue = result;
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
