package com.guardian.ebutler.ebutler.dataclasses;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class Question {
    public int pubId;
    public String pubQuestionString;
    public String pubConditions;
    public String pubOptionsType;
    public List<String> pubInformationPropertyNames;
    public UIType pubUIType;
    public boolean pubIsAsked;
    public byte pubStage;

    public boolean CheckValid()
    {
        return true;
    }
}
