package me.discretesolutions.string.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "string.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table chats(chatID TEXT primary key, name TEXT, lastMsg TEXT, sentBy INTEGER, status INTEGER, count INTEGER, time TEXT, date TEXT, param INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists chats");
    }
    public String insertMessageQuery(String id,String message,int msgId,int sentBy,String date, String time){
        String query = "INSERT INTO c"+id+" VALUES("+String.valueOf(msgId)+",'"+message+"','"+id+"',"+String.valueOf(sentBy)+",1,'"+time+"','"+date+"')";
        return query;
    }
    public String createChatRoomQuery(String id){
        String query = "CREATE TABLE "+'c'+id+"(msgId integer PRIMARY KEY, msg text, chatId text, sentBy integer, status integer, time text, date text)";
        return query;
    }
    public String updateChatRoomQuery(String id, String msg, int by, int status, int count, String date, String time){
        SQLiteDatabase db = getReadableDatabase();
        int PARAM = 0;
        Cursor cursor = db.rawQuery("Select MAX(param) FROM chats",null);
        cursor.moveToNext();
        PARAM = cursor.getInt(0);
        String query = "UPDATE chats SET lastMsg = '"+msg+"', sentBy = "+String.valueOf(by)+", status = "+String.valueOf(status)+", count = "+String.valueOf(count+1)+", time = '"+time+"', date = '"+date+"', param = "+String.valueOf(PARAM+1)+" WHERE chatID = '"+id+"'";
        return query;
    }
    public String insertChatRoomQuery(String id,String message,String name,int sentBy,int status,int count,String date, String time){
        SQLiteDatabase db = getReadableDatabase();
        int PARAM = 0;
        Cursor cursor = db.rawQuery("Select * from chats",null);
        if (cursor.getCount()==0){
            PARAM = 0;
        }
        else{
            cursor = db.rawQuery("Select MAX(param) FROM chats",null);
            cursor.moveToNext();
            PARAM = cursor.getInt(0);
        }
        String query = "INSERT INTO chats values('"+id+"','"+name+"','"+message+"',"+String.valueOf(sentBy)+","+String.valueOf(status)+","+String.valueOf(count)+",'"+time+"','"+date+"',"+String.valueOf(PARAM)+")";
        return query;
    }
    public String getChatRoomQuery(String id){
        String query = "Select * from c"+id;
        return query;
    }
    public String getAllChatsQuery(){
        String query = "Select * from chats order by param desc";
        return query;
    }
    public void insertMessage(String id, String message, String name, int sentBy, String date, String time){
        SQLiteDatabase dbr = getReadableDatabase();
        SQLiteDatabase dbw = getWritableDatabase();
        if (isTableExists("chats")){
            if(isTableExists("c"+id) && isChatExists(id)){
                Cursor cursor = dbr.rawQuery("Select count, sentBy, status from chats where chatID = "+id,null);
                cursor.moveToNext();
                int cnt = cursor.getInt(0);
                dbw.execSQL(insertMessageQuery(id,message,cnt+1,sentBy,date,time));
                dbw.execSQL(updateChatRoomQuery(id,message,sentBy,1,cnt+1,date,time));
            }
            else{
                dbw.execSQL(createChatRoomQuery(id));
                dbw.execSQL(insertChatRoomQuery(id,message,name,sentBy,1,1,date,time));
                dbw.execSQL(insertMessageQuery(id,message,1,sentBy,date,time));
            }
        }
        else{
            System.out.println("NO CHATS EXISTS==================PLS CHECK");
        }
        /*
        checkForChatInChats{
            exists:
                getMessageCount
                insertMessageInChatRoom
                updateChats
            else:
                addChatInChats
                createChatRoom
                insertMessageInChatRoom
        }
         */
    }

    private boolean isChatExists(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from chats where chatID = "+id,null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    public Cursor getAllChats(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getAllChatsQuery(),null);
        return cursor;
    }
    public Cursor getAllMessages(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getChatRoomQuery(id),null);
        return cursor;
    }
    public boolean isTableExists(String tableName) {
        boolean isExist = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }
}
/*

create Table chats(chatID TEXT primary key, name TEXT, lastMsg TEXT, sentBy integer, status INTEGER, count INTEGER, time TEXT, date text);
CREATE TABLE c99(msgId integer PRIMARY KEY, msg text, chatId text, sentBy integer, status integer, time text, date text);
INSERT INTO chats values('99','Robert','Robert',1,1,1,'16:32:05','23-04-2021');
INSERT INTO c99 VALUES(1,'Robert','id',1,1,'16:32:05','23-04-2021');
Select * from chats;
Select * from c99;
Select count from chats where chatId = '99';
INSERT INTO c99 VALUES(2,'how are you','99',1,1,'23:55:05','23-04-2021');
UPDATE chats SET lastMsg = 'how are you', sentBy = 1, status = 2, count = 2, time = '23:55:05', date = '23-04-2021' WHERE chatID = '99';
Select * from chats;
Select * from c99;

 */