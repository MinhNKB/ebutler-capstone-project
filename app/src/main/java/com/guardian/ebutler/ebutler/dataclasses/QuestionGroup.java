package com.guardian.ebutler.ebutler.dataclasses;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class QuestionGroup {
    public int pubId;
    public String pubQuestionString;
    public List<Question> pubQuestions = new ArrayList<Question>();
    public int pubDay;
    public int pubCategory;

    public boolean CheckValid()
    {
        if(pubQuestions==null || pubQuestions.size()==0)
            return false;
        for(int i=0;i<pubQuestions.size();i++)
            if(pubQuestions.get(i).pubStage==1 && pubQuestions.get(i).CheckValid()==false)
                return false;
        for(int i=0;i<pubQuestions.size();i++)
            if(pubQuestions.get(i).pubIsAsked==false && pubQuestions.get(i).CheckValid())
                return true;
        return false;
    }

    public boolean CheckValidEvenIfAsked() {
        if(pubQuestions==null || pubQuestions.size()==0)
            return false;
        for(int i=0;i<pubQuestions.size();i++)
            if(pubQuestions.get(i).pubStage==1 && pubQuestions.get(i).CheckValid()==false)
                return false;
        return true;
    }
}
