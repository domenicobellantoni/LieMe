package com.bellantoni.chetta.lieme.generalclasses;


import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by alessandro on 8/4/15.
 */
public interface Notification {

    public Timestamp getNotificationTimestamp();
    public int getNotificationType();
    public int getNotificationStatus();
    public void setNotificationStatus(int notificationStatus);
}
