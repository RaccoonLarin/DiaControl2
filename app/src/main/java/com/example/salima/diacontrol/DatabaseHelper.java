package com.example.salima.diacontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Telephony;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Salima on 11.01.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public  static final String DATABASE_NAME = "diary.db";
    public  static final String TABLE_NAME = "diary_data";
    public  static  final String TABLE_NAME_RESERVE="diary_data_reserve";

    public static  final String TABLE_FOOD = "food_data";
    public static  final String TABLE_FOOD_USER = "food_data_user";
    public  static final String COL_1 = "ID";
    public  static final String COL_2 = "BLOOD_SUGAR";
    public  static final String COL_3 = "INSULIN";
    public  static final String COL_4 = "BREADUNITS";
    public  static final String COL_5 = "WEIGHT";
    public  static final String COL_6 = "COMMENT";
    public static final String COL_7 ="DATE";
    public static final String COL_IDDIARY ="ID_DIARY";
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

    public  static final String TABLE_TOKEN = "token_data";
    public static final String COL_token="TOKEN";
    public static final String COL_Email="EMAIL";

    public static final String COL_Delete="ISDELETE";
    public static final String COL_Update="ISUPDATE";
    SettingUser settingUser;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        settingUser = new SettingUser(context);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_String = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 +" TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT,"+COL_IDDIARY+" INTEGER" + ");";
        String SQL_String_reserve = "CREATE TABLE " + TABLE_NAME_RESERVE + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 +" TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT" + ");";

        String SQL_food="CREATE TABLE " + TABLE_FOOD+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_1 + " INTEGER," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        String SQL_food_user="CREATE TABLE " + TABLE_FOOD_USER+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_food_2 + " TEXT," + COL_food_3 +" TEXT,"+ COL_food_4 + " TEXT" + ");";
        String SQL_reminder="CREATE TABLE " + TABLE_REMINDER+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_reminder_ID + " INTEGER," + COL_reminder_DATE +" TEXT,"+ COL_reminder_TEXT + " TEXT," + COL_reminder_DAY + " INTEGER," + COL_reminder_WEEK + " INTEGER," +COL_reminder_NOREPEAT + " INTEGER" + ");";
        String SQL_settings="CREATE TABLE " + TABLE_SETTINGS+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_XE_MAX + " TEXT," + COL_XE_MIN + " TEXT,"+ COL_XE_TARGET + " TEXT," + COL_XE_USER + " TEXT" + ");";
        String SQL_token="CREATE TABLE " + TABLE_TOKEN+ "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_token + " TEXT," + COL_Email + " TEXT" + ");";

        db.execSQL(SQL_String);
        db.execSQL(SQL_String_reserve);
        db.execSQL(SQL_food);
        db.execSQL(SQL_food_user);
        db.execSQL(SQL_reminder);
        db.execSQL(SQL_settings);
        db.execSQL(SQL_token);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RESERVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
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
        Cursor cr= getIdPls(date1);
       // int s=cr.getCount();
        int idd=0;
        while (cr.moveToNext()){
           idd=cr.getInt(0);
        }
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(COL_IDDIARY, idd);
        db2.update(TABLE_NAME, contentValues2, COL_7+"="+"\'"+date1+"\'", null);
            if(settingUser.isNetworkAvailable()) {
                selectReserv();
                new HttpPost().execute(ServerData.getIpServ() + "dairyInsert",  "dairyInsert", sugar, insulin, bredUnits, weight, comment, date1, Integer.toString(idd));
            } else{
                insertDataReserve(sugar, insulin,  bredUnits,  weight,  comment,  date1);
            }

        if(result == -1)
           return false;
       else
           return true;
    }

    //TODO добавить параметры и изменить базу данных

    public boolean insertDataArray(ArrayList <String> sugar, ArrayList <String>  insulin, ArrayList <String>  bredUnits,
                                   ArrayList <String>  weight, ArrayList <String>  comment, ArrayList <String>  date1, ArrayList<String> idDiary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result=0;
        for(int i=0; i<date1.size(); i++) {
            contentValues.put(COL_2, sugar.get(i));
            contentValues.put(COL_3, insulin.get(i));
            contentValues.put(COL_4, bredUnits.get(i));
            contentValues.put(COL_5, weight.get(i));
            contentValues.put(COL_6, comment.get(i));
            contentValues.put(COL_7, date1.get(i));
            contentValues.put(COL_IDDIARY, idDiary.get(i));
             result =  db.insert(TABLE_NAME, null, contentValues);
        }


        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertReminderArray(ArrayList <String> reminderId, ArrayList <String>  reminderDate, ArrayList <String>  reminderTxt,
                                   ArrayList <String>  reminderDay, ArrayList <String>  reminderWeek, ArrayList <String>  reminderNorepeat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        long result=0;

        for(int i=0; i<reminderId.size(); i++) {
            contentValues.put(COL_reminder_ID, Integer.parseInt(reminderId.get(i)));
            contentValues.put(COL_reminder_DATE, reminderDate.get(i));
            contentValues.put(COL_reminder_TEXT, reminderTxt.get(i));
            contentValues.put(COL_reminder_DAY, Integer.parseInt(reminderDay.get(i)));
            contentValues.put(COL_reminder_WEEK, Integer.parseInt(reminderWeek.get(i)));
            contentValues.put(COL_reminder_NOREPEAT, Integer.parseInt(reminderNorepeat.get(i)));
            result =  db.insert(TABLE_REMINDER, null, contentValues);
        }


        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDataReserve(String sugar, String insulin, String bredUnits, String weight, String comment, String date1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, sugar);
        contentValues.put(COL_3, insulin);
        contentValues.put(COL_4, bredUnits);
        contentValues.put(COL_5, weight);
        contentValues.put(COL_6, comment);
        contentValues.put(COL_7, date1);
        long result =  db.insert(TABLE_NAME_RESERVE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }


    public void selectReserv(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_RESERVE, null);
        //тут можно сделать добавлять ArrayList, передать в пост массив и в северном приложении парсить массив
         String sugar, insulin, bredUnits, weight, comment, date1;
         if(data==null){
             return;
         }
        while (data.moveToNext()){
            sugar=data.getString(1);
            insulin=data.getString(2);
            bredUnits=data.getString(3);
            weight=data.getString(4);
            comment=data.getString(5);
            date1=data.getString(6);


                new HttpPost().execute(ServerData.getIpServ() + "dairyInsert",  "dairyInsert", sugar, insulin, bredUnits, weight, comment, date1);



        }

        deleteReserv();

    }

    public void deleteReserv(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_RESERVE  );

    }



       String servProduct;
       String casDataFood;
       Integer idFoodInsert;
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
        if(name.size()==0){return true;}
        // String servProduct=  ServerData.getIpServ();
        casDataFood="fooddataInsert";
        servProduct=ServerData.getIpServ()+casDataFood;
        idFoodInsert=id;

        if(settingUser.isNetworkAvailable()) {

            new HttpPostFoodData().execute(name, grams, carbs);
        }

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataProductArray(ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> grams, ArrayList<String> carbs){
        long result=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=0; i<name.size(); i++) {

            contentValues.put(COL_food_1, id.get(i));
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


    public boolean insertToken(String token, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_token, token);
        contentValues.put(COL_Email, email);
        long result =  db.insert(TABLE_TOKEN, null, contentValues);
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


            if(settingUser.isNetworkAvailable()) {
                selectReserv();
                new HttpPost().execute(ServerData.getIpServ() + "reminderInsert",  "reminderInsert", Integer.toString(id), date,  text,  Integer.toString(repeatDay),
                        Integer.toString(repeatWeak), Integer.toString(noRepeat));
            }

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataSettings(Double xeMax, Double xeMin, Double xeTarget, Integer xeUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_XE_MAX, xeMax);
        contentValues.put(COL_XE_MIN, xeMin);
        contentValues.put(COL_XE_TARGET, xeTarget);
        long result =  db.insert(TABLE_SETTINGS, null, contentValues);

            if(settingUser.isNetworkAvailable()) {

                //selectReserv();
                String xeminTempStr="", xeMaxTempStr="", xeTargetTempStr="", xeUserTempStr="";

                if(!(xeMin==null)){
                    xeminTempStr=Double.toString(xeMin);
                }

                if(!(xeMax==null)){
                    xeMaxTempStr=Double.toString(xeMax);
                }


                if(!(xeTarget==null)){
                    xeTargetTempStr=Double.toString(xeTarget);
                }

                if(!(xeUser==null)){
                    xeUserTempStr=Integer.toString(xeUser);
                }
                new HttpPost().execute(ServerData.getIpServ() + "insertDataSettings",  "insertDataSettings", xeminTempStr,
                        xeMaxTempStr,  xeTargetTempStr, xeUserTempStr);

            }

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


            if(settingUser.isNetworkAvailable()) {

                //selectReserv();

                String xeminTempStr="", xeMaxTempStr="", xeTargetTempStr="", xeUserTempStr="";

                if(!(xeMin==null)){
                    xeminTempStr=Double.toString(xeMin);
                }

                if(!(xeMax==null)){
                    xeMaxTempStr=Double.toString(xeMax);
                }


                if(!(xeTarget==null)){
                    xeTargetTempStr=Double.toString(xeTarget);
                }

                if(!(xeUser==null)){
                    xeUserTempStr=Integer.toString(xeUser);
                }
                new HttpPost().execute(ServerData.getIpServ() + "updateDataSettings",  "updateDataSettings", xeminTempStr,
                        xeMaxTempStr,  xeTargetTempStr, xeUserTempStr);
            }

        return true;
    }

    public Cursor selectSettings(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_SETTINGS, null);
        return data;
    }

    public Cursor selectToken(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_TOKEN, null);
        return data;
        }


    public String selectEmail(){
        String hm="";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT EMAIL FROM " + TABLE_TOKEN, null);
        while (data.moveToNext()){
            hm=data.getString(0);

        }
      return hm;
        //return data;
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

    public Cursor getIdDiary(String date1){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT ID_DIARY FROM " + TABLE_NAME +" WHERE DATE="+ "\'"+ date1+"\'", null);
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
        Cursor cr=getIdDiary(date1);
        int idd=0;
        while (cr.moveToNext()){
            idd=cr.getInt(0);
        }
        deleteProduct(idd);
       db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID IN (SELECT ID FROM " + TABLE_NAME + " ORDER BY DATE DESC LIMIT 1 OFFSET " + id +")");

            if(settingUser.isNetworkAvailable()) {
                selectReserv();
                new HttpPost().execute(ServerData.getIpServ() + "deleteDiary", "deleteDiary", date1);
            }


    }

    //  data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE = date('" + date1 + "') ORDER BY DATE DESC LIMIT 1 OFFSET " + id , null);
    public void deleteAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME );
        db.execSQL("DELETE FROM " + TABLE_SETTINGS );
        db.execSQL("delete from sqlite_sequence where name=" + "\'"+TABLE_NAME+"\'" );
        db.execSQL("delete from sqlite_sequence where name=" + "\'"+TABLE_SETTINGS+"\'" );

    }

    public void deleteProduct(int idDairy){
        SQLiteDatabase db=this.getWritableDatabase();
       // Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " ORDER BY DATE DESC LIMIT 1 OFFSET " + numList, null);
        db.execSQL("DELETE FROM " + TABLE_FOOD + " WHERE DIARY_ID="+idDairy);
        casDataFood="deleteFoodData";
         idFoodInsert=idDairy;
        servProduct=ServerData.getIpServ()+casDataFood;
        if(settingUser.isNetworkAvailable()) {
            selectReserv();
            new HttpPostFoodData().execute();
        }


    }
    public void deleteToken(String token){
        SQLiteDatabase db=this.getWritableDatabase();
        // Cursor data = db.rawQuery("SELECT * FROM " + TABLE_FOOD + " ORDER BY DATE DESC LIMIT 1 OFFSET " + numList, null);
        db.execSQL("DELETE FROM " + TABLE_TOKEN + " WHERE TOKEN="+"\'"+token+"\'");


    }

    public void deleteReminder(int idReminder){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_REMINDER + " WHERE REMINDER_ID="+idReminder);


            if(settingUser.isNetworkAvailable()) {
                //selectReserv();
                new HttpPost().execute(ServerData.getIpServ() + "deleteReminder", "deleteReminder", Integer.toString(idReminder));
            }



    }
    public boolean update(Integer id,  String sugar,  String insulin,  String bredUnits,  String weight,  String comment,  String date1){
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


            if(settingUser.isNetworkAvailable()) {
                selectReserv();

                new HttpPost().execute(ServerData.getIpServ() + "updateDairy", "updateDairy", sugar, insulin, bredUnits, weight, comment, date1);
            } else{
                insertDataReserve(sugar, insulin,  bredUnits,  weight,  comment,  date1);
            }

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

    public Cursor getListContentsServer(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY DATE DESC", null);
        return data;
    }

    class HttpPost extends AsyncTask<String, Integer, Void> {
        Toast toast;
        protected String createJsonDiary(String... strings){
            String jsonBody="{\"dairy\": {\n\"insertData\": {\n\"email\": " + "\"" + selectEmail() + "\",\n" +
                    "\"blood_sugar\": " + "\"" + strings[0] + "\",\n" +
                    "\"breadunits\": " + "\"" + strings[2] + "\",\n" +
                    "\"insulin\": " + "\"" + strings[1] + "\",\n" +
                    "\"weight\": " + "\"" + strings[3] + "\",\n" +
                    "\"comment\": " + "\"" + strings[4] + "\",\n" +
                    "\"date\": " + "\"" + strings[5]  + "\",\n" +
                    "\"iddate\": " + "\"" + strings[6] +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }
        protected String deleteDairyJson(String... strings){
            String jsonBody="{\"diary\": {\n\"getData\": {\n\"email\": " + "\"" + selectEmail() +
                    "\",\n" +
                    "\"date\": " + "\"" + strings[0] + "\"\n"
                    +"}\n}}";

            return  jsonBody;
        }


        protected String insertJsonSettings(String... strings){
            String jsonBody="{\"diary\": {\n\"getData\": {\n\"email\": " + "\"" + selectEmail() +"\",\n" +
                    "\"xe_min\": " + "\"" + strings[0] + "\",\n" +
                    "\"xe_max\": " + "\"" + strings[1] + "\",\n" +
                    "\"xe_target\": " + "\"" + strings[2] + "\",\n" +
                    "\"xe_user\": " + "\"" + strings[3] + "\"\n"
                    +"}\n}}";

            return  jsonBody;
        }


        protected String createJsonReminder(String... strings){
            String jsonBody="{\"diary\": {\n\"reminderData\": {\n\"email\": " + "\"" + selectEmail() + "\",\n" +
                    "\"reminder_id\": " + "\"" + strings[0] + "\",\n" +
                    "\"reminder_date\": " + "\"" + strings[1] + "\",\n" +
                    "\"reminder_text\": " + "\"" + strings[2] + "\",\n" +
                    "\"repeat_day\": " + "\"" + strings[3] + "\",\n" +
                    "\"repeat_week\": " + "\"" + strings[4] + "\",\n" +
                    "\"no_repeat\": " + "\"" + strings[5] +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }

        protected String deleteReminderJson(String... strings){
            String jsonBody="{\"diary\": {\n\"reminderData\": {\n\"email\": " + "\"" + selectEmail() +
                    "\",\n" +
                    "\"reminder_id\": " + "\"" + strings[0] + "\"\n"
                    +"}\n}}";

            return  jsonBody;
        }




        @Override
        protected void onPreExecute() {

            //   spinner.setVisibility(View.VISIBLE);
        }



        @Override
        protected Void doInBackground(String... strings) {
            String mailTxt = strings[1];
            String passTxt = strings[2];

            OutputStream out = null;
            BufferedReader reader=null;
            try {
                String urlString = strings[0];
                String jsonBody;
                //  publishProgress(1);
                String hm=strings[1];

                switch(strings[1]){
                    case "dairyInsert": jsonBody= createJsonDiary(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8]); break;
                    case  "updateDairy": jsonBody= createJsonDiary(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]); break;
                    case  "deleteDiary": jsonBody= deleteDairyJson(strings[2]); break;
                    case "insertDataSettings":  jsonBody= insertJsonSettings(strings[2], strings[3], strings[4], "12"); break;
                    case "updateDataSettings": jsonBody= insertJsonSettings(strings[2], strings[3], strings[4], strings[5]); break;
                    case "reminderInsert": jsonBody= createJsonReminder(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]); break;
                    case  "reminderUpdate": jsonBody= createJsonReminder(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]); break;
                    case  "deleteReminder": jsonBody= deleteReminderJson(strings[2]); break;

                    default:  jsonBody="";
                }

                URL url = new URL(urlString);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(jsonBody);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                   // isCorrect = sb.append(line).toString();
                }



            } catch (Exception e) {

                e.printStackTrace();

            }

            finally {
                try
                {
                    reader.close();
                }

                catch(Exception ex) {}
            }






            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // spinner.setProgress(1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // spinner.setProgress(0);
            // spinner.setVisibility(View.GONE);
          //  toast.cancel();

        }
    }



    class HttpPostFoodData extends AsyncTask<ArrayList<String>, Integer, Void> {
        Toast toast;
        protected String createJsonDiary(ArrayList<String> ... strings){

            String email="\"email\": ";
            String diary_id="\"diary_id\": ";
            String name_product="\"name_product\":[ ";
            String grams_product="\"grams_product\":[ ";
            String carbs_product="\"carbs_product\":[ ";
            String emailSelected=selectEmail();
            for(int i=0; i<strings[0].size(); i++) {
              //  email+="\""+emailSelected+"\", ";

                // stringBredUnits.add(rs.getString(3));
                //diary_id+="\""+idFoodInsert+"\", ";

                //  stringInsulin.add(rs.getString(4));
                name_product+="\""+strings[0].get(i)+"\", ";

                //  stringWeight.add(rs.getString(5));
                grams_product+="\""+strings[1].get(i)+"\", ";

                // stringDate.add(rs.getString(7));
                carbs_product+="\""+strings[2].get(i)+"\", ";


            }
           // xemin+="\""+rsSettings.getString(3)+"\", \n";
            email+="\""+emailSelected+"\", \n";
            diary_id+="\""+idFoodInsert+"\", \n";
           // diary_id=diary_id.substring(0, diary_id.length()-2)+" ], \n";
            name_product=name_product.substring(0,name_product.length()-2)+" ], \n";
            grams_product=grams_product.substring(0,grams_product.length()-2)+" ], \n";
            carbs_product=carbs_product.substring(0,carbs_product.length()-2)+" ]\n";

            String jsonBody="{\n" + email + diary_id+name_product+grams_product+carbs_product+
                    "}";


            return  jsonBody;
        }
        protected String deleteDairyJson(){
            String jsonBody="{ \n\"email\": " + "\"" + selectEmail() +
                    "\",\n" +
                    "\"diary_id\": " + "\"" + idFoodInsert + "\"\n"
                    +"\n}";

            return  jsonBody;
        }



        @Override
        protected void onPreExecute() {

            //   spinner.setVisibility(View.VISIBLE);
        }

       // String servProduct=  ServerData.getIpServ() + "fooddataInsert";
        //String casDataFood=  "fooddataInsert";

        @Override
        protected Void doInBackground(ArrayList<String>... strings) {


            OutputStream out = null;
            BufferedReader reader=null;
            try {
                String urlString = servProduct;
                String jsonBody;
                //  publishProgress(1);

                switch(casDataFood){
                    case "fooddataInsert": jsonBody= createJsonDiary(strings[0], strings[1], strings[2]); break;
                   case  "updateDairyData": jsonBody= createJsonDiary(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]); break;
                    case  "deleteFoodData": jsonBody= deleteDairyJson(); break;

                    default:  jsonBody="";
                }

                URL url = new URL(urlString);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(jsonBody);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    // isCorrect = sb.append(line).toString();
                }



            } catch (Exception e) {

                e.printStackTrace();

            }

            finally {
                try
                {
                    reader.close();
                }

                catch(Exception ex) {}
            }






            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // spinner.setProgress(1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // spinner.setProgress(0);
            // spinner.setVisibility(View.GONE);
            //  toast.cancel();

        }
    }
}
