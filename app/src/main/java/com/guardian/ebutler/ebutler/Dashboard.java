package com.guardian.ebutler.ebutler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Debug;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.ebutler.displaydata.Task;
import com.guardian.ebutler.displaydata.TaskList;
import com.luksprog.dp.adapter.FilterWithSpaceAdapter;

import java.util.ArrayList;
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
        revokeFocus();
        bindEventTextView();
    }

    public final static String EXTRA_TASKNAME = "com.guardian.ebutler.ebutler.ADD_TASKNAME";

    private void bindEventTextView() {
        final FilterWithSpaceAdapter<String> adapter = new FilterWithSpaceAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.commandView);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.clear();
                adapter.add("Add " + s.toString() + " to task list");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Context context = this;
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String command = parent.getItemAtPosition(position).toString();
                textView.setText("");
                if (command.startsWith("Add ") && command.endsWith(" to task list")) {
                    Intent intent = new Intent(context, AddTaskActivity.class);
                    command = command.replace("Add ", "");
                    command = command.replace(" to task list", "");
                    intent.putExtra(EXTRA_TASKNAME, command);
                    startActivity(intent);
                }
            }
        });
    }

    private void revokeFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        View thisView = findViewById(R.id.dashboard_layout);
        thisView.requestFocus();
        thisView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                revokeFocus();
                return false;
            }
        });
    }

    private void fetchToView() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout template;
        LinearLayout taskList = (LinearLayout)findViewById(R.id.taskList);
        for(Task task : this.taskList.getTasksChronoOrder()) {
            template = (RelativeLayout) inflater.inflate(R.layout.task_layout, null);
            TextView text = (TextView)template.findViewById(R.id.textTaskName);
            text.setText(task.name);
            text = (TextView)template.findViewById(R.id.textTaskDate);
            text.setText(task.formattedDate("MM/dd HH:mm:ss"));
            View colorTab = template.findViewById(R.id.colorTaskState);
            int color = 0;
            if (task.isDone) {
                color = ContextCompat.getColor(this, R.color.colorTaskGreen);
            } else {
                Date currentDate = new Date();
                if (task.date.before(currentDate)) {
                    color = ContextCompat.getColor(this, R.color.colorTaskRed);
                } else {
                    color = ContextCompat.getColor(this, R.color.colorTaskYellow);
                }
            }
            colorTab.setBackgroundColor(color);

            taskList.addView(template);
        }
    }

    private void getTasks() {
        taskList = TaskList.getInstance();
    }
}
