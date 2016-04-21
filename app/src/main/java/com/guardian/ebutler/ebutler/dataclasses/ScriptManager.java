package com.guardian.ebutler.ebutler.dataclasses;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.timehelper.DateTimeHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
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
    private List<QuestionGroup> priCurrentQuestionGroups;
    private Question priCurrentQuestion;
    private Map<String,List<String>> priScripts;
    private Map<String,TaskSuggestionInformation> priTaskSuggestions;

    public ScriptManager(Context iContext)
    {
        priContext = iContext;
        LoadAll();
        if(priQuestionGroups.size()==0) {
            PushQuestionData();
            LoadAll();
        }

        //Set the current group
        priCurrentQuestionGroups = GetSuitableQuestionGroups();

        priScripts = GetScripts();
        priTaskSuggestions = GetSuggestion();

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

    private List<QuestionGroup> GetSuitableQuestionGroups() {
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
            if(priQuestionGroups.get(i).CheckValid()) {
                List<QuestionGroup> lQuestionGroup = new ArrayList<QuestionGroup>();
                lQuestionGroup.add(priQuestionGroups.get(i));
                return lQuestionGroup;
            }
        }
        return null;
    }

    private List<QuestionGroup> GetQuestionsInCategory(int iCategoryId) {
        if(priQuestionGroups == null) {
            return null;
        }

        List<QuestionGroup> lAvailableQuestionGroups = new ArrayList<>();
        for(int i = 0; i < priQuestionGroups.size(); i++) {
            if(priQuestionGroups.get(i).pubCategory == iCategoryId)
                lAvailableQuestionGroups.add(priQuestionGroups.get(i));
        }

        if (lAvailableQuestionGroups.size() < 1) {
            return null;
        }

        for(QuestionGroup lQuestionGroup: lAvailableQuestionGroups) {
            for(Question lQuestion: lQuestionGroup.pubQuestions) {
                lQuestion.pubIsAsked = false;
            }
            if (!lQuestionGroup.CheckValidEvenIfAsked()) {
                return null;
            }
        }
        return lAvailableQuestionGroups;
    }

    public Question GetAQuestion()
    {
        if(priCurrentQuestionGroups == null) {
            return null;
        }
        for(QuestionGroup lQuestionGroup :  priCurrentQuestionGroups) {
            for(int i=0;i< lQuestionGroup.pubQuestions.size();i++)
            {
                if(lQuestionGroup.pubQuestions.get(i).CheckValid())
                {
                    if(!lQuestionGroup.pubQuestions.get(i).pubIsAsked) {
                        priCurrentQuestion = lQuestionGroup.pubQuestions.get(i);

                        return priCurrentQuestion;
                    }
                }
                else
                {
                    DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
                    lQuestionGroup.pubQuestions.get(i).pubIsAsked = true;
                    lHelper.UpdateAQuestion(lQuestionGroup.pubQuestions.get(i));
                }
            }
        }
        return null;
    }

    public Task AnwserQuestion(List<Condition> iNewInformation)
    {
        DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
        priCurrentQuestion.pubIsAsked = true;
        lHelper.UpdateAQuestion(priCurrentQuestion);
        if(iNewInformation!=null) {
            lHelper.InsertUserInformations(iNewInformation);
            if(iNewInformation.size()==1 && iNewInformation.get(0).pubType=="Date")
            {
                if(priTaskSuggestions.containsKey(iNewInformation.get(0).pubConditionName))
                {
                    return GetSuggestedTask(iNewInformation.get(0),priTaskSuggestions.get(iNewInformation.get(0).pubConditionName));
                }
            }
        }
        return null;
    }

    private Task GetSuggestedTask(Condition iCondition, TaskSuggestionInformation iTaskSuggestionInformation) {
        try {
            SimpleDateFormat lDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date lInputDate = lDateFormat.parse(iCondition.pubValue);

            String[] lTimes = iTaskSuggestionInformation.pubTime.replace(" ","").split(";");

            lInputDate.setMonth(lInputDate.getMonth()+Integer.parseInt(lTimes[0]));
            lInputDate.setDate(lInputDate.getDate() + Integer.parseInt(lTimes[1]));
            lInputDate.setHours(7);

            Date lCurrentDate = new Date();
            if(lInputDate.before(lCurrentDate))
            {
                int lDiffDays = (int) ((lCurrentDate.getTime() - lInputDate.getTime())/ (1000 * 60 * 60 * 24));
                if(lDiffDays>=Math.abs(Integer.parseInt(lTimes[1])))
                {
                    String[] lRepeat = iTaskSuggestionInformation.pubRepeat.replace(" ", "").split(";");
                    lInputDate.setYear(lInputDate.getYear() + Integer.parseInt(lRepeat[0]));
                    lInputDate.setMonth(lInputDate.getMonth() + Integer.parseInt(lRepeat[1]));
                    lInputDate.setDate(lInputDate.getDate() + Integer.parseInt(lRepeat[2]));
                }
                else
                {
                    lInputDate = lCurrentDate;
                    lInputDate.setDate(lInputDate.getDate() + 1);
                    lInputDate.setHours(7);
                }
            }
            Task lNewTask = new Task();

            lNewTask.pubName = iTaskSuggestionInformation.pubTaskName;
            lNewTask.pubLocation = new ArrayList<Location>();
            lNewTask.pubTime = lInputDate;
            lNewTask.pubStatus = Status.Pending;
            lNewTask.pubPriority = Priority.Normal;
            lNewTask.pubTaskType = TaskType.OneTimeReminder;
            lNewTask.pubRepeat = "Không lặp lại";
            return lNewTask;
        } catch (ParseException e) {
            Log.w("ScriptManager",e.getMessage());
            return null;
        }
    }
    //endregion

    //region Refresh
    public List<Boolean> GetProgress(){
//        int lCountAnwseredQuestions = 0;
//        for(int i=0;i<priQuestionGroups.size();i++) {
//            if(!priQuestionGroups.get(i).CheckValid())
//                lCountAnwseredQuestions++;
//        }
//        return (double)lCountAnwseredQuestions/(double)priQuestionGroups.size();
        //TODO: Hardcoding 5 groups
        ArrayList lReturnProgress = new ArrayList<Boolean>(){{
            add(true);
            add(true);
            add(true);
            add(true);
            add(true);
        }};
        for (QuestionGroup lQuestionGroup: priQuestionGroups) {
            if (lQuestionGroup.CheckValid()) {
                Log.w("cool", Integer.toString(lQuestionGroup.pubCategory));
                lReturnProgress.set(lQuestionGroup.pubCategory - 1, false);
            }
        }
        return lReturnProgress;
    }

    public void Refresh(int iCategoryId)
    {
        priCurrentQuestionGroups = GetQuestionsInCategory(iCategoryId);
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
        if(lCurrentHour<=10)
            return GetAScript("Morning");
        else if(lCurrentHour<=17)
            return GetAScript("Afternoon");
        return GetAScript("Evening");
    }

    public Spanned CreateTaskNotification(String rTasks, Boolean iIsHtml) {

        String lRawString = "Sắp tới là ngày: <br/>" + rTasks;
        Spanned lReturnValue = null;
        if (iIsHtml) {
            lReturnValue = Html.fromHtml(lRawString);
        } else {
            lReturnValue = new SpannableString(lRawString);
        }
        return lReturnValue;
    }

    public String ToMapAPIChatStatement() {
        return "Sắp tới bạn có chi phí cần phải thanh toán, bạn có thể click vào đây để xem qua những địa điểm thanh toán gần nhất.";
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


    //region TaskSuggestions
    private Map<String, TaskSuggestionInformation> GetSuggestion() {
        try {
            XMLParser lParser = new XMLParser(priContext);
            return lParser.ParseTaskSuggestions();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String GetAQuestionGroupString() {
        if (priCurrentQuestionGroups == null) {
            return "";
        }
        if (priCurrentQuestionGroups.size() > 0) {
            return priCurrentQuestionGroups.get(0).pubQuestionString;
        }
        return "";
    }
    //endregion
}
