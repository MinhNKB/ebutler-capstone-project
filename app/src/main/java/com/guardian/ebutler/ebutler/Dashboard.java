package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Activity {

    private ListView listView;
    private ExtendedCalendarView extendedCalendarView;
    private ImageButton buttonArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        List<DashboardTask> tasks = this.GetTasks();
        this.listView = (ListView) findViewById(R.id.listView_tasks);
        listView.setAdapter(new DashboardAdapter(this, tasks));

        this.extendedCalendarView = (ExtendedCalendarView)findViewById(R.id.extendedCalendar);
        this.extendedCalendarView.setGesture(ExtendedCalendarView.LEFT_RIGHT_GESTURE);

        this.buttonArrow = (ImageButton) findViewById(R.id.button_arrow);

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
        }
    }
}
