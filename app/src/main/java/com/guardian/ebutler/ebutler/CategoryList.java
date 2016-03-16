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

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;


public class CategoryList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;
    private ImageView priButtonRoundAdd;

    private ListView priListViewCategory;
    private List<CustomListItem> priCategoryCustomList;
    private CustomListAdapter priCustomListAdapter;

    private ImageButton priButtonAdd;
    private ImageButton priButtonSearch;

    private List<String> priCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        this.findViewsByIds();

        DatabaseHelper iHelper = new DatabaseHelper(this);
        this.priCategoryList = iHelper.GetAllCategories();
        this.priCategoryCustomList = this.getCategories(this.priCategoryList);
        this.priCustomListAdapter = new CustomListAdapter(this, this.priCategoryCustomList);
        this.priListViewCategory.setAdapter(this.priCustomListAdapter);

        this.initListeners();
        setupUI(findViewById(R.id.category_list_layout));
    }

    private void initListeners() {
        this.initSearchViewListeners();
        this.initListViewCategoryListeners();
    }

    private void initListViewCategoryListeners()
    {
        final Intent lIntent = new Intent(this, TaskList.class);
        this.priListViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.getInstance().pubNewTask.pubCategory = priCategoryList.get(position);
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
        this.priButtonRoundAdd = (ImageView) findViewById(R.id.category_list_buttonRoundAdd);
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.category_list_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.category_list_searchView);
        this.priListViewCategory = (ListView) findViewById(R.id.category_list_listViewCategory);
        this.priButtonAdd = (ImageButton) findViewById(R.id.category_list_buttonAdd);
        this.priButtonSearch = (ImageButton) findViewById(R.id.category_list_buttonSearch);
    }

    public List<CustomListItem> getCategories(List<String> iCategoryList) {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lCustomListItem;

        for (String lCategory: iCategoryList
             ) {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            List<String> lTaskList = iHelper.GetAllTasks(lCategory);
            String lTasks = "";
            for (String lTask: lTaskList
                 ) {
                lTasks += (lTask + ", ");
            }
            lCustomListItem = new CustomListItem(lCategory, lTasks, null, 0xFFFF1744);
            result.add(lCustomListItem);
        }
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
        if (this.priSearchView.getQuery().toString() != null && !this.priSearchView.getQuery().toString().equals("")) {
            Intent intent = new Intent(this, TaskList.class);
            startActivity(intent);
        }
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
                    hideSoftKeyboard(CategoryList.this);
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