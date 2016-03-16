package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckboxFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionNameList";
    private static final String ARG_PARAM2 = "OptionNameList";

    // TODO: Rename and change types of parameters
    private ArrayList<String> priConditionNameList = new ArrayList<String>();
    private ArrayList<String> priOptionNameList = new ArrayList<String>();

    public CheckboxFragment() {
        proFragmentId = R.layout.fragment_checkbox;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rConditionNameList Condition Name List
     * @param rOptionNameList Option Name List
     * @return A new instance of fragment CheckboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckboxFragment newInstance(ArrayList<String> rConditionNameList, ArrayList<String> rOptionNameList) {
        CheckboxFragment fragment = new CheckboxFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, rConditionNameList);
        args.putSerializable(ARG_PARAM2, rOptionNameList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priConditionNameList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM1);
            priOptionNameList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    public void setValuesToView(View view) {
        LinearLayout lLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_checkbox_CheckBoxContainer);

        for (String lOptionName :
                priOptionNameList) {
            CheckBox lCheckBox = new CheckBox(getActivity());
            lCheckBox.setText(lOptionName);
            lCheckBox.setTag(lOptionName);
            lLinearLayout.addView(lCheckBox);
        }
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        int index = 0;
        for (String lConditionName :
                priConditionNameList) {
            String lOptionName = priOptionNameList.get(index);
            ++index;
            Condition lReturnValue = new Condition();
            lReturnValue.pubConditionName = lConditionName;
            lReturnValue.pubType = "boolean";
            lReturnValue.pubValue = ((CheckBox) getView().findViewWithTag(lOptionName)).isChecked() ? "true" : "false";
            lReturnValues.add(lReturnValue);
        }
        return lReturnValues;
    }
}
