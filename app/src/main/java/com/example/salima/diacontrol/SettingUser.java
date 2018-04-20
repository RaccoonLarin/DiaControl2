package com.example.salima.diacontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.widget.Toast;

import com.amitshekhar.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class SettingUser {
     public static Integer xe;
     AssetManager mngr;
     Context myContext;
     public SettingUser(Context myContext) {
           mngr = myContext.getAssets();
           this.myContext=myContext;
        //  InputStream is = mngr.open("textdb.txt");
     }

     public static ArrayList<String> textReminder, timeTextReminder;
     public static ArrayList<Integer> idRerminder, repeatDay, repeatWeak, noRepeat;
     public static ArrayList<String>  foodList;
     public static ArrayList<String>  xeString;
     public static ArrayList<String>  grams;

     //вызов метода в UserLogin
     public static void array(){
         textReminder=new ArrayList<>();
         timeTextReminder=new ArrayList<>();
         idRerminder=new ArrayList<>();
         repeatDay=new ArrayList<>();
         repeatWeak=new ArrayList<>();
         noRepeat=new ArrayList<>();
     }

     public  void getArrayFromDataBase(){
         DatabaseHelper db =new DatabaseHelper(myContext);
         Cursor data = db.getListReminder();
         RimenderFragment rt=new RimenderFragment();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         Calendar calendar = Calendar.getInstance();

         long millisecondsDateNow=0, mill=0;
         if(data.getCount()==0){

         }
         while (data.moveToNext()){
             idRerminder.add(data.getInt(1));
             timeTextReminder.add(data.getString(2));
             textReminder.add(data.getString(3));
             // stringfood=data.getString(3);
             repeatDay.add(data.getInt(4));
             repeatWeak.add(data.getInt(5));
             noRepeat.add(data.getInt(6));
         }

         for(int i=0; i<idRerminder.size(); i++){
             try{
                Calendar calendarNow = Calendar.getInstance();
                 millisecondsDateNow=calendarNow.getTimeInMillis();
                 calendar.setTime(simpleDateFormat.parse(timeTextReminder.get(i)));
                 mill=calendar.getTimeInMillis();

             } catch (ParseException e) {
                 e.printStackTrace();
             }
             if(mill>=millisecondsDateNow){
                 startAlarm(mill,convertFromIntToBoolean(repeatDay.get(i)), convertFromIntToBoolean(repeatWeak.get(i)),
                         textReminder.get(i),idRerminder.get(i));
             }
         }


        // rt.startAlarm();
     }

    public void startAlarm(long timeInMills, boolean repeatingDay, boolean repeatingWeek, String text, int id){

        AlarmManager manager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;


        myIntent= new Intent(myContext,AlarmNotificationReceiver.class);
        myIntent.putExtra("id", id);
        myIntent.putExtra("text", text);
        pendingIntent=PendingIntent.getBroadcast(myContext,id,myIntent,PendingIntent.FLAG_ONE_SHOT);

        if(repeatingDay){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        if(repeatingWeek){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, 7*AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        if(!(repeatingDay||repeatingWeek)) {
            //TODO изменить на set
            manager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
            //manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_FIFTEEN_MINUTES%10, pendingIntent);
        }

        //Toast.makeText(myContext, "Напоминание установлено", Toast.LENGTH_SHORT).show();

    }
     public void gett_json() {
          String json, json2;
          foodList = new ArrayList<>();
          xeString = new ArrayList<>();
          grams = new ArrayList<>();
          try {

               InputStream is = mngr.open("foodsamp1.json");
              // InputStream is2 = mngr.open("userProduct.json");
               int size = is.available();
             //  int size2=is2.available();
               byte[] buffer = new byte[size];
             //  byte[] buffer2 = new byte[size2];
               is.read(buffer);
               is.close();
              // is2.read(buffer2);
            //   is2.close();

               json = new String(buffer, "UTF-8");
               JSONArray jsonArray = new JSONArray(json);


             //  json2 = new String(buffer2, "UTF-8");
             //  JSONArray jsonArray2 = new JSONArray(json2);
              // int l=jsonArray2.length();


               for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    foodList.add(obj.getString("foodname"));
                    xeString.add(obj.getString("Carb"));
                    grams.add(obj.getString("grams"));
                    // String str = new String(obj.getString("foodname").getBytes("ISO-8859-1"), "UTF-8");
               }

              /* for(int i=0; i<jsonArray2.length(); i++){
                    JSONObject obj = jsonArray2.getJSONObject(i);
                    foodList.add(obj.getString("foodname"));
                    xeString.add(obj.getString("Carb"));
                    grams.add(obj.getString("grams")); //T
               }*/




          } catch (IOException e) {
               e.printStackTrace();
          } catch (JSONException e) {
               e.printStackTrace();
          }
     }

     public static Integer convertFromBoolToInt(Boolean flag){
         if(flag) return 1;
         else return 0;

     }

    public static Boolean convertFromIntToBoolean(Integer flag){
        if(flag==0) return false;
        else return true;

    }
     private static String removeLastChar(String str) {
          return str.substring(0, str.length() - 2);
     }

     public  void user__add_product_from_database(){
         DatabaseHelper db=new DatabaseHelper(myContext);
         Cursor data = db.selectAllUserProduct();
         if(data.getCount()==0){

         }

         while (data.moveToNext()){
             foodList.add(data.getString(1));
             xeString.add(data.getString(3));
             grams.add(data.getString(2));
         }

     }

}
