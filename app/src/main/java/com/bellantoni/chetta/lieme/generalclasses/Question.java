package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by alessandro on 8/3/15.
 */
public class Question {

    private String id;
    private String sender_id;
    private String receiver_id;
    private int message_read;
    private String message;
    private String timestamp;

    public Question(String id, String sender_id, String receiver_id, int message_read, String message, String timestamp) {
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

    public int getMessage_read() {
        return message_read;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
