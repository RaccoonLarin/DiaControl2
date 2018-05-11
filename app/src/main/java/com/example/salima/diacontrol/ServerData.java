package com.example.salima.diacontrol;

/**
 * Created by Salima on 04.01.2018.
 */

public class ServerData {
   // private static String ipServ = "http://146.185.150.170:8080/";
   private static String ipServ = "http://192.168.1.35:8080/";
    private static String tokentxt="mytoken.txt";

    public static String getIpServ() {
        return ipServ;
    }

    public static String getTokentxt() {
        return tokentxt;
    }
}
