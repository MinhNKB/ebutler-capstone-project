package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Priority;
import com.guardian.ebutler.ebutler.dataclasses.Status;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

import java.util.Date;

public class TaskDetail extends Activity {

    TaskDetail priThis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_task_detail);

        EditText lTaskName = (EditText) findViewById(R.id.task_detail_editTextTaskName);
        lTaskName.setText(Global.getInstance().pubNewTask.pubName);
        this.priThis = this;
        bindNavigationLocation();
        setupUI(findViewById(R.id.task_detail_parent));

    }
    static final int GET_LOCATION = 1;  // The request code
    static final int GET_TIME = 2;
    private void bindNavigationLocation() {
        EditText edit = (EditText)findViewById(R.id.task_detail_editTextLocation);
        final Context context = this;
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                          @Override
                                          public void onFocusChange(View v, boolean hasFocus) {
                                              if (hasFocus) {
                                                  Intent intent = new Intent(context, ChooseLocation.class);
                                                  startActivityForResult(intent, GET_LOCATION);
                                              }
                                          }
                                      }
        );

        edit = (EditText)findViewById(R.id.task_detail_editTextTime);
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                          @Override
                                          public void onFocusChange(View v, boolean hasFocus) {
                                              if (hasFocus) {
                                                  Intent intent = new Intent(context, ChooseTime.class);
                                                  startActivityForResult(intent, GET_TIME);
                                              }
                                          }
                                      }
        );

        edit = (EditText)findViewById(R.id.task_detail_editTextDate);
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                          @Override
                                          public void onFocusChange(View v, boolean hasFocus) {
                                              if (hasFocus) {
                                                  Intent intent = new Intent(context, ChooseTime.class);
                                                  startActivityForResult(intent, GET_TIME);
                                              }
                                          }
                                      }
        );

        ImageButton button = (ImageButton)findViewById(R.id.task_detail_buttonDone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper iHelper = new DatabaseHelper(priThis);

                Task lNewTask = Global.getInstance().pubNewTask;
                if (lNewTask == null)
                    lNewTask = new Task();
                lNewTask.pubTime = new Date();
                lNewTask.pubStatus = Status.Pending;
                if (lNewTask.pubCategory == null || lNewTask.pubCategory.equals(""))
                    lNewTask.pubCategory = "Khác";
                lNewTask.pubPriority = Priority.Important;
                iHelper.InsertATask(Global.getInstance().pubNewTask);
                Global.getInstance().pubNewTask = null;

                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button = (ImageButton)findViewById(R.id.task_detail_buttonCancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_LOCATION) {
            if (resultCode == RESULT_OK) {
                EditText edit = (EditText)findViewById(R.id.task_detail_editTextLocation);
                String location = data.getStringExtra("location");
                edit.setText(location);
            }
        } else if (requestCode == GET_TIME) {
            if (resultCode == RESULT_OK) {
                EditText edit = (EditText)findViewById(R.id.task_detail_editTextTime);
                String time = data.getStringExtra("time");
                edit.setText(time);
                edit = (EditText)findViewById(R.id.task_detail_editTextDate);
                String date = data.getStringExtra("date");
                edit.setText(date);
            }
        }
    }

    public static void showSoftKeyboard(Activity iActivity, int iType) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iActivity.getCurrentFocus() != null)
            lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), iType);
    }

    public void setupUI(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(TaskDetail.this, InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}
