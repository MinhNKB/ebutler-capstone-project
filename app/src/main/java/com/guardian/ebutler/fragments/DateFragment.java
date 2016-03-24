package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";

    public DateFragment() {
        proFragmentId = R.layout.fragment_date;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param iInformationPropertiesNames Parameter 1.
     * @return A new instance of fragment DateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateFragment newInstance(String iInformationPropertiesNames) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, iInformationPropertiesNames);
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
    public String getChatStatement() {
        String lReturnValue = "";
        DatePicker lDatePicker = (DatePicker)(getView().findViewById(R.id.fragment_date_DatePicker));
        lDatePicker.clearFocus();
        lReturnValue += String.format("%02d", lDatePicker.getDayOfMonth());
        lReturnValue += "/";
        lReturnValue += String.format("%02d", lDatePicker.getMonth() + 1);
        lReturnValue += "/";
        lReturnValue += String.format("%04d", lDatePicker.getYear());
        return lReturnValue;
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "Date";
        DatePicker lDatePicker = (DatePicker)(getView().findViewById(R.id.fragment_date_DatePicker));
        lDatePicker.clearFocus();
        String result = "";
        result += String.format("%02d", lDatePicker.getDayOfMonth());
        result += "/";
        result += String.format("%02d", lDatePicker.getMonth() + 1);
        result += "/";
        result += String.format("%04d", lDatePicker.getYear());
        lReturnValue.pubValue = result;
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
