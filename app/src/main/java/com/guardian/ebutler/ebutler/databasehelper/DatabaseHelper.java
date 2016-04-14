package com.guardian.ebutler.ebutler.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.guardian.ebutler.alarm.AlarmService;
import com.guardian.ebutler.ebutler.dataclasses.*;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by nkbmi on 3/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Database information
    private static final String DATABASE_NAME = "eButlerDatabase";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper priInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (priInstance == null) {
            priInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return priInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private Context priContext;
    public DatabaseHelper(Context iContext) {
        super(iContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.priContext = iContext;
    }

    @Override
    public void onCreate(SQLiteDatabase iDB) {
        iDB.execSQL("CREATE TABLE Location (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    Name varchar(255),\n" +
                "    Address varchar(255),\n" +
                "    CoorX float NOT NULL,\n" +
                "    CoorY float NOT NULL\n" +
                ");");

        iDB.execSQL("CREATE TABLE Task (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    Name varchar(255)  NOT NULL,\n" +
                "    Category varchar(255) ,\n" +
                "    TaskType varchar(255)  NOT NULL,\n" +
                "    Description text,\n" +
                "    Time varchar(255),\n" +
                "    Priority varchar(50)  NOT NULL,\n" +
                "    Status varchar(50)  NOT NULL\n" +
                ");");

        iDB.execSQL("CREATE TABLE Task_Location (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    TaskId integer  NOT NULL,\n" +
                "    LocationId integer  NOT NULL,\n" +
                "    FOREIGN KEY (TaskId) REFERENCES Task (Id),\n" +
                "    FOREIGN KEY (LocationId) REFERENCES Location (Id)\n" +
                ");");

        iDB.execSQL("CREATE TABLE QuestionGroup (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    QuestionString text  NOT NULL, \n" +
                "    Day integer  NOT NULL, \n" +
                "    Category integer  NOT NULL \n" +
                ");");

        iDB.execSQL("CREATE TABLE Question (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    QuestionString text  NOT NULL,\n" +
                "    Condition text,\n" +
                "    OptionTypes varchar(255),\n" +
                "    PropertiesNames text,\n" +
                "    UIType varchar(255)  NOT NULL,\n" +
                "    IsAsked boolean  NOT NULL,\n" +
                "    Stage integer  NOT NULL,\n" +
                "    QuestionGroup_Id integer  NOT NULL,\n" +
                "    FOREIGN KEY (QuestionGroup_Id) REFERENCES QuestionGroup (Id)\n" +
                ");\n");

        iDB.execSQL("CREATE TABLE UserInformation (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY  AUTOINCREMENT,\n" +
                "    PropertyName varchar(255)  NOT NULL,\n" +
                "    Value varchar(255)  NOT NULL,\n" +
                "    Type varchar(255)  NOT NULL\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase iDB, int iOldVersion, int iNewVersion) {

    }

    //region General
    public List<String> GetAllCategories()
    {
        List<String> lResult = new ArrayList<String>();
        lResult.add("Hóa đơn");
        lResult.add("Thiết bị");
        lResult.add("Sức khỏe");
        lResult.add("Việc cần làm");
        lResult.add("Khác");
        return lResult;
    }

    public List<String> GetAllTasks(String iCategoryName)
    {
        List<String> lResult = new ArrayList<String>();
        if(iCategoryName.equals("Hóa đơn"))
        {
           lResult.add("Tiền điện");
           lResult.add("Tiền nước");
           lResult.add("Tiền điện thoại");
           lResult.add("Phí quản lí");
        }
        else if(iCategoryName.equals("Thiết bị"))
        {
           lResult.add("Bảo trì tủ lạnh");
           lResult.add("Rửa xe");
           lResult.add("Vệ sinh máy giặt");
        }
        else if(iCategoryName.equals("Sức khỏe"))
        {
           lResult.add("Khám răng");
           lResult.add("Đo mắt");
           lResult.add("Sổ giun");
        }
        else if(iCategoryName.equals("Việc cần làm"))
        {
           lResult.add("Đi chợ");
           lResult.add("Đón con");
           lResult.add("Đổ rác");
        }
        else if (iCategoryName.equals("Khác")) {
        }
        return lResult;
    }
    //endregion

    //region Task table
    public long InsertATask(Task iTask)
    {
        if(iTask==null)
            return 0;
        SQLiteDatabase lDB = this.getWritableDatabase();
        ContentValues lValues = new ContentValues();
        lValues.put("Name",iTask.pubName);
        lValues.put("TaskType",iTask.pubTaskType.toString());
        lValues.put("Status", iTask.pubStatus.toString());
        lValues.put("Priority", iTask.pubPriority.toString());

        if (iTask.pubCategory != null) {
            lValues.put("Category",iTask.pubCategory);
        }
        if (iTask.pubDescription != null) {
            lValues.put("Description", iTask.pubDescription);
        }
        if (iTask.pubTime != null) {
            lValues.put("Time", iTask.pubTime.toString());
        }

        long lInsertedTaskId = lDB.insert("Task", null, lValues);
        lDB.close();
        if (iTask.pubLocation != null) {
            for (Location lLocation : iTask.pubLocation) {
                long lInsertedLocationId = FindOrInsertALocation(lLocation);
                if (lInsertedTaskId != -1 && lInsertedLocationId != -1) {
                    InsertATaskLocation(lInsertedTaskId, lInsertedLocationId);
                }
            }
        }

        setAlarms();

        return 0;
    }

    public void setAlarms(){
        priContext.stopService(new Intent(priContext, AlarmService.class));
        priContext.startService(new Intent(priContext, AlarmService.class));
    }

    public List<Task> GetAllTasks()
    {
        String[] columns = new String[] {"Name","Category", "TaskType","Description","Time","Priority","Status"};
        Cursor lCursor = this.getWritableDatabase().query("Task", columns, null, null, null, null, null);
        /*if(c==null)
            Log.v("Cursor", "C is NULL");*/
        List<Task> lResult = new ArrayList<Task>();
        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int lNameIndex = lCursor.getColumnIndex("Name");
        int lCategoryIndex = lCursor.getColumnIndex("Category");
        int lTaskTypeIndex = lCursor.getColumnIndex("TaskType");
        int lDescriptionIndex = lCursor.getColumnIndex("Description");
        int lTimeIndex = lCursor.getColumnIndex("Time");
        int lPriorityIndex = lCursor.getColumnIndex("Priority");
        int lStatusIndex = lCursor.getColumnIndex("Status");

        //Vòng lặp lấy dữ liệu của con trỏ
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Task lTempTask = new Task();
            lTempTask.pubName = lCursor.getString(lNameIndex);
            lTempTask.pubCategory = lCursor.getString(lCategoryIndex);
            lTempTask.pubTime = getTimeFromString(lCursor.getString(lTimeIndex));
            lTempTask.pubTaskType = TaskType.valueOf(lCursor.getString(lTaskTypeIndex));
            lTempTask.pubDescription = lCursor.getString(lDescriptionIndex);

            lTempTask.pubPriority = Priority.valueOf(lCursor.getString(lPriorityIndex));
            lTempTask.pubStatus = Status.valueOf(lCursor.getString(lStatusIndex));
            lResult.add(lTempTask);
        }
        lCursor.close();
        //Log.v("Result", result);
        return lResult;
    }

    private Date getTimeFromString(String iDateString) {
        Date lReturnDate = null;
        if (iDateString == null)
            return lReturnDate;
        try {
            DateFormat lDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
            lReturnDate = lDateFormat.parse(iDateString);
        }
        catch (ParseException e) {
            lReturnDate = null;
            Log.w("debug", e.toString());
        }
        return lReturnDate;
    }
    //endregion

    //region Question
    public List<QuestionGroup> GetAllQuestionGroup()
    {
        String[] columns = new String[] {"Id","QuestionString", "Day", "Category"};
        Cursor lCursor = this.getWritableDatabase().query("QuestionGroup", columns, null, null, null, null, null);
        /*if(c==null)
            Log.v("Cursor", "C is NULL");*/
        List<QuestionGroup> lResult = new ArrayList<QuestionGroup>();
        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int lIdIndex = lCursor.getColumnIndex("Id");
        int lQuestionStringIndex = lCursor.getColumnIndex("QuestionString");
        int lDayIndex = lCursor.getColumnIndex("Day");
        int lCategoryIndex = lCursor.getColumnIndex("Category");


        //Vòng lặp lấy dữ liệu của con trỏ
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            QuestionGroup lQuestionGroupTemp = new QuestionGroup();
            lQuestionGroupTemp.pubId = lCursor.getInt(lIdIndex);
            lQuestionGroupTemp.pubQuestionString = lCursor.getString(lQuestionStringIndex);
            lQuestionGroupTemp.pubDay = lCursor.getInt(lDayIndex);
            lQuestionGroupTemp.pubCategory = lCursor.getInt(lCategoryIndex);
            lQuestionGroupTemp.pubQuestions = GetAllQuestionsOfAQuestionGroup(lQuestionGroupTemp.pubId);
            lResult.add(lQuestionGroupTemp);
        }
        lCursor.close();
        //Log.v("Result", result);
        return lResult;
    }

    public List<Question> GetAllQuestionsOfAQuestionGroup(int iQuestionGroupId) {
        String[] columns = new String[] {"Id","QuestionString","Condition","OptionTypes","PropertiesNames","UIType","IsAsked","Stage"};
        Cursor lCursor = this.getWritableDatabase().query("Question", columns, "QuestionGroup_Id=?", new String[]{String.valueOf(iQuestionGroupId)}, null, null, null);
        /*if(c==null)
            Log.v("Cursor", "C is NULL");*/
        List<Question> lResult = new ArrayList<Question>();
        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int lIdIndex = lCursor.getColumnIndex("Id");
        int lQuestionStringIndex = lCursor.getColumnIndex("QuestionString");
        int lConditionIndex = lCursor.getColumnIndex("Condition");
        int lOptionTypesIndex = lCursor.getColumnIndex("OptionTypes");
        int lPropertiesNamesIndex = lCursor.getColumnIndex("PropertiesNames");
        int lUITypeIndex = lCursor.getColumnIndex("UIType");
        int lIsAskedIndex = lCursor.getColumnIndex("IsAsked");
        int lStageIndex = lCursor.getColumnIndex("Stage");


        //Vòng lặp lấy dữ liệu của con trỏ
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Question lQuestionTemp = new Question();
            lQuestionTemp.pubId = lCursor.getInt(lIdIndex);
            lQuestionTemp.pubQuestionString = lCursor.getString(lQuestionStringIndex);
            lQuestionTemp.pubConditions = lCursor.getString(lConditionIndex);
            lQuestionTemp.pubOptionsType = lCursor.getString(lOptionTypesIndex);
            lQuestionTemp.pubInformationPropertiesNames = ParsePropertiesNames(lCursor.getString(lPropertiesNamesIndex));
            lQuestionTemp.pubUIType = UIType.valueOf(lCursor.getString(lUITypeIndex));
            lQuestionTemp.pubIsAsked = lCursor.getInt(lIsAskedIndex)==1;
            lQuestionTemp.pubStage = lCursor.getInt(lStageIndex);

            lResult.add(lQuestionTemp);
        }
        lCursor.close();
        //Log.v("Result", result);
        return lResult;
    }

    private ArrayList<String> ParsePropertiesNames(String iPropertiesNamesString) {

        String[] lTemp = iPropertiesNamesString.replace(" ","").split(";");
        ArrayList<String> lResult = new ArrayList<String>(Arrays.asList(lTemp));
        return lResult;
    }

    public void InsertAQuestionGroup(QuestionGroup iQuestionGroup)
    {
        SQLiteDatabase lDB = this.getWritableDatabase();
        ContentValues lValues = new ContentValues();
        lValues.put("QuestionString",iQuestionGroup.pubQuestionString);
        lValues.put("Day",iQuestionGroup.pubDay);
        lValues.put("Category",iQuestionGroup.pubCategory);
        lDB.insert("QuestionGroup", null, lValues);
        Cursor lCursor = lDB.query("QuestionGroup", new String[]{"Id"}, null, null, null, null, "Id DESC");
        lCursor.moveToFirst();
        int lIdIndex = lCursor.getColumnIndex("Id");
        int lLastID = lCursor.getInt(lIdIndex);
        lDB.close();
        for(int i=0;i<iQuestionGroup.pubQuestions.size();i++)
        {
            InsertAQuestion(iQuestionGroup.pubQuestions.get(i),lLastID);
        }
    }

    public void InsertAQuestion(Question iQuestion, int iQuestionGroupId)
    {
        SQLiteDatabase lDB = this.getWritableDatabase();
        ContentValues lValues = new ContentValues();
        lValues.put("QuestionString",iQuestion.pubQuestionString);
        lValues.put("Condition", iQuestion.pubConditions);
        lValues.put("OptionTypes",iQuestion.pubOptionsType);
        lValues.put("PropertiesNames",MergePropertiesNames(iQuestion.pubInformationPropertiesNames));
        lValues.put("UIType",iQuestion.pubUIType.toString());
        lValues.put("IsAsked",iQuestion.pubIsAsked);
        lValues.put("Stage",iQuestion.pubStage);
        lValues.put("QuestionGroup_Id", iQuestionGroupId);
        lDB.insert("Question", null, lValues);
        lDB.close();
    }

    public void UpdateAQuestion(Question iQuestion)
    {
        ContentValues lValues = new ContentValues();
        lValues.put("QuestionString",iQuestion.pubQuestionString);
        lValues.put("Condition", iQuestion.pubConditions);
        lValues.put("OptionTypes",iQuestion.pubOptionsType);
        lValues.put("PropertiesNames",MergePropertiesNames(iQuestion.pubInformationPropertiesNames));
        lValues.put("UIType",iQuestion.pubUIType.toString());
        lValues.put("IsAsked", iQuestion.pubIsAsked);
        lValues.put("Stage", iQuestion.pubStage);
        SQLiteDatabase lDB = this.getWritableDatabase();
        lDB.update("Question",lValues,"Id=?",new String[]{String.valueOf(iQuestion.pubId)});
        lDB.close();
    }

    private String MergePropertiesNames(List<String> iPropertiesNames) {
        String lResult = "";
        if (iPropertiesNames.size() == 0)
            return lResult;
        for(int i=0;i<iPropertiesNames.size()-1;i++)
            lResult += iPropertiesNames.get(i) + ";";
        lResult += iPropertiesNames.get(iPropertiesNames.size()-1);
        return lResult;
    }

    public Condition GetUserInformation(String iPropertyName) {
        String[] columns = new String[] {"PropertyName","Value","Type"};
        Cursor lCursor = this.getWritableDatabase().query("UserInformation", columns, "PropertyName=?", new String[]{iPropertyName}, null, null, null);
        if(lCursor==null)
            return null;

        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int lPropertyNameIndex = lCursor.getColumnIndex("PropertyName");
        int lValueIndex = lCursor.getColumnIndex("Value");
        int lTypeIndex = lCursor.getColumnIndex("Type");
        lCursor.moveToFirst();
        Condition lResult = new Condition();

        lResult.pubConditionName = lCursor.getString(lPropertyNameIndex);
        lResult.pubValue = lCursor.getString(lValueIndex);
        lResult.pubType = lCursor.getString(lTypeIndex);

        lCursor.close();
        //Log.v("Result", result);
        return lResult;
    }

    public void InsertUserInformations(List<Condition> iNewInformation) {
        try {
            SQLiteDatabase lDB = this.getWritableDatabase();
            for(int i =0;i<iNewInformation.size();i++) {
                Condition lInformation = iNewInformation.get(i);
                ContentValues lValues = new ContentValues();
                lValues.put("PropertyName", lInformation.pubConditionName);
                lValues.put("Value", lInformation.pubValue);
                lValues.put("Type", lInformation.pubType);
                lDB.insert("UserInformation", null, lValues);
            }
            lDB.close();
        }
        catch(Exception ex)
        {
            Log.w("DatabseHelper", ex.getMessage());

        }
    }

    public void UpdateUserInformations(Condition iInformation)
    {
        ContentValues lValues = new ContentValues();
        lValues.put("PropertyName", iInformation.pubConditionName);
        lValues.put("Value", iInformation.pubValue);
        lValues.put("Type", iInformation.pubType);
        SQLiteDatabase lDB = this.getWritableDatabase();
        lDB.update("UserInformation", lValues, "PropertyName=?", new String[]{iInformation.pubConditionName});
        lDB.close();
    }
    //endregion

    //region Question
    public long FindOrInsertALocation(Location iLocation)
    {
        long returnId = -1;
        SQLiteDatabase lDB = this.getWritableDatabase();
        String[] columns = new String[] {"Id"};
        String[] parameters = new String[]{iLocation.pubName, Float.toString(iLocation.pubCoorX), Float.toString(iLocation.pubCoorY)};
        Cursor lCursor = lDB.query("Location", columns, "Name=? AND abs(CoorX-?)<0.001 AND abs(CoorY-?)<0.001", parameters, null, null, null);
        if (lCursor == null || lCursor.getCount() == 0) {
            ContentValues lValues = new ContentValues();
            lValues.put("Name", iLocation.pubName);
            lValues.put("Address", iLocation.pubAndress);
            lValues.put("CoorX", iLocation.pubCoorX);
            lValues.put("CoorY", iLocation.pubCoorY);
            returnId = lDB.insert("Location", null, lValues);
        } else {
            int lIdIndex = lCursor.getColumnIndex("Id");
            lCursor.moveToFirst();
            returnId = lCursor.getInt(lIdIndex);
            lCursor.close();
        }
        lDB.close();
        return returnId;
    }

    public List<Location> GetAllLocations()
    {
        String[] columns = new String[] {"Name", "Address", "CoorX", "CoorY"};
        Cursor lCursor = this.getWritableDatabase().query("Location", columns, null, null, null, null, null);
        List<Location> lResult = new ArrayList<>();
        int lNameIndex = lCursor.getColumnIndex("Name");
        int lAddressIndex = lCursor.getColumnIndex("Address");
        int lCoorXIndex = lCursor.getColumnIndex("CoorX");
        int lCoorYIndex = lCursor.getColumnIndex("CoorY");

        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Location lLocation = new Location();
            lLocation.pubName = lCursor.getString(lNameIndex);
            lLocation.pubAndress = lCursor.getString(lAddressIndex);
            lLocation.pubCoorX = lCursor.getFloat(lCoorXIndex);
            lLocation.pubCoorY = lCursor.getFloat(lCoorYIndex);
            lResult.add(lLocation);
        }
        lCursor.close();
        return lResult;
    }
    //endregion

    //region Task_Location
    public long InsertATaskLocation(long iTaskId, long iLocationId) {
        SQLiteDatabase lDB = this.getWritableDatabase();
        ContentValues lValues = new ContentValues();
        lValues.put("TaskId", iTaskId);
        lValues.put("LocationId", iLocationId);
        long insertedId = lDB.insert("Task_Location", null, lValues);
        lDB.close();
        return insertedId;
    }
    //endregion

}