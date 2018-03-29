package com.example.salima.diacontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by Salima on 11.01.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public  static final String DATABASE_NAME = "diary.db";
    public  static final String TABLE_NAME = "diary_data";
    public  static final String COL_1 = "ID";
    public  static final String COL_2 = "BLOOD_SUGAR";
    public  static final String COL_3 = "INSULIN";
    public  static final String COL_4 = "COMMENT";
    public static final String COL_5="DATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_String = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 +" TEXT,"+ COL_5 + " TEXT" + ");";
        db.execSQL(SQL_String);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    //TODO добавить параметры и изменить базу данных

    public boolean insertData(String sugar, String insulin, String comment, String date1){
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, comment);
        contentValues.put(COL_5, date1);
       long result =  db.insert(TABLE_NAME, null, contentValues);
       if(result == -1)
           return false;
       else
           return true;
    }

    public Cursor getListContents(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC", null);
         return data;
    }


    public Cursor getId(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT id FROM " + TABLE_NAME +" WHERE DATE >= datetime('"+ date1.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+date1.substring(0,10)+"23:59:59')"+" ORDER BY DATE", null);
        return data;
    }

    public Cursor select(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id +")", null);
        return data;
    }

    public void delete(int id, String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data;
      //  try {
        // data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= datetime('"+date1.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+date1.substring(0,10)+"23:59:59')"+" ORDER BY DATE DESC LIMIT 1 OFFSET " + id , null);
      db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE COMMENT IN (SELECT COMMENT FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id +")");
       // data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id, null);

      /*  }
        catch (Exception e){
            data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = date('" + date1 + "')", null);
        }*/

     //   return data;
    }

    //  data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = date('" + date1 + "') ORDER BY DATE DESC LIMIT 1 OFFSET " + id , null);

    public Cursor getListContentsTrial(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= datetime('"+date1+"23:59:59', '-1 day') AND DATE <= datetime('"+date1+"23:59:59')"+" ORDER BY DATE DESC", null);
        return data;
    }
}
