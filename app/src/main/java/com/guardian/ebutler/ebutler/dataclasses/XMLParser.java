package com.guardian.ebutler.ebutler.dataclasses;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.guardian.ebutler.ebutler.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nkbmi on 3/23/2016.
 */
public class XMLParser {
    private static final String ns = null;
    private Context priContext;

    public XMLParser(Context iContext)
    {
        priContext = iContext;
    }

    public List ParseQuestionGroups() throws XmlPullParserException, IOException
    {
        InputStream lInput = priContext.getResources().openRawResource(R.raw.questions);
        List lResult = null;
        try{
            XmlPullParser lParser = Xml.newPullParser();
            lParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            lParser.setInput(lInput, null);
            lParser.nextTag();
            lResult = ReadQuestionGroups(lParser);
        }
        catch (Exception ex)
        {
            Log.w("XML",ex.getMessage());
        }
        finally {
            lInput.close();
            return lResult;
        }
    }

    private List ReadQuestionGroups(XmlPullParser iParser)throws XmlPullParserException, IOException {
        List lGroups = new ArrayList();

        iParser.require(XmlPullParser.START_TAG, ns, "Groups");
        while (iParser.next() != XmlPullParser.END_TAG) {
            if (iParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = iParser.getName();
            // Starts by looking for the entry tag
            if (name.equals("QuestionGroup")) {
                lGroups.add(ReadQuestionGroup(iParser));
            } else {
                Skip(iParser);
            }
        }
        return lGroups;
    }

    private QuestionGroup ReadQuestionGroup(XmlPullParser iParser) throws IOException, XmlPullParserException {
        iParser.require(XmlPullParser.START_TAG,ns,"QuestionGroup");
        QuestionGroup lQuesionGroupTemp = new QuestionGroup();

        while (iParser.next() != XmlPullParser.END_TAG) {
            if (iParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = iParser.getName();
            if (name.equals("QuestionString")) {
                lQuesionGroupTemp.pubQuestionString = ReadLeafTag(iParser, name);;
            } else if (name.equals("Questions")) {
                lQuesionGroupTemp.pubQuestions = ReadQuestions(iParser);
            }
            else {
                Skip(iParser);
            }
        }

        return  lQuesionGroupTemp;
    }

    private List<Question> ReadQuestions(XmlPullParser iParser) throws IOException, XmlPullParserException {
        List<Question> lQuestions = new ArrayList<Question>();

        iParser.require(XmlPullParser.START_TAG, ns, "Questions");
        while (iParser.next() != XmlPullParser.END_TAG) {
            if (iParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = iParser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Question")) {
                lQuestions.add(ReadQuestion(iParser));
            } else {
                Skip(iParser);
            }
        }
        return lQuestions;
    }

    private Question ReadQuestion(XmlPullParser iParser) throws IOException, XmlPullParserException {
        iParser.require(XmlPullParser.START_TAG,ns,"Question");
        Question lQuestion = new Question();

        while (iParser.next() != XmlPullParser.END_TAG) {
            if (iParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = iParser.getName();
            if (name.equals("QuestionString")) {
                lQuestion.pubQuestionString = ReadLeafTag(iParser, name);
            }
            else if (name.equals("Conditions")) {
                lQuestion.pubConditions = ReadLeafTag(iParser, name);
            }
            else if (name.equals("OptionsType")) {
                lQuestion.pubOptionsType = ReadLeafTag(iParser, name);
            }
            else if (name.equals("PropertiesNames")) {
                lQuestion.pubInformationPropertiesNames = ParsePropertiesNames(ReadLeafTag(iParser, name));
            }
            else if (name.equals("UIType")) {
                lQuestion.pubUIType = UIType.valueOf(ReadLeafTag(iParser, name));
            }
            else if (name.equals("Stage")) {
                lQuestion.pubStage = Integer.valueOf(ReadLeafTag(iParser, name));
            }
            else {
                Skip(iParser);
            }
        }

        return  lQuestion;
    }

    private ArrayList<String> ParsePropertiesNames(String iPropertiesNamesString) {

        String[] lTemp = iPropertiesNamesString.replace(" ","").split(";");
        ArrayList<String> lResult = new ArrayList<String>(Arrays.asList(lTemp));
        return lResult;
    }

    private String ReadLeafTag(XmlPullParser iParser, String iTagName) throws IOException, XmlPullParserException {
        iParser.require(XmlPullParser.START_TAG, ns, iTagName);
        String QuestionString = ReadText(iParser);
        iParser.require(XmlPullParser.END_TAG, ns, iTagName);
        return QuestionString;
    }


    private String ReadText(XmlPullParser iParser) throws IOException, XmlPullParserException {
        String lResult = "";
        if (iParser.next() == XmlPullParser.TEXT) {
            lResult = iParser.getText();
            iParser.nextTag();
        }
        return lResult;
    }


    private void Skip(XmlPullParser iParser) throws XmlPullParserException, IOException {
        if (iParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (iParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
