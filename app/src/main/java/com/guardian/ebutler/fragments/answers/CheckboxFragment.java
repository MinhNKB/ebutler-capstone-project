package com.guardian.ebutler.fragments.answers;

import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;
import com.guardian.ebutler.resourcehelper.EnumDisplayStringHelper;

import org.json.JSONObject;

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
    private static final String ARG_PARAM1 = "ConditionList";
    private static final String ARG_PARAM2 = "EnumList";
    private static final String ARG_PARAM3 = "DefaultValue";

    // TODO: Rename and change types of parameters
    private ArrayList<String> priConditionList = new ArrayList<String>();
    private ArrayList<String> priEnumList = new ArrayList<String>();
    private ArrayList<String> priDefaultValue = new ArrayList<String>();
    private ArrayList<String> priOptionNameList = new ArrayList<String>();

    public CheckboxFragment() {
        proFragmentId = R.layout.fragment_checkbox;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rEnumList Condition Name List
     * @return A new instance of fragment CheckboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckboxFragment newInstance(ArrayList<String> rConditionList, ArrayList<String> rEnumList, ArrayList<String> rDefaultValue) {
        CheckboxFragment fragment = new CheckboxFragment();
        Bundle args = new Bundle();

        args.putSerializable(ARG_PARAM1, rConditionList);
        args.putSerializable(ARG_PARAM2, rEnumList);
        args.putSerializable(ARG_PARAM3, rDefaultValue);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priConditionList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM1);
            priEnumList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM2);
            priDefaultValue = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    @Override
    public String getChatStatement() {
        String lReturnValue = "";
        Boolean lFirstAnswer = true;

        for (String lOptionName :
                priOptionNameList) {
            if (((CheckBox) getView().findViewWithTag(lOptionName)).isChecked()) {
                lReturnValue += lFirstAnswer ? "" : ", ";
                lFirstAnswer = false;
                lReturnValue += lOptionName;
            }
        }
        if (lReturnValue.equals(""))
            lReturnValue = this.proView.getResources().getString(R.string.checkbox_fragment_null_string_true);
        return lReturnValue;
    }

    @Override
    public void setValuesToView(View view) {
        LinearLayout lLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_checkbox_CheckBoxContainer);
        priOptionNameList = EnumDisplayStringHelper.map(getActivity().getApplicationContext(), priEnumList);

        int currentIndex = 0;
        for (String lOptionName :
                priOptionNameList) {
            CheckBox lCheckBox = new CheckBox(getActivity());
            lCheckBox.setText(lOptionName);
            lCheckBox.setTag(lOptionName);
            if (priDefaultValue != null && priDefaultValue.indexOf(Integer.toString(currentIndex)) != -1) {
                lCheckBox.setChecked(true);
            }
            lLinearLayout.addView(lCheckBox);
            ++currentIndex;
        }
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();

        int index = 0;
        for (String lConditionName :
                priConditionList) {
            String lOptionName = priOptionNameList.get(index);
            ++index;

            Condition lReturnValue = new Condition();
            lReturnValue.pubConditionName = lConditionName;
            lReturnValue.pubType = "Boolean";
            lReturnValue.pubValue = ((CheckBox) getView().findViewWithTag(lOptionName)).isChecked() ? "true" : "false";
            lReturnValues.add(lReturnValue);
        }
        return lReturnValues;
    }
}
