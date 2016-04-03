package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.custom.*;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.*;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Dashboard extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priCustomListView;
    private CustomListAdapter priCustomListAdapter;

    private boolean priIsCalendarView = false;

    private boolean priIsAlarmFiltered = true;
    private boolean priIsChecklistFiltered = true;
    private boolean priIsNoteFiltered = true;

    private TaskComparator priSortType = TaskComparator.DATE_SORT;
    private boolean priIsAscending = true;
    private Spinner priSpinnerSort;

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
        this.initSort();
        this.initializeCustomListView();
        this.initSearchView();
        this.setupUI(findViewById(R.id.dashboard_parent));

        Global.getInstance().pubNewTask = null;
    }

    private void initSort() {
        this.initSpinnerSortListener();
    }

    private void initSpinnerSortListener() {
        this.priSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        priSortType = TaskComparator.DATE_SORT;
                        break;
                    case 1:
                        priSortType = TaskComparator.NAME_SORT;
                        break;

                }
                performSort();
                performFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void initializeCustomListView()
    {
        try
        {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            this.priTaskList = iHelper.GetAllTasks();
            this.performSort();
            this.performFilter();
        }
        catch (Exception ex){
            String cause = ex.toString();
        }
    }

    public void performSort(){
        if (this.priIsAscending) {
            Collections.sort(this.priTaskList, TaskComparator.acending(TaskComparator.getComparator(this.priSortType)));
        }
        else {
            Collections.sort(this.priTaskList, TaskComparator.decending(TaskComparator.getComparator(this.priSortType)));
        }

        List<CustomListItem> lTasksList = this.getCustomItems(this.priTaskList);
        this.priCustomListAdapter = new CustomListAdapter(this, lTasksList);
        this.priCustomListView.setAdapter(this.priCustomListAdapter);
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
        this.priSpinnerSort = (Spinner) findViewById(R.id.dashboard_spinnerSort);
    }


    public List<CustomListItem> getCustomItems(List<Task> iTaskList) {
        List<CustomListItem> lResult = new ArrayList<CustomListItem>();

        for (Task lTask: iTaskList
             )
            lResult.add(this.createCustomListItem(lTask));
        return lResult;
    }

    private CustomListItem createCustomListItem(Task lTask) {
        CustomListItem lCustomListItem;

        String lFirstLine = getFirstLine(lTask);
        String lSecondLine = getSecondLine(lTask);
        String lThirdLine = getThirdLine(lTask);

        lCustomListItem = new CustomListItem(Global.getInstance().getTaskTypeEnum(lTask),
                lFirstLine, lSecondLine, lThirdLine, Global.getInstance().getTaskTypeDrawable(this, lTask));

        return  lCustomListItem;
    }

    private String getThirdLine(Task lTask) {
        String lThirdLine = null;
        switch (lTask.pubTaskType) {
            case OneTimeReminder:
                if (lTask.pubLocation != null){
                    lThirdLine = "";
                    for (Location lLocation:lTask.pubLocation
                            )
                        lThirdLine += lLocation.pubName;
                }
                if (lThirdLine == null || lThirdLine.equals("")){
                    lThirdLine = "";
                    long lRemainingTime = lTask.pubTime.getTime() - (new Date()).getTime();
                    if (lRemainingTime < 0){
                        lThirdLine += "Bạn đã trễ ";
                        lRemainingTime *= -1;
                    }
                    else
                        lThirdLine += "Còn khoảng ";
                    long hours = TimeUnit.MILLISECONDS.toHours(lRemainingTime);
                    long days = hours/24;
                    long months = days/30;
                    if (hours < 24)
                        lThirdLine += hours + " giờ ";
                    else if (days < 30)
                        lThirdLine += days + " ngày ";
                    else if (months < 12)
                        lThirdLine += months + " tháng";
                    else
                        lThirdLine += months/12 + " năm";
                }
                break;
            default:
                if (lTask.pubTime == null)
                    lThirdLine = "Không có thời gian";
                else
                    lThirdLine = "Tạo vào lúc " + lTask.pubTime.getHours() + ":" + lTask.pubTime.getMinutes() +","
                            + " ngày " + lTask.pubTime.getDate() + "/" + (lTask.pubTime.getMonth() + 1) + "/" + (lTask.pubTime.getYear() + 1900);
                break;
        }
        return lThirdLine;
    }

    private String getSecondLine(Task lTask) {
        String lSecondLine = null;
        switch (lTask.pubTaskType) {
            case OneTimeReminder:
                if (lTask.pubTime == null)
                    lSecondLine = "Không có thời gian";
                else
                    lSecondLine = "Nhắc vào lúc " + lTask.pubTime.getHours() + ":" + lTask.pubTime.getMinutes() +","
                            + " ngày " + lTask.pubTime.getDate() + "/" + (lTask.pubTime.getMonth() + 1) + "/" + (lTask.pubTime.getYear() + 1900);
                break;
            case CheckList:
                if (lTask.pubDescription == null || lTask.pubDescription.equals(""))
                    lSecondLine = "Không có danh sách";
                else {
                    lSecondLine = lTask.pubDescription.replace(":0", "");
                    lSecondLine = lSecondLine.replace(":1", "");
                    if (lSecondLine.replace(",", "").equals(""))
                        lSecondLine = "Không có danh sách";
                }
                break;
            default:
                if (lTask.pubDescription == null || lTask.pubDescription.equals(""))
                    lSecondLine = "Không có nội dung";
                else
                    lSecondLine = lTask.pubDescription;
                break;
        }
        return lSecondLine;
    }

    private String getFirstLine(Task lTask) {
        if (lTask.pubName != null && !lTask.pubName.equals(""))
            return  lTask.pubName;
        return "Không có tiêu đề";
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
            this.setButtonAlarmBackground(true);
            this.setButtonCheckListBackground(true);
            this.setButtonNoteBackground(true);
        }
        performFilter();
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
        Global.getInstance().pubNewTask = new TaskChecklist();
//        Global.getInstance().pubNewTask.pubName = "Danh sách ";
        this.createNewTask(TaskType.CheckList);
    }

    public void buttonAlarmAdd_onClick(View view) {
        Global.getInstance().pubNewTask = new TaskOneTimeReminder();
//        Global.getInstance().pubNewTask.pubName = "Nhắc nhở ";
        this.createNewTask(TaskType.OneTimeReminder);
    }

    public void textViewAddNote_onClick(View view) {
        Global.getInstance().pubNewTask = new TaskNote();
//        Global.getInstance().pubNewTask.pubName = "Ghi chú";
        this.createNewTask(TaskType.Note);
    }

    public void createNewTask(TaskType iTaskType){
        if (Global.getInstance().pubNewTask == null)
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
        this.priIsAscending = !this.priIsAscending;
        this.performSort();
        this.performFilter();
        this.priImageButtonSort.setImageResource(this.priIsAscending == true ?
                R.mipmap.ic_arrow_downward : R.mipmap.ic_arrow_upward);
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
        priSearchView.setQuery("", true);
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
