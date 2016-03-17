package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeSpanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeSpanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSpanFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionNameList";

    // TODO: Rename and change types of parameters
    private ArrayList<String> priConditionNameList = new ArrayList<String>();

    public TimeSpanFragment() {
        proFragmentId = R.layout.fragment_time_span;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rConditionNameList Parameter 1.
     * @return A new instance of fragment TimeSpanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeSpanFragment newInstance(ArrayList<String> rConditionNameList) {
        TimeSpanFragment fragment = new TimeSpanFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, rConditionNameList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priConditionNameList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    public String getChatStatement() {
        String lReturnValue = getResources().getString(R.string.chat_fragment_AnswerPrefix);
        lReturnValue += " ";
        lReturnValue += ((Spinner)getView().findViewById(R.id.fragment_time_span_SpinnerStart)).getSelectedItem().toString();
        lReturnValue += " - ";
        lReturnValue += ((Spinner)getView().findViewById(R.id.fragment_time_span_SpinnerEnd)).getSelectedItem().toString();
        return lReturnValue;
    }

    @Override
    public void setValuesToView(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.fragment_time_span_SpinnerStart);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fragment_time_span_hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.fragment_time_span_SpinnerEnd);
        spinner.setAdapter(adapter);
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionNameList.get(0);
        lReturnValue.pubType = "time";
        lReturnValue.pubValue = ((Spinner)getView().findViewById(R.id.fragment_time_span_SpinnerStart)).getSelectedItem().toString();
        lReturnValues.add(lReturnValue);

        lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionNameList.get(1);
        lReturnValue.pubType = "time";
        lReturnValue.pubValue = ((Spinner)getView().findViewById(R.id.fragment_time_span_SpinnerEnd)).getSelectedItem().toString();
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
