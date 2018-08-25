package invenz.roy.blooddonationapp1.notification;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import invenz.roy.blooddonationapp1.HomeActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    private static final String TAG = "roy";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From (MyFirebaseMessagingService): " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived (MyFirebaseMessagingService): "+remoteMessage.getNotification().getBody());

        showNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());

    }

    /*##### My method ######*/
    private void showNotification(String from, String notification) {

        /*#####                      MyNotificationManager object                    #######*/
        MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());
        Intent intent = new Intent(this, HomeActivity.class);

        /*##             sending notification to MyNotificationManager                ##*/
        myNotificationManager.showMyNotification(from, notification, intent);
    }



}
