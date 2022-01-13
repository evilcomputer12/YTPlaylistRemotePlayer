package com.martin.androidwebplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLDataException;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context1;
    private SQLiteDatabase database;

    public DatabaseManager(Context ctx1) {
        context1 = ctx1;
    }

    public DatabaseManager open() throws SQLDataException {
        dbHelper = new DatabaseHelper(context1);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String link) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LINK, link);
        database.insert(DatabaseHelper.DATABASE_TABLE, null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper.ID, DatabaseHelper.LINK};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String link) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LINK, link);
        int ret = database.update(DatabaseHelper.DATABASE_TABLE, contentValues, DatabaseHelper.ID + "=" + _id, null);
        return ret;
    }
    public void delete (long _id) {
        database.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.ID + "=" + _id, null);
    }
}
