package com.guardian.ebutler.ebutler.dataclasses;
import android.content.Context;
import android.util.Log;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class Question {
    public int pubId;
    public String pubQuestionString;
    public String pubConditions;
    public String pubOptionsType;
    public ArrayList<String> pubInformationPropertiesNames;
    public UIType pubUIType;
    public boolean pubIsAsked;
    public int pubStage;

    private Context priContext;


    public boolean CheckValid()
    {
        try {
            if (pubConditions == null || pubConditions.equals(""))
                return true;

            List<String> lConditionsList = GetConditionsFromString(pubConditions);

            for(int i=0;i<lConditionsList.size();i++)
            {
                if(CheckACondition(lConditionsList.get(i))==false)
                    return false;
            }

            return true;
        }
        catch (Exception ex)
        {
            Log.w("Question",ex.getMessage());
            return false;
        }
    }

    private boolean CheckACondition(String iConditionString) {
        try {
            String[] lTemp = null;
            if(iConditionString.contains("=="))
            {
                lTemp = iConditionString.replace(" ","").split("==");
            }
            else if(iConditionString.contains("!="))
            {
                lTemp = iConditionString.replace(" ","").split("!=");
            }


            String lPropertyName,lValue;
            lPropertyName = lTemp[0];
            lValue = lTemp[1];

            DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
            Condition lConditionInDB = lHelper.GetUserInformation(lPropertyName);

            if(lConditionInDB == null)
            {
                if(lValue.equals("null"))
                {
                    if(iConditionString.contains("=="))
                        return true;
                    else if (iConditionString.contains("!="))
                        return false;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if(lValue.equals("null"))
                {
                    if(iConditionString.contains("=="))
                        return false;
                    else if (iConditionString.contains("!="))
                        return true;
                }

                if(iConditionString.contains("=="))
                {
                    if (lValue.equals(lConditionInDB.pubValue.replace(" ", "")))
                        return true;
                    return false;
                }
                else if (iConditionString.contains("!="))
                {
                    if (!lValue.equals(lConditionInDB.pubValue.replace(" ", "")))
                        return true;
                    return false;
                }
                else
                {
                    double lRight,lLeft;
                    lRight = Double.parseDouble(lValue);
                    lLeft = Double.parseDouble(lConditionInDB.pubValue);
                    if(iConditionString.contains(">"))
                        if(lLeft>lRight)
                            return true;
                        else
                            return false;
                    else if(iConditionString.contains(">"))
                        if(lLeft<lRight)
                            return true;
                        else
                            return false;
                }
            }
            return false;
        }
        catch (Exception ex)
        {
            Log.w("Question",ex.getMessage());
            return false;
        }
    }

    private List<String> GetConditionsFromString(String iConditionsString) {
        String[] lTemp = iConditionsString.replace(" ","").split(";");
        ArrayList<String> lResult = new ArrayList<String>(Arrays.asList(lTemp));
        return lResult;
    }

    public ArrayList<String> getOptions() {
        ArrayList<String> lResult = new ArrayList<String>();
        if(pubOptionsType.equals("Gender"))
        {
            lResult.add("male");
            lResult.add("female");
        }
        else if(pubOptionsType.equals("Weekdays"))
        {
            lResult.add("monday");
            lResult.add("tuesday");
            lResult.add("wednesday");
            lResult.add("thursday");
            lResult.add("friday");
            lResult.add("saturday");
            lResult.add("sunday");
        }
        else if(pubOptionsType.equals("MaritalStatus"))
        {
            lResult.add("single");
            lResult.add("married");
        }
        else if(pubOptionsType.equals("BillTypes"))
        {
            lResult.add("phone");
            lResult.add("cable");
            lResult.add("electric");
            lResult.add("water");
        }
        return lResult;
    }
}
