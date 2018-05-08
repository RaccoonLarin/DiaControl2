package com.example.salima.diacontrol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.salima.diacontrol.DiaryActivity.REQUEST_CODE_FUCNCTIONONE;


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
   private String isCorrect; //false or token
   Button buttonexport, buttonSave;
   EditText targetXe, minXe, maxXe, userXE;
   TextView txtExit;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonexport=(Button) getActivity().findViewById(R.id.exportButton);
        buttonSave=(Button) getActivity().findViewById(R.id.saveSettingsButton);
        targetXe  = (EditText) getActivity().findViewById(R.id.targetXEEdit);
        minXe = (EditText) getActivity().findViewById(R.id.minXEEdit);
        maxXe = (EditText) getActivity().findViewById(R.id.maxXEEdit);
        txtExit=(TextView) getActivity().findViewById(R.id.exit);
        userXE=(EditText) getActivity().findViewById(R.id.xeUserEdit);
        txtExit.setPaintFlags(txtExit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //подчеркнуть текст

        if(SettingUser.xeTarget!=null) {
            targetXe.setText(SettingUser.xeTarget.toString());
        }

        if(SettingUser.xeMin!=null) {
            minXe.setText(SettingUser.xeMin.toString());
        }

        if(SettingUser.xeMax!=null) {
            maxXe.setText(SettingUser.xeMax.toString());
        }

        userXE.setText(SettingUser.xe.toString());

        addListenerOnButton();
        addListenerSaveSettings();
        addListenerOnText();
    }


    public void addListenerOnButton() {


        buttonexport.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FileWriter mfileWrite;
                        String baseDir=Environment.getExternalStorageDirectory().getAbsolutePath();
                        Random random=new Random();
                        Integer s=random.nextInt();
                        String str = "DiaControl_"+s.toString()+".csv";
                        String filePath=baseDir+File.separator+str;
                        CSVWriter csvWrite=null;
                        File f=new File(filePath);
                        if(f.exists()&&!f.isDirectory()){
                            try {
                                 mfileWrite=new FileWriter(filePath, true);
                                 csvWrite=new CSVWriter(mfileWrite);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }  else{
                            try {
                                csvWrite=new CSVWriter(new FileWriter(filePath));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        DatabaseHelper dbhelper=new DatabaseHelper(getContext());
                        DatabaseHelper db =new DatabaseHelper(getContext());
                        Cursor curCSV = db.selectExport();
                        String column[] ={"Уровень сахара", "Инсулин", "ХЕ", "Вес", "Комментарий", "Дата"};
                        csvWrite.writeNext(column);
                        while(curCSV.moveToNext())
                        {
                            //Which column you want to exprort
                            String arrStr[] ={curCSV.getString(1), curCSV.getString(2), curCSV.getString(3),
                                    curCSV.getString(4), curCSV.getString(5),  curCSV.getString(6)};
                            csvWrite.writeNext(arrStr);
                        }
                        try {
                            csvWrite.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        curCSV.close();
                        Toast.makeText(getContext(), "Сохранено", Toast.LENGTH_SHORT).show();

                       /* File dbFile= new File("diary.db");
                        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
                        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
                        if (!exportDir.exists())
                        {
                            exportDir.mkdirs();
                        }

                        Random random=new Random();
                        Integer s=random.nextInt();
                        String str = s.toString();
                        File file = new File(exportDir, "DiaControl_"+str+".csv");
                        try
                        {
                            if(!file.exists())
                                file.mkdirs();
                            else if(!file.isDirectory()&&file.canWrite()){
                                file.delete();
                                file.mkdirs();
                            }
                            file.createNewFile();
                            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                            SQLiteDatabase db = dbhelper.getReadableDatabase();
                            Cursor curCSV = db.rawQuery("SELECT * FROM diary_data",null);
                            csvWrite.writeNext(curCSV.getColumnNames());
                            while(curCSV.moveToNext())
                            {
                                //Which column you want to exprort
                                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
                                csvWrite.writeNext(arrStr);
                            }
                            csvWrite.close();
                            curCSV.close();
                        }
                        catch(Exception sqlEx)
                        {
                            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                        }*/
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
                       // Intent createIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                     //   createIntent.setType("text/*").addCategory(Intent.CATEGORY_OPENABLE);
                     //   String filename="dfsd.pdf";
                   //     createIntent.putExtra(Intent.EXTRA_TITLE, filename);
                     // startActivityForResult(createIntent, RESULT_OK);
                  //  Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

                   // startActivityForResult(intent, RESULT_OK);

                    }
                });
    }

    public void addListenerSaveSettings() {

        buttonSave.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String targXe=targetXe.getText().toString();
                        String mXe=minXe.getText().toString();
                        String mxXe=maxXe.getText().toString();
                        String oneXE=userXE.getText().toString();
                        DatabaseHelper db=new DatabaseHelper(getContext());


                        //  SettingUser.xeTarget=null;
                      //  SettingUser.xeMin=null;
                       // SettingUser.xeMax=null;
                        Double tempTarget=SettingUser.xeTarget, tempMin=SettingUser.xeMin, tempMax=SettingUser.xeMax;
                         if(targXe.matches("") && mXe.matches("") && mxXe.matches("")){
                             SettingUser.xeTarget=null;
                             SettingUser.xeMin=null;
                             SettingUser.xeMax=null;
                             db.updateSettings(SettingUser.xeMax, SettingUser.xeMin, SettingUser.xeTarget, SettingUser.xe);
                             Toast.makeText(getContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                             return;
                         }

                         if(oneXE.matches("")){
                             userXE.setText("12");
                         } else{
                             SettingUser.xe= Integer.parseInt(oneXE);
                         }
                        if (!targXe.matches(""))
                            tempTarget = Double.parseDouble(targXe);
                         else
                            tempTarget=null;

                        if (!mXe.matches(""))
                            tempMin= Double.parseDouble(mXe);
                         else
                             tempMin=null;

                        if (!mxXe.matches(""))
                            tempMax = Double.parseDouble(mxXe);
                        else
                            tempMax=null;


                        try {
                            if (tempMin>= tempMax) {
                                Toast.makeText(getContext(), "Низкий сахар должен быть меньше высокого", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e){

                        }
                        try {
                            if (tempTarget>= tempMax) {
                                Toast.makeText(getContext(), "Целевой сахар должен быть между низким и высоким сахаром", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e){

                        }

                        try {
                            if (tempTarget <= tempMin) {
                                Toast.makeText(getContext(), "Целевой сахар должен быть между низким и высоким сахаром", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e){

                        }

                        SettingUser.xeTarget=tempTarget;
                        SettingUser.xeMin=tempMin;
                        SettingUser.xeMax=tempMax;
                        if(SettingUser.xeTarget!=null) {
                            targetXe.setText(SettingUser.xeTarget.toString());
                        }

                        if(SettingUser.xeMin!=null) {
                            minXe.setText(SettingUser.xeMin.toString());
                        }

                        if(SettingUser.xeMax!=null) {
                            maxXe.setText(SettingUser.xeMax.toString());
                        }
                        Toast.makeText(getContext(), "сохранено", Toast.LENGTH_SHORT).show();


                        db.updateSettings(SettingUser.xeMax, SettingUser.xeMin, SettingUser.xeTarget, SettingUser.xe);

                    }
                }
        );
    }

    //Выход из системы
    public void addListenerOnText(){
        txtExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(ServerData.getTokentxt());
                        boolean deleted = file.delete();

                        if(!SettingUser.isGuest){
                            try {

                                new HttpPost().execute(ServerData.getIpServ()+"exit").get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        getContext().deleteFile(ServerData.getTokentxt());
                        Intent intent = new Intent(getContext(), UserLogin.class);
                        startActivity(intent);
                        getActivity().finish();

                        //File dir = getFilesDir();

                    }
                }
        );
    }


    /*rivate void selectExportFile() {
		Intent createIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		createIntent.setType("text/*").addCategory(Intent.CATEGORY_OPENABLE);
		String bookName = BooksDbAdapter.getInstance().getActiveBookDisplayName();

		if (mExportFormat == ExportFormat.XML || mExportFormat == ExportFormat.QIF) {
			createIntent.setType("application/zip");
		}

		String filename = Exporter.buildExportFilename(mExportFormat, bookName);
		if (mExportTarget == ExportParams.ExportTarget.URI && mExportFormat == ExportFormat.QIF){
			filename += ".zip";
		}

		createIntent.putExtra(Intent.EXTRA_TITLE, filename);
		startActivityForResult(createIntent, REQUEST_EXPORT_FILE);*/


    /*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
 Callback for when the activity chooser dialog is completed
        switch (requestCode){
            case BackupPreferenceFragment.REQUEST_RESOLVE_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    BackupPreferenceFragment.mGoogleApiClient.connect();
                }
                break;

            case REQUEST_EXPORT_FILE:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        mExportUri = data.getData();
                    }

                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getActivity().getContentResolver().takePersistableUriPermission(mExportUri, takeFlags);

                    mTargetUriTextView.setText(mExportUri.toString());
                    if (mExportStarted)
                        startExport();

                }
                break;
        }
    }
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
     //   super.onActivityResult(requestCode, resultCode, data);
       Uri mExportUri = data.getData();
        Log.i(TAG, mExportUri.toString());
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
        Log.i(TAG, filePath);
            Toast.makeText(getContext(), filePath, Toast.LENGTH_LONG).show();
            // Do anything with file

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


    class HttpPost extends AsyncTask<String, Integer, Void> {
        Toast toast;
        String token;
        protected String createJsonString(String... strings){
            String jsonBody="{\"diary\": {\n\"exit\": {\n\"token\": " + "\"" + token + "\"\n" +
                    "}\n}}";

            return  jsonBody;
        }

        public void readFile(){

        }

        @Override
        protected void onPreExecute() {
            //  super.onPreExecute();
            toast= Toast.makeText(getContext(), "Пожалуйста, подождите", Toast.LENGTH_SHORT);
            toast.show();
         //   spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {

                OutputStream out = null;
                BufferedReader reader=null;
            DatabaseHelper db=new DatabaseHelper(getActivity());
            ArrayList<String> theList=new ArrayList<>();
            // Cursor data = db.getListContentsTrial(dateSet);
            Cursor data = db.selectToken();

            if(data.getCount()==0){

            }


            while (data.moveToNext()){
                token=data.getString(1);


            }

            db.deleteToken(token);

                try {
                    String urlString = strings[0];
                    String jsonBody = createJsonString(token);
                    publishProgress(1);

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
         //   spinner.setProgress(0);
          //  spinner.setVisibility(View.GONE);
            toast.cancel();

        }
    }
}
