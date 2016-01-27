package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        List<DashboardTask> tasks = this.GetTasks();
        final ListView listview = (ListView) findViewById(R.id.listView_tasks);
        listview.setAdapter(new DashboardAdapter(this, tasks));
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

}
