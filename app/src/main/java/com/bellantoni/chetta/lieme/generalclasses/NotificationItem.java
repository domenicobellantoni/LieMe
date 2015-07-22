package com.bellantoni.chetta.lieme.generalclasses;

import java.util.Date;

/**
 * Created by Domenico on 21/07/2015.
 */
public class NotificationItem {

    private int idQuestion;
    private int typeNotification;
    private int stateNotification;
    private long timeNotification;

    public NotificationItem(int idQuestion, int typeNotification, int stateNotification, long timeNotification){

        this.idQuestion=idQuestion ;
        this.typeNotification= typeNotification;
        this.stateNotification = stateNotification;
        this.timeNotification = timeNotification;


    }

    public int getQuestionId() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public int getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(int typeNotification) {
        this.typeNotification = typeNotification;
    }

    /*public NotificationItem getNotification(){
        return new NotificationItem(this.idQuestion,this.typeNotification, this.stateNotification, this.timeNotification);
    }*/

    public int getStateNotification() {
        return stateNotification;
    }

    public void setStateNotification(int stateNotification) {
        this.stateNotification = stateNotification;
    }

    public long getTimeNotification() {
        return timeNotification;
    }

    public void setTimeNotification(long timeNotification) {
        this.timeNotification = timeNotification;
    }
}
