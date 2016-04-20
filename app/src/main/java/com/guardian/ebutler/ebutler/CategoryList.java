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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.custom.CustomListItem;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;


public class CategoryList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;

    private ListView priListViewCategory;
    private List<CustomListItem> priCategoryCustomList;
    private CustomListAdapter priCustomListAdapter;

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
                Global.getInstance().pubNewTask.pubRepeat = priCategoryList.get(position);
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
        this.priTextViewButlerSpeech = (TextView) findViewById(R.id.category_list_textViewButlerSpeech);
        this.priSearchView = (SearchView) findViewById(R.id.category_list_searchView);
        this.priListViewCategory = (ListView) findViewById(R.id.category_list_listViewCategory);
        this.priButtonSearch = (ImageButton) findViewById(R.id.category_list_buttonSearch);
    }

    public List<CustomListItem> getCategories(List<String> iCategoryList) {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lCustomListItem;

        for (String lCategory: iCategoryList
             ) {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            //List<String> lTaskList = iHelper.GetAllTasks(lCategory);
            String lTasks = "";
//            for (String lTask: lTaskList
//                 ) {
//                lTasks += (lTask + ", ");
//            }
            if (lTasks.equals(""))
                lTasks = null;
            lCustomListItem = new CustomListItem(null,
                    lCategory, lTasks, null, Global.getInstance().getCategoryColor(this, lCategory));
            result.add(lCustomListItem);
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

    public static void showSoftKeyboard(Activity iActivity, int iType) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iActivity.getCurrentFocus() != null)
            lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), iType);
    }

    public void setupUI(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(CategoryList.this, InputMethodManager.HIDE_NOT_ALWAYS);
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
