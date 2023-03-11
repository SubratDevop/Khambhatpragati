package com.khambhatpragati.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khambhatpragati.helper.PreferencesManager;

import static com.khambhatpragati.activity.SplashScreenActivity.fcmToken;
import static com.khambhatpragati.constants.Keys.KEY_FCM_TOKEN;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "com.mpcdfplaceorder";

    @Override
    public void onTokenRefresh() {
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);

        if(fcmToken == null)
            fcmToken="";

        if(token.equalsIgnoreCase("")) {
            fcmToken= PreferencesManager.getString(getApplicationContext(), KEY_FCM_TOKEN);
        }else {
            PreferencesManager.put(getApplicationContext(), KEY_FCM_TOKEN, token);
            fcmToken=token;
        }
    }
}