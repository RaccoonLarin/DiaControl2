package com.example.salima.diacontrol;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RimenderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RimenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RimenderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RimenderFragment() {
        // Required empty public constructor
    }

    public static RimenderFragment newInstance(String param1, String param2) {
        RimenderFragment fragment = new RimenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button reminderButton;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    TextView txtReminder, dateTxt;
    View view1;
    int IdReminder;
    int hour_x, minute_x, seconds_x; //seconds_x;
    int year_x, month_x, day_x;
    ListView listView;
    Fragment newFragmen;
    CustomAdapter customListView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderButton=(Button)  getView().findViewById(R.id.reminderButton);
         //view1=getLayoutInflater().inflate(R.layout.reminder_deign_layout, null);

        newFragmen=this;
        Calendar calendar  = Calendar.getInstance();
        hour_x=calendar.get(Calendar.HOUR_OF_DAY);
        minute_x=calendar.get(Calendar.MINUTE);
        year_x=calendar.get(Calendar.YEAR);
        month_x=calendar.get(Calendar.MONTH);
        day_x=calendar.get(Calendar.DAY_OF_MONTH);

        addListenerOnButtonReminder();


        reminderButton.setFocusable(false);




        //SettingUser.textReminder.add("yo");
     //   SettingUser.timeTextReminder.add("15"+":"+"30");

        //TODO этот год в диалог

        listView=(ListView) getView().findViewById(R.id.remindList);
        customListView = new CustomAdapter();
        listView.setAdapter(customListView);

        listViewListener();
        listViewListenerEdit();
    }

    public void getList(){

    }

    public void listViewListener(){

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub


                //Toast.makeText(getContext(), Integer.toString(pos), Toast.LENGTH_LONG).show();

                //  Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
                final View view1=arg1;
                final  int position1=pos;
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Удалить запись?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                int id= SettingUser.idRerminder.get(position1);


                                Intent intent = new Intent(getContext(), AlarmNotificationReceiver.class);
                                intent.putExtra("notification_id", id);
                                intent.putExtra("text", SettingUser.textReminder.get(position1));
                                PendingIntent sender = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
                                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                                alarmManager.cancel(sender);


                                DatabaseHelper db=new DatabaseHelper(getContext());
                                db.deleteReminder(SettingUser.idRerminder.get(position1));
                                SettingUser.idRerminder.remove(position1);
                                SettingUser.noRepeat.remove(position1);
                                SettingUser.repeatWeak.remove(position1);
                                SettingUser.repeatDay.remove(position1);
                                SettingUser.timeTextReminder.remove(position1);
                                SettingUser.textReminder.remove(position1);

                                getFragmentManager()
                                        .beginTransaction()
                                        .detach(newFragmen)
                                        .attach(newFragmen)
                                        .commit();
                                listView.invalidateViews();


                            }
                        })
                        .setNegativeButton("Нет", null).create();
                dialog.show();



                return true;
            }
        });

    }

    String textRemindChange="";
    int remindday, remindweek, remindnorepeat;
    boolean isEdit=false;
    int poss;

    public void listViewListenerEdit(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final View view1=view;
                 poss=position;
                TextView textViewRem = (TextView) view.findViewById(R.id.textReminder);
                TextView textViewSeconds = (TextView) view.findViewById(R.id.textTime);

                DatabaseHelper db=new DatabaseHelper(getContext());
                Cursor data=db.selectReminderById(position);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                while (data.moveToNext()){

                    try {
                        calendar.setTime(simpleDateFormat.parse(data.getString(2)));
                        year_x=calendar.get(Calendar.YEAR);
                        month_x=calendar.get(Calendar.MONTH);
                        day_x=calendar.get(Calendar.DAY_OF_MONTH);
                        hour_x=calendar.get(Calendar.HOUR_OF_DAY);
                        minute_x=calendar.get(Calendar.MINUTE);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    textRemindChange=data.getString(3);

                    remindday=data.getInt(4);
                    remindweek=data.getInt(5);
                    remindnorepeat=data.getInt(6);

                }


                isEdit=true;
                reminderButton.callOnClick();


            }
        });


    }






    protected  TimePickerDialog.OnTimeSetListener timePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
           hour_x = i;
           minute_x = i1;
           txtReminder.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x) );


        }
    };



    public void  addListenerOnText(TextView txtReminder) {
        dateTxt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();

                    }
                });

        txtReminder.setOnClickListener(
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


    public void addListenerOnButtonReminder(){
        reminderButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                alert.setMessage("Введите данные");

                final View view2= getLayoutInflater().inflate(R.layout.reminder_deign_layout, null);

                final EditText editTextComment =(EditText) view2.findViewById(R.id.textAddComment);

                if(!isEdit){
                    Calendar calendar  = Calendar.getInstance();
                    hour_x=calendar.get(Calendar.HOUR_OF_DAY);
                    minute_x=calendar.get(Calendar.MINUTE);
                    year_x=calendar.get(Calendar.YEAR);
                    month_x=calendar.get(Calendar.MONTH);
                    day_x=calendar.get(Calendar.DAY_OF_MONTH);
                }

                txtReminder=(TextView) view2.findViewById(R.id.timeReminder);
                dateTxt = (TextView) view2.findViewById(R.id.dateReminder);
                txtReminder.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x));
                dateTxt.setText(year_x+"-"+getStringMonth(month_x+1)+ "-"+getStringDay(day_x)  );

                timePickerDialog=new TimePickerDialog(getContext(), timePickerListner,hour_x, minute_x,  true);
                datePickerDialog=new DatePickerDialog(getContext(), dpickerListner, year_x, month_x, day_x);

                txtReminder.setPaintFlags(txtReminder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                dateTxt.setPaintFlags(dateTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                addListenerOnText(txtReminder);
                final RadioButton radioButton=(RadioButton) view2.findViewById(R.id.radioButton);
                final RadioButton radioButtonWeek=(RadioButton) view2.findViewById(R.id.radioButtonWeek);
                final RadioButton radioButtonNRepeat=(RadioButton) view2.findViewById(R.id.radioButtonDontReepat);
                if(isEdit){
                    editTextComment.setText(textRemindChange);
                    radioButton.setChecked(SettingUser.convertFromIntToBoolean(remindday));
                    radioButtonWeek.setChecked(SettingUser.convertFromIntToBoolean(remindweek));
                    radioButtonNRepeat.setChecked(SettingUser.convertFromIntToBoolean(remindnorepeat));
                }
                alert.setView(view2);

                alert.setPositiveButton("СОХРАНИТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year_x);
                        calendar.set(Calendar.MONTH, month_x);
                        calendar.set(Calendar.DAY_OF_MONTH, day_x);
                     calendar.set(Calendar.HOUR_OF_DAY, hour_x);
                     calendar.set(Calendar.MINUTE, minute_x);
                    // Toast.makeText(getContext(),Calendar.YEAR + " " +Calendar.MONTH + " " +Calendar.DAY_OF_MONTH + " " + hour_x + " "+ minute_x, Toast.LENGTH_LONG).show();
                        String YouEditTextValue = editTextComment.getText().toString();

                        SettingUser.textReminder.add(YouEditTextValue);
                        SettingUser.timeTextReminder.add(year_x+"-"+getStringMonth(month_x+1)+ "-" +getStringDay(day_x) +" "+ getStringTime(hour_x)+":"+getStringTime(minute_x));
                        customListView = new CustomAdapter();
                        listView.setAdapter(customListView);

                        long time= System.currentTimeMillis();
                        if(calendar.getTimeInMillis()<time){
                            return;
                        }

                        boolean dayRepeat=radioButton.isChecked();
                        boolean weekRepeat=radioButtonWeek.isChecked();
                        boolean nonRepeat=radioButtonNRepeat.isChecked();

                        if(radioButton.isChecked()){
                            startAlarm(calendar.getTimeInMillis(), true, false, YouEditTextValue);
                            SettingUser.repeatDay.add(1);
                            SettingUser.repeatWeak.add(0);
                            SettingUser.noRepeat.add(0);

                        }
                        if(weekRepeat){
                            startAlarm(calendar.getTimeInMillis(), false, true, YouEditTextValue);
                            SettingUser.repeatDay.add(0);
                            SettingUser.repeatWeak.add(1);
                            SettingUser.noRepeat.add(0);
                        }
                        if(nonRepeat){
                            startAlarm(calendar.getTimeInMillis(), false, false, YouEditTextValue);
                            SettingUser.repeatDay.add(0);
                            SettingUser.repeatWeak.add(0);
                            SettingUser.noRepeat.add(1);
                        }
                        if(!(dayRepeat||weekRepeat||nonRepeat)){
                            startAlarm(calendar.getTimeInMillis(), false, false, YouEditTextValue);
                            SettingUser.repeatDay.add(0);
                            SettingUser.repeatWeak.add(0);
                            SettingUser.noRepeat.add(1);
                            nonRepeat=true;
                        }



                        DatabaseHelper db=new DatabaseHelper(getContext());
                        db.insertDataReminder(IdReminder, +year_x+"-"+getStringMonth(month_x+1)+"-" +getStringDay(day_x)+" "+ getStringTime(hour_x)+":"+getStringTime(minute_x), YouEditTextValue,
                                SettingUser.convertFromBoolToInt(dayRepeat),
                                SettingUser.convertFromBoolToInt(weekRepeat),
                                SettingUser.convertFromBoolToInt(nonRepeat));

                        if(isEdit){
                            int id= SettingUser.idRerminder.get(poss);


                            Intent intent = new Intent(getContext(), AlarmNotificationReceiver.class);
                            intent.putExtra("notification_id", id);
                            intent.putExtra("text", SettingUser.textReminder.get(poss));
                            PendingIntent sender = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                            alarmManager.cancel(sender);


                           // DatabaseHelper db=new DatabaseHelper(getContext());
                            db.deleteReminder(SettingUser.idRerminder.get(poss));
                            SettingUser.idRerminder.remove(poss);
                            SettingUser.noRepeat.remove(poss);
                            SettingUser.repeatWeak.remove(poss);
                            SettingUser.repeatDay.remove(poss);
                            SettingUser.timeTextReminder.remove(poss);
                            SettingUser.textReminder.remove(poss);

                            getFragmentManager()
                                    .beginTransaction()
                                    .detach(newFragmen)
                                    .attach(newFragmen)
                                    .commit();
                            listView.invalidateViews();

                            isEdit=false;
                        }

                    }
                });

                alert.setNegativeButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                }
                });
                final AlertDialog dialog = alert.create();
                dialog.show();



                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(false);

                editTextComment.addTextChangedListener(new TextWatcher() {
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
                        if (TextUtils.isEmpty(s)) {
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

            }
        });


    }

    int k=0;

    public void startAlarm(long timeInMills, boolean repeatingDay, boolean repeatingWeek, String text){

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        k++;
        myIntent= new Intent(getContext(),AlarmNotificationReceiver.class);  //createIIntent("action " +k, "extra "+ k);

        myIntent.putExtra("id", k);
        Random random=new Random();
         IdReminder=  random.nextInt(Integer.MAX_VALUE-1000)+1000;
        SettingUser.idRerminder.add(IdReminder);
        myIntent.putExtra("id", IdReminder);
        myIntent.putExtra("text", text);
        pendingIntent=PendingIntent.getBroadcast(getContext(),IdReminder,myIntent,PendingIntent.FLAG_ONE_SHOT);

        if(repeatingDay){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        if(repeatingWeek){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, 7*AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        if(!(repeatingDay||repeatingWeek)) {
            //TODO изменить на set
            manager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
            //manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, AlarmManager.INTERVAL_FIFTEEN_MINUTES%10, pendingIntent);
        }

        Toast.makeText(getContext(), "Напоминание установлено", Toast.LENGTH_SHORT).show();

    }

    Intent createIIntent(String action, String extra){
        Intent intent=new Intent(getContext(), AlarmNotificationReceiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }

    /*public void startAlarm(boolean isNotification, boolean isRepeat){

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

         myIntent=new Intent(getContext(),AlarmNotificationReceiver.class);
         pendingIntent=PendingIntent.getBroadcast(getContext(),0,myIntent,0);


        if(!isRepeat){
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000, pendingIntent);
        }
        else{
            manager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime()+3000,3000, pendingIntent);
        }
    }*/

    private String getStringTime(int time){


        if(time < 10){

            return  "0" + time;
        }

        return Integer.toString(time);
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


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return SettingUser.timeTextReminder.size();
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
            View view1=getLayoutInflater().inflate(R.layout.reminder_listview_design, null);
            ImageView imageViewSReminder= (ImageView) view1.findViewById(R.id.reminderImage);
            TextView time= (TextView) view1.findViewById(R.id.textReminder);
            TextView commentTime= (TextView) view1.findViewById(R.id.textTime);
            LinearLayout relativeLayoutSugar = (LinearLayout) view1.findViewById(R.id.timeLayout);

            if(!SettingUser.timeTextReminder.get(i).equals("")){
               relativeLayoutSugar.setVisibility(View.VISIBLE);
               String s= SettingUser.timeTextReminder.get(i);
                time.setText(s);

            } else{
                //imageViewSReminder.setVisibility(View.GONE);
                //time.setVisibility(View.GONE);
                relativeLayoutSugar.setVisibility(View.GONE);
            }

            if(!SettingUser.textReminder.get(i).equals("")){
                commentTime.setVisibility(View.VISIBLE);
                commentTime.setText(SettingUser.textReminder.get(i));
            } else{
                commentTime.setVisibility(View.GONE);
            }

            //view1.setOnTouchListener(this);







            return view1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_rimender, container, false);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
