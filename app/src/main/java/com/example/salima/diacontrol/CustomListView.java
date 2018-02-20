package com.example.salima.diacontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Salima on 20.01.2018.
 */

public class CustomListView extends BaseAdapter {

    private  Integer[] img;
    public void  CustomListView(Integer [] img){
        this.img=img;

    }
    @Override
    public int getCount() {
        return img.length;
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
       // View view1=getL
        return null;
    }


  /*  private  Activity context;
    private ArrayList<String> text;
    private  Integer[] img;

    ImageView imageViewSugar;
    ImageView imageViewinsulin;
    ImageView imageViewfood;
    ImageView imageViewcomment;
    TextView textSugar;
    TextView textinsulin;
    TextView textfood;
    TextView textcomment;

    public CustomListView(Activity context, ArrayList<String>  text, Integer [] img) {
        super(context, R.layout.listview_design, text);
        this.context=context;
        this.text=text;
        this.img=img;
    }

    public CustomListView(Activity context, String sugar, String insulin, String food, String comment, Integer [] img) {
        super(context, R.layout.listview_design);
        this.context=context;

        this.img=img;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder=null;
        if (r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_design, null, true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else{
            viewHolder=(ViewHolder) r.getTag();
        }

        viewHolder.imageViewSugar.setImageResource(img[0]);
        viewHolder.imageViewinsulin.setImageResource(img[1]);
        viewHolder.imageViewfood.setImageResource(img[2]);
        viewHolder.imageViewcomment.setImageResource(img[3]);

        viewHolder.textSugar.setText(text.get(0));
        viewHolder.textinsulin.setText(text.get(1));
        viewHolder.textfood.setText(text.get(2));
        viewHolder.textcomment.setText(text.get(3));
        return super.getView(position, convertView, parent);
    }

    class ViewHolder {

        ImageView imageViewSugar;
        ImageView imageViewinsulin;
        ImageView imageViewfood;
        ImageView imageViewcomment;
        TextView textSugar;
        TextView textinsulin;
        TextView textfood;
        TextView textcomment;

        ViewHolder(View view1){
             imageViewSugar= (ImageView) view1.findViewById(R.id.imageSuagr);
             imageViewinsulin= (ImageView) view1.findViewById(R.id.insulinIamge);
             imageViewfood= (ImageView) view1.findViewById(R.id.foodImage);
             imageViewcomment= (ImageView) view1.findViewById(R.id.commentImage);
             textSugar= (TextView) view1.findViewById(R.id.sugarText);
             textinsulin= (TextView) view1.findViewById(R.id.insulinText);
             textfood= (TextView) view1.findViewById(R.id.foodText);
             textcomment= (TextView) view1.findViewById(R.id.commentText);

        }
    }*/
}
