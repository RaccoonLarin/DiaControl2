package com.example.salima.diacontrol;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;

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
import java.util.ArrayList;

public class setUse {
     public static Integer xe;
     AssetManager mngr;
     Context myContext;
     public setUse(Context myContext) {
           mngr = myContext.getAssets();
           this.myContext=myContext;
        //  InputStream is = mngr.open("textdb.txt");
     }

     public static ArrayList<String>  foodList;
     public static ArrayList<String>  xeString;
     public static ArrayList<String>  grams;
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
