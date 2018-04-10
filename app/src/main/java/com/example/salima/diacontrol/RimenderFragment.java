package com.example.salima.diacontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


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
    int hour_x, minute_x, seconds_x; //seconds_x;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderButton=(Button)  getView().findViewById(R.id.reminderButton);


        //TODO этот год в диалог

        /*txtReminder=(TextView) getView().findViewById(R.id.timeReminder);


        Calendar calendar  = Calendar.getInstance();
        hour_x=calendar.get(Calendar.HOUR_OF_DAY);
        minute_x=calendar.get(Calendar.MINUTE);
        seconds_x=calendar.get(Calendar.SECOND);
        timePickerDialog=new TimePickerDialog(getContext(), timePickerListner,hour_x, minute_x,  true);
      // txtReminder.setPaintFlags(txtReminder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст
        txtReminder.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x));
        addListenerOnText();*/
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
           // timeTxt.setText( getStringTime(hour_x)+ ":" + getStringTime(minute_x) );


        }
    };


    public void  addListenerOnText() {
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



    /*
    public void addListenerOnButtonAddFood(){
        reminderButton.setOnClickListener( new View.OnClickListener() {
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
                            DatabaseHelper db= new DatabaseHelper(getApplicationContext());
                            db.insertDataProductUser(YouEditTextValue, YouEditTextValue2, YouEditTextValue3);
                            setUse.foodList.add(YouEditTextValue);
                            setUse.xeString.add(YouEditTextValue3);
                            setUse.grams.add(YouEditTextValue2);
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
