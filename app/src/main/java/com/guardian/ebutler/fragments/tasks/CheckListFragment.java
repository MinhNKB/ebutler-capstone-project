package com.guardian.ebutler.fragments.tasks;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckListFragment extends AbstractTaskFragment {
    protected ArrayList<CheckListItemFragment> proFragmentList = new ArrayList<CheckListItemFragment>();
    public CheckListFragment() {
        proFragmentId = R.layout.fragment_checklist;
    }

    public static CheckListFragment newInstance() {
        CheckListFragment fragment = new CheckListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void getValues(Task rNewTask) {
        rNewTask.pubDescription = "";
        for (CheckListItemFragment fragment : proFragmentList) {
            rNewTask.pubDescription += fragment.pubText;
            rNewTask.pubDescription += ":";
            rNewTask.pubDescription += fragment.pubIsChecked ? "1":"0";
            if (proFragmentList.indexOf(fragment) != proFragmentList.size() - 1) {
                rNewTask.pubDescription += ",";
            }
        }
    }

    public void setValuesToView(View view) {
        final EditText lEditText = (EditText) view.findViewById(R.id.fragment_checklist_TemplateEditText);
        lEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addValue();
                return false;
            }
        });
    }

    public void addValue() {
        EditText lEditBox = (EditText) proView.findViewById(R.id.fragment_checklist_TemplateEditText);
        String lTemplateString = lEditBox.getText().toString();
        lEditBox.setText("");
        CheckBox lCheckBox = ((CheckBox) proView.findViewById(R.id.fragment_checklist_TemplateCheckbox));
        Boolean lIsChecked = lCheckBox.isChecked();
        lCheckBox.setChecked(false);

        CheckListItemFragment lChecklistItemFragment = CheckListItemFragment.newInstance(lTemplateString, lIsChecked);
        proFragmentList.add(lChecklistItemFragment);

        getFragmentManager().beginTransaction().add(R.id.fragment_checklist_ChecklistContainer, lChecklistItemFragment).commit();
    }
}
