package com.logicals.compratodo.firebase;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.logicals.compratodo.R;

import java.util.Map;


public class FirebaseNotification extends FirebaseMessagingService {


    PendingIntent pendingIntent;
    public static boolean isNotify = false;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    NotificationCompat.Builder mBuilder;
    RemoteViews remoteViews;


    @SuppressLint("LogNotTimber")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("remoteMessage", "" + remoteMessage.getData().toString());
        Map<String,String> myData = remoteMessage.getData();
        Log.e("remoteMessage","body " + myData.get("body"));
        Log.e("remoteMessage","title " + myData.get("title"));
        String body = myData.get("body");
        String title = myData.get("title");
        mBuilder = new NotificationCompat.Builder(this,"1");
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_view);

//      remoteViews.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this,
//      System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

        remoteViews.setTextViewText(R.id.content_title, myData.get("title"));
        remoteViews.setTextViewText(R.id.content_message, myData.get("body"));
        remoteViews.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

/*

        Intent intentBlank = new Intent(this,
                HomeActivity.class);
        intentBlank.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(this,
                0, intentBlank,
                PendingIntent.FLAG_ONE_SHOT);

        notifyMe(pendingIntent,title,body);
*/


    }


    private void notifyMe(PendingIntent getPendingIntent, String title, String body) {

        mBuilder.setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(getPendingIntent);

//                .setCustomBigContentView(remoteViews);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                            "eatmates", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

        assert mNotificationManager != null;
        mNotificationManager.notify(1 /* Request Code */, mBuilder.build());

    }


}
