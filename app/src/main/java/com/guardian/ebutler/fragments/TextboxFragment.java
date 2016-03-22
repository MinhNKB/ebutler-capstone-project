package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextboxFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";

    public TextboxFragment() {
        proFragmentId = R.layout.fragment_textbox;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ConditionName Parameter 1.
     * @return A new instance of fragment TextboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextboxFragment newInstance(String iConditionName) {
        TextboxFragment fragment = new TextboxFragment();
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

    @Override
    public String getChatStatement() throws Exception{
        String lReturnValue = "";
        lReturnValue += ((EditText) getView().findViewById(R.id.fragment_textbox_Input)).getText().toString();
        if (lReturnValue.equals(""))
            throw new Exception(this.proView.getResources().getString(R.string.textbox_fragment_null_string_false));
        return lReturnValue;
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "string";
        lReturnValue.pubValue = ((EditText) getView().findViewById(R.id.fragment_textbox_Input)).getText().toString();
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
