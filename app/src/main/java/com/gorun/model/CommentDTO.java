package com.gorun.model;

public class CommentDTO {

    private int idcomment;
    private int iduser;
    private int idstory;
    private User user;
    private String text;
    private String date;

    public CommentDTO(int idcomment, int iduser, int idstory, User user, String text, String date) {
        this.idcomment = idcomment;
        this.iduser = iduser;
        this.idstory = idstory;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return user.getName() + ": " + text;
    }
}
