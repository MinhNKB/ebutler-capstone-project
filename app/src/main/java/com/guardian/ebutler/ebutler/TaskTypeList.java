package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.custom.CustomListItem;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;


public class TaskTypeList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priListViewTaskType;
    private List<CustomListItem> priTaskTypeCustomList;
    private CustomListAdapter priCustomListAdapter;

    private ImageButton priButtonSearch;

    private TaskType[] priTaskTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_type_list);

        this.findViewsByIds();

        this.priTaskTypeCustomList = this.getTaskTypes();
        this.priCustomListAdapter = new CustomListAdapter(this, this.priTaskTypeCustomList);
        this.priListViewTaskType.setAdapter(this.priCustomListAdapter);
        this.priTaskTypeList = TaskType.values();

        this.initListeners();
        setupUI(findViewById(R.id.task_type_list_layout));
    }

    private void initListeners() {
        this.initSearchViewListeners();
        this.initListViewTaskTypeListeners();
    }

    private void initListViewTaskTypeListeners()
    {

        this.priListViewTaskType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent lIntent;
                if (Global.getInstance().pubNewTask == null)
                    Global.getInstance().pubNewTask = new Task();
                Global.getInstance().pubTaskType = priTaskTypeList[position];
                if (Global.getInstance().pubTaskType == TaskType.PeriodicReminder)
                    lIntent = new Intent(TaskTypeList.this, CategoryList.class);
                else
                    lIntent = new Intent(TaskTypeList.this, TaskDetail.class);
                startActivity(lIntent);
        }
        });
    }

    private void initSearchViewListeners() {
        this.priSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return searchView_onTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return searchView_onTextChange(newText);
            }
        });

        this.priSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                priSearchView.setVisibility(View.GONE);
                priTextViewButlerSpeech.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    private boolean searchView_onTextChange(String newText)
    {
        this.priCustomListAdapter.getFilter().filter(newText);
        return true;
    }

    private void findViewsByIds() {
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.task_type_list_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.task_type_list_searchView);
        this.priListViewTaskType = (ListView) findViewById(R.id.task_type_list_listViewTaskType);
        this.priButtonSearch = (ImageButton) findViewById(R.id.task_type_list_buttonSearch);
    }

    public List<CustomListItem> getTaskTypes() {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lCustomListItem;

        TaskType[] lTaskTypeList = TaskType.values();

        for (TaskType lTaskType: lTaskTypeList
                ) {
            Pair<String, String> lTaskTypeInfo = this.getTaskTypeString(lTaskType);
            lCustomListItem = new CustomListItem(null,
                    lTaskTypeInfo.first, null, null, Global.getInstance().getTaskTypeDrawable(this, lTaskType));
            result.add(lCustomListItem);
        }
        return result;
    }

    public Pair<String, String> getTaskTypeString(TaskType iTaskType){
        switch (iTaskType){
            case Note:
                return new Pair<String, String>(getResources().getString(R.string.task_type_name_note), getResources().getString(R.string.task_type_description_note));
            case CheckList:
                return new Pair<String, String>(getResources().getString(R.string.task_type_name_checkList), getResources().getString(R.string.task_type_description_checkList));
            case PeriodicReminder:
                return new Pair<String, String>(getResources().getString(R.string.task_type_name_periodicReminder), getResources().getString(R.string.task_type_description_periodicReminder));
            case OneTimeReminder:
                return new Pair<String, String>(getResources().getString(R.string.task_type_name_oneTimeReminder), getResources().getString(R.string.task_type_description_oneTimeReminder));
            default:
                return null;
        }
    }


    public void buttonSearch_onClick(View view) {
        this.priTextViewButlerSpeech.setVisibility(View.GONE);
        this.priSearchView.setVisibility(View.VISIBLE);
        this.priSearchView.requestFocusFromTouch();
        this.priSearchView.setIconified(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.priSearchView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showSoftKeyboard(Activity iActivity, int iType) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iActivity.getCurrentFocus() != null)
            lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), iType);
    }

    public void setupUI(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(TaskTypeList.this, InputMethodManager.HIDE_NOT_ALWAYS);
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
