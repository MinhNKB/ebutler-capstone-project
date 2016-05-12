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
import com.guardian.ebutler.fragments.tasks.AbstractTaskFragment;
import com.guardian.ebutler.fragments.tasks.AlarmFragment;
import com.guardian.ebutler.fragments.tasks.CheckListFragment;
import com.guardian.ebutler.fragments.tasks.NoteFragment;
import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

public class TaskDetail extends Activity {

    TaskDetail priThis;

    private EditText priEditTextTaskName;
    private ImageButton priButtonDone;
    private ImageButton priButtonCancel;
    private ImageButton priButtonDelete;
    private View priTaskFragmentContainer;
    private AbstractTaskFragment priTaskFragment;
    private Task priTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_task_detail);
        findViewsByIds();
        priTask = getNewTask();
        setTaskDetails();
        createTaskFragment();
        priThis = this;
        bindNavigationLocation();
        priButtonDelete.setVisibility(Global.getInstance().pubSelectedTask == null ? View.GONE : View.VISIBLE);
        setupUI(findViewById(R.id.task_detail_parent));

    }

    private void setTaskDetails() {
        if (Global.getInstance().pubNewTask != null) {
            priEditTextTaskName.setText(Global.getInstance().pubNewTask.pubName);
        }

        if(Global.getInstance().pubSelectedTask!=null)
        {
            priEditTextTaskName.setText(Global.getInstance().pubSelectedTask.pubName);
        }
    }

    private void createTaskFragment() {
        switch (priTask.pubTaskType) {
            case Note:
                priTaskFragment = NoteFragment.newInstance();
                break;
            case CheckList:
                priTaskFragment = CheckListFragment.newInstance(this);
                break;
            case OneTimeReminder:
                priTaskFragment = AlarmFragment.newInstance();
                break;
            case PeriodicReminder:
                priTaskFragment = AlarmFragment.newInstance();
                break;
            default:
                break;
        }
        getFragmentManager().beginTransaction().add(priTaskFragmentContainer.getId(), priTaskFragment).commit();
    }

    private void findViewsByIds() {
        priEditTextTaskName = (EditText) findViewById(R.id.task_detail_editTextTaskName);
        priButtonDone = (ImageButton) findViewById(R.id.task_detail_buttonDone);
        priButtonCancel = (ImageButton) findViewById(R.id.task_detail_buttonCancel);
        priButtonDelete = (ImageButton) findViewById(R.id.task_detail_buttonDelete);
        priTaskFragmentContainer = findViewById(R.id.task_detail_TaskFragmentContainer);
    }

    private void bindNavigationLocation() {
        final Context context = this;
        priButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper iHelper = new DatabaseHelper(priThis);
                priTask.pubName = priEditTextTaskName.getText().toString();
                priTask.pubStatus = Status.Pending;
                if (priTask.pubRepeat == null || priTask.pubRepeat.equals(""))
                    priTask.pubRepeat = "Khác";
                priTask.pubPriority = Priority.Important;

                priTaskFragment.getValues(priTask);
                if (Global.getInstance().pubSelectedTask == null)
                    iHelper.InsertATask(priTask);
                else
                    iHelper.UpdateATask(priTask);
                clearGlobalTask();

                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        priButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBackToDashboard();
            }
        });

        priButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper lHelper = new DatabaseHelper(priThis);
                lHelper.DeleteATask(priTask);
                getBackToDashboard();
            }
        });
    }

    private Task getNewTask() {
        if (Global.getInstance().pubSelectedTask != null)
            return Global.getInstance().pubSelectedTask;
        Task lNewTask = Global.getInstance().pubNewTask;
        if (lNewTask == null) {
            lNewTask = new Task();
            Global.getInstance().pubNewTask = lNewTask;
        }
        return lNewTask;
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

    public void getBackToDashboard(){
        clearGlobalTask();
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        getBackToDashboard();
    }

    public void clearGlobalTask(){
        Global.getInstance().pubNewTask = null;
        Global.getInstance().pubSelectedTask = null;
    }
}
