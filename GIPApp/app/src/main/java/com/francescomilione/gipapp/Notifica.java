package com.francescomilione.gipapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Notifica{
    private Activity activity;

    public Notifica(Activity activity) {
        this.activity = activity;
    }

    public Notifica() {

    }

    public void notifica(String titolo, String messaggio){
        this.createNotificationChannel();
        Intent snoozeIntent = new Intent(activity, MainActivity3.class);
        snoozeIntent.setAction("Ciao");
        snoozeIntent.putExtra("Invia", true);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(activity, 0, snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+393291599577"));
        PendingIntent call = PendingIntent.getActivity(activity, 0, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(activity, "A")
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.baseline_warning_amber_24)
                        .setContentTitle(titolo)
                        .setContentText(messaggio)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(R.drawable.baseline_warning_amber_24, "Chiama", call)
                        .addAction(R.drawable.baseline_warning_amber_24, "Cancella", snoozePendingIntent)
                        .setAutoCancel(true)
                ;
        ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        notificationManager.notify(100, builder.build());
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            CharSequence name = "channel1";
            String description = "Canale notifiche GIP";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("A", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setSound(defaultSoundUri, null);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);


            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
