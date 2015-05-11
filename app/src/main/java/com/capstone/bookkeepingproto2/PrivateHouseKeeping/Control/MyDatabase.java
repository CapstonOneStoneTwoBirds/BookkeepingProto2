package com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by YeomJi on 15. 4. 23..
 */



public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "CapstonTest1.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}