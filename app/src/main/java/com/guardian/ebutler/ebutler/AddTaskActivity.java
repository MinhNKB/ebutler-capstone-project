package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.guardian.ebutler.displaydata.Task;
import com.guardian.ebutler.displaydata.TaskList;
import com.guardian.ebutler.screenhelper.FullScreenHelper;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_add_task);
        Intent intent = getIntent();
        String taskName = intent.getStringExtra(Dashboard.EXTRA_TASKNAME);
        EditText editText = (EditText)findViewById(R.id.editCreateNewTaskName);
        editText.setText(taskName);
        bindEventButton();
        revokeFocus();
    }

    private void revokeFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        View thisView = findViewById(R.id.new_task_layout);
        thisView.requestFocus();
        thisView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                revokeFocus();
                return false;
            }
        });
    }

    private void bindEventButton() {
        final Button createTaskButton = (Button)findViewById(R.id.buttonCreateTask);
        final Activity thisActivity = this;
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskList taskList = TaskList.getInstance();
                String taskName = ((EditText)findViewById(R.id.editCreateNewTaskName)).getText().toString();
                DatePicker datePicker = (DatePicker)findViewById(R.id.dateCreateNewTask);
                int day  = datePicker.getDayOfMonth();
                int month= datePicker.getMonth();
                int year = datePicker.getYear();
                TimePicker timePicker = (TimePicker)findViewById(R.id.timeCreateNewTask);
                int hrs = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hrs, minute);
                Date date = calendar.getTime();
                taskList.tasks.add(new Task(taskName, date, false));
                Intent intent = new Intent(thisActivity, Dashboard.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
