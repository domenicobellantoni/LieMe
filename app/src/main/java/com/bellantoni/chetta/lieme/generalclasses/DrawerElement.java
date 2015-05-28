package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by Domenico on 28/05/2015.
 */
public class DrawerElement {

    private int idImg;
    private String section;

    public DrawerElement(int idImg, String section){
        this.section=section;
        this.idImg=idImg;

    }


    public int getIdImg() {
        return idImg;
    }

    public String getSection() {
        return section;
    }
}

