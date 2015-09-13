package com.bellantoni.chetta.lieme.backend;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperNotification;
import com.bellantoni.chetta.lieme.drawnerActivity;
import com.bellantoni.chetta.lieme.fragments.ContactListFragment;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.bellantoni.chetta.lieme.network.UpdateNotifications;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.sql.Timestamp;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    FeedReaderDbHelperMessages mDbHelper;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Intent";
    /**
     * Db access object
     *
     * */

    public GcmIntentService() {

        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
                Intent i = new Intent("android.intent.action.MAIN").putExtras(extras);
                this.sendBroadcast(i);
                Log.i(TAG, "Received: " + extras.toString());

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(Bundle msg) {


        String user_id = drawnerActivity.myFacebookId;

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(this, drawnerActivity.class);
        newIntent.setAction("OPEN_NOTIFICATION");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,newIntent, 0);

        mDbHelper = new FeedReaderDbHelperMessages(getApplicationContext());
        UpdateMessages updateMessages = new UpdateMessages(mDbHelper);

        if(msg.getString("notificationType").equals("question"))
        {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_notifica_domanda_mini)
                            .setContentTitle("LieMe")
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("New question"))
                            .setContentText("New question");


            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            updateMessages.update(user_id);
        }
        if(msg.getString("notificationType").equals("answer"))
        {
            Contact userAnswer = ContactListFragment.findContactById(msg.getString("friendId"));
            Timestamp notificationTimestamp = Timestamp.valueOf(msg.getString("timestamp"));
            Log.i(TAG, "Answer notification received from: "+ userAnswer.getName() + " timestamp: " + notificationTimestamp.toString());
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_mini_dialog)
                            .setContentTitle("LieMe")
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("New answer"))
                            .setContentText(userAnswer.getName() + " answered");

            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

            long[] pattern = {
                    0,  // Start immediately
                    100,
                    100,
                    100
            };

            v.vibrate(pattern, -1);

            FeedReaderDbHelperNotification mDbHelperNotifications = new FeedReaderDbHelperNotification(getApplicationContext());
            UpdateNotifications updateNotifications = new UpdateNotifications(mDbHelperNotifications, mDbHelper);
            updateNotifications.update(user_id);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}