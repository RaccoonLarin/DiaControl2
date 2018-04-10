package com.example.salima.diacontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
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
    public  static final String COL_5 = "COMMENT";
    public static final String COL_6="DATE";
    public static  final String COL_food_1="DIARY_ID";
    public static  final String COL_food_2="NAME_PRODUCT";
    public static  final String COL_food_3="GRAMS_PRODUCT";
    public static  final String COL_food_4="CARBS_PRODUCT";
    public static  final String COL_food_5="XE_PRODUCT";
    public static  final String COL_food_6="TOTAL_CARBS_PRODUCT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_String = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 +" TEXT,"+ COL_5 + " TEXT," + COL_6 + " TEXT" + ");";
        String SQL_food="CREATE TABLE " + TABLE_FOOD+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_1 + " INTEGER," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        String SQL_food_user="CREATE TABLE " + TABLE_FOOD_USER+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        db.execSQL(SQL_String);
        db.execSQL(SQL_food);
        db.execSQL(SQL_food_user);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_USER);
        onCreate(db);

    }

    //TODO добавить параметры и изменить базу данных

    public boolean insertData(String sugar, String insulin, String bredUnits, String comment, String date1){
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, bredUnits);
        contentValues.put(COL_5, comment);
        contentValues.put(COL_6, date1);
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

    public void deleteProduct(int idDairy){
        SQLiteDatabase db=this.getWritableDatabase();
       // Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " ORDER BY DATE DESC LIMIT 1 OFFSET " + numList, null);
        db.execSQL("DELETE FROM " + TABLE_FOOD + " WHERE DIARY_ID="+idDairy);


    }
    public boolean update(Integer id, String sugar, String insulin, String bredUnits, String comment, String date1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, bredUnits);
        contentValues.put(COL_5, comment);
        contentValues.put(COL_6, date1);
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
