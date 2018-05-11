package com.example.salima.diacontrol;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.salima.diacontrol.DiaryActivity.REQUEST_CODE_FUCNCTIONONE;


/*Отображаются записи, который сделал пользователь. Показывается при нажатии кнопки "book" в DiaryActivity*/
public class DiaryFragment extends Fragment {





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
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

        return inflater.inflate(R.layout.fragment_diary, container, false);
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

    Toolbar toolbar;
    Integer [] images = {R.drawable.bloodsugar, R.drawable.insulin, R.drawable.bread, R.drawable.weight, R.drawable.comment};


    //DatabaseHelper db;
    //СЮДА ПИШИ
    TextView txt;

    private ListView listView; //лол ну листвью че непонятна
    ArrayAdapter<String> adapter; //нинаю зачем он
    ArrayList<String> listItems=new ArrayList<String>();//тут храним лист текста
    LinearLayout linearLayout; //внутри нее остальные компоненты
    ArrayList<String> stringSugar; //записываем всее сзаписи сахара из бд
    ArrayList<String>stringInsulin; //записываем все записи инсулина из бд
    ArrayList<String> stringBredUnits;
    ArrayList<String> stringWeight;
    ArrayList<String> stringComment; //записываем все комменты из бд
    ArrayList<String> stringDate; //записываем дату из бд
    ArrayList<String> seconds; //записываем дату из бд
    DatabaseHelper db;
    ListView help;
    Fragment newFragment;
    CustomAdapter customListView;
    RelativeLayout  relativeLayoutSugar;
    RelativeLayout relativeLayoutInsulin;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {




        listView  = (ListView) getView().findViewById(R.id.listV);
        db=new DatabaseHelper(getActivity());
        ArrayList<String> theList=new ArrayList<>();
        Cursor data     = db.getListContents();

        newFragment = this;
        if(data.getCount()==0){

        }

        listViewClickListener();
        // CustomListView customListView=new CustomListView(this,)
        stringSugar=new ArrayList<>();
        stringInsulin=new ArrayList<>();
        stringBredUnits=new ArrayList<>();
        stringComment=new ArrayList<>();
        stringWeight=new ArrayList<>();
        stringDate=new ArrayList<>();
        seconds=new ArrayList<>();


        while (data.moveToNext()){
            stringSugar.add(data.getString(1));
            stringInsulin.add(data.getString(2));
            stringBredUnits.add(data.getString(3));
           // stringfood=data.getString(3);
            stringWeight.add(data.getString(4));
            stringComment.add(data.getString(5));
            String  [] dateParts = ((data.getString(6)).split(" "));
            String [] time= dateParts[1].split(":");
            String newDate=dateParts[0]+" "+time[0]+":"+time[1];
            stringDate.add(newDate);
            seconds.add(time[2]);

        }
         customListView = new CustomAdapter();
        listView.setAdapter(customListView);


    }




    public void listViewClickListener(){

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Intent intent = new Intent(getActivity(), AddActivity.class);
              TextView textView = (TextView) view.findViewById(R.id.dateDiary);
              TextView textViewSeconds = (TextView) view.findViewById(R.id.seconds);
              intent.putExtra("edit", "true");
             intent.putExtra("item", position); //i - строка которую выбрали в listview

              startActivityForResult(intent, REQUEST_CODE_FUCNCTIONONE);

          }
      });


       listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, final View view,
                                                   int position, long id) {
                        //  Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
                        final View view1=view;
                        final  int position1=position;
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Удалить запись?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        db = new DatabaseHelper(getActivity());
                                        TextView textView = (TextView) view1.findViewById(R.id.dateDiary);
                                        TextView textViewSeconds = (TextView) view1.findViewById(R.id.seconds);
                                        String text = textView.getText().toString();
                                        db.delete(position1, text);

                                        getFragmentManager()
                                                .beginTransaction()
                                                .detach(newFragment)
                                                .attach(newFragment)
                                                .commit();
                                        listView.invalidateViews();


                                    }
                                })
                                .setNegativeButton("Нет", null).create();
                        dialog.show();





                        return  true;
                    }
                }

                        // }

        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void setTextView(String sugar, String insulin, String comment){

    }

     class CustomAdapter extends BaseAdapter{

         @Override
         public int getCount() {
             return stringDate.size();
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
             View view1=getLayoutInflater().inflate(R.layout.listview_design, null);
             ImageView imageViewSugar= (ImageView) view1.findViewById(R.id.imageSuagr);
             ImageView imageViewinsulin= (ImageView) view1.findViewById(R.id.insulinIamge);
              ImageView imageViewfood= (ImageView) view1.findViewById(R.id.foodImage);
             ImageView imageWeight= (ImageView) view1.findViewById(R.id.weightImage);
             ImageView imageViewcomment= (ImageView) view1.findViewById(R.id.commentImage);
             TextView textSugar= (TextView) view1.findViewById(R.id.sugarText);
             TextView textinsulin= (TextView) view1.findViewById(R.id.insulinText);
             TextView textfood= (TextView) view1.findViewById(R.id.foodText);
             TextView textDate = (TextView) view1.findViewById(R.id.dateDiary);
             TextView textWeight = (TextView) view1.findViewById(R.id.weighText);
             TextView textcomment= (TextView) view1.findViewById(R.id.commentText);
             TextView textSeconds= (TextView) view1.findViewById(R.id.seconds);
             RelativeLayout  relativeLayoutSugar = (RelativeLayout) view1.findViewById(R.id.sugarLayout);
             RelativeLayout relativeLayoutInsulin = (RelativeLayout) view1.findViewById(R.id.insulinLayou);
             RelativeLayout relativeLayoutFood = (RelativeLayout) view1.findViewById(R.id.foodLayout);
             RelativeLayout relativeLayoutComment = (RelativeLayout) view1.findViewById(R.id.commentLayout);
             RelativeLayout relativeLayoutWeight = (RelativeLayout) view1.findViewById(R.id.weightLayout);




             if(!stringSugar.get(i).equals("")){
                 relativeLayoutSugar.setVisibility(View.VISIBLE);
                 textSugar.setText(stringSugar.get(i));
                 imageViewSugar.setImageResource(images[0]);
             } else{
                 relativeLayoutSugar.setVisibility(View.GONE);
             }

             if(!stringInsulin.get(i).equals("")){
                 relativeLayoutInsulin.setVisibility(View.VISIBLE);
                 textinsulin.setText(stringInsulin.get(i));
                 imageViewinsulin.setImageResource(images[1]);
             } else
             {
                 relativeLayoutInsulin.setVisibility(View.GONE);
             }


             if(!stringBredUnits.get(i).equals("")){
                 relativeLayoutFood.setVisibility(View.VISIBLE);
                 textfood.setText(stringBredUnits.get(i));
                 imageViewfood.setImageResource(images[2]);
             } else{
                 relativeLayoutFood.setVisibility(View.GONE);
             }


             if(!stringWeight.get(i).equals("")){
                 relativeLayoutWeight.setVisibility(View.VISIBLE);
                 textWeight.setText(stringWeight.get(i));
                 imageWeight.setImageResource(images[3]);
             } else{
                 relativeLayoutWeight.setVisibility(View.GONE);
             }


             if(!stringComment.get(i).equals("")){
                 relativeLayoutComment.setVisibility(View.VISIBLE);
                 textcomment.setText(stringComment.get(i));
                 imageViewcomment.setImageResource(images[4]);
             } else{
                 relativeLayoutComment.setVisibility(View.GONE);
             }

             if(!stringDate.get(i).equals("")){
                 textDate.setVisibility(View.VISIBLE);
                 textDate.setText(stringDate.get(i));

             } else{
                 textDate.setVisibility(View.GONE);
             }

             if(!stringDate.get(i).equals("")){

                 textSeconds.setText(stringDate.get(i));

             }







             return view1;
         }
     }


    class HttpPost extends AsyncTask<String, Integer, Void> {
        Toast toast;

        protected String getJsonArray(String... strings){
            DatabaseHelper db=new DatabaseHelper(getContext());

            String jsonBody="{\"dairy\": {\n\"getData\": {\n\"email\": " + "\"" + db.selectEmail() +
                    "\"\n"+"}\n}}";

            return  jsonBody;
        }



        @Override
        protected void onPreExecute() {

            //   spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String mailTxt = strings[1];
            String passTxt = strings[2];

            OutputStream out = null;
            BufferedReader reader=null;
            try {
                String urlString = strings[0];
                String jsonBody = getJsonArray(strings[1]);
                //  publishProgress(1);

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
                    // isCorrect = sb.append(line).toString();
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
            // spinner.setProgress(0);
            // spinner.setVisibility(View.GONE);
            //  toast.cancel();

        }
    }
}
