package com.khambhatpragati.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.khambhatpragati.activity.OTPVerificationActivity;
import com.khambhatpragati.helper.PreferencesManager;

import static com.khambhatpragati.activity.SplashScreenActivity.fcmToken;
import static com.khambhatpragati.constants.Keys.KEY_FCM_TOKEN;

/**
 * Created by Dell on 25-07-2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

/*    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    *//**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     *//*
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
//        GroupsActivity actvitiyContext = new GroupsActivity();
//        String mobileNo = PreferencesManager.getString(actvitiyContext, KEY_MOBILE_NO);
//        UpdateFCMTokenPresenter presenter = new UpdateFCMTokenPresenterImpl(actvitiyContext);
//        presenter.updateFCMToken(actvitiyContext, mobileNo, token);
        //chetana
        if(fcmToken == null)
            fcmToken="";

        if(token.equalsIgnoreCase("")) {
            fcmToken= PreferencesManager.getString(getApplicationContext(), KEY_FCM_TOKEN);
        }else {
            PreferencesManager.put(getApplicationContext(), KEY_FCM_TOKEN, token);
            fcmToken=token;
        }
    }*/
}
