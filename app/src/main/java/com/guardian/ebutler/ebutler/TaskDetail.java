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

import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

public class TaskDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_task_detail);

        EditText lTaskName = (EditText) findViewById(R.id.task_detail_editTextTaskName);
        lTaskName.setText(Global.getInstance().pubNewTask.pubName);

        bindNavigationLocation();
        revokeFocus();
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
                Intent intent = new Intent(context, Dashboard.class);
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
                revokeFocus();
            }
        } else if (requestCode == GET_TIME) {
            if (resultCode == RESULT_OK) {
                EditText edit = (EditText)findViewById(R.id.task_detail_editTextTime);
                String time = data.getStringExtra("time");
                edit.setText(time);
                edit = (EditText)findViewById(R.id.task_detail_editTextDate);
                String date = data.getStringExtra("date");
                edit.setText(date);
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
