package com.bellantoni.chetta.lieme.generalclasses;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by alessandro on 8/3/15.
 */
public class Question implements Notification {

    private String id;
    private String sender_id;
    private String receiver_id;
    private String message_read;
    private String message;
    private Timestamp timestamp;
    private final int notificationType = 0;
    private int notificationStatus = 0;


    public Question(String id, String sender_id, String receiver_id, String message_read, String message, Timestamp timestamp) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_read = message_read;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sender: " + sender_id + " Receiver: " + receiver_id + " Message: " + message + " Timestamp: " + timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getMessage_read() {
        return message_read;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public Timestamp getNotificationTimestamp() {
        return timestamp;
    }

    @Override
    public int getNotificationType() {
        return notificationType;
    }

    @Override
    public int getNotificationStatus() {
        return notificationStatus;
    }

    @Override
    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
