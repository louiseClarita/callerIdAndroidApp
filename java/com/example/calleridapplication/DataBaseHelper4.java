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

public class DataBaseHelper4 extends SQLiteOpenHelper {



    public static final String CODE = "CODE";
    public static final String COUNTRYCODE = "COUNTRY" + CODE;
    public static final String DESCRIPTION = "DESCRIPTION";

    public DataBaseHelper4(@Nullable Context context) {
        super(context, "countryCode.db", null, 1);
    }
    // this is called the first time a database is accessed.There should be code in here that creates a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " + COUNTRYCODE + " ( " + CODE + " TEXT PRIMARY KEY ," + DESCRIPTION + "  TEXT) ";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(CountryCode c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CODE,c.getCode());
        cv.put(DESCRIPTION,c.getCountry());


        long insert= db.insert(COUNTRYCODE,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }

    public List<CountryCode> getEveryone(){


        List<CountryCode> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " +COUNTRYCODE;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            if (cursor.moveToFirst()) {


                do {
                    String code = cursor.getString(0);
                    String description = cursor.getString(1);

             CountryCode c = new CountryCode(description,code);
                    returnList.add(c);
                } while (cursor.moveToNext());
                cursor.close();

            }}catch(Exception e){
            Log.e("error",e.toString());


        }
        db.close();
        return returnList;

    }
}