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

import java.util.ArrayList;
import java.util.List;


public class TaskList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;
    private ImageView priButtonRoundAdd;

    private ListView priListViewTask;
    private List<CustomListItem> priTaskList;
    private CustomListAdapter priCustomListAdapter;

    private ImageButton priButtonAdd;
    private ImageButton priButtonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        this.findViewsByIds();

        this.priTaskList = this.getCategories();
        this.priCustomListAdapter = new CustomListAdapter(this, this.priTaskList);
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
                priButtonRoundAdd.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private boolean searchView_onTextChange(String newText)
    {
        if (newText != null && !newText.equals(""))
            this.priButtonRoundAdd.setImageResource(R.mipmap.ic_add_round);
        else
            this.priButtonRoundAdd.setImageResource(R.mipmap.ic_add_round_disabled);
        this.priCustomListAdapter.getFilter().filter(newText);
        return true;
    }

    private void findViewsByIds() {
        this.priButtonRoundAdd = (ImageView) findViewById(R.id.task_list_buttonRoundAdd);
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.task_list_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.task_list_searchView);
        this.priListViewTask = (ListView) findViewById(R.id.task_list_listViewTask);
        this.priButtonAdd = (ImageButton) findViewById(R.id.task_list_buttonAdd);
        this.priButtonSearch = (ImageButton) findViewById(R.id.task_list_buttonSearch);
    }

    public List<CustomListItem> getCategories() {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lTask;

        lTask = new CustomListItem("Thanh toán tiền nước", "Hàng tháng", null, R.color.transparent);
        result.add(lTask);

        lTask = new CustomListItem("Đóng tiền học phí", "Học kỳ", null, R.color.transparent);
        result.add(lTask);

        lTask = new CustomListItem("Đóng tiền bảo hiểm", "Hàng năm", null, R.color.transparent);
        result.add(lTask);

        lTask = new CustomListItem("Thánh toán tiền điện thoại", "Hàng tháng", null, R.color.transparent);
        result.add(lTask);

        lTask = new CustomListItem("Đóng phí quản lý chung cư", "Mỗi 6 tháng", null, R.color.transparent);
        result.add(lTask);

        lTask = new CustomListItem("Thanh toán tiền internet", "Hàng tháng", null, R.color.transparent);
        result.add(lTask);

        return result;
    }


    public void buttonSearch_onClick(View view) {
        this.priButtonRoundAdd.setVisibility(View.VISIBLE);
        this.priTextViewButlerSpeech.setVisibility(View.GONE);
        this.priSearchView.setVisibility(View.VISIBLE);
        this.priSearchView.requestFocusFromTouch();
        this.priSearchView.setIconified(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.priSearchView, InputMethodManager.SHOW_IMPLICIT);
    }

    public void buttonRoundAdd_onClick(View view) {
        Intent intent = new Intent(this, TaskDetail.class);
        startActivity(intent);
    }

    public void buttonAdd_onClick(View view) {
        this.priButtonRoundAdd.setVisibility(View.VISIBLE);
        this.priTextViewButlerSpeech.setVisibility(View.GONE);
        this.priSearchView.setVisibility(View.VISIBLE);
        this.priSearchView.requestFocusFromTouch();
        this.priSearchView.setIconified(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.priSearchView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity iActivity) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText) || !(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(TaskList.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
