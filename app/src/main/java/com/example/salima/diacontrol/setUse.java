package com.example.salima.diacontrol;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class setUse {
     public static Integer xe;
     AssetManager mngr;
     public setUse(Context myContext) {
           mngr = myContext.getAssets();
        //  InputStream is = mngr.open("textdb.txt");
     }

     public static ArrayList<String>  foodList;
     public static ArrayList<String>  xeString;
     public static ArrayList<String>  grams;
     public void gett_json() {
          String json;
          foodList = new ArrayList<>();
          xeString = new ArrayList<>();
          grams = new ArrayList<>();
          try {

               InputStream is = mngr.open("foodsamp1.json");
               int size = is.available();
               byte[] buffer = new byte[size];
               is.read(buffer);
               is.close();

               json = new String(buffer, "UTF-8");
               JSONArray jsonArray = new JSONArray(json);


               for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    foodList.add(obj.getString("foodname"));
                    xeString.add(obj.getString("Carb"));
                    grams.add(obj.getString("grams")); //TODO change
                    // String str = new String(obj.getString("foodname").getBytes("ISO-8859-1"), "UTF-8");
               }




          } catch (IOException e) {
               e.printStackTrace();
          } catch (JSONException e) {
               e.printStackTrace();
          }
     }

}
