package com.example.salima.diacontrol;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UserLogin extends AppCompatActivity {

    private EditText mail;
    private EditText pass;
    private Button sigin;
    private TextView txtCreateAcc, signInguest;
    private String isCorrect; //false or token
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        txtCreateAcc = (TextView) findViewById(R.id.signup);
        signInguest = (TextView) findViewById(R.id.signinguest);
        signInguest.setPaintFlags(signInguest.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст
        txtCreateAcc.setPaintFlags(txtCreateAcc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст

        sigin = (Button) findViewById(R.id.signin);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        SettingUser ss=new SettingUser(getApplicationContext());
        ss.array(); //SET REMINDER LIST
        ss.getArrayFromDataBase();
        ss.getXEUserFromDatabse();


        String[] files = fileList();
        int k=0;
        for (String file : files) {

            if (file.equals(ServerData.getTokentxt())) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
                finish();
            }

        }




    }

    public void createFile(){
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(ServerData.getTokentxt(), Context.MODE_PRIVATE);
            outputStream.write(isCorrect.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //проверка верно ли введена структура почты
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //проверка верно ли введена структура пароля
    public final static boolean isValidPass(CharSequence target) {
        return !TextUtils.isEmpty(target) && (target.length()>=8);
    }

    //Button Войти
    public void onClick(View v) {

        try {
            //создаем 1 row  в бд settingsXE
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());


          //  new HttpPost().execute(ServerData.getIpServ()+"signinpost");
           SettingUser su= new SettingUser(getApplicationContext());
                if(!su.isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
                    return;
                }

            Toast.makeText(getApplicationContext(), "Пожалуйста, подождите", Toast.LENGTH_LONG).show();
            HttpPost task = new HttpPost();
            try {
                task.execute(ServerData.getIpServ() + "signinpost").get(5000, TimeUnit.MILLISECONDS);
            } catch (Exception ew){
                Toast toast = Toast.makeText(getApplicationContext(), "Сервер недоступен", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
         //   Thread.sleep(1000);

          //  new HttpPost().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ServerData.getIpServ()+"signinpost");

            if(isCorrect.equals("false")){
                Toast toast = Toast.makeText(getApplicationContext(), "Неверная почта или пароль", Toast.LENGTH_SHORT);
                toast.show();
                return;

            }
            else {
              //  Toast toast = Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT);
              //  toast.show();
                createFile();
                //DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                db.insertToken(isCorrect, mail.getText().toString());
                SettingUser.isGuest=false;
                Cursor data = db.selectSettings();
                if(data.getCount()<=0) {
                    db.insertDataSettings(SettingUser.xeMax, SettingUser.xeMin, SettingUser.xeTarget, SettingUser.xe);
                }
                //new HttpPostGetList().execute(ServerData.getIpServ()+"getDiaryList");
                //new HttpPostGetList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ServerData.getIpServ()+"getDiaryList");
                HttpPostGetList task2 = new HttpPostGetList();
                task2.execute(ServerData.getIpServ()+"getDiaryList").get();
               // Thread.sleep(1000);
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                intent.putExtra("token", isCorrect);
                startActivity(intent);
                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Создать аккаунт
    public void onClickText(View v){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    //Создать аккаунт
    public void onClickGuestTExt(View v){
        isCorrect="AbShHjskaHjaskjsA";
        SettingUser.isGuest=true;
        createFile();
        Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
        intent.putExtra("token", isCorrect);
        startActivity(intent);
        finish();
    }



    //Проверяем есть ли  в бд данные о пользователе

    class HttpPost extends AsyncTask<String, Integer, Void> {
        Toast toast;
        protected String createJsonString(String... strings){
            String jsonBody="{\"diary\": {\n\"SignIn\": {\n\"email\": " + "\"" + strings[0] + "\",\n" + "\"password\": " + "\"" + strings[1] +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }

        protected String createJsonToGetList(String... strings){
            String jsonBody="{\"diary\": {\n\"getlistdairy\": {\n\"email\": " + "\"" + strings[0] + "\"" + strings[1] +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }

        @Override
        protected void onPreExecute() {
          //  super.onPreExecute();
          //  toast= Toast.makeText(getApplicationContext(), "Пожалуйста, подождите", Toast.LENGTH_SHORT);
           // toast.show();

            //  spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String mailTxt = mail.getText().toString();
            String passTxt = pass.getText().toString();
            SettingUser.email=mailTxt;
            if(isValidEmail(mailTxt) && isValidPass(passTxt) ){

                OutputStream out = null;
                BufferedReader reader=null;
                try {
                    String urlString = strings[0];
                    String jsonBody = createJsonString(mailTxt, passTxt);
                    publishProgress(1);

                    URL url = new URL(urlString);

                    // Send POST data request

                    URLConnection conn = url.openConnection();
                    conn.setConnectTimeout(15000);
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
                        isCorrect = sb.append(line).toString();
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




            }

            else {
                isCorrect="false";
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
          //  spinner.setProgress(1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
         //  spinner.setProgress(0);
          // spinner.setVisibility(View.GONE);

           // toast.cancel();

        }
    }

    //TODO ДОДЕОЛАТЬ

    class HttpPostGetList extends AsyncTask<String, Integer, Void> {
       // Toast toast;
      String createJsonToGetList(String... strings){
            String jsonBody="{\"diary\": {\n\"getlistdiary\": {\n\"email\": " + "\"" + strings[0] + "\"\n"+"}\n}}";

            return  jsonBody;
        }

        @Override
        protected void onPreExecute() {
            //  super.onPreExecute();
         //   toast= Toast.makeText(getApplicationContext(), "Пожалуйста, подождите", Toast.LENGTH_SHORT);
          //  toast.show();
          //  spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String mailTxt = mail.getText().toString();

                OutputStream out = null;
                BufferedReader reader=null;
                try {
                    String urlString = strings[0];
                    String jsonBody = createJsonToGetList(mailTxt);
                    publishProgress(1);

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

                    String jsonString="";
                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        jsonString+= line+"\n";

                    }
                    Log.d("m", jsonString);
                  //  JsonArray jsonElements=new JsonParser().parse(jsonString).getAsJsonArray();
                    DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                    db.deleteAll(); //удаляем все данные из локальной таблицы
                    JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
                    ArrayList<String> sugar=new ArrayList<>();

                    JsonArray arraySugar = (JsonArray)jsonObject.get("sugar");
                    JsonArray arrayBreadUnits = (JsonArray)jsonObject.get("breadunits");
                    JsonArray arrayInsulin = (JsonArray)jsonObject.get("insulin");
                    JsonArray arrayWeight = (JsonArray)jsonObject.get("weight");
                    JsonArray arrayComment= (JsonArray)jsonObject.get("comment");
                    JsonArray arrayDate= (JsonArray)jsonObject.get("date");

                    String xemin = jsonObject.get("xemin").getAsString();
                    String xemax = jsonObject.get("xemax").getAsString();
                    String xetarget = jsonObject.get("xetarget").getAsString();
                    String xeuser = jsonObject.get("xeuser").getAsString();
                    Double xeminTemp, xeMaxTemp, xeTargetTemp;
                    Integer xeUserTemp;

                    if(xemin.equals("")){
                        xeminTemp=null;
                    } else {
                        xeminTemp=Double.parseDouble(xemin);
                    }
                    if(xemax.equals("")){
                        xeMaxTemp=null;
                    } else {
                        xeMaxTemp=Double.parseDouble(xemax);
                    }
                    if(xetarget.equals("")){
                        xeTargetTemp=null;
                    } else {
                        xeTargetTemp=Double.parseDouble(xetarget);
                    }
                    if(xeuser.equals("")){
                        xeUserTemp=null;
                    } else {
                        xeUserTemp=Integer.parseInt(xeuser);
                    }

                    ArrayList <String> arraySugarList =new ArrayList<>();
                    ArrayList <String> arrayBreadUnitsList = new ArrayList<>();
                    ArrayList <String> arrayInsulinList = new ArrayList<>();
                    ArrayList <String> arrayWeightList = new ArrayList<>();
                    ArrayList <String> arrayCommentList= new ArrayList<>();
                    ArrayList <String> arrayDateList= new ArrayList<>();

                    for (int i=0; i<arrayDate.size(); i++) {
                        arraySugarList.add(arraySugar.get(i).getAsString());
                        arrayBreadUnitsList.add( arrayBreadUnits.get(i).getAsString());
                        arrayInsulinList.add(arrayInsulin.get(i).getAsString());
                        arrayWeightList.add(arrayWeight.get(i).getAsString());
                        arrayCommentList.add(arrayComment.get(i).getAsString());
                        arrayDateList.add(arrayDate.get(i).getAsString());
                       // db.insertData(arraySugar.get(i).getAsString(), arrayInsulin.get(i).getAsString(), arrayBreadUnits.get(i).getAsString(),
                             //   arrayWeight.get(i).getAsString(), arrayComment.get(i).getAsString(),  arrayDate.get(i).getAsString());
                     //   sugar.add( array.get(i).getAsString() );
                    }
                  //  String s = array.get(0).getAsString();
                    db.insertDataArray(arraySugarList, arrayInsulinList, arrayBreadUnitsList, arrayWeightList,
                            arrayCommentList, arrayDateList);


                    db.insertDataSettings(xeMaxTemp, xeminTemp,xeTargetTemp, xeUserTemp);

                    SettingUser ss=new SettingUser(getApplicationContext());
                   // ss.array(); //SET REMINDER LIST
                   // ss.getArrayFromDataBase();
                    ss.getXEUserFromDatabse();



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
            spinner.setProgress(1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
         //   spinner.setProgress(0);
         //   spinner.setVisibility(View.GONE);
           // toast.cancel();

        }
    }
}


