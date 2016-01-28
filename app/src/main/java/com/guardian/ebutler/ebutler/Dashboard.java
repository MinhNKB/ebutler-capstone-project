package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Activity {

    private ListView listView;
    private ExtendedCalendarView extendedCalendarView;
    private ImageButton buttonArrow;
    private LinearLayout miniTaskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        List<DashboardTask> tasks = this.GetTasks();
        this.listView = (ListView) findViewById(R.id.listView_tasks);
        listView.setAdapter(new DashboardAdapter(this, tasks));

        this.extendedCalendarView = (ExtendedCalendarView)findViewById(R.id.extendedCalendar);
        this.extendedCalendarView.setGesture(ExtendedCalendarView.LEFT_RIGHT_GESTURE);
        this.extendedCalendarView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                if (day.getMonth() == 1 && (day.getDay() == 27 || day.getDay() == 28))
                    miniTaskView.setVisibility(View.VISIBLE);
                else
                    miniTaskView.setVisibility(View.GONE);
            }
        });

        this.buttonArrow = (ImageButton) findViewById(R.id.button_arrow);
        this.miniTaskView = (LinearLayout) findViewById(R.id.miniTaskView);

        bindNavigationLocation();
    }

    private void bindNavigationLocation() {
        final Context context = this;

        Button button = (Button)findViewById(R.id.dashboard_buttonAddTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryDetail.class);
                startActivity(intent);
            }
        });
    }

    private List<DashboardTask> GetTasks()
    {
        List<DashboardTask> result = new ArrayList<DashboardTask>();
        DashboardTask dashboardTask;

        dashboardTask = new DashboardTask("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF1DE9B6);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Ủi đồ", "Sáng nay 8 giờ", "Còn 10 phút nữa", 0xFF1DE9B6);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Đóng tiền điện", "Sáng nay 8 giờ 45 phút", "1.200.000 VND", 0xFF2979FF);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        result.add(dashboardTask);

        dashboardTask = new DashboardTask("Thể dục buổi sáng", "Sáng nay 7 giờ 30 phút", "Đã trễ 20 phút", 0xFFFF1744);
        result.add(dashboardTask);

        return result;
    }

    public void button_arrow_onclick(View view)
    {
        if (this.extendedCalendarView.getVisibility() == View.GONE)
        {
            this.extendedCalendarView.setVisibility(View.VISIBLE);
            this.listView.setVisibility(View.GONE);
            this.buttonArrow.setImageResource(R.mipmap.ic_arrow_up_black);
        }
        else
        {
            this.extendedCalendarView.setVisibility(View.GONE);
            this.listView.setVisibility(View.VISIBLE);
            this.buttonArrow.setImageResource(R.mipmap.ic_arrow_down_black);
            this.miniTaskView.setVisibility(View.GONE);
        }
    }

    public void miniTaskView_onClick(View view)
    {
        this.extendedCalendarView.setVisibility(View.GONE);
        this.listView.setVisibility(View.VISIBLE);
        this.buttonArrow.setImageResource(R.mipmap.ic_arrow_down_black);
        this.miniTaskView.setVisibility(View.GONE);
    }
}
