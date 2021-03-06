package com.guardian.ebutler.fragments.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.TaskDetail;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckListFragment extends AbstractTaskFragment {
    public ArrayList<CheckListItemFragment> pubFragmentList = new ArrayList<CheckListItemFragment>();
    public CheckListFragment() {
        proFragmentId = R.layout.fragment_checklist;
    }

    public Activity pubActivity;

    public static CheckListFragment newInstance(Activity iActivity) {
        CheckListFragment fragment = new CheckListFragment();
        fragment.pubActivity = iActivity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void getValues(Task rNewTask) {
        rNewTask.pubDescription = "";
        for (CheckListItemFragment fragment : pubFragmentList) {
            rNewTask.pubDescription += fragment.pubTextView.getText();
            rNewTask.pubDescription += ":";
            rNewTask.pubDescription += fragment.pubIsChecked ? "1":"0";
            if (pubFragmentList.indexOf(fragment) != pubFragmentList.size() - 1) {
                rNewTask.pubDescription += ",";
            }
        }
        rNewTask.pubTime = new Date();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Global.getInstance().pubSelectedTask == null)
            addNewItem();
    }

    public void setValuesToView(View view) {
        final ImageButton lAddItem = (ImageButton) view.findViewById(R.id.fragmen_checklist_addItem);
        lAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });
    }

    public void setValues(Task iTask) {
        String[] lStrings = iTask.pubDescription.split(",");
        for(int i=0;i<lStrings.length;i++)
        {
            String lItem = lStrings[i];
            String lItemName = lItem.substring(0,lItem.lastIndexOf(":"));
            Character lIsChecked = lItem.charAt(lItem.length() - 1);
            CheckListItemFragment lFragment = CheckListItemFragment.newInstance(lItemName, lIsChecked == '1' ? true : false);
            pubFragmentList.add(lFragment);
            if (lIsChecked != '1')
                addItemToView(R.id.fragment_checklist_ChecklistContainer, lFragment);
            else {
                addItemToView(R.id.fragment_checklist_donechecklistContainer, lFragment);
                LinearLayout lDoneCheckListContainerGeneral = (LinearLayout) proView.findViewById(R.id.fragment_checklist_doneChecklistContainerGeneralLayout);
                LinearLayout lDoneCheckListContainer = (LinearLayout) proView.findViewById(R.id.fragment_checklist_donechecklistContainer);
                int lMin = lIsChecked == '1' ? -1 : 1;
                lDoneCheckListContainerGeneral.setVisibility(lDoneCheckListContainer.getChildCount() > lMin ? View.VISIBLE : View.GONE);
            }

        }
    }

//    public void initCheckList(){
//        LinearLayout lDoneCheckListContainerGeneral = (LinearLayout) proView.findViewById(R.id.fragment_checklist_doneChecklistContainerGeneralLayout);
//        lDoneCheckListContainerGeneral.setVisibility(View.GONE);
//
//        ArrayList<CheckListItemFragment> oldFragmentList = proFragmentList;
//        proFragmentList = new ArrayList<CheckListItemFragment>();
//
//        for (int i = 0; i < oldFragmentList.size(); ++i){
//            if (oldFragmentList.get(i).pubIsChecked == false)
//                addItemToView(R.id.fragment_checklist_ChecklistContainer, createNewCheckListItemFragment(
//                        oldFragmentList.get(i).pubText, oldFragmentList.get(i).pubIsChecked));
//            else
//            {
//                lDoneCheckListContainerGeneral.setVisibility(View.VISIBLE);
//                addItemToView(R.id.fragment_checklist_donechecklistContainer, createNewCheckListItemFragment(
//                        oldFragmentList.get(i).pubText, oldFragmentList.get(i).pubIsChecked));
//            }
//        }
//    }

    public void addNewItem(){
        addItemToView(R.id.fragment_checklist_ChecklistContainer, createNewCheckListItemFragment("", false));
    }

    public void addItemToView(int containerViewId, CheckListItemFragment fragment) {
        fragment.pubCheckListFragment = this;
        getFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }

    public CheckListItemFragment createNewCheckListItemFragment(String iText, boolean iIsChecked){
        CheckListItemFragment lChecklistItemFragment = CheckListItemFragment.newInstance(iText, iIsChecked);
        pubFragmentList.add(lChecklistItemFragment);
        lChecklistItemFragment.pubCheckListFragment = this;
        return lChecklistItemFragment;
    }

    public void reallocateFragment(CheckListItemFragment iCheckListItemFragment) {
        getFragmentManager().beginTransaction().remove(iCheckListItemFragment).commit();
        pubFragmentList.remove(iCheckListItemFragment);

        CheckListItemFragment lNewItem = createNewCheckListItemFragment(iCheckListItemFragment.pubText, iCheckListItemFragment.pubIsChecked);
        getFragmentManager().beginTransaction().add(iCheckListItemFragment.pubIsChecked == true ?
                        R.id.fragment_checklist_donechecklistContainer : R.id.fragment_checklist_ChecklistContainer ,
                lNewItem).commit();

        LinearLayout lDoneCheckListContainerGeneral = (LinearLayout) proView.findViewById(R.id.fragment_checklist_doneChecklistContainerGeneralLayout);
        LinearLayout lDoneCheckListContainer = (LinearLayout) proView.findViewById(R.id.fragment_checklist_donechecklistContainer);

        int lMin = iCheckListItemFragment.pubIsChecked == true ? -1 : 1;
        lDoneCheckListContainerGeneral.setVisibility(lDoneCheckListContainer.getChildCount() > lMin ? View.VISIBLE : View.GONE);
    }
}
