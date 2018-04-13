package com.example.salima.diacontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

   /*
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

   Button buttonexport;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonexport=(Button) getActivity().findViewById(R.id.exportButton);
        addListenerOnButton();
    }


    public void addListenerOnButton() {


        buttonexport.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       /* CSVWriter writer = null;
                        try
                        {
                            writer = new CSVWriter(new FileWriter("myfile.csv"),',','\'','-', "~");

                            String[] entries = "first#second#third".split("#"); // array of your values
                            writer.writeNext(entries);
                            writer.close();
                        }
                        catch (IOException e)
                        {
                            //error
                        }*/
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(intent, RESULT_OK);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(getContext(), filePath, Toast.LENGTH_LONG).show();
            // Do anything with file

    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, String, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getContext());
        boolean memoryErr = false;

        // to show Loading dialog box
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        // to write process
        protected Boolean doInBackground(final String... args) {


          /*  boolean success = false;

            //String currentDateString = new SimpleDateFormat(Constants.SimpleDtFrmt_ddMMyyyy).format(new Date());

            File dbFile = new File("diary.db");
        //    Log.v(TAG, "Db path is: " + dbFile); // get the path of db
            File exportDir = new File(Environment.getExternalStorageDirectory() + File.separator, "");

            long freeBytesInternal = new File(getContext().getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
            long megAvailable = freeBytesInternal / 1048576;

            if (megAvailable < 0.1) {
                System.out.println("Please check"+megAvailable);
                memoryErr = true;
            }else {
                exportDirStr = exportDir.toString();// to show in dialogbox
               // Log.v(TAG, "exportDir path::" + exportDir);
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                try {
                    List<SalesActivity> listdata = salesLst;
                    SalesActivity sa = null;
                    String lob = null;
                    for (int index = 0; index < listdata.size();) {
                        sa = listdata.get(index);
                        lob = sa.getLob();
                        break;
                    }
                    if (Constants.Common.OCEAN_LOB.equals(lob)) {

                        file = new File(exportDir, Constants.FileNm.FILE_OFS + currentDateString + ".csv");
                    } else {
                        file = new File(exportDir, Constants.FileNm.FILE_AFS + currentDateString + ".csv");
                    }
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


                    // this is the Column of the table and same for Header of CSV
                    // file
                    if (Constants.Common.OCEAN_LOB.equals(lob)) {
                        csvWrite.writeNext(Constants.FileNm.CSV_O_HEADER);
                    }else{
                        csvWrite.writeNext(Constants.FileNm.CSV_A_HEADER);
                    }
                    String arrStr1[] = { "SR.No", "CUTSOMER NAME", "PROSPECT", "PORT OF LOAD", "PORT OF DISCHARGE" };
                    csvWrite.writeNext(arrStr1);

                    if (listdata.size() > 0) {
                        for (int index = 0; index < listdata.size(); index++) {
                            sa = listdata.get(index);
                            String pol;
                            String pod;
                            if (Constants.Common.OCEAN_LOB.equals(sa.getLob())) {
                                pol = sa.getPortOfLoadingOENm();
                                pod = sa.getPortOfDischargeOENm();
                            } else {
                                pol = sa.getAirportOfLoadNm();
                                pod = sa.getAirportOfDischargeNm();
                            }
                            int srNo = index;
                            String arrStr[] = { String.valueOf(srNo + 1), sa.getCustomerNm(), sa.getProspectNm(), pol, pod };
                            csvWrite.writeNext(arrStr);
                        }
                        success = true;
                    }
                    csvWrite.close();

                } catch (IOException e) {
                    Log.e("SearchResultActivity", e.getMessage(), e);
                    return success;
                }
            }
            return success;*/
          return null;
        }

        // close dialog and give msg
        protected void onPostExecute(Boolean success) {
           /* if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                dialogBox(Constants.Flag.FLAG_EXPRT_S);
            } else {
                if (memoryErr==true) {
                    dialogBox(Constants.Flag.FLAG_MEMORY_ERR);
                } else {
                    dialogBox(Constants.Flag.FLAG_EXPRT_F);
                }
            }*/
        }
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
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
