package com.example.salima.diacontrol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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
    ArrayList<String> weekList;
    ArrayList<Integer> blood_sugar;
    ArrayList<String> nameWeek;
    HashMap<String, Integer> weekBlood;
    ArrayList<String> nameMonth, monthList;
    DatePickerDialog datePickerDialog;
    Button buttonDay, buttonWeek, buttonMonth;
    HashMap<Integer, Integer> hashMapWweek;
    HashMap<Integer, Integer> hasMapMonthInt;
    HashMap<String, Integer> hashMapMonth;
    HashMap <String, Integer> hashMapMonthUser;
    Boolean flagDay=false, flagWeek=false, flagMonth=false;
    TextView dateTxt;
    int year_x, month_x, day_x;

    LineChart chartLine;
    ScatterChart scatterChart, scatterChartMonth;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        hours=new ArrayList<>();
        blood_sugar=new ArrayList<>();
        weekList=new ArrayList<>();
        monthList=new ArrayList<>();
        weekBlood=new HashMap<>();
        dateTxt=(TextView) getActivity().findViewById(R.id.dateStatistic);
        dateTxt.setPaintFlags(dateTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст
        hashMapMonth=new HashMap<>();
        hashMapMonthUser=new HashMap<>();


        Calendar calendar  = Calendar.getInstance();
        year_x=calendar.get(Calendar.YEAR);
        month_x=calendar.get(Calendar.MONTH);
        day_x=calendar.get(Calendar.DAY_OF_MONTH);

        buttonDay=(Button) getActivity().findViewById(R.id.buttonDay);
        buttonWeek=(Button) getActivity().findViewById(R.id.buttonWeek);
        buttonMonth=(Button) getActivity().findViewById(R.id.buttonMonth);
        datePickerDialog=new DatePickerDialog(getContext(), dpickerListner, year_x, month_x, day_x);
        dateTxt.setText(getStringDay(day_x) + "." + getStringMonth(month_x+1) + "." + year_x);
        addListenerOnText();
        addListenerOnButtonMonth();
        addListenerOnButtonDay();
        addListenerOnButtonWeek();
        getDate();
        flagDay=true;

        scatterChart= getActivity().findViewById(R.id.chartScatter);
        scatterChartMonth= getActivity().findViewById(R.id.chartScatterMonth);

        caseButton();

        LineData ld = new LineData();


       // ld.addLimitLine(ll);




    }

    private  DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x=i;
            month_x=i1;
            day_x=i2;
            dateTxt.setText(getStringDay(day_x) + "." + getStringMonth(month_x+1) + "." + year_x);
            caseButton();


            //  Toast.makeText(AddActivity.this, day_x + "." + month_x + "." + year_x, Toast.LENGTH_LONG).show();
        }
    };
    public void  addListenerOnText() {
        dateTxt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();

                    }
                });



    }
  int tempWeek;

    public void addListenerOnButtonDay(){
        buttonDay.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        getDate();
                        flagDay=true;
                        flagMonth=false;
                        flagWeek=false;
                       // ScatterChart sctrchart=(ScatterChart) getActivity().findViewById(R.id.chart)
                        chartLine = (LineChart) getActivity().findViewById(R.id.chart);
                        chartLine.setVisibility(View.VISIBLE);
                        scatterChart.setVisibility(View.GONE);
                        scatterChartMonth.setVisibility(View.GONE);

                        PieChart  pieChart=(PieChart) getActivity().findViewById(R.id.piechart);
                        chartLine.clear();
                        pieChart.clear();
                        chartLine.setScaleEnabled(false);
                        chartLine.setDragEnabled(true);
                        chartLine.setTouchEnabled(true);

                        if(hours.size()==0){
                            chartLine.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            chartLine.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            // chart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }

                        if(blood_sugar.size()==0){
                            chartLine.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            chartLine.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }




                        int precMin=0, precMax=0, precTarget=0;
                        for(int i=0; i<blood_sugar.size(); i++){
                            if(SettingUser.xeMin!=null) {

                                if (blood_sugar.get(i) <= SettingUser.xeMin) {
                                    precMin++;
                                    continue;
                                }
                            }
                            if(SettingUser.xeMax!=null) {
                                if (blood_sugar.get(i) >= SettingUser.xeMax) {
                                    precMax++;
                                    continue;
                                }
                            }
                            precTarget++;

                        }

                        if(SettingUser.xeMin==null){
                            precMin=0;

                        }

                        if(SettingUser.xeMax==null){
                            precMax=0;
                        }

                        // if(SettingUser.xeTarget==null){
                        //  precTarget=0;
                        //  }

                        //chart.animateX(1000, Easing.EasingOption. EaseInQuad);

                        XAxis xAxis = chartLine.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        ArrayList<String> clock=new ArrayList<>();
                        for(int i=0; i<24; i++){
                            clock.add( Integer.toString(i+1));
                        }

                        xAxis.setLabelCount(12, true);
                        // xAxis.setGranularity(2.0f);
                        xAxis.setValueFormatter(new MyAxisValueFormatter(clock));
                        xAxis.setAxisMinimum(1);
                        xAxis.setAxisMaximum(24); //

                        //лимит прямая, добавить если пользователь ввел свои параметры максимум и минимум глкозы
                        /*
                        LimitLine ll = new LimitLine(70);
                        ll.setLineColor(Color.RED);
                        ll.setLineWidth(1f);

                        YAxis leftAxis = chart.getAxisLeft();
                        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                        leftAxis.addLimitLine(ll);*/
                        YAxis leftAxis = chartLine.getAxisLeft();
                        leftAxis.removeAllLimitLines();
                        if(SettingUser.xeMin!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMin.toString()));
                            ll.setLineColor(ContextCompat.getColor(getContext(), R.color.yellowColor));
                            ll.setLineWidth(2f);

                            //  YAxis leftAxis = chart.getAxisLeft();
                            //leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                            leftAxis.addLimitLine(ll);
                        }

                        if(SettingUser.xeMax!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMax.toString()));
                            ll.setLineColor(Color.RED);
                            ll.setLineWidth(2f);


                            leftAxis.addLimitLine(ll);
                        }



                        //  axisX.setValues(axisValues)
                        ArrayList<Entry> entries = new ArrayList<Entry>();

                        for (int i=0; i<blood_sugar.size(); i++) {

                            // turn your data into Entry objects
                            entries.add(new Entry(hours.get(i), blood_sugar.get(i)));
                        }

                        LineDataSet dataSet = new LineDataSet(entries, "Dataset 1"); // add entries to dataset
                        chartLine.getLegend().setEnabled(false);
                        chartLine.setDescription(null);
                        dataSet.setFillAlpha(110);
                        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                        dataSet.setLineWidth(2f);
                        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.blackStar));
                        dataSet.setCircleColorHole(ContextCompat.getColor(getContext(), R.color.blackStar));
                        // IMarker marker = new YourMarkerView();
                        //fchart.setMarker(marker);

                        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                        dataSets.add(dataSet);

                        LineData lineData = new LineData(dataSets);
                        dataSet.setDrawValues(false);
                        lineData.setDrawValues(false);

                        chartLine.setData(lineData);
                        chartLine.invalidate(); // refresh
                        //dataSet.setColor();
                        //dataSet.setValueTextColor(...); // styling, ...


                        pieChart.setUsePercentValues(true);
                        // pieChart.setDescription(null);

                        pieChart.getDescription().setEnabled(false);
                        //  pieChart.getLegend().setEnabled(false);
                        pieChart.setExtraOffsets(5,10,5,5);
                        pieChart.setDragDecelerationFrictionCoef(0.95f);
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setHoleColor(Color.WHITE);
                        pieChart.setTransparentCircleRadius(61f);
                        pieChart.setDrawEntryLabels(false);
                        // pieChart.setEntryLabelTextSize(0f);
                        // pieChart.setEntryLabelColor(Color.BLUE);
                        // pieChart.setDrawSliceText(false);
                        // pieChart.setDrawSlicesUnderHole(false);



                        pieChart.setHoleRadius(60f);
                        // pieChart.setTouchEnabled(false);

                        ArrayList<PieEntry> yValue=new ArrayList<>();

                        yValue.add(new PieEntry(precMax, "Повышенный сахар"));
                        yValue.add(new PieEntry(precMin, "Пониженный сахар"));
                        yValue.add(new PieEntry(precTarget, "Сахар в норме"));
                        // yValue.add(new PieEntry(20f));

                        PieDataSet dataSetPie = new PieDataSet(yValue, "");

                        //dataSetPie.x

                        // dataSetPie.setSliceSpace(2f);
                        //dataSetPie.setSelectionShift(1f);
                        //  dataSetPie.setColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                        final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.redColor), ContextCompat.getColor(getContext(), R.color.yellowColor), ContextCompat.getColor(getContext(), R.color.greenColor)};

                        //   final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.cuteColor), ContextCompat.getColor(getContext(), R.color.myBlue)};
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        for(int c: MY_COLORS) colors.add(c);
                        dataSetPie.setColors(colors);

                        PieData pieData=new PieData(dataSetPie);
                        pieChart.setData(pieData);
                        pieChart.invalidate();


                    }
                });

    }
    public void addListenerOnButtonWeek(){
        buttonWeek.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        flagDay=false;
                        flagMonth=false;
                        flagWeek=true;
                        getWeekDate();
                        chartLine.setVisibility(View.GONE);
                        scatterChart.setVisibility(View.VISIBLE);
                        scatterChartMonth.setVisibility(View.GONE);
                       // scatterChart=(ScatterChart) getActivity().findViewById(R.id.chartScatter);

                        //LineChart chart = (LineChart) getActivity().findViewById(R.id.chart);
                        PieChart  pieChart=(PieChart) getActivity().findViewById(R.id.piechart);
                        pieChart.clear();
                        // pieChart.clearValues();
                        scatterChart.clear();

                        scatterChart.setScaleEnabled(true);
                        scatterChart.setDragEnabled(true);
                        scatterChart.setTouchEnabled(true);

                        //scatterChart.setVisibleXRangeMaximum(30); // allow 20 values to be displayed at once on the x-axis, not more
                       // scatterChart.moveViewToX(1);

                        //  chart.animateX(1000, Easing.EasingOption.EaseOutBack);
                        if(weekList.size()==0){
                            scatterChart.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            scatterChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }

                        if(blood_sugar.size()==0){
                            scatterChart.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            scatterChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }



                        //считаем процентное соотношение сахаров пользователя
                        int precMin=0, precMax=0, precTarget=0;
                        for(int i=0; i<blood_sugar.size(); i++){
                            if(SettingUser.xeMin!=null) {

                                if (blood_sugar.get(i) <= SettingUser.xeMin) {
                                    precMin++;
                                    continue;
                                }
                            }
                            if(SettingUser.xeMax!=null) {
                                if (blood_sugar.get(i) >= SettingUser.xeMax) {
                                    precMax++;
                                    continue;
                                }
                            }
                            precTarget++;

                        }

                        if(SettingUser.xeMin==null){
                            precMin=0;

                        }

                        if(SettingUser.xeMax==null){
                            precMax=0;
                        }

                        if(SettingUser.xeTarget==null){
                            precTarget=0;
                        }


                        ArrayList<Entry> entries = new ArrayList<Entry>();
                        int temp=hours.get(0);
                        int hour;
                        //  ArrayList<Integer> shiift=new ArrayList<>();
                        // int shiftt=hours.indexOf(0);
                        //shiift=multiplyShiftLeft(hours, shiftt);


                        for (int i=0; i<blood_sugar.size(); i++) {

                            // turn your data into Entry objects

                            hour= hashMapWweek.get(hours.get(i));
                            //  if(hours.get(i)<tempWeek){
                            //  hour=hours.get(i)+tempWeek;
                            // }
                            entries.add(new Entry(hour, blood_sugar.get(i)));

                        }

                        YAxis leftAxis = scatterChart.getAxisLeft();
                        leftAxis.removeAllLimitLines();
                        if(SettingUser.xeMin!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMin.toString()));
                            ll.setLineColor(ContextCompat.getColor(getContext(), R.color.yellowColor));
                            ll.setLineWidth(2f);

                            //  YAxis leftAxis = chart.getAxisLeft();
                            //leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                            leftAxis.addLimitLine(ll);
                        }

                        if(SettingUser.xeMax!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMax.toString()));
                            ll.setLineColor(Color.RED);
                            ll.setLineWidth(2f);


                            leftAxis.addLimitLine(ll);
                        }

                        //  axisX.setValues(axisValues)

                        ScatterDataSet dataSet = new ScatterDataSet(entries, "Dataset 1"); // add entries to dataset
                        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                        //  ;
                     //   dataSet.setFillAlpha(110);

                        ArrayList<IScatterDataSet> dataSets=new ArrayList<>();
                        dataSets.add(dataSet);

                        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                       // dataSet.setLineWidth(2f);
                        //dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.blackStar));
                        //dataSet.setCircleColorHole(ContextCompat.getColor(getContext(), R.color.blackStar));
                        ScatterData lineData = new ScatterData(dataSets);
                        dataSet.setDrawValues(false);
                        lineData.setDrawValues(false);

                        scatterChart.setData(lineData);
                        scatterChart.invalidate(); // refresh
                        //dataSet.setColor();
                        //dataSet.setValueTextColor(...); // styling, ...

                        XAxis xAxis = scatterChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        // String [] val=new String[] {"Sr", "Ch", "Pt", "SUb", "Vos", "Pn", "Vt"};
                        xAxis.setValueFormatter(new MyAxisValueFormatter(nameWeek));
                        xAxis.setLabelCount(8, false);
                        xAxis.setGranularity(1f);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(7); //





                        pieChart.setUsePercentValues(true);
                        // pieChart.setDescription(null);

                        pieChart.getDescription().setEnabled(false);
                        //  pieChart.getLegend().setEnabled(false);
                        pieChart.setExtraOffsets(5,10,5,5);
                        pieChart.setDragDecelerationFrictionCoef(0.95f);
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setHoleColor(Color.WHITE);
                        pieChart.setTransparentCircleRadius(61f);
                        pieChart.setDrawEntryLabels(false);


                        pieChart.setHoleRadius(60f);

                        ArrayList<PieEntry> yValue=new ArrayList<>();

                        yValue.add(new PieEntry(precMax, "Повышенный сахар"));
                        yValue.add(new PieEntry(precMin, "Пониженный сахар"));
                        yValue.add(new PieEntry(precTarget, "Сахар в норме"));

                        PieDataSet dataSetPie = new PieDataSet(yValue, "");

                        final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.redColor), ContextCompat.getColor(getContext(), R.color.yellowColor), ContextCompat.getColor(getContext(), R.color.greenColor)};

                        //   final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.cuteColor), ContextCompat.getColor(getContext(), R.color.myBlue)};
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        for(int c: MY_COLORS) colors.add(c);
                        dataSetPie.setColors(colors);

                        PieData pieData=new PieData(dataSetPie);
                        pieChart.setData(pieData);
                        pieChart.invalidate();

                    }
                });
    }
    public void addListenerOnButtonMonth() {


        buttonMonth.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        flagDay=false;
                        flagMonth=true;
                        flagWeek=false;
                        getMonthDate();
                        chartLine.setVisibility(View.GONE);
                        scatterChart.setVisibility(View.GONE);
                        scatterChartMonth.setVisibility(View.VISIBLE);

                        PieChart  pieChart=(PieChart) getActivity().findViewById(R.id.piechart);
                        pieChart.clear();
                        scatterChartMonth.clear();

                        scatterChartMonth.setScaleEnabled(true);
                        scatterChartMonth.setDragEnabled(true);
                        scatterChartMonth.setTouchEnabled(true);

                        if(monthList.size()==0){
                            scatterChartMonth.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            scatterChartMonth.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }

                        if(blood_sugar.size()==0){
                            scatterChartMonth.setNoDataText("Нет данных");
                            pieChart.setNoDataText("");
                            scatterChartMonth.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                            return;
                        }



                        int precMin=0, precMax=0, precTarget=0;
                        for(int i=0; i<blood_sugar.size(); i++){
                            if(SettingUser.xeMin!=null) {

                                if (blood_sugar.get(i) <= SettingUser.xeMin) {
                                    precMin++;
                                    continue;
                                }
                            }
                            if(SettingUser.xeMax!=null) {
                                if (blood_sugar.get(i) >= SettingUser.xeMax) {
                                    precMax++;
                                    continue;
                                }
                            }
                            precTarget++;

                        }

                        if(SettingUser.xeMin==null){
                            precMin=0;

                        }

                        if(SettingUser.xeMax==null){
                            precMax=0;
                        }

                        if(SettingUser.xeTarget==null){
                            precTarget=0;
                        }


                        ArrayList<Entry> entries = new ArrayList<Entry>();
//                        int temp=hours.get(0);
                        int hour;
                        //  ArrayList<Integer> shiift=new ArrayList<>();
                        // int shiftt=hours.indexOf(0);
                        //shiift=multiplyShiftLeft(hours, shiftt);


                        for (int i=0; i<blood_sugar.size(); i++) {

                            // turn your data into Entry objects

                           hour= hours.get(i);
                            //  if(hours.get(i)<tempWeek){
                            //  hour=hours.get(i)+tempWeek;
                            // }
                            entries.add(new Entry(hour, blood_sugar.get(i)));

                        }

                        YAxis leftAxis = scatterChartMonth.getAxisLeft();
                        leftAxis.removeAllLimitLines();
                        if(SettingUser.xeMin!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMin.toString()));
                            ll.setLineColor(ContextCompat.getColor(getContext(), R.color.yellowColor));
                            ll.setLineWidth(2f);

                            //  YAxis leftAxis = chart.getAxisLeft();
                            //leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                            leftAxis.addLimitLine(ll);
                        }

                        if(SettingUser.xeMax!=null) {
                            LimitLine ll = new LimitLine(Float.parseFloat(SettingUser.xeMax.toString()));
                            ll.setLineColor(Color.RED);
                            ll.setLineWidth(2f);


                            leftAxis.addLimitLine(ll);
                        }

                        //  axisX.setValues(axisValues)

                        ScatterDataSet dataSet = new ScatterDataSet(entries, "Dataset 1"); // add entries to dataset
                        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                        //  ;
                        //   dataSet.setFillAlpha(110);

                        ArrayList<IScatterDataSet> dataSets=new ArrayList<>();
                        dataSets.add(dataSet);

                        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.myBlue));
                        // dataSet.setLineWidth(2f);
                        //dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.blackStar));
                        //dataSet.setCircleColorHole(ContextCompat.getColor(getContext(), R.color.blackStar));
                        ScatterData lineData = new ScatterData(dataSets);
                        dataSet.setDrawValues(false);
                        lineData.setDrawValues(false);

                        scatterChartMonth.setData(lineData);
                        scatterChartMonth.invalidate(); // refresh
                        //dataSet.setColor();
                        //dataSet.setValueTextColor(...); // styling, ...

                        XAxis xAxis = scatterChartMonth.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        // String [] val=new String[] {"Sr", "Ch", "Pt", "SUb", "Vos", "Pn", "Vt"};
                        xAxis.setValueFormatter(new MyAxisValueFormatter(nameMonth));
                       xAxis.setLabelCount(32, false);
                        xAxis.setGranularity(1f);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(31); //

                        scatterChartMonth.setVisibleXRangeMaximum(11); // allow 20 values to be displayed at once on the x-axis, not more
                        scatterChartMonth.moveViewToX(31);

                        pieChart.setUsePercentValues(true);
                        // pieChart.setDescription(null);

                        pieChart.getDescription().setEnabled(false);
                        //  pieChart.getLegend().setEnabled(false);
                        pieChart.setExtraOffsets(5,10,5,5);
                        pieChart.setDragDecelerationFrictionCoef(0.95f);
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setHoleColor(Color.WHITE);
                        pieChart.setTransparentCircleRadius(61f);
                        pieChart.setDrawEntryLabels(false);


                        pieChart.setHoleRadius(60f);

                        ArrayList<PieEntry> yValue=new ArrayList<>();

                        yValue.add(new PieEntry(precMax, "Повышенный сахар"));
                        yValue.add(new PieEntry(precMin, "Пониженный сахар"));
                        yValue.add(new PieEntry(precTarget, "Сахар в норме"));

                        PieDataSet dataSetPie = new PieDataSet(yValue, "");

                        final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.redColor), ContextCompat.getColor(getContext(), R.color.yellowColor), ContextCompat.getColor(getContext(), R.color.greenColor)};

                        //   final int[] MY_COLORS = {ContextCompat.getColor(getContext(), R.color.cuteColor), ContextCompat.getColor(getContext(), R.color.myBlue)};
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        for(int c: MY_COLORS) colors.add(c);
                        dataSetPie.setColors(colors);

                        PieData pieData=new PieData(dataSetPie);
                        pieChart.setData(pieData);
                        pieChart.invalidate();

                    }
                });
    }

    public int daysBetween(Date d1, Date d2){
        return (int)(d2.getTime() - d1.getTime());
    }
    public void getMonthDate(){
        nameMonth=new ArrayList<>();
        blood_sugar.clear();
        monthList.clear();
        hours.clear();
        dichh=new ArrayList<>();
       // weekList.clear();
        String date1 = year_x + "-" + getStringMonth(month_x + 1) + "-" + getStringDay(day_x);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date myDate = null;
        try {
            myDate = simpleDateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        Calendar calendar2= calendar;
        calendar.add(Calendar.MONTH, -1);


     //   tempWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Date newDate = calendar.getTime();
        String dateStart = simpleDateFormat.format(newDate);
        long tempDays=myDate.getTime()-newDate.getTime();
       long days= TimeUnit.DAYS.convert(tempDays,TimeUnit.MILLISECONDS);
       fillMonth(newDate, days);


        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor data = db.getTimeMonth(dateStart, date1);


        boolean flag=true;
        while (data.moveToNext()) {
            String ss = data.getString(0);
            if (ss.equals("")) {
                continue;
            }
            blood_sugar.add(Integer.parseInt(data.getString(0)));
            try {
                String dates=data.getString(1);
                calendar.setTime(simpleDateFormat2.parse(data.getString(1)));
                if(flag) {
                    // fillWeek(simpleDateFormat2.parse(data.getString(1)));
                    flag=false;

                }
                String dayLongName = getStringDay(calendar.get(Calendar.DAY_OF_MONTH)) + "."+getStringMonth(calendar.get(Calendar.MONTH)+1);

                //int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                 monthList.add(dayLongName);
                hours.add(hashMapMonth.get(dayLongName));
               // hashMapMonthUser.put(dayLongName, hashMapMonth.get(dayLongName));
              //  hashMapMonth.put(dayLongName, Integer.parseInt(data.getString(0)));
              //  hours.add(dayOfWeek);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<String> dichh;
    public void fillMonth(Date date, long count){
        nameMonth.clear();
        dichh.clear();
        Calendar calendar = Calendar.getInstance();

        //hashMapWweek=new HashMap<>();

        calendar.setTime(date);
       // calendar.add(Calendar.DAY_OF_WEEK, 0);
        //String dayLongNam4e = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String nameDayMontth= getStringDay(calendar.get(Calendar.DAY_OF_MONTH)) + "."+getStringMonth(calendar.get(Calendar.MONTH)+1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        nameMonth.add(nameDayMontth);
        hashMapMonth.put(nameDayMontth, 0);
        // numOfWeekNAme.add(0);
        //hashMapWweek.put(dayOfWeek, 0);
        for (int i = 0; i < count; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, +1);
             nameDayMontth= getStringDay(calendar.get(Calendar.DAY_OF_MONTH)) + "."+getStringMonth(calendar.get(Calendar.MONTH)+1);
           String nameDayMontth2= getStringDay(calendar.get(Calendar.DAY_OF_MONTH));
            nameMonth.add(nameDayMontth);
            dichh.add(nameDayMontth2);
            hashMapMonth.put(nameDayMontth, i+1);
            //Date newDate2 = calendar.getTime();
            // String dateStarrt = simpleDateFormat.format(newDate2);
        }


    }

    public void getWeekDate() {

        nameWeek=new ArrayList<>();
        blood_sugar.clear();
        hours.clear();
        weekList.clear();
        String date1 = year_x + "-" + getStringMonth(month_x + 1) + "-" + getStringDay(day_x);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date myDate = null;
        try {
            myDate = simpleDateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);

        calendar.add(Calendar.DAY_OF_YEAR, -6);
        String meow = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        tempWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Date newDate = calendar.getTime();
        String dateStart = simpleDateFormat.format(newDate);
        fillWeek(newDate);

        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor data = db.getTimeWeek(dateStart, date1);

        Calendar sCalendar = Calendar.getInstance();


        boolean flag=true;
        while (data.moveToNext()) {
            String ss = data.getString(0);
            if (ss.equals("")) {
                continue;
            }
            blood_sugar.add(Integer.parseInt(data.getString(0)));
            try {
                String dates=data.getString(1);
                calendar.setTime(simpleDateFormat2.parse(data.getString(1)));
                if(flag) {
                   // fillWeek(simpleDateFormat2.parse(data.getString(1)));
                    flag=false;

                }
                String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                weekList.add(dayLongName);
                hours.add(dayOfWeek);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }






    public void fillWeek(Date date) {
        nameWeek.clear();
        Calendar calendar = Calendar.getInstance();

        hashMapWweek=new HashMap<>();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, 0);
        String dayLongNam4e = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        nameWeek.add(cutWeekRusName(dayLongNam4e));
       // numOfWeekNAme.add(0);
        hashMapWweek.put(dayOfWeek, 0);
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DAY_OF_WEEK, +1);
            String dayLongNam = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            nameWeek.add(cutWeekRusName(dayLongNam));
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            hashMapWweek.put(dayOfWeek, i+1);
            //Date newDate2 = calendar.getTime();
            // String dateStarrt = simpleDateFormat.format(newDate2);
        }
    }

    public void getDate() {
        blood_sugar.clear();
        hours.clear();
        String date1 = year_x + "-" + getStringMonth(month_x + 1) + "-" + getStringDay(day_x);
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor data = db.getTime(date1);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (data.moveToNext()) {
            String ss = data.getString(0);
            if (ss.equals("")) {
                continue;
            }
            blood_sugar.add(Integer.parseInt(data.getString(0)));
            try {
                calendar.setTime(simpleDateFormat.parse(data.getString(1)));
                int hour_x = calendar.get(Calendar.HOUR_OF_DAY);
                hours.add(hour_x);

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
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

    public String cutWeekRusName(String weekName){
        switch (weekName){
            case "понедельник": return "пн";
            case "вторник": return "вт";
            case "среда": return "ср";
            case "четверг": return "чт";
            case "пятница": return "пт";
            case "суббота": return "сб";
            case "воскресенье": return "вс";

        }
        return "";

    }

    public void caseButton(){
        if(flagDay){
            buttonDay.callOnClick();
            return;
        }

        if(flagWeek){
            buttonWeek.callOnClick();
        }


        if(flagMonth){
            buttonMonth.callOnClick();
        }
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

    public  class MyAxisValueFormatter implements IAxisValueFormatter{
        private  ArrayList<String> mValues;
      //  private String [] mVlues;

        public MyAxisValueFormatter(ArrayList<String> values){
            this.mValues=values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return  mValues.get((int)value%mValues.size());
        }


    }


}
