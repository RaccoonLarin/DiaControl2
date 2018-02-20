package com.example.salima.diacontrol;

/**
 * Created by Salima on 22.01.2018.
 */

public class DateToString {

    public static String getStringDay(int day){


        if(day < 10){

            return  "0" + day;
        }

        return Integer.toString(day);
    }

    public static String getStringMonth(int month){


        if(month < 10){

            return  "0" + month;
        }

        return Integer.toString(month);
    }


    public static String getStringTime(int time){


        if(time < 10){

            return  "0" + time;
        }

        return Integer.toString(time);
    }

    public static String getFormatTime(int day, int month, int year){
        return year+"-"+getStringMonth(month+1)+"-"+getStringDay(day);
    }
}
