package com.gorun.model;

public class Remember {

    private int idremember;
    private int iduser;
    private int idstory;

    public Remember(int idremember, int iduser, int idstory) {
        this.idremember = idremember;
        this.iduser = iduser;
        this.idstory = idstory;
    }

    public int getIdremember() {
        return idremember;
    }

    public void setIdremember(int idremember) {
        this.idremember = idremember;
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

    @Override
    public String toString() {
        return "Remember{" +
                "idremember=" + idremember +
                ", iduser=" + iduser +
                ", idstory=" + idstory +
                '}';
    }
}
