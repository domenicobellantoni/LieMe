package com.bellantoni.chetta.lieme.generalclasses;

import java.util.Comparator;

/**
 * Created by alessandro on 8/7/15.
 */
public class TimestampComparator implements Comparator<Notification> {
    @Override
    public int compare(Notification o1, Notification o2) {
        int result = o1.getNotificationTimestamp().compareTo(o2.getNotificationTimestamp());

        if(result>0)
            return -1;
        if(result<0)
            return 1;
        return 0;

    }
}