package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by Domenico on 21/07/2015.
 */
public class NotificationItem {

    private int idQuestion;

    public NotificationItem(int idQuestion){

        this.idQuestion=idQuestion ;

    }

    public int getQuestionId() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }
}
