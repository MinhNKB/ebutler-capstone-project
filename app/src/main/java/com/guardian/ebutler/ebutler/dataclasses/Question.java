package com.guardian.ebutler.ebutler.dataclasses;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class Question {
    public int pubId;
    public String pubQuestionString;
    public String pubConditionExpressions;
    public String pubOptionsType;
    public List<String> pubInformationPropertyNames;
    public UIType pubUIType;
    public boolean pubIsAsked;
    public byte pubStage;
}
