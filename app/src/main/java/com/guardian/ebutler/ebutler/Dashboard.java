package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
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
import com.guardian.ebutler.fragments.MiniTaskFragment;
import com.guardian.ebutler.timehelper.DateTimeHelper;
import com.guardian.ebutler.world.Global;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Dashboard extends android.support.v4.app.FragmentActivity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priCustomListView;
    private CustomListAdapter priCustomListAdapter;

    private ExpandableListView priCustomExpandableListView;
    private CustomExpandableListAdapter priCustomExpandableListAdapter;

    private boolean priIsSearchView = false;
    private boolean priIsCalendarView = false;

    private boolean priIsAlarmFiltered = true;
    private boolean priIsChecklistFiltered = true;
    private boolean priIsNoteFiltered = true;

    private TaskComparator priSortType = TaskComparator.DATE_SORT;
    private boolean priIsAscending = true;
    private Spinner priSpinnerSort;

    private List<Task> priTaskList;
    private List<List<Task>> priExpandableTaskList;

    private LinearLayout priLinearLayoutAddTaskbar;
    private RelativeLayout priRelativeLayoutTaskbar;
    private LinearLayout priLinearLayoutSearch;
    private LinearLayout priLinearLayoutTopBar;

    private LinearLayout priLinearLayoutCalendarView;
    private LinearLayout getPriLinearLayoutCalendarViewPeek;
    private LinearLayout priLinearLayoutCalendarViewMiniTasks;
    private List<Task> priMiniTaskList;

    private ImageButton priImageButtonAlarm;
    private ImageButton priImageButtonCheckList;
    private ImageButton priImageButtonNote;
    private ImageButton priImageButtonSort;
    private ImageButton priImageButtonViewType;

    private CaldroidFragment priCaldroidFragment;
    private Calendar priCalendar;
    private Date priSelectedDate;

    private boolean priIsCustomListViewLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        Global.getInstance().pubSelectedTask = null;
        this.findViewsByIds();
        this.initSort();
//        this.initCustomListView();
        this.initCustomExpandableListView();
        this.initCalendarView();
        this.initSearchView();
        this.setupUI(findViewById(R.id.dashboard_parent));
        Global.getInstance().pubNewTask = null;

    }


    private void initCustomExpandableListView() {
        try {
            if (this.priTaskList == null){
                DatabaseHelper iHelper = new DatabaseHelper(this);
                this.priTaskList = iHelper.GetAllTasks();
                iHelper.close();
            }
            this.addTasksToCustomExpandableListView();
            this.setCustomExpandableListViewListeners();
        }
        catch (Exception ex){
            Log.w("wel", ex.toString());
        }
    }

    private void setCustomListViewListeners(){
        this.priCustomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTaskDetail((Task) priCustomListAdapter.getObject(position));
            }
        });

    }

    //To-do: navigate to taskdetail.java
    private void showTaskDetail(Task iTask) {
        //Toast.makeText(Dashboard.this, iTask.pubName, Toast.LENGTH_SHORT).show();
        Global.getInstance().pubSelectedTask = iTask;
        Intent lTaskDetailIntent = new Intent(this, TaskDetail.class);
        lTaskDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(lTaskDetailIntent);
    }

    private void setCustomExpandableListViewListeners() {
        this.priCustomExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                showTaskDetail(priExpandableTaskList.get(groupPosition).get(childPosition));
                return false;
            }
        });
    }


    public void addTasksToCustomExpandableListView(){
        this.priCustomExpandableListAdapter = new CustomExpandableListAdapter(this, getHeaders(), getGroupItems(this.priTaskList));
        this.priCustomExpandableListView.setAdapter(this.priCustomExpandableListAdapter);
        for (int i = 0; i < this.priCustomExpandableListAdapter.getGroupCount(); ++i)
            this.priCustomExpandableListView.expandGroup(i);
    }

    private List<String> getHeaders() {
        List<String> lResult = new ArrayList<String>();
        lResult.add("Hôm nay");
        lResult.add("Tuần này");
        lResult.add("Còn lại");
        return lResult;
    }

    private HashMap<String, List<CustomListItem>> getGroupItems(List<Task> iTaskList){
        HashMap<String, List<CustomListItem>> lResult = new HashMap<String, List<CustomListItem>>();
        this.priExpandableTaskList = new ArrayList<List<Task>>();
        List<Task> lToday = new ArrayList<Task>();
        List<Task> lThisWeek = new ArrayList<Task>();
        List<Task> lRemain = new ArrayList<Task>();
        for (Task lTask : iTaskList) {
            Date lCurrentDate = new Date();
            if (Global.getInstance().getZeroTimeDate(lTask.pubTime).equals(Global.getInstance().getZeroTimeDate(lCurrentDate)))
                lToday.add(lTask);
            else {
                long lRemainingTime = lTask.pubTime.getTime() - lCurrentDate.getTime();
                long hours = TimeUnit.MILLISECONDS.toHours(lRemainingTime);
                long days = hours/24;
                if (days <= 7 && days >= 0)
                    lThisWeek.add(lTask);
                else
                    lRemain.add(lTask);
            }
        }
        priExpandableTaskList.add(lToday);
        priExpandableTaskList.add(lThisWeek);
        priExpandableTaskList.add(lRemain);
        lResult.put("Hôm nay", getCustomItems(lToday));
        lResult.put("Tuần này", getCustomItems(lThisWeek));
        lResult.put("Còn lại", getCustomItems(lRemain));
        return lResult;
    }

    private void initCalendarView() {
        this.priCaldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        priCalendar = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, priCalendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, priCalendar.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidCustom);
        priCaldroidFragment.setArguments(args);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.dashboard_linearLayoutCalendarViewCalendar, priCaldroidFragment);
        t.commit();

        this.setCaldroidFragmentSelectedDate(this.priCalendar.getTime());
        this.initCalendarViewListeners();
        this.initeCalendarViewEvents();
    }

    private void initeCalendarViewEvents() {
        for (Task lTask: this.priTaskList
             ) {
            if (Global.getInstance().getZeroTimeDate(new Date()).equals(Global.getInstance().getZeroTimeDate(lTask.pubTime)))
                continue;
            Calendar lCalendar = Calendar.getInstance();
            lCalendar.setTime(lTask.pubTime);
            this.priCaldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.caldroid_custom_event), lCalendar.getTime());
            this.priCaldroidFragment.setTextColorForDate(R.color.background, lCalendar.getTime());
        }
        priCaldroidFragment.refreshView();
    }

    private void initCalendarViewListeners() {
        CaldroidListener lListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                setCaldroidFragmentSelectedDate(date);
            }
        };
        this.priCaldroidFragment.setCaldroidListener(lListener);
    }

    private void setCaldroidFragmentSelectedDate(Date iDate){
        if (priSelectedDate != null && priSelectedDate.equals(iDate)){
            priCaldroidFragment.refreshView();
            return;
        }
        priCaldroidFragment.clearSelectedDates();
        priCaldroidFragment.setSelectedDate(iDate);
        priSelectedDate = iDate;
        showTasksListPeek();
        priCaldroidFragment.refreshView();
    }


    private void showTasksListPeek() {
        if (priSelectedDate != null){
            this.priMiniTaskList = new ArrayList<Task>();
            this.priLinearLayoutCalendarViewMiniTasks.removeAllViews();
            for(int i = 0; i < this.priTaskList.size(); ++i)
                if (Global.getInstance().getZeroTimeDate(this.priTaskList.get(i).pubTime).equals(
                        Global.getInstance().getZeroTimeDate(priSelectedDate))){
                    this.priMiniTaskList.add(this.priTaskList.get(i));
                    getFragmentManager().beginTransaction().add(this.priLinearLayoutCalendarViewMiniTasks.getId(), MiniTaskFragment.newInstance(this.priTaskList.get(i))).commit();
                }
            this.getPriLinearLayoutCalendarViewPeek.setVisibility(this.priMiniTaskList.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void initSort() {
        this.initSpinnerSortListener();
    }

    private void initSpinnerSortListener() {
        this.priSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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


    public void initCustomListView()
    {
        if (priIsCustomListViewLoaded == true)
            return;
        try {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            this.priTaskList = iHelper.GetAllTasks();
            this.performSort();
            this.performFilter();
            this.setCustomListViewListeners();
            priIsCustomListViewLoaded = true;
        }
        catch (Exception ex){
            Log.w("wel", ex.toString());
        }
    }

    public void performSort(){
        if (this.priIsAscending) {
            Collections.sort(this.priTaskList, TaskComparator.acending(TaskComparator.getComparator(this.priSortType)));
        }
        else {
            Collections.sort(this.priTaskList, TaskComparator.decending(TaskComparator.getComparator(this.priSortType)));
        }
        this.addTasksListView();
    }

    public void addTasksListView(){
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
        if (iIsCalendarView == true)
            this.priCustomExpandableListView.setVisibility(View.GONE);
        else if (priIsSearchView == false)
            this.priCustomExpandableListView.setVisibility(View.VISIBLE);
        this.priLinearLayoutCalendarView.setVisibility(this.priIsCalendarView == true ? View.VISIBLE : View.GONE);
    }

    public void findViewsByIds() {
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.dashboard_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.dashboard_searchView);
        this.priCustomListView = (ListView) findViewById(R.id.dashboard_listViewTasks);
        this.priCustomExpandableListView = (ExpandableListView) findViewById(R.id.dashboard_expandableListViewTasks);
        this.priLinearLayoutSearch = (LinearLayout) findViewById(R.id.dashboard_linearLayoutSearch);
        this.priLinearLayoutTopBar = (LinearLayout) findViewById(R.id.dashboard_linearLayoutButlerBar);
        this.priImageButtonAlarm = (ImageButton) findViewById(R.id.dashboard_buttonAlarm);
        this.priImageButtonCheckList = (ImageButton) findViewById(R.id.dashboard_buttonCheckList);
        this.priImageButtonNote = (ImageButton) findViewById(R.id.dashboard_buttonNote);
        this.priImageButtonSort = (ImageButton) findViewById(R.id.dashboard_buttonSort);
        this.priImageButtonViewType = (ImageButton) findViewById(R.id.dashboard_buttonViewType);
        this.priSpinnerSort = (Spinner) findViewById(R.id.dashboard_spinnerSort);
        this.priLinearLayoutCalendarView = (LinearLayout) findViewById(R.id.dashboard_linearLayoutCalendarView);
        this.priLinearLayoutCalendarViewMiniTasks = (LinearLayout) findViewById(R.id.dashboard_linearLayoutCalendarViewMiniTasks);
        this.getPriLinearLayoutCalendarViewPeek = (LinearLayout) findViewById(R.id.dashboard_linearLayoutCalendarViewPeek);
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

        lCustomListItem = new CustomListItem(lTask, Global.getInstance().getTaskTypeEnum(lTask),
                lFirstLine, lSecondLine, lThirdLine, Global.getInstance().getTaskTypeDrawable(lTask));

//        lCustomListItem = new CustomListItem(Global.getInstance().getTaskTypeEnum(lTask),
//                lFirstLine, lSecondLine, lThirdLine, -1);

        return  lCustomListItem;
    }

    public static String getThirdLine(Task lTask) {
        String lThirdLine = null;
        switch (lTask.pubTaskType) {
            case PeriodicReminder:
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
                    lThirdLine = "Tạo vào lúc " + DateTimeHelper.getDateStringFromDate(lTask.pubTime);
                break;
        }
        return lThirdLine;
    }

    public static String getSecondLine(Task lTask) {
        String lSecondLine = null;
        switch (lTask.pubTaskType) {
            case PeriodicReminder:
            case OneTimeReminder:
                if (lTask.pubTime == null)
                    lSecondLine = "Không có thời gian";
                else
                    lSecondLine = "Nhắc vào lúc " + DateTimeHelper.getDateStringFromDate(lTask.pubTime);
                break;
            case CheckList:
                if (lTask.pubDescription == null || lTask.pubDescription.equals(""))
                    lSecondLine = "Không có danh sách";
                else {
                    lSecondLine = lTask.pubDescription.replace(":0", "");
                    lSecondLine = lSecondLine.replace(":1", "");
                    lSecondLine = lSecondLine.replace(",,", "");
                    lSecondLine = lSecondLine.replace(",", ", ");
                    if (lSecondLine.replace(", ", "").equals(""))
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

    public static String getFirstLine(Task lTask) {
        if (lTask.pubName != null && !lTask.pubName.equals(""))
            return  lTask.pubName;
        return "Không có tiêu đề";
    }


    public void buttonSearch_onClick(View view) {
        this.switchToSearchView(true);
    }

    public void switchToSearchView(boolean iIsSearchView){
        this.priIsSearchView = iIsSearchView;
        this.priLinearLayoutTopBar.setVisibility(iIsSearchView == true ? View.GONE : View.VISIBLE);
        this.priLinearLayoutSearch.setVisibility(iIsSearchView == false ? View.GONE : View.VISIBLE);
        this.priCustomExpandableListView.setVisibility(iIsSearchView == true ? View.GONE : View.VISIBLE);
        this.priCustomListView.setVisibility(iIsSearchView == false ? View.GONE : View.VISIBLE);

        if (iIsSearchView == true){
            this.priSearchView.setIconified(false);
            this.priSearchView.requestFocusFromTouch();
            this.initCustomListView();
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
        if (priIsSearchView == true){
            switchToSearchView(false);
            return;
        }
        else if (priIsCalendarView == true){
            switchView(false);
            return;
        }
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
        Global.getInstance().pubNewTask.pubTaskType = iTaskType;
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

    public void imageViewEbutler_onClick(View view) {
        Intent setIntent = new Intent(this, UserInfoInput.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
    }
}
