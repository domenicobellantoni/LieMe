package com.bellantoni.chetta.lieme.backend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bellantoni.chetta.lieme.fragments.ProfileFragment;
import com.bellantoni.chetta.lieme.generalclasses.Contact;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessandro on 6/8/15.
 */
public class MessageHandler {
    private Contact receiver;
    private String senderFacebookId;
    private String message;
    private final String TAG = "MessageHandler";
    private Context context;
    boolean sentResult;

    public MessageHandler(Contact receiver, String senderFacebookId, String message, Context context) {
        this.receiver = receiver;
        this.senderFacebookId = senderFacebookId;
        this.message = message;
        this.context=context;
        this.sentResult = false;
    }

    public void send(){
        Log.i(TAG, "Sending message to " + receiver.getName() + " receiver id: " + receiver.getFacebook_id() + " sender id: " + senderFacebookId + " message: " + message);

        new SenderAsync().execute(senderFacebookId,message, receiver.getFacebook_id());
    }



    private class SenderAsync extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(MessageHandler.this.sentResult==true){
                Toast toast = Toast.makeText(MessageHandler.this.context, "Message Sent", Toast.LENGTH_LONG);
                toast.show();

            }else{
                Toast toast = Toast.makeText(MessageHandler.this.context, "ERROR: Message not Sent", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();

            try {
                List<NameValuePair> parameter = new ArrayList<>(1);
                parameter.add(new BasicNameValuePair("regId", "empty"));
                parameter.add(new BasicNameValuePair("sender_id", params[0]));
                parameter.add(new BasicNameValuePair("receiver_facebook_id", params[2]));
                parameter.add(new BasicNameValuePair("message", params[1]));

                String paramString = URLEncodedUtils.format(parameter, "utf-8");
                HttpGet get = new HttpGet(ProfileFragment.SERVER_URL_SEND_MESSAGE+"?"+paramString);

                Log.i(TAG, paramString);
                HttpResponse resp = client.execute(get);

                if(resp.getStatusLine().getStatusCode()==200){
                    MessageHandler.this.sentResult = true;
                }
            } catch (IOException e) {

                Log.i(TAG,"Error :" + e.getMessage());
            }
            return null;
        }
    }
}
