package com.bellantoni.chetta.lieme.generalclasses;

import java.sql.Timestamp;

/**
 * Created by alessandro on 8/7/15.
 */
public class NotificationImpl implements Notification{
    private Timestamp timestamp;
    private int notificationType;
    private int notificationStatus;
    private String id;
    private String content;

    public NotificationImpl(Timestamp timestamp, int notificationType, int notificationStatus, String content, String id) {
        this.timestamp = timestamp;
        this.notificationType = notificationType;
        // content contains the question Id for the answer notification
        this.content = content;
        this.id = id;
        this.notificationStatus = notificationStatus;
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

    public String getId(){
        return  id;
    }
    public String getContent(){
        return  content;
    }

}
