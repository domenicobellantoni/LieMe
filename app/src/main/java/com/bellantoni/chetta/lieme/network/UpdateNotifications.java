package com.bellantoni.chetta.lieme.network;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderContractNotification;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperNotification;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

/**
 * Created by alessandro on 8/28/15.
 */
public class UpdateNotifications {

    static final String TAG = "UpdateNotifications";
    private FeedReaderDbHelperNotification mDbHelper;
    private FeedReaderDbHelperMessages mDbHelperMessages;
    private final String NOTIFICATION_MANAGER_URL = "http://computersecurityproject.altervista.org/gcm_server_php/notification_manager.php?user_id=";

    public UpdateNotifications(FeedReaderDbHelperNotification mDbHelper, FeedReaderDbHelperMessages mDbHelperMessages) {
        this.mDbHelper = mDbHelper;
        this.mDbHelperMessages = mDbHelperMessages;
    }

    public void update(String user_id){

        GetNotificationsFromServer getMessagesFromServer = new GetNotificationsFromServer();
        getMessagesFromServer.execute(user_id);
    }
    private void updateLocalDB(JSONArray messages){

        UpdateLocalDBBackground updateLocalDBBackground = new UpdateLocalDBBackground();
        updateLocalDBBackground.execute(messages);

    }
    private class GetNotificationsFromServer extends AsyncTask<String,String,String> {
        JSONArray notifications;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            updateLocalDB(notifications);
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
            Log.i(TAG, "Downloading notifications for:  " + userId);
            String msg = "";
            String s = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            Log.i(TAG, "URL " + NOTIFICATION_MANAGER_URL+userId);
            try {
                get.setURI(new URI(NOTIFICATION_MANAGER_URL+userId));
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
                notifications = json.getJSONArray("notifications");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    private class UpdateLocalDBBackground extends AsyncTask<JSONArray,String,String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(JSONArray ... params) {
            JSONArray notifications = params[0];

            SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();


            for(int i = 0; i< notifications.length(); i++)
            {
                JSONObject notification = null;
                try {
                    notification = notifications.getJSONObject(i);
                    Log.i(TAG, "notification: " + i + " content: " + notification.getString("content"));


                    String[] projection = {
                            FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TYPE,
                            FeedReaderContractNotification.FeedEntry.COLUMN_NAME_STATUS,
                            FeedReaderContractNotification.FeedEntry.COLUMN_NAME_CONTENT,
                            FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP
                    };

                    SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
                    Cursor c = dbReader.query(
                            FeedReaderContractNotification.FeedEntry.TABLE_NAME,  // The table to query
                            projection,                               // The columns to return
                            FeedReaderContractNotification.FeedEntry._ID + "=?",   // The columns for the WHERE clause
                            new String[]{String.valueOf(notification.getString("id"))},                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            null                                 // The sort order
                    );

                    if(c.moveToNext()){
                        Log.i(TAG, "Notification IN THE LOCAL DB");
                    }else{
                        Log.i(TAG, "Notification NOT IN THE LOCAL DB");
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContractNotification.FeedEntry._ID, notification.getString("id"));
                        values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TYPE, notification.getString("type"));
                        values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_STATUS, notification.getString("status"));
                        values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_CONTENT, notification.getString("content"));
                        values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP, notification.getString("timestamp"));
                        long newRowId = dbWriter.insert(FeedReaderContractNotification.FeedEntry.TABLE_NAME,null,values);
                        // Update the question with the answer
                        JSONObject json = new JSONObject(notification.getString("content"));
                        UpdateMessages updateMessages = new UpdateMessages(mDbHelperMessages);
                        updateMessages.updateRowWithAnswer(json.getString("id"), json.getString("answer"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
