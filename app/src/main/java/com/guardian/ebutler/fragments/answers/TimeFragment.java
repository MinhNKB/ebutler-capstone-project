package com.guardian.ebutler.fragments.answers;

import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;
import com.guardian.ebutler.timehelper.DateTimeHelper;

import java.util.ArrayList;
import java.util.Calendar;

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
    private static final String ARG_PARAM2 = "DefaultValue";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";
    private ArrayList<String> priDefaultValue = new ArrayList<>();

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
    public static TimeFragment newInstance(String lConditionName, ArrayList<String> rDefaultValue) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, lConditionName);
        args.putSerializable(ARG_PARAM2, rDefaultValue);
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
            priDefaultValue = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public void setValuesToView(View view) {
        TimePicker lTimePicker = (TimePicker)(view.findViewById(R.id.fragment_time_TimePicker));
        int lMinute, lHour;
        Calendar lCalendar = Calendar.getInstance();
        if (priDefaultValue == null || priDefaultValue.size() < 2) {
            lHour = lCalendar.get(Calendar.HOUR_OF_DAY);
        } else {
            lHour = Integer.parseInt(priDefaultValue.get(1));
        }
        if (priDefaultValue == null || priDefaultValue.size() < 2) {
            lMinute = lCalendar.get(Calendar.MINUTE);
        } else {
            lMinute = Integer.parseInt(priDefaultValue.get(0)) - 1;
        }
        DateTimeHelper.setTimeToTimePicker(lTimePicker, lHour, lMinute);

    }

    public String getChatStatement() {
        TimePicker lTimePicker = (TimePicker)(getView().findViewById(R.id.fragment_time_TimePicker));
        lTimePicker.clearFocus();
        String lReturnValue = DateTimeHelper.getTimeFromTimePicker(lTimePicker);
        return lReturnValue;
    }

    @Override
    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "Time";
        TimePicker lTimePicker = (TimePicker)(getView().findViewById(R.id.fragment_time_TimePicker));
        lTimePicker.clearFocus();
        String result = DateTimeHelper.getTimeFromTimePicker(lTimePicker);
        lReturnValue.pubValue = result;
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
