package com.example.salima.diacontrol;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;


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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RimenderFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    TextView txtReminder;
    View view1;
    int hour_x, minute_x, seconds_x; //seconds_x;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderButton=(Button)  getView().findViewById(R.id.reminderButton);
         //view1=getLayoutInflater().inflate(R.layout.reminder_deign_layout, null);

        Calendar calendar  = Calendar.getInstance();
        hour_x=calendar.get(Calendar.HOUR_OF_DAY);
        minute_x=calendar.get(Calendar.MINUTE);
        addListenerOnButtonReminder();

        //TODO этот год в диалог


    }
    private String getStringTime(int time){


        if(time < 10){

            return  "0" + time;
        }

        return Integer.toString(time);
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
        /*dateTxt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();

                    }
                });*/

        txtReminder.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        timePickerDialog.show();

                    }
                });
    }



    public void addListenerOnButtonReminder(){
        reminderButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                alert.setMessage("Введите данные");

                final View view2= getLayoutInflater().inflate(R.layout.reminder_deign_layout, null);;
                txtReminder=(TextView) view2.findViewById(R.id.timeReminder);
                txtReminder.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x));
                timePickerDialog=new TimePickerDialog(getContext(), timePickerListner,hour_x, minute_x,  true);
                //  txtReminder.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x));
                txtReminder.setPaintFlags(txtReminder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                addListenerOnText(txtReminder);
                final RadioButton radioButton=(RadioButton) view2.findViewById(R.id.radioButton);

                alert.setView(view2);

                alert.setPositiveButton("СОХРАНИТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       Calendar calendar = Calendar.getInstance();
                     calendar.set(Calendar.HOUR_OF_DAY, hour_x);
                     calendar.set(Calendar.MINUTE, minute_x);
                     Toast.makeText(getContext(),Calendar.YEAR + " " +Calendar.MONTH + " " +Calendar.DAY_OF_MONTH + " " + hour_x + " "+ minute_x, Toast.LENGTH_LONG).show();

                       if(radioButton.isChecked()){
                            startAlarm(calendar.getTimeInMillis(), true);

                        } else startAlarm(calendar.getTimeInMillis(), false);
                    }
                });

                alert.setNegativeButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                }
                });
                final AlertDialog dialog = alert.create();
                dialog.show();

            }
        });


    }

    int k=0;

    public void startAlarm(long timeInMills, boolean repeating){

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        k++;
        myIntent= new Intent(getContext(),AlarmNotificationReceiver.class);  //createIIntent("action " +k, "extra "+ k);
        myIntent.putExtra("id", k);
        Random random=new Random();
        int m = random.nextInt(9999-1000)+1000;
        pendingIntent=PendingIntent.getBroadcast(getContext(),m,myIntent,PendingIntent.FLAG_ONE_SHOT);

        if(!repeating) {
            manager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
        }
        else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills+30000, 3000, pendingIntent);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
