package me.discretesolutions.string.ui;

public class yourcontacts {
    String username,status;
    String number;
    int dpimg;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public yourcontacts(String username, String status, int dpimg, String no) {
        this.username = username;
        this.status = status;
        this.dpimg = dpimg;
        this.number = no;

        // [0:n], [a,b]
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public int getDpimg() {
        return dpimg;
    }

    public void setDpimg(int dpimg) {
        this.dpimg = dpimg;
    }
}
