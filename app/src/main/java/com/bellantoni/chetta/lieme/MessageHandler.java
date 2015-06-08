package com.bellantoni.chetta.lieme;

import android.util.Log;

import com.bellantoni.chetta.lieme.generalclasses.Contact;

/**
 * Created by alessandro on 6/8/15.
 */
public class MessageHandler {
    private Contact receiver;
    private String senderFacebookId;
    private String message;
    private final String TAG = "MessageHandler";

    public MessageHandler(Contact receiver, String senderFacebookId, String message) {
        this.receiver = receiver;
        this.senderFacebookId = senderFacebookId;
        this.message = message;
    }

    public void send(){
        Log.i(TAG, "Sending message to " + receiver.getName());
    }
}
