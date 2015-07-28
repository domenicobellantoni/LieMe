package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by Domenico on 28/07/2015.
 */
public class ItemHome {


    private String question;
    private String nameFrom;
    private String nameTo;
    private String idFrom;
    private String idTo;
    private boolean resultQuestion;
    private int idImg;

    public ItemHome(String question,String nameFrom, String nameTo, String idFrom, String idTo, int idImg, boolean resultQuestion){
        this.question=question;
        this.nameFrom= nameFrom;
        this.nameTo=nameTo;
        this.idFrom=idFrom;
        this.idTo=idTo;
        this.idImg=idImg;
        this.resultQuestion=resultQuestion;

    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public boolean isResultQuestion() {
        return resultQuestion;
    }

    public void setResultQuestion(boolean resultQuestion) {
        this.resultQuestion = resultQuestion;
    }

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }
}
