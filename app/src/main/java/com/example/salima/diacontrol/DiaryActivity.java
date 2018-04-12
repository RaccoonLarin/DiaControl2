package com.example.salima.diacontrol;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amitshekhar.DebugDB;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DiaryActivity extends AppCompatActivity  implements DiaryFragment.OnFragmentInteractionListener, StatisticFragment.OnFragmentInteractionListener, RimenderFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener{
    public static final int REQUEST_CODE_FUCNCTIONONE = 100;
    private TextView mTextMessage;
    private String token;
    private  boolean isFirstOpenActivity=true; //первый раз ли зашли в данное активити
    public boolean isInDiaryFragment; //находимся ли в фрагменте fragment_diary
    Toolbar toolbar;
    TextView toolbartext;
    BottomNavigationViewEx bottomNavigationViewEx;


    DatePickerDialog datePickerDialog;
    int year_x, month_x, day_x;

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fn=getSupportFragmentManager();
            FragmentTransaction ft=fn.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_diary:  //menu navigation
                    toolbartext.setText("Дневник");
                    ft.replace(R.id.content, new DiaryFragment() ).commit();
                    return true;
                case R.id.navigation_add: // добавляем новую запись
                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                    intent.putExtra("user-age", "Roman");
                   // startActivityForResult(intent, REQUEST_CODE_FUCNCTIONONE);
                  startActivity(intent);
                  //finish();//убрать если передаются данные
                 //   overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    return  true;
                case R.id.navigation_statistoc:
                    toolbartext.setText("Статистика");
                    ft.replace(R.id.content, new StatisticFragment()).commit();
                    isInDiaryFragment=false;
                    return  true;

                case R.id.navigation_alaram:
                    toolbartext.setText("Напоминания");
                    ft.replace(R.id.content, new RimenderFragment()).commit();
                    isInDiaryFragment=false;
                    return  true;
                case R.id.navigation_more:
                    toolbartext.setText("Настройки");
                    ft.replace(R.id.content, new SettingsFragment()).commit();
                    //isInDiaryFragment=false;
                    return  true;



            }
            return false;
        }
    };
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        token=getIntent().getStringExtra("token");
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbartext = (TextView) findViewById(R.id.toolbar_title);
        db=new DatabaseHelper(this);

        Log.d(DebugDB.getAddressLog(), "EEE");

        int fragmentId = getIntent().getIntExtra("FRAGMENT_ID", 0);

        //Устанавлиаем XE пользователя
        setUse.xe=12; //TODO установка
        setUse stUs=new setUse(getApplicationContext());
        stUs.gett_json(); //парсим джейсон файл с продуктам





        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navigation);
        setupBottomNavigationView();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(fragmentId==1) {
            Bundle bundle = new Bundle();
            bottomNavigationViewEx.setSelectedItemId(R.id.navigation_diary);

        }



        bottomNavigationViewEx.setSelectedItemId(R.id.navigation_diary);



    }

    private  DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x=i;
            month_x=i1;
            day_x=i2;
            Bundle bundle=new Bundle();
            bundle.putString("dateSet", DateToString.getFormatTime(day_x, month_x, year_x));
            DiaryFragment diaryFragment=new DiaryFragment();
            diaryFragment.setArguments(bundle);
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.diarFr);

            FragmentManager fn=getSupportFragmentManager();

            FragmentTransaction ft=fn.beginTransaction();
            if(isInDiaryFragment) {
                ft.replace(R.id.content, diaryFragment).commit();
            }


        }
    };

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        bottomNavigationViewEx.setSelectedItemId(R.id.navigation_diary);
      super.onSaveInstanceState(outState);
    }


    public  void sendDateToFragment(String dateFormat){

        Bundle bundle=new Bundle();
        bundle.putString("dateSet", dateFormat);
        DiaryFragment diaryFragment=new DiaryFragment();
        diaryFragment.setArguments(bundle);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }





}
