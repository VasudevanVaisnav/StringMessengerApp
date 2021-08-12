package me.discretesolutions.string.ui;

public class chat {
    String message,msg_type,time,date;

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public chat(String message, String msg_type, String time, String date) {
        this.message = message;
        this.date=date;
        this.msg_type = msg_type;
        this.time = time;
    }
}
