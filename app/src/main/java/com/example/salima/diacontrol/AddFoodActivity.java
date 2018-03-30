package com.example.salima.diacontrol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {


    private AutoCompleteTextView autoCompleteTextView;
    TextView textView3;
    ArrayList<String> foodList;
    ArrayList<String> xeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView3 = (TextView) findViewById(R.id.textView3);

        gett_json();

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.completeTxt);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, foodList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        setAutoCompleteTextViewListener();

    }


    public void gett_json() {
        String json;
        foodList = new ArrayList<>();
        xeString=new ArrayList<>();
        try {

            InputStream is = getAssets().open("foodsamp.json");
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
                // String str = new String(obj.getString("foodname").getBytes("ISO-8859-1"), "UTF-8");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAutoCompleteTextViewListener() {

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                textView3.setText(foodList.get(position) + "-" + xeString.get(position));
            }
        });

    }

}
