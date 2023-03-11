package com.khambhatpragati.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Dell on 12-09-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "khambhatpragati.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_API_RESPONSE = "api_response";
    private static final String COLUMN_API_NAME = "api_name";
    private static final String COLUMN_API_RESPONSE = "response";
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_API_RESPONSE + " (" + COLUMN_API_NAME + " TEXT," + COLUMN_API_RESPONSE + " TEXT)";
        db.execSQL(query);
        //Log.d(TAG, "Table created " + query);
    }

    public long insertApiResponse(String apiName, String response) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_API_RESPONSE, COLUMN_API_NAME + " = ? ", new String[]{apiName});
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_API_NAME, apiName);
        contentValues.put(COLUMN_API_RESPONSE, response);
        return db.insert(TABLE_API_RESPONSE, null, contentValues);
    }

    public String selectResponse(String apiName) {
        SQLiteDatabase db = this.getReadableDatabase();
        //"SELECT * FROM " + tableName + " where Category = '" +categoryex + "'"
        Cursor cursor = db.rawQuery("select * from " + TABLE_API_RESPONSE + " where " + COLUMN_API_NAME + " = '" + apiName+"'", null);
        String apiResponse = null;
        if (cursor.moveToFirst()) {
            do {
                apiResponse = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return apiResponse;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_API_RESPONSE);
        onCreate(db);
    }
}