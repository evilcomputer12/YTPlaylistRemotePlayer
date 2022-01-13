package com.martin.androidwebplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "database.db";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_TABLE = "links";
    static final String ID = "id";
    static final String LINK = "link";

    private static final String CREATE_DB_QUERY = "CREATE TABLE " + DATABASE_TABLE +" ( "
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LINK + " TEXT NOT NULL );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
    }
}
