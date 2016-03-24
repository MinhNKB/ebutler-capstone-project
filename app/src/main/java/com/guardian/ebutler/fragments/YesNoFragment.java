package com.guardian.ebutler.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YesNoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YesNoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesNoFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";

    public YesNoFragment() {
        proFragmentId = R.layout.fragment_yes_no;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param iConditionName Parameter 1.
     * @return A new instance of fragment YesNoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YesNoFragment newInstance(String iConditionName) {
        YesNoFragment fragment = new YesNoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, iConditionName);
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


    public String getChatStatement() {
        String lReturnValue = "";
        if (((RadioButton) getView().findViewById(R.id.fragment_yes_no_RadioButtonAccept)).isChecked()) {
            lReturnValue += getResources().getString(R.string.fragment_yes_no_StringAccept);
        } else {
            lReturnValue += getResources().getString(R.string.fragment_yes_no_StringDecline);
        }
        return lReturnValue;
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "Boolean";
        lReturnValue.pubValue = ((RadioButton) getView().findViewById(R.id.fragment_yes_no_RadioButtonAccept)).isChecked() ? "true" : "false";

        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
