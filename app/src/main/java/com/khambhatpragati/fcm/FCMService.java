package com.khambhatpragati.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.khambhatpragati.R;
import com.khambhatpragati.activity.GroupsActivity;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.utils.AppUtil;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.khambhatpragati.activity.SplashScreenActivity.fcmToken;
import static com.khambhatpragati.constants.Keys.KEY_FCM_TOKEN;

public class FCMService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);

        if(fcmToken == null)
            fcmToken="";

        if(s.equalsIgnoreCase("")) {
            fcmToken= PreferencesManager.getString(getApplicationContext(), KEY_FCM_TOKEN);
        }else {
            PreferencesManager.put(getApplicationContext(), KEY_FCM_TOKEN, s);
            fcmToken=s;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

// Create Notification
        Intent intent = new Intent(this, GroupsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AppUtil.getRandomNumber(), notificationBuilder.build());
    }
}

/*
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import com.khambhatpragati.R;
import com.khambhatpragati.activity.GroupsActivity;
import com.khambhatpragati.utils.AppUtil;

import com.google.firebase.messaging.RemoteMessage;

*/
/**
 * Created by Dell on 25-07-2017.
 *//*


public class FCMService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final int NOTIFICATION_ID = 237;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        // Create Notification
        Intent intent = new Intent(this, GroupsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.test_1)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AppUtil.getRandomNumber(), notificationBuilder.build());
    }
}*/
