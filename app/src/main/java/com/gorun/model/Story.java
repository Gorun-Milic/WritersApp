package com.gorun.model;

public class Story {

    private int idstory;
    private String title;
    private String text;
    private String date;
    private int iduser;
    private int idcategory;

    public Story() {

    }

    public Story(int idstory, String title, String text, String date, int iduser, int idcategory) {
        this.idstory = idstory;
        this.title = title;
        this.text = text;
        this.date = date;
        this.iduser = iduser;
        this.idcategory = idcategory;
    }

    public int getIdstory() {
        return idstory;
    }

    public void setIdstory(int idstory) {
        this.idstory = idstory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }

    @Override
    public String toString() {
        return title;
    }
}
