package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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

    }

    private void initializeCustomListView()
    {
        List<CustomListItem> lTasksList= this.getTasks();
        this.priCustomListAdapter = new CustomListAdapter(this, lTasksList);
        priCustomListView.setAdapter(this.priCustomListAdapter);
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



    private List<CustomListItem> getTasks() {
        List<CustomListItem> lResult = new ArrayList<CustomListItem>();
        CustomListItem lDashboardTask;

        lDashboardTask = new CustomListItem("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF1DE9B6);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF1DE9B6);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        lResult.add(lDashboardTask);

        lDashboardTask = new CustomListItem("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        lResult.add(lDashboardTask);

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
}
