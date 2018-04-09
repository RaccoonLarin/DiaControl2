package com.example.salima.diacontrol;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {

    Double finalCarbs=0.0;
    Double itogo=0.0;
    private AutoCompleteTextView autoCompleteTextView;
    TextView textView3, finalText;
    EditText gramsEdit, calEdit;
    //данные из json
    //rrayList<String> foodList, xeString, grams;
    ListView listView;
    CustomAdapter customListView;
    Button buttonSaveFood, buttonAddFood;

    String flag;

   //пользовательские данные
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
        finalText = (TextView) findViewById(R.id.textView10);
        finalText.setText("Итого: ");
        foodList1 = new ArrayList<>();
        xeString1 = new ArrayList<>();
        grams1 = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listviewFood);
        buttonSaveFood=(Button) findViewById(R.id.buttonSaveFood);
        buttonAddFood=(Button) findViewById(R.id.buttonAddFood);
       // gett_json();



        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.completeTxt);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, setUse.foodList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        setAutoCompleteTextViewListener();
        addListenerOnButtonSaveFood();
        addListenerOnButtonAddFood();
        flag=getIntent().getStringExtra("flag");
        if(flag.equals("true")){
            fullList(getIntent());
        }
        customListView = new AddFoodActivity.CustomAdapter();
        listView.setAdapter(customListView);
        setOnLongClickListener();

    }


    /*public void gett_json() {
        String json;
        foodList = new ArrayList<>();
        xeString = new ArrayList<>();
        grams = new ArrayList<>();
        try {

            InputStream is = getAssets().open("foodsamp1.json");
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
    }*/

    void fullList(Intent data){
        foodList1 =(ArrayList<String>) data.getStringArrayListExtra("foodList");
        grams1 =(ArrayList<String>) data.getStringArrayListExtra("gramsList");
        xeString1 =(ArrayList<String>) data.getStringArrayListExtra("carbsList");
        itogo=data.getDoubleExtra("xe", 1);
        finalCarbs=0.0;
        for(int i=0; i<xeString1.size(); i++){
            finalCarbs+= Double.parseDouble(xeString1.get(i));
        }


        itogo = finalCarbs / setUse.xe;
        itogo= new BigDecimal(itogo).setScale(2, RoundingMode.HALF_UP).doubleValue();
        finalCarbs=new BigDecimal(finalCarbs).setScale(2, RoundingMode.HALF_UP).doubleValue();
        finalText.setText("Итого: " + itogo.toString() + " ХЕ или " + finalCarbs.toString() + " грамм");

    }


    public void setOnLongClickListener(){

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, final View view,
                                                   final int position, long id) {
                        //  Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
                        final View view1=view;
                        final  int position1=position;
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(AddFoodActivity.this)
                                .setTitle("Удалить запись?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        foodList1.remove(position);
                                        grams1.remove(position);
                                        xeString1.remove(position);
                                        // listView = (ListView) findViewById(R.id.listviewFood);

                                        customListView = new AddFoodActivity.CustomAdapter();
                                        listView.setAdapter(customListView);

                                        try {
                                            finalCarbs=0.0;
                                            for(int i=0; i<xeString1.size(); i++){
                                                finalCarbs+= Double.parseDouble(xeString1.get(i));
                                            }


                                             itogo = finalCarbs / setUse.xe;
                                            itogo= new BigDecimal(itogo).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            finalCarbs=new BigDecimal(finalCarbs).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            finalText.setText("Итого: " + itogo.toString() + " ХЕ или " + finalCarbs.toString() + " грамм");
                                        }
                                        catch (Exception e){
                                            finalText.setText("Итого: ");
                                        }

                                    }
                                })
                                .setNegativeButton("Нет", null).create();
                        dialog.show();


                        return  true;
                    }
                }

                // }

        );
    }




    public void setAutoCompleteTextViewListener() {

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
               // textView3.setText(foodList.get(position) + "-" + xeString.get(position));

                int id=setUse.foodList.indexOf(selection);
                foodList1.add(selection);
                grams1.add(setUse.grams.get(id));
                xeString1.add( setUse.xeString.get(id));
               // listView = (ListView) findViewById(R.id.listviewFood);

                customListView = new AddFoodActivity.CustomAdapter();
                listView.setAdapter(customListView);
                 finalCarbs+= Double.parseDouble(setUse.xeString.get(id));
                 itogo=finalCarbs/setUse.xe;
                itogo= new BigDecimal(itogo).setScale(2, RoundingMode.HALF_UP).doubleValue();
                finalCarbs=new BigDecimal(finalCarbs).setScale(2, RoundingMode.HALF_UP).doubleValue();

                finalText.setText("Итого: "+itogo.toString() + " ХЕ или " + finalCarbs.toString() + " грамм");

            }
        });

    }


    public void addListenerOnListView(){

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, final View view,
                                                   final int position, long id) {
                        //  Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
                        final View view1=view;
                        final  int position1=position;
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getApplicationContext())
                                .setTitle("Удалить запись?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        foodList1.remove(position);
                                        grams1.remove(position);
                                        xeString1.remove(position);
                                        customListView = new AddFoodActivity.CustomAdapter();
                                        listView.setAdapter(customListView);

                                    }
                                })
                                .setNegativeButton("Нет", null).create();
                        dialog.show();

                        return  true;
                    }
                }

                // }

        );
    }





    public void addListenerOnButtonSaveFood(){
        buttonSaveFood.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int k =listView.getCount();
                Intent intent = new Intent();
                if(k==0){
                   // Toast.makeText(AddFoodActivity.this, "Добавьте продукты", Toast.LENGTH_LONG).show();
                    setResult(-2, intent);
                    finish();
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


                intent.putStringArrayListExtra("foodList", foodList1);
                intent.putStringArrayListExtra("carbsList", tempcal);
                intent.putStringArrayListExtra("gramsList", tempGrams);
                intent.putExtra("xe", itogo);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent();
        if(foodList1.size()<=0) {
            setResult(-2, intent);
        }
        finish();
    }*/

    //TODO view1 создать новый лейаут дизайн кастомный для мессаджбокса

    public void addListenerOnButtonAddFood(){
        buttonAddFood.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText edittext = new EditText(v.getContext());
                final CheckBox checkbox=new CheckBox(v.getContext());
                LinearLayout linearLayout=new LinearLayout(v.getContext());

                alert.setMessage("Введите данные");

                final View view1=getLayoutInflater().inflate(R.layout.design_add_food_dialog, null);
                final EditText editText1=(EditText)view1.findViewById(R.id.editText);
                final EditText editText2=(EditText)view1.findViewById(R.id.editText2);
                final EditText editText3=(EditText)view1.findViewById(R.id.editText3);
                final CheckBox checkBox=(CheckBox) view1.findViewById(R.id.checkBox);
                LinearLayout foodLayout=(LinearLayout) view1.findViewById(R.id.addFoodLayout);

                alert.setView(view1);

                alert.setPositiveButton("СОХРАНИТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                       // Editable YouEditTextValue = edittext.getText();
                        //OR

                        String YouEditTextValue = editText1.getText().toString();
                        String YouEditTextValue2 = editText2.getText().toString();
                        String YouEditTextValue3 = editText3.getText().toString();
                        foodList1.add(YouEditTextValue);
                        grams1.add(YouEditTextValue2);
                        xeString1.add(YouEditTextValue3);
                        customListView = new AddFoodActivity.CustomAdapter();
                        listView.setAdapter(customListView);

                        if(checkBox.isChecked()){
                            //setUse setUse=new setUse(getApplicationContext());
                            //setUse.user_json_add(YouEditTextValue, YouEditTextValue2, YouEditTextValue3);
                               //addJson(YouEditTextValue, YouEditTextValue2, YouEditTextValue3);
                            //TODO записывать еду пользователя в sqlite
                        }
                    }
                });

                alert.setNegativeButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });
                final AlertDialog dialog = alert.create();
                dialog.show();


                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(false);

                editText1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Check if edittext is empty
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(editText2.getText()) || TextUtils.isEmpty(editText3.getText())) {
                            // Disable ok button
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }

                    }
                });

                editText2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Check if edittext is empty
                        String userArg=s.toString();

                        if (TextUtils.isEmpty(s) || userArg.equals("") || TextUtils.isEmpty(editText1.getText()) || TextUtils.isEmpty(editText3.getText())) {
                            // Disable ok button
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            return;
                        }

                        if(userArg.equals("0")){
                            userArg="1";

                            editText2.setText(userArg);
                            editText2.post(new Runnable() {
                                @Override
                                public void run() {
                                    editText2.setSelection(1);
                                }
                            });
                        }

                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON_POSITIVE).setEnabled(true);


                    }
                });

                editText3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Check if edittext is empty
                        String userArg=s.toString();

                        if (TextUtils.isEmpty(s) || userArg.equals("") || TextUtils.isEmpty(editText1.getText()) || TextUtils.isEmpty(editText2.getText())) {
                            // Disable ok button
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            return;
                        }

                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON_POSITIVE).setEnabled(true);


                    }
                });

            }
        });


    }

   public  void addJson(String name, String grams, String carbs){

       try {
           String  json2;
           FileOutputStream outputStream;

               outputStream = openFileOutput("userProduct2.json", Context.MODE_PRIVATE);
              // outputStream.write(isCorrect.getBytes());
               outputStream.close();

           InputStream is2 = getAssets().open("userProduct2.json");
           int size2=is2.available();
           byte[] buffer2 = new byte[size2];
           is2.read(buffer2);
           is2.close();
           json2 = new String(buffer2, "UTF-8");
           JSONArray jsonArray2 = new JSONArray(json2);
           int l=jsonArray2.length();

           JSONObject jO = new JSONObject(); //new Json Object

           //Add data
           jO.put("foodname", name);
           jO.put("Carb", carbs);
           jO.put("grams", grams);
           jsonArray2.put(jO);
           OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("userProduct2.json", Context.MODE_PRIVATE));
           //  outputStreamWriter.write(jO.toString() );


           outputStreamWriter.write(jsonArray2.toString());

           outputStreamWriter.close();

       } catch (IOException e) {
           e.printStackTrace();
       } catch (JSONException e) {
           e.printStackTrace();
       }

   }

    class CustomAdapter extends BaseAdapter  implements View.OnTouchListener {

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


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.editText1.setFocusable(false);
                holder.editText1.setFocusableInTouchMode(false);
                holder.editText2.setFocusable(false);
                holder.editText2.setFocusableInTouchMode(false);
            }
            return false;
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
            final EditText gramsEdit=(EditText) view.findViewById(R.id.gramsEdit);
             final EditText foodEdit=(EditText) view.findViewById(R.id.foodEdit);
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
                       // double temp=Integer.parseInt(xeString1.get(holder.ref))*Integer.parseInt(arg0.toString())/Integer.parseInt(grams1.get(holder.ref));
                        xeString1.set(holder.ref, arg0.toString());
                    }
                });


            holder.editText1.setOnTouchListener(this);
            holder.editText2.setOnTouchListener(this);
            view.setOnTouchListener(this);


            //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           // imm.showSoftInput( holder.editText1, InputMethodManager.SHOW_IMPLICIT);



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
                    //TODO на ноль делить нельзяяя
                    String userArg=arg0.toString();
                    if(userArg.equals("")|| (userArg.substring(userArg.length()-1)).equals(".")){
                        foodEdit.setText(xeString1.get(holder.ref));
                        return;
                    }


                    if(userArg.equals("0")){
                        userArg="1";
                        gramsEdit.setText(userArg);
                        gramsEdit.post(new Runnable() {
                            @Override
                            public void run() {
                                gramsEdit.setSelection(1);
                            }
                        });
                    }

                  //  if(Double.parseDouble(userArg)<1){
                        //grams1.set(holder.ref, "1");
                    //    userArg="1";
                      //  gramsEdit.setText("1");
                    //}


                    double temp=Double.parseDouble(xeString1.get(holder.ref))*Double.parseDouble(userArg)/Double.parseDouble(grams1.get(holder.ref));
                    foodEdit.setText(Double.toString(temp));
                    grams1.set(holder.ref, userArg);
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


