package com.example.salima.diacontrol;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*Добавление записи в дневник. Показывается когда пользователь нажимает кнопку + в DiaryActivity*/
public class AddActivity extends AppCompatActivity {

    private Button button;
    TextView dateTxt;
    TextView timeTxt;
    int year_x, month_x, day_x;

    int hour_x, minute_x, seconds_x; //seconds_x;
    SimpleDateFormat simpleDateFormat;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addListenerOnButton();

        dateTxt=(TextView) findViewById(R.id.date);
        timeTxt=(TextView) findViewById(R.id.time);

        dateTxt.setPaintFlags(dateTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст
        timeTxt.setPaintFlags(timeTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст
        Calendar calendar  = Calendar.getInstance();



        year_x=calendar.get(Calendar.YEAR);
        month_x=calendar.get(Calendar.MONTH);
        day_x=calendar.get(Calendar.DAY_OF_MONTH);
        hour_x=calendar.get(Calendar.HOUR_OF_DAY);
        minute_x=calendar.get(Calendar.MINUTE);
        seconds_x=calendar.get(Calendar.SECOND);
      //  seconds_x=calendar.get(Calendar.SECOND);

        timePickerDialog=new TimePickerDialog(this, timePickerListner,hour_x, minute_x,  true);
        datePickerDialog=new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);

        dateTxt.setText(getStringDay(day_x) + "." + getStringMonth(month_x+1) + "." + year_x);
       timeTxt.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x));

        addListenerOnText();



      //  String edit=getIntent().getStringExtra("edit");
     //   if(edit.equals("true")){

      //  }
      //  int i=0;
     //   i=getIntent().getIntExtra("item", -1);
    }

    DatabaseHelper db;
    String str;
    String str1;
    String str2;

    public void  addListenerOnText() {
        dateTxt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       datePickerDialog.show();

                    }
                });

        timeTxt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        timePickerDialog.show();

                    }
                });



    }



    private  DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x=i;
            month_x=i1;
            day_x=i2;
            dateTxt.setText(getStringDay(day_x) + "." + getStringMonth(month_x+1) + "." + year_x);


          //  Toast.makeText(AddActivity.this, day_x + "." + month_x + "." + year_x, Toast.LENGTH_LONG).show();
        }
    };

    protected  TimePickerDialog.OnTimeSetListener timePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            hour_x = i;
            minute_x = i1;
            timeTxt.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x) );

          //  timePickerDialog.closeOptionsMenu();


        }
    };

    public void addListenerOnButton(

    ) {

        button = (Button) findViewById(R.id.okButton);


        button.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) findViewById(R.id.sugarEdit);
                        EditText editText1 = (EditText) findViewById(R.id.insulinEdit);
                        EditText editText2 = (EditText) findViewById(R.id.commentEdit);
                         str = editText.getText().toString();
                         str1 = editText1.getText().toString();
                         str2=editText2.getText().toString();
                        int i=0;
                        i=getIntent().getIntExtra("item", -1);
                        Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                     //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("FRAGMENT_ID", 1);
                        intent.putExtra("year_x", year_x);
                        intent.putExtra("month_x", month_x);
                        intent.putExtra("day_x", day_x);
                        addData();
                    //    setResult(RESULT_OK,  intent);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        new DiaryFragment();
                  //     finish(); // TODO убрать если передаются данные

                    }
                });
    }

    public void addData(){
        boolean isInserted;

       db=new DatabaseHelper(this);
        String plswork = str;
        String insulin = str1;
        String comment = str2;
        String date1=year_x+"-"+getStringMonth(month_x+1)+"-"+getStringDay(day_x)+" "+getStringTime(hour_x)+":"+getStringTime(minute_x)+":"+getStringTime(seconds_x);

        if (plswork.equals("") ||insulin.equals("")  || comment.equals("")  ){

            return;
        }



            isInserted= db.insertData(plswork, insulin, comment, date1);

        if(isInserted)
            Toast.makeText(this, "Data inserted", Toast.LENGTH_LONG);
        else
            Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
        //adapter.notifyDataSetChanged();

        //  bottomNavigationViewEx.setSelectedItemId(R.id.navigation_diary);
    }

    private String getStringDay(int day){


        if(day < 10){

            return  "0" + day;
        }

        return Integer.toString(day);
    }

   private String getStringMonth(int month){


        if(month < 10){

            return  "0" + month;
        }

        return Integer.toString(month);
    }


    private String getStringTime(int time){


        if(time < 10){

            return  "0" + time;
        }

        return Integer.toString(time);
    }



}
