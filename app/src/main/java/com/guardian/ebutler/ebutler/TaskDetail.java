package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Priority;
import com.guardian.ebutler.ebutler.dataclasses.Status;
import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

import java.util.Date;

public class TaskDetail extends Activity {

    TaskDetail priThis;

    private EditText priEditTextTaskName;
    private EditText priEditTextLocation;
    private EditText priEditTextTime;
    private EditText priEditTextDate;
    private ImageButton priButtonDone;
    private ImageButton priButtonCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_task_detail);

        this.findViewsByIds();
        priEditTextTaskName.setText(Global.getInstance().pubNewTask.pubName);
        this.priThis = this;
        bindNavigationLocation();
        revokeFocus();
    }

    private void findViewsByIds() {
        priEditTextTaskName = (EditText) findViewById(R.id.task_detail_editTextTaskName);
        priEditTextLocation = (EditText)findViewById(R.id.task_detail_editTextLocation);
        priEditTextTime = (EditText)findViewById(R.id.task_detail_editTextTime);
        priEditTextDate = (EditText)findViewById(R.id.task_detail_editTextDate);
        priButtonDone = (ImageButton)findViewById(R.id.task_detail_buttonDone);
        priButtonCancel = (ImageButton)findViewById(R.id.task_detail_buttonCancel);
    }

    static final int GET_LOCATION = 1;  // The request code
    static final int GET_TIME = 2;
    private void bindNavigationLocation() {
        final Context context = this;
        priEditTextLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                          @Override
                                          public void onFocusChange(View v, boolean hasFocus) {
                                              if (hasFocus) {
                                                  Intent intent = new Intent(context, ChooseLocation.class);
                                                  startActivityForResult(intent, GET_LOCATION);
                                              }
                                          }
                                      }
        );

        priEditTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                          @Override
                                          public void onFocusChange(View v, boolean hasFocus) {
                                              if (hasFocus) {
                                                  Intent intent = new Intent(context, ChooseTime.class);
                                                  startActivityForResult(intent, GET_TIME);
                                              }
                                          }
                                      }
        );

        priEditTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                     @Override
                                                     public void onFocusChange(View v, boolean hasFocus) {
                                                         if (hasFocus) {
                                                             Intent intent = new Intent(context, ChooseTime.class);
                                                             startActivityForResult(intent, GET_TIME);
                                                         }
                                                     }
                                                 }
        );

        priButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper iHelper = new DatabaseHelper(priThis);
                try
                {
                    Global.getInstance().pubNewTask.pubTime = new Date();
                    Global.getInstance().pubNewTask.pubStatus = Status.Done;
                    Global.getInstance().pubNewTask.pubPriority = Priority.Important;
                    iHelper.InsertATask(Global.getInstance().pubNewTask);
                }
                catch (Exception ex)
                {
                    String cause = ex.toString();
                }
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
            }
        });

        priButtonCancel.setOnClickListener(new View.OnClickListener() {
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
                String location = data.getStringExtra("location");
                priEditTextLocation.setText(location);
                revokeFocus();
            }
        } else if (requestCode == GET_TIME) {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra("time");
                priEditTextTime.setText(time);
                String date = data.getStringExtra("date");
                priEditTextDate.setText(date);
                revokeFocus();
            }
        }
    }

    private void revokeFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        View thisView = findViewById(R.id.task_detail_layout);
        thisView.requestFocus();
        thisView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                revokeFocus();
                return false;
            }
        });
    }
}
