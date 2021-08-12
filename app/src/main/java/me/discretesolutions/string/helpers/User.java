package me.discretesolutions.string.helpers;

public class User {
    public static String name;
    public static String id;
    public static String privateKeyMod;
    public static String privateKeyExp;
    public static String notifToken;
    public static String currentChat = "-1";

    public static void load(String Name, String Id, String PrivateKeyExp, String PrivateKeyMod, String NotifToken){
        name = Name;
        id = Id;
        privateKeyExp = PrivateKeyExp;
        privateKeyMod = PrivateKeyMod;
        notifToken = NotifToken;
    }

    public static String getName(){
        return name;
    }

    public static String getId(){
        return id;
    }

    public static String getPrivateKeyMod(){
        return privateKeyMod;
    }

    public static String getPrivateKeyExp(){
        return privateKeyExp;
    }

    public static String getNotifToken(){
        return notifToken;
    }
}
