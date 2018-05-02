package com.example.salima.diacontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Salima on 11.01.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public  static final String DATABASE_NAME = "diary.db";
    public  static final String TABLE_NAME = "diary_data";
    public static  final String TABLE_FOOD = "food_data";
    public static  final String TABLE_FOOD_USER = "food_data_user";
    public  static final String COL_1 = "ID";
    public  static final String COL_2 = "BLOOD_SUGAR";
    public  static final String COL_3 = "INSULIN";
    public  static final String COL_4 = "BREADUNITS";
    public  static final String COL_5 = "WEIGHT";
    public  static final String COL_6 = "COMMENT";
    public static final String COL_7 ="DATE";
    public static  final String COL_food_1="DIARY_ID";
    public static  final String COL_food_2="NAME_PRODUCT";
    public static  final String COL_food_3="GRAMS_PRODUCT";
    public static  final String COL_food_4="CARBS_PRODUCT";
    public static  final String COL_food_5="XE_PRODUCT";
    public static  final String COL_food_6="TOTAL_CARBS_PRODUCT";

    public static  final String TABLE_REMINDER = "reminder_data";
    public static final String COL_reminder_ID="REMINDER_ID";
    public static final String COL_reminder_DATE="REMINDER_DATE";
    public static final String COL_reminder_TEXT="REMINDER_TEXT";
    public static final String COL_reminder_DAY="REPEAT_DAY";
    public static final String COL_reminder_WEEK="REPEAT_WEEK";
    public static final String COL_reminder_NOREPEAT="NO_REPEAT";

    public static  final String TABLE_SETTINGS = "settings_data";
    public static final String COL_XE_MAX="XE_MAX";
    public static final String COL_XE_MIN="XE_MIN";
    public static final String COL_XE_TARGET="XE_TARGET";
    public  static final String COL_XE_USER="XE_USER";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_String = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 +" TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT" + ");";
        String SQL_food="CREATE TABLE " + TABLE_FOOD+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_1 + " INTEGER," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        String SQL_food_user="CREATE TABLE " + TABLE_FOOD_USER+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        String SQL_reminder="CREATE TABLE " + TABLE_REMINDER+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_reminder_ID + " INTEGER," + COL_reminder_DATE +" TEXT,"+ COL_reminder_TEXT + " TEXT," + COL_reminder_DAY + " INTEGER," + COL_reminder_WEEK + " INTEGER," +COL_reminder_NOREPEAT + " INTEGER" + ");";
        String SQL_settings="CREATE TABLE " + TABLE_SETTINGS+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_XE_MAX + " TEXT," + COL_XE_MIN + " TEXT,"+ COL_XE_TARGET + " TEXT," + COL_XE_USER + " TEXT" + ");";

        db.execSQL(SQL_String);
        db.execSQL(SQL_food);
        db.execSQL(SQL_food_user);
        db.execSQL(SQL_reminder);
        db.execSQL(SQL_settings);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);

    }

    //TODO добавить параметры и изменить базу данных

    public boolean insertData(String sugar, String insulin, String bredUnits, String weight, String comment, String date1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, bredUnits);
        contentValues.put(COL_5, weight);
        contentValues.put(COL_6, comment);
        contentValues.put(COL_7, date1);
       long result =  db.insert(TABLE_NAME, null, contentValues);
       if(result == -1)
           return false;
       else
           return true;
    }


    public boolean insertDataProduct(Integer id, ArrayList<String> name, ArrayList<String> grams, ArrayList<String> carbs){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=0; i<name.size(); i++) {

            contentValues.put(COL_food_1, id);
            contentValues.put(COL_food_2, name.get(i));
            contentValues.put(COL_food_3, grams.get(i));
            contentValues.put(COL_food_4, carbs.get(i));

            result = db.insert(TABLE_FOOD, null, contentValues);
        }


        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertDataReminder(Integer id, String date, String text, Integer repeatDay, Integer repeatWeak, Integer noRepeat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_reminder_ID, id);
        contentValues.put(COL_reminder_DATE, date);
        contentValues.put(COL_reminder_TEXT, text);
        contentValues.put(COL_reminder_DAY, repeatDay);
        contentValues.put(COL_reminder_WEEK, repeatWeak);
        contentValues.put(COL_reminder_NOREPEAT, noRepeat);
        long result =  db.insert(TABLE_REMINDER, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataSettings(Double xeMax, Double xeMin, Double xeTarget){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_XE_MAX, xeMax);
        contentValues.put(COL_XE_MIN, xeMin);
        contentValues.put(COL_XE_TARGET, xeTarget);
        long result =  db.insert(TABLE_SETTINGS, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateSettings(Double xeMax, Double xeMin, Double xeTarget, Integer xeUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_XE_MAX, xeMax);
        contentValues.put(COL_XE_MIN, xeMin);
        contentValues.put(COL_XE_TARGET, xeTarget);
        contentValues.put(COL_XE_USER, xeUser);
        db.update(TABLE_SETTINGS, contentValues, "ID="+1, null);
        return true;
    }

    public Cursor selectSettings(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_SETTINGS, null);
        return data;
    }


    public Cursor getListReminder(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_REMINDER + " ORDER BY ID", null);
        return data;
    }


    public Cursor selectReminderById(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_REMINDER + " DESC LIMIT 1 OFFSET " + id, null);
        return data;
    }

    public Cursor getListContents(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC", null);
         return data;
    }

    public Cursor getIdPls(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT id FROM " + TABLE_NAME +" WHERE DATE="+ "\'"+ date1+"\'", null);
        return data;
    }
    public Cursor getTime(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT BLOOD_SUGAR, DATE FROM " + TABLE_NAME +" WHERE DATE >= datetime('"+ date1.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+date1.substring(0,10)+"23:59:59')"+" ORDER BY DATE", null);
        return data;
    }

    public Cursor getTimeWeek(String dateStart, String dateEnd){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT BLOOD_SUGAR, DATE FROM " + TABLE_NAME +" WHERE DATE >= datetime('"+ dateStart.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+dateEnd.substring(0,10)+"23:59:59')"+" ORDER BY DATE", null);
        return data;
    }

    public Cursor getTimeMonth(String dateStart, String dateEnd){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT BLOOD_SUGAR, DATE FROM " + TABLE_NAME +" WHERE DATE >= datetime('"+ dateStart.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+dateEnd.substring(0,10)+"23:59:59')"+" ORDER BY DATE", null);
        return data;
    }

    public Cursor select(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id, null);
        return data;
    }

    public Cursor selectProduct(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " WHERE DIARY_ID="+ "\'"+ id+"\'" + " ORDER BY ID",  null);
        return data;
    }
    public Cursor selectExport(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM diary_data", null);
        return data;
    }
    //удалить запись дневника
    public void delete(int id, String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data;
      //  try {
        // data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= datetime('"+date1.substring(0,10)+"23:59:59', '-1 day') AND DATE <= datetime('"+date1.substring(0,10)+"23:59:59')"+" ORDER BY DATE DESC LIMIT 1 OFFSET " + id , null);
      db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID IN (SELECT ID FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id +")");
       // data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id, null);

      /*  }
        catch (Exception e){
            data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = date('" + date1 + "')", null);
        }*/

     //   return data;
    }

    //  data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = date('" + date1 + "') ORDER BY DATE DESC LIMIT 1 OFFSET " + id , null);

    public void deleteProduct(int idDairy){
        SQLiteDatabase db=this.getWritableDatabase();
       // Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " ORDER BY DATE DESC LIMIT 1 OFFSET " + numList, null);
        db.execSQL("DELETE FROM " + TABLE_FOOD + " WHERE DIARY_ID="+idDairy);


    }


    public void deleteReminder(int idReminder){
        SQLiteDatabase db=this.getWritableDatabase();
        // Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " ORDER BY DATE DESC LIMIT 1 OFFSET " + numList, null);
        db.execSQL("DELETE FROM " + TABLE_REMINDER + " WHERE REMINDER_ID="+idReminder);


    }
    public boolean update(Integer id, String sugar, String insulin, String bredUnits, String weight, String comment, String date1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, bredUnits);
        contentValues.put(COL_5, weight);
        contentValues.put(COL_6, comment);
        contentValues.put(COL_7, date1);
        db.update(TABLE_NAME, contentValues, "ID="+id, null);
        return true;
    }

    public void updateFood(Integer id, ArrayList<String> name, ArrayList<String> grams, ArrayList<String> carbs){
        deleteProduct(id);
        if(name.size()<=0){
          return;
        } else{
            insertDataProduct(id, name, grams, carbs);
        }
    }
    public Cursor getListContentsTrial(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= datetime('"+date1+"23:59:59', '-1 day') AND DATE <= datetime('"+date1+"23:59:59')"+" ORDER BY DATE DESC", null);
        return data;
    }


    public boolean insertDataProductUser( String name, String grams, String carbs){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_food_2, name);
        contentValues.put(COL_food_3, grams);
        contentValues.put(COL_food_4, carbs);

        result = db.insert(TABLE_FOOD, null, contentValues);


        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor selectAllUserProduct(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD_USER, null);
        return data;
    }
}
