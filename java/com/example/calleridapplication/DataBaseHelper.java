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

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String CONTACT_ID = "CONTACT_ID";
    public static final String CONTACT_TABLE = "CONTACT_TABLE";
    public static final String CONTACT_FNAME = "CONTACT_FNAME";
    public static final String CONTACT_LNAME = "CONTACT_LNAME";
    public static final String CONTACT_COMPANY = "CONTACT_COMPANY";
    public static final String CONTACT_JOB = "CONTACT_JOB";
    public static final String CONTACT_EMAIL = "CONTACT_EMAIL";
    public static final String CONTACT_MOBILEPHONE = "CONTACT_MOBILEPHONE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "contacts.db", null, 1);
    }
// this is called the first time a database is accessed.There should be code in here that creates a database
    @Override
    public void onCreate(SQLiteDatabase db) {
          String createTableStatement =
                  "CREATE TABLE " + CONTACT_TABLE + " (" + CONTACT_ID + " TEXT PRIMARY KEY ," + CONTACT_FNAME + " TEXT," +
                          CONTACT_LNAME + " TEXT, " + CONTACT_COMPANY + " TEXT , " + CONTACT_JOB + " TEXT ," + CONTACT_EMAIL + " TEXT , " + CONTACT_MOBILEPHONE + " TEXT " +
                          " ) ";

          db.execSQL(createTableStatement);
 }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(ContactModel contact){
     SQLiteDatabase db = this.getWritableDatabase();
     ContentValues cv = new ContentValues();
        cv.put(CONTACT_FNAME,contact.getContact_fname());
        cv.put(CONTACT_LNAME,contact.getContact_lname());
        cv.put(CONTACT_ID,contact.getContact_id());
      // cv.put(CONTACT_ID,"i");
        cv.put(CONTACT_EMAIL,contact.getContact_email());
        if(contact.getContact_company().equals(null)){
            cv.put(CONTACT_COMPANY,"no company data");
        }else{
        cv.put(CONTACT_COMPANY,contact.getContact_company());
        }
        cv.put(CONTACT_JOB,contact.getContact_job());
        cv.put(CONTACT_MOBILEPHONE,contact.getContact_mobilephone());

       long insert= db.insert(CONTACT_TABLE,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }


    public List<ContactModel> getEveryone(){


        List<ContactModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " +CONTACT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results
     ///// "CREATE TABLE " + CONTACT_TABLE + " (" + CONTACT_ID + " INTEGER PRIMARY KEY," + CONTACT_FNAME + " TEXT," +
     //                          CONTACT_LNAME + " STRING, " + CONTACT_COMPANY + " TEXT , " + CONTACT_JOB + " TEXT," + CONTACT_EMAIL + " TEXT ) ";
     //
     //          db.execSQL(createTableStatement);
                do {
                    String contactID = cursor.getString(0);
                    String contactfname = cursor.getString(1);
                    String contactlname = cursor.getString(2);
                    String contactcompany = cursor.getString(3);
                    String contactjob = cursor.getString(4);
                    String contactemail = cursor.getString(5);
                    String contactmobilephone = cursor.getString(6);
                    // public ContactModel(String contact_id, String contact_lname, String contact_fname, String contact_company, String contact_job, String contact_email, String contact_mobilephone) {
                    ContactModel c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);

               returnList.add(c);
                } while (cursor.moveToNext());
              cursor.close();

            }}catch(Exception e){
           Log.e("error",e.toString());


        }

        return returnList;
    }




    public ContactModel fetchcontact(String number){
        String queryString = "SELECT * FROM " +CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + " = "+ number + "";
        //   String queryString = "SELECT * FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + " = 70753661";
        SQLiteDatabase db = this.getReadableDatabase();
        ContactModel c = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                 Log.d("contact",">>>>>fouund");

                    String contactID = cursor.getString(0);

                    String contactfname = cursor.getString(1);
                Log.d("name",contactfname);
                    String contactlname = cursor.getString(2);
                    String contactcompany = cursor.getString(3);
                    String contactjob = cursor.getString(4);
                    String contactemail = cursor.getString(5);
                    String contactmobilephone = cursor.getString(6);

                     c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);
                Log.d("contactttt",c.toString());
                cursor.close();

            }else{
                c = new ContactModel("","","","","","","");
            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return c;

    }


    public int deleteContact(ContactModel contact){
        String queryString = "DELETE FROM "+CONTACT_TABLE+" WHERE "+CONTACT_ID+" = \""+contact.getContact_id()+"\";";
        SQLiteDatabase db = this.getWritableDatabase();

       int c = db.delete(CONTACT_TABLE,CONTACT_ID+" = \""+contact.getContact_id()+"\"",null);
      if(c == -1){
          return 0;
      }return 1;

    }

public String getContactId(String email){

    String queryString = "SELECT "+CONTACT_ID+" FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_EMAIL + " = \""+email+"\"";
    SQLiteDatabase db = this.getReadableDatabase();
    String contactid = null;
    try (Cursor cursor = db.rawQuery(queryString,null)) {
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            //if there are result i will loop into the results

            Log.d("contact",">>>>>fouund");

             contactid = cursor.getString(0);

            cursor.close();

        }else{
            Log.d("contact",">>>>>notfouund");
        }
    }catch(Exception e){
        Log.e("error",e.toString());
    }
    return contactid;



}


    public String getContactIdByPhone(String phone){

        String queryString = "SELECT "+CONTACT_ID+" FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + "="+phone+"";
        SQLiteDatabase db = this.getReadableDatabase();
        String contactid = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                contactid = cursor.getString(0);

                cursor.close();

            }else{
                Log.d("contact",">>>>>notfouund");
            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return contactid;



    }

    public String getContactName(String contactid){
        String queryString = "SELECT "+CONTACT_FNAME+","+CONTACT_LNAME+" FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_ID + "=\""+contactid+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        String contactname = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");
               if(cursor.getString(0).equals("null")){
                   contactname =cursor.getString(1);

            }else{
                contactname = cursor.getString(0) +" "+cursor.getString(1);

                Log.d("contactName",contactname);
                cursor.close();

            }}else {
                Log.d("contact",">>>>>notfouund");
                contactname = "";

            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return contactname;
    }


    public ContactModel fetchAllInfo(String id){

        String queryString = "SELECT * FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_ID + " = \""+id+"\"";
        ContactModel c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                String contactID = cursor.getString(0);

                String contactfname = cursor.getString(1);
                Log.d("name",contactfname);
                String contactlname = cursor.getString(2);
                String contactcompany = cursor.getString(3);
                String contactjob = cursor.getString(4);
                String contactemail = cursor.getString(5);
                String contactmobilephone = cursor.getString(6);

                c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);
                Log.d("contactttt",c.toString());
                cursor.close();

            }else{
                c = new ContactModel("","","","","","","");
            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return c;



    }


}
