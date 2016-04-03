package com.guardian.ebutler.ebutler.dataclasses;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class ScriptManager {

    private Context priContext;
    private List<QuestionGroup> priQuestionGroups;
    private QuestionGroup priCurrentQuestionGroup;
    private Question priCurrentQuestion;
    private Map<String,List<String>> priScripts;

    public ScriptManager(Context iContext)
    {
        priContext = iContext;
        LoadAll();
        if(priQuestionGroups.size()==0) {
            PushQuestionData();
            LoadAll();
        }

        //Set the current group
        priCurrentQuestionGroup = GetASuitableQuestionGroup();

        priScripts = GetScripts();

    }

    //region Question
    public void PushQuestionData()
    {
        try {
            XMLParser lParser = new XMLParser(priContext);
            DatabaseHelper lDBHelper = DatabaseHelper.getInstance(priContext);
            List<QuestionGroup> lGroups = lParser.ParseQuestionGroups();

            for(int i=0;i<lGroups.size();i++)
            {
                lDBHelper.InsertAQuestionGroup(lGroups.get(i));
            }

//            Question lQuestionTemp = new Question();
//            lQuestionTemp.pubQuestionString = "Họ tên của bạn là";
//            lQuestionTemp.pubStage = 1;
//            lQuestionTemp.pubIsAsked = false;
//            lQuestionTemp.pubUIType = UIType.Textbox;
//            lQuestionTemp.pubConditions = null;
//            lQuestionTemp.pubInformationPropertiesNames = new ArrayList<String>();
//            lQuestionTemp.pubOptionsType = null;
//
//            QuestionGroup lQuestionGroupTemp = new QuestionGroup();
//            lQuestionGroupTemp.pubQuestions.add(lQuestionTemp);
//            priQuestionGroups.add(lQuestionGroupTemp);
//
//

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Date date = new Date();
//        DatabaseHelper lDBHelper = new DatabaseHelper(null);
//        Condition lLastDateAsked = lDBHelper.GetUserInformation("LastDateAsked");
//        boolean lIsAsked = false;
//        if(lLastDateAsked==null)
//        {
//            List<Condition> lTemp = new ArrayList<Condition>();
//            lLastDateAsked = new Condition();
//            lLastDateAsked.pubConditionName = "LastDateAsked";
//            lLastDateAsked.pubValue = dateFormat.format(date);
//            lLastDateAsked.pubType = "Date";
//            lTemp.add(lLastDateAsked);
//            lDBHelper.InsertUserInformations(lTemp);
//        }
//        {
//            if(lLastDateAsked.pubValue.equals(dateFormat.format(date)))
//            {
//                lIsAsked = true;
//            }
//            else
//            {
//                lLastDateAsked.pubValue = dateFormat.format(date);
//                lDBHelper.UpdateUserInformations(lLastDateAsked);
//            }
//        }
//
//        if(lIsAsked==true)
//            return null;

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
            {
                if(priCurrentQuestionGroup.pubQuestions.get(i).pubIsAsked==false) {
                    priCurrentQuestion = priCurrentQuestionGroup.pubQuestions.get(i);
                    return priCurrentQuestion;
                }
            }
            else
            {
                DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
                priCurrentQuestionGroup.pubQuestions.get(i).pubIsAsked = true;
                lHelper.UpdateAQuestion(priCurrentQuestionGroup.pubQuestions.get(i));
            }
        }
        return null;
    }

    public void AnwserQuestion(List<Condition> iNewInformation)
    {
        DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
        if(iNewInformation!=null) {
            lHelper.InsertUserInformations(iNewInformation);
        }
        priCurrentQuestion.pubIsAsked = true;
        lHelper.UpdateAQuestion(priCurrentQuestion);
    }
    //endregion

    //region Refresh
    public double GetProgress(){
        int lCountAnwseredQuestions = 0;
        for(int i=0;i<priQuestionGroups.size();i++) {
            if(!priQuestionGroups.get(i).CheckValid())
                lCountAnwseredQuestions++;
        }
        return (double)lCountAnwseredQuestions/(double)priQuestionGroups.size();
    }

    public void Refresh()
    {
        priCurrentQuestionGroup = GetASuitableQuestionGroup();
    }
    //endregion

    //region Scripts
    private Map<String, List<String>> GetScripts() {
        try {
            XMLParser lParser = new XMLParser(priContext);
            return lParser.ParseScripts();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String GetAGreeting()
    {
        Date lCurrentDate = new Date();
        int lCurrentHour = lCurrentDate.getHours();
        if(lCurrentHour<=11)
            return GetAScript("Morning");
        else if(lCurrentHour<=15)
            return GetAScript("Afternoon");
        return GetAScript("Evening");
    }

    public String GetAFinishString()
    {
        return GetAScript("Thanks");
    }

    private String GetAScript(String lType) {
        List<String> lScripts = priScripts.get(lType);
        int lMax = lScripts.size();
        Random lRandom = new Random();
        int  lIndex = lRandom.nextInt(lMax);
        return lScripts.get(lIndex);
    }
    //endregion

}
