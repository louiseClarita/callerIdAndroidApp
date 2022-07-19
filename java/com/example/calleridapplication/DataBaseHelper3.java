package com.example.calleridapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper3 extends SQLiteOpenHelper {


    public static final String SETTINGS = "SETTINGS";
    public static final String ID = "ID";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";

    public DataBaseHelper3(@Nullable Context context) {
        super(context, "settings.db", null, 1);
    }
    // this is called the first time a database is accessed.There should be code in here that creates a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " + SETTINGS + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + "  TEXT, " + EMAIL + "  TEXT ) ";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(User u){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID,1);
        cv.put(USERNAME,u.getName());
        cv.put(EMAIL,u.getEmail());

        long insert= db.insert(SETTINGS,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }
    public User getUser(){



        String queryString = "SELECT * FROM " +SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        User u=new User("","");
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results
//                "CREATE TABLE " + CALLLOGS_TABLE + " (" +
//                CALLLOGS_ID + " TEXT PRIMARY KEY AUTOINCREMENT,"
//                + CALLLOGS_DURATION + "  TEXT, " +
//                CALLLOGS_DATE+ "  TEXT UNIQUE, " +
//                CALLLOGS_PHONENUMBER + " TEXT , " +
//                CALLLOGS_DIRECTION +
//                " TEXT ,"
//                + CALLLOGS_SAVED + " INT ) ";                //


                do {
                   // String ID = cursor.getString(0);
                    String name = cursor.getString(1);
                    String email = cursor.getString(2);


                    //  public CallLogs(String duration, String direction, String date, String phoneNbre, int saved, String callername) {
                  u = new User(name,email);



                } while (cursor.moveToNext());
                cursor.close();

            }}catch(Exception e){
            Log.e("error",e.toString());


        }

        return u;
    }


    public int getCount(){
        String queryString = "SELECT * FROM "+SETTINGS;
        SQLiteDatabase db = this.getWritableDatabase();
int count= 0;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            if (cursor.moveToFirst()) {
                count++;
                } while (cursor.moveToNext());

    }
        return count;
    }

    public int deleteUser(){
        String queryString = "DELETE FROM "+SETTINGS+" WHERE "+ID+ "=1";
        SQLiteDatabase db = this.getWritableDatabase();

        int c = db.delete(SETTINGS,null,null);
        if(c == -1){
            return 0;
        }return 1;

    }
}