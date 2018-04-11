package com.example.salima.diacontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MychanelIDD");
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("DiaControl")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentText("Принять таблетки");
        Random random=new Random();
        int m = random.nextInt(9999-1000)+1000;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, builder.build());


       /* NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent=new Intent(context, RimenderFragment.class);
        int i=0;
        i=intent.getIntExtra("id", -1);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,repeating_intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, "myid")
               .setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Integer.toString(i)).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentText("Принять таблетки").setAutoCancel(true);

        Random random=new Random();
        int m = random.nextInt(9999-1000)+1000;

        notificationManager.notify(m, builder.build());*/

    }
}
