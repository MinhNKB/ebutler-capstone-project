package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Priority;
import com.guardian.ebutler.ebutler.dataclasses.Status;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;
import com.guardian.ebutler.fragments.tasks.AbstractTaskFragment;
import com.guardian.ebutler.fragments.tasks.NoteFragment;
import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetail extends Activity {

    TaskDetail priThis;

    private EditText priEditTextTaskName;
    private EditText priEditTextLocation;
    private EditText priEditTextTime;
    private EditText priEditTextDate;
    private ImageButton priButtonDone;
    private ImageButton priButtonCancel;
    private View priTaskFragmentContainer;
    private AbstractTaskFragment priTaskFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_task_detail);

        findViewsByIds();
        if (Global.getInstance().pubNewTask != null) {
            priEditTextTaskName.setText(Global.getInstance().pubNewTask.pubName);
        }
        createTaskFragment();
        priThis = this;
        bindNavigationLocation();
        setupUI(findViewById(R.id.task_detail_parent));

    }

    private void createTaskFragment() {
        switch (Global.getInstance().pubTaskType) {
            case Note:
                priTaskFragment = NoteFragment.newInstance();
                break;
            default:
                break;
        }
        getFragmentManager().beginTransaction().add(priTaskFragmentContainer.getId(), priTaskFragment).commit();
    }

    private void findViewsByIds() {
        priEditTextTaskName = (EditText) findViewById(R.id.task_detail_editTextTaskName);
        priEditTextLocation = (EditText) findViewById(R.id.task_detail_editTextLocation);
        priEditTextTime = (EditText) findViewById(R.id.task_detail_editTextTime);
        priEditTextDate = (EditText) findViewById(R.id.task_detail_editTextDate);
        priButtonDone = (ImageButton) findViewById(R.id.task_detail_buttonDone);
        priButtonCancel = (ImageButton) findViewById(R.id.task_detail_buttonCancel);
        priTaskFragmentContainer = findViewById(R.id.task_detail_TaskFragmentContainer);
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
                if (priEditTextTaskName.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(TaskDetail.this, "Bạn phải nhập tên công việc", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                DatabaseHelper iHelper = new DatabaseHelper(priThis);
                Task lNewTask = Global.getInstance().pubNewTask;
                if (lNewTask == null) {
                    lNewTask = new Task();
                    Global.getInstance().pubNewTask = lNewTask;
                }
                lNewTask.pubTime = priThis.getTimeFromDateTextbox();
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

        priButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    private Date getTimeFromDateTextbox() {
        SimpleDateFormat lDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date lDate = null;
        try {
            lDate = lDateFormat.parse(priEditTextDate.getText() + " " + priEditTextTime.getText());
        } catch (ParseException e) {
            lDate = null;
        } finally {
            if (lDate == null) {
                lDate = new Date();
            }
        }
        return lDate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_LOCATION) {
            if (resultCode == RESULT_OK) {
                String location = data.getStringExtra("location");
                priEditTextLocation.setText(location);
            }
        } else if (requestCode == GET_TIME) {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra("time");
                priEditTextTime.setText(time);
                String date = data.getStringExtra("date");
                priEditTextDate.setText(date);
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
