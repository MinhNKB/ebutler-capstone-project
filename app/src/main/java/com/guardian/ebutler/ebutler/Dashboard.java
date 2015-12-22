package com.guardian.ebutler.ebutler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guardian.ebutler.displaydata.Task;
import com.guardian.ebutler.displaydata.TaskList;

import java.util.Date;

public class Dashboard extends AppCompatActivity {

    private TaskList taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.EXTRA_USERNAME);
        TextView textview = (TextView)findViewById(R.id.user_name);
        textview.setText("Hi, " + username);
        getTasks();
        fetchToView();
    }

    private void fetchToView() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = null;
        LinearLayout taskList = (LinearLayout)findViewById(R.id.taskList);
        for(Task task : this.taskList.getTasksChronoOrder()) {
            layout = (LinearLayout) inflater.inflate(R.layout.task_layout, null);
            TextView text = (TextView)layout.findViewById(R.id.textTaskName);
            text.setText(task.name);
            text = (TextView)layout.findViewById(R.id.textTaskDate);
            text.setText(task.formattedDate("MM/dd HH:mm:ss"));
            View colorTab = layout.findViewById(R.id.colorTaskState);
            int color = 0;
            if (task.isDone) {
                color = ContextCompat.getColor(this, R.color.colorTaskGrayBlue);
            } else {
                Date currentDate = new Date();
                if (task.date.before(currentDate)) {
                    color = ContextCompat.getColor(this, R.color.colorTaskGreen);
                } else {
                    color = ContextCompat.getColor(this, R.color.colorTaskGreen);
                }
            }
            colorTab.setBackgroundColor(color);

            taskList.addView(layout);
        }
    }

    private void getTasks() {
        taskList = TaskList.getInstance();
    }
}
