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

            if(lConditionInDB == null || lConditionInDB.pubValue.equals(""))
            {
                if(lValue.equals("null") || lValue.equals(""))
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
                if(lValue.equals("null") || lValue.equals(""))
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
        } //------------------------------------------
        else if(pubOptionsType.equals("VehicleTypes"))
        {
            lResult.add("car");
            lResult.add("motor");
            lResult.add("bike");
            lResult.add("walk");
        }
        else if(pubOptionsType.equals("HomeAppliances"))
        {
            lResult.add("fridge");
            lResult.add("conditioner");
            lResult.add("washing_machine");
        }
        else if(pubOptionsType.equals("Vaccinations"))
        {
            lResult.add("measles");
            lResult.add("mumps");
            lResult.add("hepatitis");
            lResult.add("varicella");
            lResult.add("rubella");
        }
        else if(pubOptionsType.equals("TravelTypes"))
        {
            lResult.add("relax");
            lResult.add("explore");
            lResult.add("venture");
        }
        else if(pubOptionsType.equals("FoodStyles"))
        {
            lResult.add("europe");
            lResult.add("asian");
        }
        else if(pubOptionsType.equals("BookTypes"))
        {
            lResult.add("romance");
            lResult.add("adventure");
            lResult.add("science");
            lResult.add("horror");
            lResult.add("fiction");
            lResult.add("journals");
        }
        else if(pubOptionsType.equals("MusicTypes"))
        {
            lResult.add("pop");
            lResult.add("rock");
            lResult.add("rap");
            lResult.add("jazz");
            lResult.add("country");
            lResult.add("electronic");
        }
        else if(pubOptionsType.equals("PetTypes"))
        {
            lResult.add("dog");
            lResult.add("cat");
            lResult.add("other");
        }
        else if(pubOptionsType.equals("JobTypes"))
        {
            lResult.add("student");
            lResult.add("worker");
            lResult.add("officestaff");
            lResult.add("engineer");
            lResult.add("farmer");
            lResult.add("otherjob");
        }
        return lResult;
    }
}
