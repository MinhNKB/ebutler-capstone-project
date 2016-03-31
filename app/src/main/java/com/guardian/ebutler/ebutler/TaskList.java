package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.custom.CustomListItem;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;


public class TaskList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priListViewTask;
    private List<CustomListItem> priTaskCustomList;
    private CustomListAdapter priCustomListAdapter;
    private List<String> priTaskList;

    private ImageButton priButtonAdd;
    private ImageButton priButtonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        this.findViewsByIds();

        DatabaseHelper iHelper = new DatabaseHelper(this);
        this.priTaskList = iHelper.GetAllTasks(Global.getInstance().pubNewTask.pubCategory);
        this.priTaskCustomList = this.getCategories(this.priTaskList);
        this.priCustomListAdapter = new CustomListAdapter(this, this.priTaskCustomList);
        this.priListViewTask.setAdapter(this.priCustomListAdapter);

        this.initListeners();

        setupUI(findViewById(R.id.task_list_layout));
    }

    private void initListeners() {
        this.initSearchViewListeners();
        this.initListViewTaskListeners();
    }

    private void initListViewTaskListeners()
    {
        final Intent lIntent = new Intent(this, TaskDetail.class);
        this.priListViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.getInstance().pubNewTask.pubName = priTaskList.get(position);
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
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.task_list_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.task_list_searchView);
        this.priListViewTask = (ListView) findViewById(R.id.task_list_listViewTask);
        this.priButtonAdd = (ImageButton) findViewById(R.id.task_list_buttonAdd);
        this.priButtonSearch = (ImageButton) findViewById(R.id.task_list_buttonSearch);
    }

    public List<CustomListItem> getCategories(List<String> iTaskList) {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lCustomItem;

        for (String lTask: iTaskList
             ) {
            lCustomItem = new CustomListItem(lTask, null, null, R.color.transparent);
            result.add(lCustomItem);
        }

        return result;
    }


    public void buttonSearch_onClick(View view) {
        this.priTextViewButlerSpeech.setVisibility(View.GONE);
        this.priSearchView.setVisibility(View.VISIBLE);
        this.priSearchView.requestFocusFromTouch();
        this.priSearchView.setIconified(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.priSearchView, InputMethodManager.SHOW_IMPLICIT);
    }

    public void buttonAdd_onClick(View view) {
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
                showSoftKeyboard(TaskList.this, InputMethodManager.HIDE_NOT_ALWAYS);
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
