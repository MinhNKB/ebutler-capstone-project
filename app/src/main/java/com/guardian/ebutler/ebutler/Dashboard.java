package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.custom.CustomListItem;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Location;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Activity {
    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priCustomListView;
    private CustomListAdapter priCustomListAdapter;

    private boolean priIsCalendarView = false;

    private boolean priIsAlarmFiltered = false;
    private boolean priIsChecklistFiltered = false;
    private boolean priIsNoteFiltered = false;

    private boolean priIsSorted = false;

    private List<Task> priTaskList;

    private LinearLayout priLinearLayoutAddTaskbar;
    private RelativeLayout priRelativeLayoutTaskbar;
    private LinearLayout priLinearLayoutSearch;
    private LinearLayout priLinearLayoutTopBar;

    private ImageButton priImageButtonAlarm;
    private ImageButton priImageButtonCheckList;
    private ImageButton priImageButtonNote;
    private ImageButton priImageButtonSort;

    private ImageButton priImageButtonViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.findViewsByIds();
        this.initializeCustomListView();
        this.initSearchView();
        this.setupUI(findViewById(R.id.dashboard_parent));
    }


    public void initializeCustomListView()
    {
        try
        {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            this.priTaskList = iHelper.GetAllTasks();
            List<CustomListItem> lTasksList = this.getCustomItems(this.priTaskList);
            this.priCustomListAdapter = new CustomListAdapter(this, lTasksList);
            this.priCustomListView.setAdapter(this.priCustomListAdapter);
        }
        catch (Exception ex){
            String cause = ex.toString();
        }
    }

    public void initSearchView() {
        this.priSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return searchView_onTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return searchView_onTextChange(newText);
            }
        });

        this.priSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                priSearchView.setQuery("", true);
                switchToSearchView(false);
                return false;
            }
        });
    }

    public boolean searchView_onTextChange(String newText) {
        this.performFilter(newText);
        return true;
    }

    private void switchView(boolean iIsCalendarView) {
        this.priIsCalendarView = iIsCalendarView;
        this.priImageButtonViewType.setImageResource(this.priIsCalendarView == true ? R.mipmap.ic_list : R.mipmap.ic_date_range);
    }

    public void findViewsByIds() {
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.dashboard_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.dashboard_searchView);
        this.priCustomListView = (ListView) findViewById(R.id.dashboard_listViewTasks);
        this.priLinearLayoutAddTaskbar = (LinearLayout) findViewById(R.id.dashboard_linearLayout_addTaskBar);
        this.priRelativeLayoutTaskbar = (RelativeLayout) findViewById(R.id.dashboard_taskBar);
        this.priLinearLayoutSearch = (LinearLayout) findViewById(R.id.dashboard_linearLayoutSearch);
        this.priLinearLayoutTopBar = (LinearLayout) findViewById(R.id.dashboard_topBar);
        this.priImageButtonAlarm = (ImageButton) findViewById(R.id.dashboard_buttonAlarm);
        this.priImageButtonCheckList = (ImageButton) findViewById(R.id.dashboard_buttonCheckList);
        this.priImageButtonNote = (ImageButton) findViewById(R.id.dashboard_buttonNote);
        this.priImageButtonSort = (ImageButton) findViewById(R.id.dashboard_buttonSort);
        this.priImageButtonViewType = (ImageButton) findViewById(R.id.dashboard_buttonViewType);
    }


    public List<CustomListItem> getCustomItems(List<Task> iTaskList) {
        List<CustomListItem> lResult = new ArrayList<CustomListItem>();
        CustomListItem lCustomListItem;

        for (Task lTask: iTaskList
             ) {
            String lSecondLine = "";
            if (lTask.pubDescription != null && !lTask.pubDescription.equals(""))
                lSecondLine = lTask.pubDescription;
            else if (lTask.pubTime != null)
                lSecondLine = lTask.pubTime.toString();
            else if (lTask.pubLocation != null){
                for (Location lLocation:lTask.pubLocation
                     ) {
                    lSecondLine += lLocation.pubName;
                }
            }

            lCustomListItem = new CustomListItem(Global.getInstance().getTaskTypeEnum(lTask),
                    lTask.pubName, lSecondLine, lTask.pubPriority.toString(), Global.getInstance().getTaskTypeDrawable(this, lTask));
            lResult.add(lCustomListItem);
        }
        return lResult;
    }

    public void switchToAddTaskbar(boolean iIsAddTaskbar) {
        this.priRelativeLayoutTaskbar.setVisibility(iIsAddTaskbar == true ? View.GONE : View.VISIBLE);
        this.priLinearLayoutAddTaskbar.setVisibility(iIsAddTaskbar == false ? View.GONE : View.VISIBLE);
    }

    public void buttonBack_onClick(View view) {
        this.switchToAddTaskbar(false);
    }

    public void buttonSearch_onClick(View view) {
        this.switchToSearchView(true);
    }

    public void switchToSearchView(boolean iIsSearchView){

        this.priLinearLayoutTopBar.setVisibility(iIsSearchView == true ? View.GONE : View.VISIBLE);
        this.priLinearLayoutSearch.setVisibility(iIsSearchView == false ? View.GONE : View.VISIBLE);

        if (iIsSearchView == true){
            this.priSearchView.setIconified(false);
            this.priSearchView.requestFocusFromTouch();
        }
        else {
            this.setButtonAlarmBackground(false);
            this.setButtonCheckListBackground(false);
            this.setButtonNoteBackground(false);
        }
        //showSoftKeyboard(this, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftKeyboard(Activity iActivity, int iType) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iActivity.getCurrentFocus() != null)
            lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), iType);
    }

    public void setupUI(View view) {
        if (!(view instanceof SearchView)){
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    showSoftKeyboard(Dashboard.this, InputMethodManager.HIDE_NOT_ALWAYS);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, UserInfoInput.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
    }

    public void buttonCheckListAdd_onClick(View view) {
        this.createNewTask(TaskType.CheckList);
    }

    public void buttonAlarmAdd_onClick(View view) {
        this.createNewTask(TaskType.OneTimeReminder);
    }

    public void textViewAddNote_onClick(View view) {
        this.createNewTask(TaskType.Note);
    }

    public void createNewTask(TaskType iTaskType){
        Global.getInstance().pubNewTask = new Task();
        Global.getInstance().pubTaskType = iTaskType;
        Intent intent = new Intent(this, TaskDetail.class);
        startActivity(intent);
    }

    public void buttonAlarm_onClick(View view) {
        this.setButtonAlarmBackground(!this.priIsAlarmFiltered);
        this.performFilter();
    }

    public void setButtonAlarmBackground(boolean iIsActive){
        this.priIsAlarmFiltered = iIsActive;
        this.priImageButtonAlarm.setBackground(iIsActive == true ?
                getResources().getDrawable(R.drawable.blue_round_button_active) : getResources().getDrawable(R.drawable.blue_round_button_inactive));
    }


    public void buttonCheckList_onClick(View view) {
        this.setButtonCheckListBackground(!this.priIsChecklistFiltered);
        this.performFilter();
    }

    public void setButtonCheckListBackground(boolean iIsActive){
        this.priIsChecklistFiltered = iIsActive;
        this.priImageButtonCheckList.setBackground(iIsActive == true ?
                getResources().getDrawable(R.drawable.blue_round_button_active) : getResources().getDrawable(R.drawable.blue_round_button_inactive));
    }

    public void buttonNote_onClick(View view) {
        this.setButtonNoteBackground(!priIsNoteFiltered);
        this.performFilter();
    }

    private void performFilter() {
        this.performFilter(this.priSearchView.getQuery().toString());
    }

    public void setButtonNoteBackground(boolean iIsActive){
        this.priIsNoteFiltered = iIsActive;
        this.priImageButtonNote.setBackground(iIsActive == true ?
                getResources().getDrawable(R.drawable.blue_round_button_active) : getResources().getDrawable(R.drawable.blue_round_button_inactive));
    }

    public void buttonSort_onClick(View view) {
        this.priIsSorted = !this.priIsSorted;
        this.priImageButtonSort.setBackground(this.priIsSorted == true ?
                getResources().getDrawable(R.drawable.blue_round_button_active) : getResources().getDrawable(R.drawable.blue_round_button_inactive));
    }

    public void performFilter(String newText){
        this.switchView(false);
        this.priCustomListAdapter.getFilter(this.getFilters()).filter(newText);
    }

    public void buttonViewType_onClick(View view) {
        this.switchView(!this.priIsCalendarView);
    }

    public void buttonAdd_onClick(View view) {
        this.switchToAddTaskbar(true);
    }

    public void buttonBackSearch_onClick(View view) {
        this.switchToSearchView(false);
    }

    public boolean[] getFilters() {
        boolean[] lResult = new boolean[3];
        lResult[0] = this.priIsAlarmFiltered;
        lResult[1] = this.priIsChecklistFiltered;
        lResult[2] = this.priIsNoteFiltered;
        return lResult;
    }
}
