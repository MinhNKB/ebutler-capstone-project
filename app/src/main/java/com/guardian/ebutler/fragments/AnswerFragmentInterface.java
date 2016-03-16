package com.guardian.ebutler.fragments;

import android.view.View;

import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * Created by Tabuzaki IA on 3/16/2016.
 */
public interface AnswerFragmentInterface {
    void setValuesToView(View view);
    ArrayList<Condition> getValues();
    String getChatStatement();
}
