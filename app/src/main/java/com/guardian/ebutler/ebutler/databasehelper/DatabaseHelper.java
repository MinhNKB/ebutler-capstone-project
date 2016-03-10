package com.guardian.ebutler.ebutler.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nkbmi on 3/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper priInstance;

    //Database information
    private static final String DATABASE_NAME = "eButlerDatabase";
    private static final int DATABASE_VERSION = 1;
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
    private DatabaseHelper(Context iContext) {
        super(iContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase iDB) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase iDB, int iOldVersion, int iNewVersion) {

    }
}