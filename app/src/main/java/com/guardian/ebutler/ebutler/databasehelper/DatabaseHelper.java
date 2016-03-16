package com.guardian.ebutler.ebutler.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;

import com.guardian.ebutler.ebutler.dataclasses.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkbmi on 3/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Database information
    private static final String DATABASE_NAME = "eButlerDatabase";
    private static final int DATABASE_VERSION = 1;
//    public static synchronized DatabaseHelper getInstance(Context context) {
//
//        // Use the application context, which will ensure that you
//        // don't accidentally leak an Activity's context.
//        // See this article for more information: http://bit.ly/6LRzfx
//        if (priInstance == null) {
//            priInstance = new DatabaseHelper(context.getApplicationContext());
//        }
//        return priInstance;
//    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    public DatabaseHelper(Context iContext) {
        super(iContext, DATABASE_NAME, null, DATABASE_VERSION);
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
                "    Category varchar(255)  NOT NULL,\n" +
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

        iDB.execSQL("Table: QuestionGroup\n" +
                "CREATE TABLE QuestionGroup (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY,\n" +
                "    QuestionString text  NOT NULL\n" +
                ");");

        iDB.execSQL("CREATE TABLE Question (\n" +
                "    Id integer  NOT NULL   PRIMARY KEY,\n" +
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
                "    Id integer  NOT NULL   PRIMARY KEY,\n" +
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

        return lResult;
    }
    //endregion

    //region Task table
    public long InsertATask(Task iTask)
    {
        SQLiteDatabase lDB = this.getWritableDatabase();
        ContentValues lValues = new ContentValues();
        lValues.put("Name",iTask.pubName);
        lValues.put("Category",iTask.pubCategory);
        lValues.put("Description",iTask.pubDescription);
        lValues.put("Time",iTask.pubTime.toString());
        lValues.put("Status", iTask.pubStatus.toString());
        lValues.put("Priority", iTask.pubPriority.toString());
        lDB.insert("Task", null, lValues);
        lDB.close();
        return 0;
    }

    public List<Task> GetAllTasks()
    {
        String[] columns = new String[] {"Name","Category","Description","Time","Priority","Status"};
        Cursor lCursor = this.getWritableDatabase().query("Task", columns, null, null, null, null, null);
        /*if(c==null)
            Log.v("Cursor", "C is NULL");*/
        List<Task> lResult = new ArrayList<Task>();
        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int lNameIndex = lCursor.getColumnIndex("Name");
        int lCategoryIndex = lCursor.getColumnIndex("Category");
        int lDescriptionIndex = lCursor.getColumnIndex("Description");
        int lTimeIndex = lCursor.getColumnIndex("Time");
        int lPriorityIndex = lCursor.getColumnIndex("Priority");
        int lStatusIndex = lCursor.getColumnIndex("Status");

        //Vòng lặp lấy dữ liệu của con trỏ
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Task lTempTask = new Task();
            lTempTask.pubName = lCursor.getString(lNameIndex);
            lTempTask.pubCategory = lCursor.getString(lCategoryIndex);
            lTempTask.pubDescription = lCursor.getString(lDescriptionIndex);
            //lTempTask.pubTime = Date.valueOf(lCursor.getString(lTimeIndex));
            lTempTask.pubTime = new java.util.Date();
            lTempTask.pubPriority = Priority.valueOf(lCursor.getString(lPriorityIndex));
            lTempTask.pubStatus = Status.valueOf(lCursor.getString(lStatusIndex));
            lResult.add(lTempTask);
        }
        lCursor.close();
        //Log.v("Result", result);
        return lResult;
    }
    //endregion


}