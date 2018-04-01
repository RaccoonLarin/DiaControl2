package com.example.salima.diacontrol;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    ArrayList<String> grams;
    ListView listView;
    CustomAdapter customListView;
    Button buttonSaveFood;


    ArrayList<String> foodList1;
    ArrayList<String> xeString1;
    ArrayList<String> grams1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView3 = (TextView) findViewById(R.id.textView3);
        foodList1 = new ArrayList<>();
        xeString1 = new ArrayList<>();
        grams1 = new ArrayList<>();
        buttonSaveFood=(Button) findViewById(R.id.buttonSaveFood);
        gett_json();

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.completeTxt);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, foodList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        setAutoCompleteTextViewListener();
        addListenerOnButtonSaveFood();




    }


    public void gett_json() {
        String json;
        foodList = new ArrayList<>();
        xeString = new ArrayList<>();
        grams = new ArrayList<>();
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
                grams.add("100");
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
                String selection = (String) parent.getItemAtPosition(position);
               // textView3.setText(foodList.get(position) + "-" + xeString.get(position));


                int id=foodList.indexOf(selection);
                foodList1.add(selection);
                grams1.add("100");
                xeString1.add( xeString.get(id));
                listView = (ListView) findViewById(R.id.listviewFood);

                customListView = new AddFoodActivity.CustomAdapter();
                listView.setAdapter(customListView);
            }
        });

    }

    public void addListenerOnButtonSaveFood(){
        buttonSaveFood.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

             //   Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
               // intent.putExtra("user-age", "Roman");
                // startActivityForResult(intent, REQUEST_CODE_FUCNCTIONONE);
               // startActivityForResult(intent, 1);
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View viewTelefone = listView.getChildAt(i);
                    EditText gramsEdit = (EditText) viewTelefone.findViewById(R.id.gramsEdit);
                    EditText calEdit = (EditText) viewTelefone.findViewById(R.id.foodEdit);
                    String edd= gramsEdit.getText().toString();
                    String eee=calEdit.getText().toString();
                }



                Intent intent = new Intent();
                intent.putExtra("foodList", foodList1);
               // intent.putExtra("")
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }




    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return foodList1.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1=getLayoutInflater().inflate(R.layout.food_listview_design, null);

            TextView nameFoodTxt= (TextView) view1.findViewById(R.id.nameFoodTxt);
            EditText gramsEdit=(EditText) view1.findViewById(R.id.gramsEdit);
            EditText foodEdit=(EditText) view1.findViewById(R.id.foodEdit);
            LinearLayout foodLayout=(LinearLayout) view1.findViewById(R.id.layoutFood);
            // TextView textinsulin= (TextView) view1.findViewById(R.id.insulinText);
            //   TextView textfood= (TextView) view1.findViewById(R.id.foodText);
            // TextView textDate = (TextView) view1.findViewById(R.id.dateDiary);
            // TextView textcomment= (TextView) view1.findViewById(R.id.commentText);
            // TextView textSeconds= (TextView) view1.findViewById(R.id.seconds);
            // RelativeLayout relativeLayoutSugar = (RelativeLayout) view1.findViewById(R.id.sugarLayout);
            //RelativeLayout relativeLayoutInsulin = (RelativeLayout) view1.findViewById(R.id.insulinLayou);
            // RelativeLayout relativeLayoutFood = (RelativeLayout) view1.findViewById(R.id.foodLayout);
            // RelativeLayout relativeLayoutComment = (RelativeLayout) view1.findViewById(R.id.commentLayout);

                foodLayout.setVisibility(View.VISIBLE);
                nameFoodTxt.setText(foodList1.get(i));
                gramsEdit.setText("100");
                foodEdit.setText(xeString1.get(i));










                return view1;
            }
        }

}


