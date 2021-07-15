package com.example.bupedia.database;

import android.content.Context;

import com.example.bupedia.MyApp;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;



public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "dbbupedia.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper databaseHelper;

    public DatabaseHelper() {
        super(MyApp.context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static DatabaseHelper getInstance() {

        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) { //thread safe singleton
                if (databaseHelper == null)
                    databaseHelper = new DatabaseHelper();
            }
        }
        return databaseHelper;
    }

}
