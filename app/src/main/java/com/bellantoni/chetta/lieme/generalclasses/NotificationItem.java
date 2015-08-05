package com.bellantoni.chetta.lieme.generalclasses;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Domenico on 21/07/2015.
 */
public class NotificationItem {

    private String idQuestion;
    private int typeNotification;
    private int stateNotification;
    private Timestamp timeNotification;

    public NotificationItem(String idQuestion, int typeNotification, int stateNotification, Timestamp timeNotification){

        this.idQuestion=idQuestion ;
        this.typeNotification= typeNotification;
        this.stateNotification = stateNotification;
        this.timeNotification = timeNotification;


    }

    public String getQuestionId() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
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

    public Timestamp getTimeNotification() {
        return timeNotification;
    }

    public void setTimeNotification(Timestamp timeNotification) {
        this.timeNotification = timeNotification;
    }
}
