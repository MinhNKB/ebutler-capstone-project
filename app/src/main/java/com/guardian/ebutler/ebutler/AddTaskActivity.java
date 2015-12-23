package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AddTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Intent intent = getIntent();
        String taskName = intent.getStringExtra(Dashboard.EXTRA_TASKNAME);
        TextView textview = (TextView)findViewById(R.id.textCreateNewTask);
        textview.setText("Create " + taskName);
    }
}
