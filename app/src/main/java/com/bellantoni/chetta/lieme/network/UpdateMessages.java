package com.bellantoni.chetta.lieme.network;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;

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
 * Created by alessandro on 8/4/15.
 */
public class UpdateMessages {

    static final String TAG = "UpdateMessages";
    /**
     * Db access object
     * */
    private FeedReaderDbHelperMessages mDbHelper;
    private final String MESSAGE_MANAGER_URL = "http://computersecurityproject.altervista.org/gcm_server_php/message_manager.php?user_id=";

    public UpdateMessages(FeedReaderDbHelperMessages mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

    public void update(String user_id){

        GetMessagesFromServer getMessagesFromServer = new GetMessagesFromServer();
        getMessagesFromServer.execute(user_id);
    }

    private void updateLocalDB(JSONArray messages){

        /*SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();


        for(int i = 0; i< messages.length(); i++)
        {
            JSONObject message = null;
            try {
                message = messages.getJSONObject(i);
                Log.i(TAG, "message: "+ i + " " + message.getString("message"));


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
                        new String[]{String.valueOf(message.getString("id"))},                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                if(c.moveToNext()){
                    Log.i(TAG, "MESSAGE IN THE LOCAL DB");
                }else{

                    Log.i(TAG, "MESSAGE NOT IN THE LOCAL DB");
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContractMessages.FeedEntry._ID, message.getString("id"));
                    values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID, message.getString("sender_id"));
                    values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE, message.getString("message"));
                    values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ, message.getString("message_read"));
                    values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID, message.getString("receiver_id"));
                    values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP, message.getString("timestamp"));
                    long newRowId = dbWriter.insert(FeedReaderContractMessages.FeedEntry.TABLE_NAME,null,values);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        UpdateLocalDBBackground updateLocalDBBackground = new UpdateLocalDBBackground();
        updateLocalDBBackground.execute(messages);

    }

    public void updateRowWithAnswer(String questionId, String answer){
        Log.i(TAG, "Updating question: "+ questionId + " with: " + answer);
        new UpdateQeustionWithAnswer().execute(questionId, answer);
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
            updateLocalDB(messages);
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
                /*for(int i = 0; i< messages.length(); i++)
                {
                    JSONObject message = messages.getJSONObject(i);
                    Log.i(TAG, "message: "+ i + " " + message.getString("message"));
                }*/
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
            JSONArray messages = params[0];

            SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();


            for(int i = 0; i< messages.length(); i++)
            {
                JSONObject message = null;
                try {
                    message = messages.getJSONObject(i);
                    Log.i(TAG, "message: "+ i + " " + message.getString("message"));


                    String[] projection = {
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE,
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ,
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID,
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID,
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP,
                            FeedReaderContractMessages.FeedEntry.COLUMN_NAME_ANSWER
                    };

                    SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
                    Cursor c = dbReader.query(
                            FeedReaderContractMessages.FeedEntry.TABLE_NAME,  // The table to query
                            projection,                               // The columns to return
                            FeedReaderContractMessages.FeedEntry._ID + "=?",   // The columns for the WHERE clause
                            new String[]{String.valueOf(message.getString("id"))},                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            null                                 // The sort order
                    );

                    if(c.moveToNext()){
                        Log.i(TAG, "MESSAGE IN THE LOCAL DB");
                    }else{

                        Log.i(TAG, "MESSAGE NOT IN THE LOCAL DB");
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContractMessages.FeedEntry._ID, message.getString("id"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID, message.getString("sender_id"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE, message.getString("message"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ, message.getString("message_read"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID, message.getString("receiver_id"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP, message.getString("timestamp"));
                        values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_ANSWER, message.getString("answer"));
                        long newRowId = dbWriter.insert(FeedReaderContractMessages.FeedEntry.TABLE_NAME,null,values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


    private class UpdateQeustionWithAnswer extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {

            String questionId = params[0];
            String answer = params[1];

            ContentValues values = new ContentValues();
            values.put(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_ANSWER, answer);
            SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();
            dbWriter.update(FeedReaderContractMessages.FeedEntry.TABLE_NAME, values,FeedReaderContractMessages.FeedEntry._ID +" = "+questionId, null);

            return null;
        }
    }
}
