package com.bellantoni.chetta.lieme;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bellantoni.chetta.lieme.db.FeedReaderContract;
import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelper;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Intent";
    /**
     * Db access object
     * */
    private FeedReaderDbHelperMessages mDbHelper;
    private final String MESSAGE_MANAGER_URL = "http://computersecurityproject.altervista.org/gcm_server_php/message_manager.php?user_id=";
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                sendNotification(extras);
                Intent i = new Intent("android.intent.action.MAIN").putExtras(extras);
                this.sendBroadcast(i);
                Log.i(TAG, "Received: " + extras.toString());

            }


        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, drawnerActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_mini_dialog)
                        .setContentTitle("LieMe")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("New question"))
                        .setContentText("New question");

        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        mDbHelper = new FeedReaderDbHelperMessages(getApplicationContext());
        SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID, msg.getString("sender_facebook_id"));
        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE, msg.getString("message"));
        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ, 0);
        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID, "me");
        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP, "");
        long newRowId = dbWriter.insert(FeedReaderContractMessages.FeedEntry.TABLE_NAME,null,values);


        String[] projection = {
                FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE,
                FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ,
                FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID,
                FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID,
                FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP
        };

        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        Cursor c = dbReader.query(
                FeedReaderContractMessages.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                FeedReaderContractMessages.FeedEntry._ID + "=?",   // The columns for the WHERE clause
                new String[]{String.valueOf(newRowId)},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.moveToNext()){
            Log.i(TAG, "MESSAGGIO PRESO" + c.getString(c.getColumnIndex("message")));
        }


        GetMessagesFromServer getMessagesFromServer = new GetMessagesFromServer();
        getMessagesFromServer.execute("455542404610206");



    }

    private class GetMessagesFromServer extends AsyncTask<String,String,String> {
        JSONArray messages;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        public String convertStreamToString(InputStream inputStream) throws IOException {
            if (inputStream != null) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    inputStream.close();
                }
                return writer.toString();
            } else {
                return "";
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            Log.i(TAG, "Downloading messages for:  " + userId);
            String msg = "";
            String s = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            Log.i(TAG, "URL " + MESSAGE_MANAGER_URL+userId);
            try {
                get.setURI(new URI(MESSAGE_MANAGER_URL+userId));
                HttpResponse resp = client.execute(get);
                s = convertStreamToString(resp.getEntity().getContent());

                Log.i(TAG, "Messages JSON: " + s);
            } catch (IOException | URISyntaxException e) {
                msg = "Error :" + e.getMessage();
                Log.i(TAG, "Error: " + msg);
            }

            JSONObject json = null;
            try {
                json = new JSONObject(s);
                messages = json.getJSONArray("messages");
                for(int i = 0; i< messages.length(); i++)
                {
                    JSONObject message = messages.getJSONObject(i);
                    Log.i(TAG, "message: "+ i + " " + message.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}