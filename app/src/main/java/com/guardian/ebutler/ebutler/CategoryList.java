package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CategoryList extends Activity {

    private TextView priTextViewButlerSpeech;
    private SearchView priSearchView;
    private ImageView priButtonRoundAdd;

    private ListView priListViewCategory;
    private List<CustomListItem> priCategoryList;
    private CustomListAdapter priCustomListAdapter;

    private ImageButton priButtonAdd;
    private ImageButton priButtonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        this.findViewsByIds();

        this.priCategoryList = this.getCategories();
        this.priCustomListAdapter = new CustomListAdapter(this, this.priCategoryList);
        this.priListViewCategory.setAdapter(this.priCustomListAdapter);

        this.initListeners();
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

    public List<CustomListItem> getCategories() {
        List<CustomListItem> result = new ArrayList<CustomListItem>();
        CustomListItem lCategory;

        lCategory = new CustomListItem("Hoa don", "Dien, nuoc, truyen hinh cam, ...", null, R.color.transparent);
        result.add(lCategory);

        lCategory = new CustomListItem("Thiet bi", "Bao tri tu lanh, may dieu hoa, ...", null, R.color.transparent);
        result.add(lCategory);

        lCategory = new CustomListItem("Suc khoe", "Kham rang, do mat, so giun, ...", null, R.color.transparent);
        result.add(lCategory);

        lCategory = new CustomListItem("Viec can lam", "Di cho, don con, ...", null, R.color.transparent);
        result.add(lCategory);

        lCategory = new CustomListItem("Loai cong viec nguoi dung them moi", "Vi du 1, vi du 2, ...", null, R.color.transparent);
        result.add(lCategory);

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
        Intent intent = new Intent(this, TaskList.class);
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
}
