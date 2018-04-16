package com.example.salima.diacontrol;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_statistic, container, false);
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

    TextView txt;
    ArrayList<Integer> hours;
    ArrayList<Integer> blood_sugar;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        hours=new ArrayList<>();
        blood_sugar=new ArrayList<>();
        ArrayList<Integer> data2=new ArrayList<>();
        data2.add(0);
        data2.add(1);
        data2.add(2);
        data2.add(3);
        data2.add(4);
        data2.add(5);
        data2.add(6);
        data2.add(7);
        data2.add(8);
        data2.add(9);
        data2.add(10);
        data2.add(11);
        ArrayList<Integer> data=new ArrayList<>();
        data.add(0);
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        data.add(5);


        ArrayList<Integer> dataXE=new ArrayList<>();
        dataXE.add(80);
        dataXE.add(70);
        dataXE.add(20);
        dataXE.add(50);
        dataXE.add(70);
        dataXE.add(40);
        gett_json();
        LineData ld = new LineData();


       // ld.addLimitLine(ll);
        LineChart chart = (LineChart) getActivity().findViewById(R.id.chart);
        chart.setScaleEnabled(false);
        chart.setDragEnabled(true);
        chart.setTouchEnabled(true);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       //xAxis.setLabelCount(12, true);
       // xAxis.setGranularity(1.0f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(24); //

        LimitLine ll = new LimitLine(70); // set where the line should be drawn
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
// .. and more styling options
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll);


      //  axisX.setValues(axisValues)
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i=0; i<data.size(); i++) {

            // turn your data into Entry objects
            entries.add(new Entry(data.get(i), dataXE.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Dataset 1"); // add entries to dataset
     //  ;
       dataSet.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);
        dataSet.setDrawValues(false);
        lineData.setDrawValues(false);

        chart.setData(lineData);
        chart.invalidate(); // refresh
        //dataSet.setColor();
        //dataSet.setValueTextColor(...); // styling, ...



    }

    public void gett_json(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int year_x=calendar.get(Calendar.YEAR);
        int month_x=calendar.get(Calendar.MONTH);
        int day_x=calendar.get(Calendar.DAY_OF_MONTH);
        int hour_x=calendar.get(Calendar.HOUR_OF_DAY);
        int minute_x=calendar.get(Calendar.MINUTE);
        int seconds_x=calendar.get(Calendar.SECOND);

        String date1=year_x+"-"+getStringMonth(month_x+1)+"-"+getStringDay(day_x)+" "+getStringTime(hour_x)+":"+getStringTime(minute_x)+":"+getStringTime(seconds_x);

        DatabaseHelper db=new DatabaseHelper(getContext());
        Cursor data = db.getTime(date1);

        while (data.moveToNext()){
            String ss=data.getString(0);
            if(ss.equals("")){
                continue;
            }
            blood_sugar.add(Integer.parseInt(data.getString(0)));
            try {
                calendar.setTime(simpleDateFormat.parse( data.getString(1)));
                hour_x=calendar.get(Calendar.HOUR_OF_DAY);
                hours.add(hour_x);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
       /* while (data.moveToNext()){
            editText.setText(data.getString(1));
            editText1.setText(data.getString(2));
            editText3.setText(data.getString(3));
            // stringfood=data.getString(3);
            editText2.setText(data.getString(4));
            try {
                calendar.setTime(simpleDateFormat.parse(data.getString(5)));*/

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
