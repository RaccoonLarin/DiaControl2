package com.example.salima.diacontrol;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txt=(TextView) getView().findViewById(R.id.textStatistic);
        gett_json();

       /* TextView textt= (TextView) getView().findViewById(R.id.textStatistic);
        db = new DbHelp(getActivity());
        File databse= getContext().getDatabasePath(DbHelp.DB_NAME);
        if(false==databse.exists()){
            db.getReadableDatabase();
            if(copyDataBase(getActivity())){
                Toast.makeText(getActivity(), "Copy databse ok", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getActivity(), "Copy databse error", Toast.LENGTH_SHORT).show();
                return;

            }
        }

        textt.setText(db.getTxt());*/
        /*DbHelp myDbHelper;
        myDbHelper = new DbHelp(getActivity());

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

        Cursor data = myDbHelper.select(1);

        while (data.moveToNext()){
            textt.setText(data.getString(1));
        }*/
    }

    public void gett_json(){
        String json;
        try {

            InputStream is = getActivity().getAssets().open("foodsamp.json");
            int size= is.available();
            byte [] buffer=new byte[size];
            is.read(buffer);
            is.close();

            json=new String(buffer, "UTF-8");
            JSONArray jsonArray=new JSONArray(json);


                JSONObject obj=jsonArray.getJSONObject(0);
                String str=new String(obj.getString("foodname").getBytes("ISO-8859-1"), "UTF-8");
            txt.setText(obj.getString("foodname"));
               // Toast.makeText(getActivity(), obj.getString("foodname"), Toast.LENGTH_LONG).show();


        }
        catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
