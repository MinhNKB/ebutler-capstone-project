package com.guardian.ebutler.ebutler.wizard.model;

import android.support.v4.app.Fragment;

import com.example.android.wizardpager.wizard.model.ModelCallbacks;
import com.example.android.wizardpager.wizard.model.Page;
import com.example.android.wizardpager.wizard.model.ReviewItem;
import com.guardian.ebutler.ebutler.wizard.ui.UserInfoFormFragment;

import java.util.ArrayList;

public class UserInfoFormPage extends Page {

    public static final String NAME_DATA_KEY = "name";
    public static final String GENDER_DATA_KEY = "gender";
    public static final String DOB_DATA_KEY = "dob";
    public static final String ADDRESS_DATA_KEY = "address";
    public static final String MARRIEDSTATS_DATA_KEY = "marriedstats";

    public UserInfoFormPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserInfoFormFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Your Name", mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Your Gender", mData.getString(GENDER_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Your DOB", mData.getString(DOB_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Your Address", mData.getString(ADDRESS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Your Married Status", mData.getString(MARRIEDSTATS_DATA_KEY), getKey(), -1));
    }
}
