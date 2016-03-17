package com.guardian.ebutler.ebutler.dataclasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkbmi on 3/16/2016.
 */
public class QuestionGroup {
    public int pubId;
    public String pubQuestionString;
    public List<Question> pubQuestions = new ArrayList<Question>();

    public boolean CheckValid()
    {
        return true;
    }
}
