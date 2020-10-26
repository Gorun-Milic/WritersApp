package com.gorun.model;

public class Comment {

    private int idcomment;
    private int iduser;
    private int idstory;
    private String text;
    private String date;

    public Comment() {

    }

    public Comment(int idcomment, int iduser, int idstory, String text, String date) {
        this.idcomment = idcomment;
        this.iduser = iduser;
        this.idstory = idstory;
        this.text = text;
        this.date = date;
    }

    public int getIdcomment() {
        return idcomment;
    }

    public void setIdcomment(int idcomment) {
        this.idcomment = idcomment;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdstory() {
        return idstory;
    }

    public void setIdstory(int idstory) {
        this.idstory = idstory;
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

    @Override
    public String toString() {
        return text;
    }
}
