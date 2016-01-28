package com.guardian.ebutler.ebutler.wizard.model;

import com.example.android.wizardpager.wizard.model.*;
import com.guardian.ebutler.ebutler.wizard.model.UserInfoFormPage;

import android.content.Context;

public class UserWizardModel extends AbstractWizardModel {
    public UserWizardModel(Context context) {
        super(context);
    }

    public Context getCurrentContext() { return mContext; }

    @Override
    protected PageList onNewRootPageList() {
        //TODO:(nthoang/task1) Put wizard pages from 0 to n - 1 here (n is the last page)
        return new PageList(
            new UserInfoFormPage(this, "")
        );

    }
}
