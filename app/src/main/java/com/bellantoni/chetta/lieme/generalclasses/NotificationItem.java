package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by Domenico on 21/07/2015.
 */
public class NotificationItem {

    private String idQuestion;
    private int typeNotification;
    private int stateNotification;
    private String timeNotification;
    private String answeredQuestionId;

    public NotificationItem(String idQuestion, int typeNotification, int stateNotification, String timeNotification){

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

    public int getStateNotification() {
        return stateNotification;
    }

    public void setStateNotification(int stateNotification) {
        this.stateNotification = stateNotification;
    }

    public String getTimeNotification() {
        return timeNotification;
    }

    public void setTimeNotification(String timeNotification) {
        this.timeNotification = timeNotification;
    }

    public void setAnsweredQuestionId(String answeredQuestionId){
        this.answeredQuestionId = answeredQuestionId;
    }

    public String getAnsweredQuestionId(){
        return this.answeredQuestionId;
    }
}
