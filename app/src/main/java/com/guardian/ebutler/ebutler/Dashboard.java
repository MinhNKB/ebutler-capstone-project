package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Location;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.world.Global;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.List;

enum ViewState
{
    ListView,
    CalendarView
}


public class Dashboard extends Activity {

    private ImageView priButtonRoundAdd;
    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priCustomListView;
    private CustomListAdapter priCustomListAdapter;

    private List<Task> priTaskList;

    private ExtendedCalendarView priExtendedCalendarView;
    private ImageButton priButtonArrow;
    private LinearLayout priMiniTaskView;

    private ViewState priViewState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.findViewsByIds();

        this.priViewState = ViewState.ListView;

        this.initializeCustomListView();
        this.initializeExtendedCalendarView();
        this.initSearchView();

        setupUI(findViewById(R.id.dashboard_layout));
    }


    private void initializeCustomListView()
    {
        this.priTaskList = DatabaseHelper.getTasks();
        List<CustomListItem> lTasksList= this.getCustomItems(this.priTaskList);
        this.priCustomListAdapter = new CustomListAdapter(this, lTasksList);
        this.priCustomListView.setAdapter(this.priCustomListAdapter);
    }

    private void initSearchView() {
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
                priSearchView.setVisibility(View.GONE);
                priTextViewButlerSpeech.setVisibility(View.VISIBLE);
                priButtonRoundAdd.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private boolean searchView_onTextChange(String newText)
    {
        if (newText != null && !newText.equals(""))
            this.priButtonRoundAdd.setImageResource(R.mipmap.ic_add_round);
        else
            this.priButtonRoundAdd.setImageResource(R.mipmap.ic_add_round_disabled);
        switchToListView();
        this.priCustomListAdapter.getFilter().filter(newText);
        return true;
    }

    private void initializeExtendedCalendarView() {
        this.priExtendedCalendarView.setGesture(ExtendedCalendarView.LEFT_RIGHT_GESTURE);
        this.priExtendedCalendarView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                if (day.getMonth() == 1 && (day.getDay() == 27 || day.getDay() == 28))
                    priMiniTaskView.setVisibility(View.VISIBLE);
                else
                    priMiniTaskView.setVisibility(View.GONE);
            }
        });

    }
    private void findViewsByIds() {
        this.priButtonRoundAdd = (ImageView) findViewById(R.id.dashboard_buttonRoundAdd);
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.dashboard_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.dashboard_searchView);
        this.priCustomListView = (ListView) findViewById(R.id.dashboard_listViewTasks);
        this.priExtendedCalendarView = (ExtendedCalendarView) findViewById(R.id.dashboard_extendedCalendar);
        this.priButtonArrow = (ImageButton) findViewById(R.id.dashboard_buttonArrow);
        this.priMiniTaskView = (LinearLayout) findViewById(R.id.dashboard_miniTaskView);
    }


    private List<CustomListItem> getCustomItems(List<Task> iTaskList) {
        List<CustomListItem> lResult = new ArrayList<CustomListItem>();
        CustomListItem lCustomListItem;

        for (Task lTask: iTaskList
             ) {
            String lLocationName = "";
            for (Location lLocation: lTask.pubLocation
                 ) {
                lLocationName += lLocation.pubName;
            }
            lCustomListItem = new CustomListItem(lTask.pubName, lTask.pubTime.toString(), lLocationName, 0xFFFF1744);
            lResult.add(lCustomListItem);
        }
        return lResult;
    }

    public void buttonArrow_onclick(View view) {
        switch (this.priViewState) {
            case ListView:
                this.switchToCalendarView();
                break;
            case CalendarView:
                this.switchToListView();
                break;
        }
    }

    private void switchToCalendarView() {
        this.priExtendedCalendarView.setVisibility(View.VISIBLE);
        this.priCustomListView.setVisibility(View.GONE);
        this.priButtonArrow.setImageResource(R.mipmap.ic_arrow_up_black);
        this.priViewState = ViewState.CalendarView;
    }

    private void switchToListView() {
        this.priExtendedCalendarView.setVisibility(View.GONE);
        this.priCustomListView.setVisibility(View.VISIBLE);
        this.priButtonArrow.setImageResource(R.mipmap.ic_arrow_down_black);
        this.priMiniTaskView.setVisibility(View.GONE);
        this.priViewState = ViewState.ListView;
    }

    public void miniTaskView_onClick(View view) {
        //To-do: add filter
        this.switchToListView();
    }

    public void buttonAdd_onClick(View view) {
        Global.getInstance().pubNewTask = new Task();
        Intent intent = new Intent(this, CategoryList.class);
        startActivity(intent);
    }

    public void buttonSearch_onClick(View view) {
        this.priButtonRoundAdd.setVisibility(View.VISIBLE);
        this.priTextViewButlerSpeech.setVisibility(View.GONE);
        this.priSearchView.setVisibility(View.VISIBLE);
        this.priSearchView.requestFocusFromTouch();
        this.priSearchView.setIconified(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.priSearchView, InputMethodManager.SHOW_IMPLICIT);
    }

    public void buttonRoundAdd_onClick(View view) {
        if (this.priSearchView.getQuery().toString() != null && !this.priSearchView.getQuery().toString().equals("")) {
            Intent intent = new Intent(this, TaskDetail.class);
            startActivity(intent);
        }
    }


    public static void hideSoftKeyboard(Activity iActivity) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText) || !(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Dashboard.this);
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
}
