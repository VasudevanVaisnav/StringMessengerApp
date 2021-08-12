package me.discretesolutions.string.ui;

public class chatfriends {
    private String friendname,msgtime,nmsgs,msgcontent,number;
    private int dp,notifycircle;

    public chatfriends(String friendname, String msgtime, String nmsgs, String msgcontent, int dp, int notifycircle, String number) {
        this.friendname = friendname;
        this.msgtime = msgtime;
        this.number = number;
        this.nmsgs = nmsgs;
        this.msgcontent = msgcontent;
        this.dp = dp;
        this.notifycircle = notifycircle;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(String msgtime) {
        this.msgtime = msgtime;
    }

    public String getNmsgs() {
        return nmsgs;
    }

    public void setNmsgs(String nmsgs) {
        this.nmsgs = nmsgs;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }

    public int getDp() {
        return dp;
    }

    public void setDp(int dp) {
        this.dp = dp;
    }

    public int getNotifycircle() {
        return notifycircle;
    }

    public void setNotifycircle(int notifycircle) {
        this.notifycircle = notifycircle;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
