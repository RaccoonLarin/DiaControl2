package com.example.salima.diacontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SignUpActivity extends AppCompatActivity {

    private EditText mail;
    private EditText pass;
    private Button signup;
    private TextView txtCreateAcc;
    private String isCorrect; //false or token
  //  private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.createAcc);
    }

    //проверка верно ли введена структура почты
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //проверка верно ли введена структура пароля
    public final static boolean isValidPass(CharSequence target) {
        return !TextUtils.isEmpty(target) && (target.length()>=8);
    }


    public void onClick(View v){
        try {
            // sigin.setBackgroundColor(Color.GRAY);
            String mailTxt = mail.getText().toString();
            String passTxt = pass.getText().toString();
            if(!isValidEmail(mailTxt) ) {
                Toast toast = Toast.makeText(getApplicationContext(), "Неверная почта", Toast.LENGTH_SHORT);
                toast.show();
                return;

            } else if(!isValidPass(passTxt)){
                Toast toast = Toast.makeText(getApplicationContext(), "Пароль должен состоять из 8 или более символов", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            new HttpPost().execute(ServerData.getIpServ()+"signuppost", mailTxt, passTxt).get();
           // Thread.sleep(1000);
            if(isCorrect.equals("false")){
                Toast toast = Toast.makeText(getApplicationContext(), "Вы уже были ранее зарегестрированы", Toast.LENGTH_SHORT);
                toast.show();

            } else{
                Toast toast = Toast.makeText(getApplicationContext(), "Вы успешено зарегестрированы", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);

            }
            //  new HttpPost().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).execute("http://192.168.1.48:8080/signinpost").get();
            //hread.sleep(3000);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class HttpPost extends AsyncTask<String, Integer, Void> {
        Toast toast;
        protected String createJsonString(String... strings){
            String jsonBody="{\"diary\": {\n\"SignUp\": {\n\"email\": " + "\"" + strings[0] + "\",\n" + "\"password\": " + "\"" + strings[1] +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }

        @Override
        protected void onPreExecute() {
            //  super.onPreExecute();
            toast= Toast.makeText(getApplicationContext(), "Пожалуйста, подождите", Toast.LENGTH_SHORT);
            toast.show();
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
                    String jsonBody = createJsonString(mailTxt, passTxt);
                  //  publishProgress(1);

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
            toast.cancel();

        }
    }
}
