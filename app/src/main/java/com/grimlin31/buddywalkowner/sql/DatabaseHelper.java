package com.grimlin31.buddywalkowner.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grimlin31.buddywalkowner.model.Walker;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "WalkerManager.db";
    //Users Table Name
    private static final String TABLE_WALKER = "walker";
    //User Table Columns
    private static final String COLUMN_WALKER_ID = "walker_id";
    private static final String COLUMN_WALKER_EMAIL = "walker_email";
    private static final String COLUMN_WALKER_PASSWORD = "walker_password";
    private static final String COLUMN_WALKER_USERNAME = "walker_username";
    private static final String COLUMN_WALKER_LATITUDE = "walker_latitude";
    private static final String COLUMN_WALKER_LONGITUDE = "walker_longitude";
    private static final String COLUMN_WALKER_BUSY = "walker_busy";
    private static final String COLUMN_WALKER_ONLINE = "walker_online";

    //SQL Queries
    private String CREATE_WALKER_TABLE = "CREATE TABLE " + TABLE_WALKER + "(" +
            COLUMN_WALKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WALKER_USERNAME + " TEXT, " +
            COLUMN_WALKER_EMAIL + " TEXT, " +
            COLUMN_WALKER_PASSWORD + " TEXT," +
            COLUMN_WALKER_LATITUDE + " TEXT," +
            COLUMN_WALKER_LONGITUDE + " TEXT," +
            COLUMN_WALKER_BUSY + " TEXT, " +
            COLUMN_WALKER_ONLINE + " TEXT " + ")";

    private String DROP_WALKER_TABLE = "DROP TABLE IF EXISTS " + TABLE_WALKER;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WALKER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_WALKER_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param walker
     */
    public void addUser(Walker walker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALKER_USERNAME, walker.getUsername());
        values.put(COLUMN_WALKER_EMAIL, walker.getEmail());
        values.put(COLUMN_WALKER_PASSWORD, walker.getPassword());
        values.put(COLUMN_WALKER_LATITUDE, walker.getLatitude());
        values.put(COLUMN_WALKER_LONGITUDE, walker.getLongitude());
        values.put(COLUMN_WALKER_BUSY, walker.getBusy());
        values.put(COLUMN_WALKER_ONLINE, walker.getOnline());
        // Inserting Row
        db.insert(TABLE_WALKER, null, values);
        db.close();
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    @SuppressLint("Range")
    public List<Walker> getAllWalker() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_WALKER_ID,
                COLUMN_WALKER_EMAIL,
                COLUMN_WALKER_USERNAME,
                COLUMN_WALKER_PASSWORD,
                COLUMN_WALKER_LATITUDE,
                COLUMN_WALKER_LONGITUDE,
                COLUMN_WALKER_BUSY,
                COLUMN_WALKER_ONLINE
        };
        // sorting orders
        String sortOrder =
                COLUMN_WALKER_EMAIL + " ASC";
        List<Walker> walkerList = new ArrayList<Walker>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_WALKER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Walker walker = new Walker();
                walker.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_WALKER_ID))));
                walker.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_WALKER_EMAIL)));
                walker.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_WALKER_PASSWORD)));
                walker.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_WALKER_USERNAME)));
                walker.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_WALKER_LATITUDE)));
                walker.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_WALKER_LONGITUDE)));
                walker.setBusy(cursor.getInt(cursor.getColumnIndex(COLUMN_WALKER_BUSY)));
                walker.setOnline(cursor.getInt(cursor.getColumnIndex(COLUMN_WALKER_ONLINE)));
                // Adding user record to list
                walkerList.add(walker);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return walkerList;
    }
    /**
     * This method to update user record
     *
     * @param walker
     */
    public void updateWalker(Walker walker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALKER_EMAIL, walker.getEmail());
        values.put(COLUMN_WALKER_PASSWORD, walker.getPassword());
        values.put(COLUMN_WALKER_USERNAME, walker.getUsername());
        values.put(COLUMN_WALKER_LATITUDE, walker.getLatitude());
        values.put(COLUMN_WALKER_LONGITUDE, walker.getLongitude());
        values.put(COLUMN_WALKER_BUSY, walker.getBusy());
        values.put(COLUMN_WALKER_ONLINE, walker.getOnline());
        // updating row
        db.update(TABLE_WALKER, values, COLUMN_WALKER_ID + " = ?",
                new String[]{String.valueOf(walker.getId())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param walker
     */
    public void deleteWalker(Walker walker) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_WALKER, COLUMN_WALKER_ID + " = ?",
                new String[]{String.valueOf(walker.getId())});
        db.close();
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkWalker(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_WALKER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_WALKER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_WALKER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkWalker(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_WALKER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_WALKER_EMAIL + " = ?" + " AND " + COLUMN_WALKER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_WALKER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
