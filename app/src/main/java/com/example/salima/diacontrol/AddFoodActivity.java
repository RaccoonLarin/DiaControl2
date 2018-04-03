package com.example.salima.diacontrol;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.Toast;

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
    EditText gramsEdit, calEdit;
    ArrayList<String> foodList;
    ArrayList<String> xeString;
    ArrayList<String> grams;
    ListView listView;
    CustomAdapter customListView;
    Button buttonSaveFood;

    String flag;


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
        listView = (ListView) findViewById(R.id.listviewFood);
        buttonSaveFood=(Button) findViewById(R.id.buttonSaveFood);
        gett_json();



        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.completeTxt);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, foodList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        setAutoCompleteTextViewListener();
        addListenerOnButtonSaveFood();

        flag=getIntent().getStringExtra("flag");
        if(flag.equals("true")){
            fullList(getIntent());
        }
        customListView = new AddFoodActivity.CustomAdapter();
        listView.setAdapter(customListView);

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

    void fullList(Intent data){
        foodList1 =(ArrayList<String>) data.getStringArrayListExtra("foodList");
        grams1 =(ArrayList<String>) data.getStringArrayListExtra("gramsList");
        xeString1 =(ArrayList<String>) data.getStringArrayListExtra("carbsList");

    }

    public void listenerFocusChange(){


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
               // listView = (ListView) findViewById(R.id.listviewFood);

                customListView = new AddFoodActivity.CustomAdapter();
                listView.setAdapter(customListView);
            }
        });

    }




    public void addListenerOnButtonSaveFood(){
        buttonSaveFood.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int k =listView.getCount();

                if(k==0){
                    Toast.makeText(AddFoodActivity.this, "Добавьте продукты", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<String> tempGrams=new ArrayList<>();
                ArrayList<String> tempcal=new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View viewTelefone = listView.getChildAt(i);
                    gramsEdit = (EditText) viewTelefone.findViewById(R.id.gramsEdit);
                    calEdit = (EditText) viewTelefone.findViewById(R.id.foodEdit);
                    if(TextUtils.isEmpty(gramsEdit.getText().toString()) || TextUtils.isEmpty(calEdit.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show();
                       // gramsEdit.setError("Your message");
                       // gramsEdit.setBackgroundColor(getResources().getColor(R.color.cuteColor));
                      //  gramsEdit.set
                        return;
                    }
                    tempGrams.add(gramsEdit.getText().toString());
                    tempcal.add(calEdit.getText().toString());
                }

                Intent intent = new Intent();
                intent.putStringArrayListExtra("foodList", foodList1);
                intent.putStringArrayListExtra("carbsList", tempcal);
                intent.putStringArrayListExtra("gramsList", tempGrams);
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
            return foodList1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

      //об
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           // View view1=getLayoutInflater().inflate(R.layout.food_listview_design, null);

            final ViewHolder holder;
            if(view==null){
                holder=new ViewHolder();
                LayoutInflater inflater = AddFoodActivity.this.getLayoutInflater();
                view = inflater.inflate(R.layout.food_listview_design, null);
                holder.textView1 = (TextView) view.findViewById(R.id.nameFoodTxt);
                holder.editText1 = (EditText) view.findViewById(R.id.foodEdit);
                holder.editText2 = (EditText) view.findViewById(R.id.gramsEdit);
                view.setTag(holder);

        }  else {

            holder = (ViewHolder) view.getTag();
        }
            TextView nameFoodTxt= (TextView) view.findViewById(R.id.nameFoodTxt);
            EditText gramsEdit=(EditText) view.findViewById(R.id.gramsEdit);
             EditText foodEdit=(EditText) view.findViewById(R.id.foodEdit);
            LinearLayout foodLayout=(LinearLayout) view.findViewById(R.id.layoutFood);


                foodLayout.setVisibility(View.VISIBLE);
                nameFoodTxt.setText(foodList1.get(i));


            holder.ref = i;

            holder.textView1.setText(foodList1.get(i));
            holder.editText1.setText(xeString1.get(i));
            holder.editText2.setText(grams1.get(i));

            holder.editText1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        xeString1.set(holder.ref, arg0.toString());
                    }
                });



            holder.editText2.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    grams1.set(holder.ref, arg0.toString());
                }
            });

              gramsEdit.setText(grams1.get(i));
              foodEdit.setText(xeString1.get(i));




                return view;
            }



    private class ViewHolder {
        TextView textView1;
        EditText editText1;
        EditText editText2;
        int ref;
        int ref2;
    }



        }

}


