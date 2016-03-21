package com.guardian.ebutler.ebutler.dataclasses;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class ScriptManager {

    private Context priContext;
    private List<QuestionGroup> priQuestionGroups;
    private QuestionGroup priCurrentQuestionGroup;

    public ScriptManager(Context iContext)
    {
        priContext = iContext;
        LoadAll();
        if(priQuestionGroups.size()==0)
            PushQuestionData();

        //Set the current group
        priCurrentQuestionGroup = GetASuitableQuestionGroup();
    }

    public void PushQuestionData()
    {
        Question lQuestionTemp = new Question();
        lQuestionTemp.pubQuestionString = "Họ tên của bạn là";
        lQuestionTemp.pubStage = 1;
        lQuestionTemp.pubIsAsked = false;
        lQuestionTemp.pubUIType = UIType.Textbox;
        lQuestionTemp.pubConditions = null;
        lQuestionTemp.pubInformationPropertiesNames = new ArrayList<String>();
        lQuestionTemp.pubOptionsType = null;

        QuestionGroup lQuestionGroupTemp = new QuestionGroup();
        lQuestionGroupTemp.pubQuestions.add(lQuestionTemp);
        priQuestionGroups.add(lQuestionGroupTemp);

        DatabaseHelper lDBHelper = DatabaseHelper.getInstance(priContext);
        lDBHelper.InsertAQuestionGroup(lQuestionGroupTemp);
    }

    public void LoadAll()
    {
        DatabaseHelper lDBHelper = new DatabaseHelper(priContext);
        //Load all question group data from the DB
        priQuestionGroups = lDBHelper.GetAllQuestionGroup();
    }

    private QuestionGroup GetASuitableQuestionGroup() {
        if(priQuestionGroups==null)
            return null;
        for(int i=0;i<priQuestionGroups.size();i++)
        {
            if(priQuestionGroups.get(i).CheckValid())
                return priQuestionGroups.get(i);
        }
        return null;
    }

    public Question GetAQuestion()
    {
        if(priCurrentQuestionGroup==null)
            return null;
        for(int i=0;i<priCurrentQuestionGroup.pubQuestions.size();i++)
        {
            if(priCurrentQuestionGroup.pubQuestions.get(i).CheckValid())
                return priCurrentQuestionGroup.pubQuestions.get(i);
        }
        return null;
    }

    public void AnwserQuestion()
    {

    }


}
