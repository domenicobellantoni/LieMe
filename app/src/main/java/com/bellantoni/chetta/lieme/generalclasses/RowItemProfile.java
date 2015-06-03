package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by Domenico on 24/05/2015.
 */
public class RowItemProfile {

    private String question;
    private String nameSurname;
    private String id;
    private int idImg;
    private boolean resultQuestion;

    public RowItemProfile(String question, String nameSurname, String id, int idImg, boolean resultQuestion){
        this.question=question;
        this.nameSurname=nameSurname;
        this.id=id;
        this.idImg=idImg;
        this.resultQuestion=resultQuestion;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getFacebookId() {
        return id;
    }

    public void setFacebookId(String id) {
        this.id = id;
    }

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }

    public boolean getResultQuestion() {
        return resultQuestion;
    }

    public void setResultQuestion(boolean resultQuestion) {
        this.resultQuestion = resultQuestion;
    }
}
