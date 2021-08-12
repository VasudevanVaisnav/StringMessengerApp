package me.discretesolutions.string.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.ApiHandler;
import me.discretesolutions.string.helpers.DBHelper;
import me.discretesolutions.string.helpers.User;

public class chatactivity extends AppCompatActivity {
    TextView name;
    public ImageView back;
    static chatAdapter chatAdapter;
    public EditText typemessage;
    static Context context;
    public static String id,nm;
    static RecyclerView chatsrecycler;
    ArrayList<me.discretesolutions.string.ui.chat> chats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = getApplicationContext();
        typemessage=(EditText)findViewById(R.id.typemsg);
        back = (ImageView)findViewById(R.id.goback);
        name= (TextView)findViewById(R.id.chatnameactivity);
        TextView lastseen = (TextView)findViewById(R.id.lastseen);
        ImageView dp=(ImageView)findViewById(R.id.userimage);
        System.out.println("sasasasa");
        String username = null,lastseenstr = null;
        int dpimg = 0;
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            username=extras.getString("username");
            lastseenstr=extras.getString("no");
            id = lastseenstr;
            nm = username;
            dpimg=extras.getInt("dp");
        }
        name.setText(username);
        lastseen.setText(lastseenstr);
        dp.setImageResource(dpimg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onback();
            }
        });


        typemessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        chats=new ArrayList<>();
        chatsrecycler=(RecyclerView)findViewById(R.id.chatsrecycler);
        setchatslist(lastseenstr);
        setchatadapter();
    }

    private void setchatadapter() {
        chatAdapter = new chatAdapter(chats);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        chatsrecycler.setLayoutManager(layoutManager);
        chatsrecycler.setItemAnimator(new DefaultItemAnimator());
        chatsrecycler.setAdapter(chatAdapter);
        chatsrecycler.scrollToPosition(chats.size()-1);
    }

    public static void updateChatScreen(){
        DBHelper dbHelper = new DBHelper(context);
        String type[] = {"current user","friend"};
        ArrayList<chat> chats = new ArrayList<>();
        if(dbHelper.isTableExists("c"+id)){
            Cursor cursor = dbHelper.getAllMessages(id);
            while (cursor.moveToNext()){
                chats.add(new me.discretesolutions.string.ui.chat(cursor.getString(1),type[cursor.getInt(3)],cursor.getString(5),cursor.getString(6)));
            }
        }
        chatAdapter = new chatAdapter(chats);
//        RecyclerView chatsrecycler=(RecyclerView) findViewById(R.id.chatsrecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        chatsrecycler.setLayoutManager(layoutManager);
        chatsrecycler.setItemAnimator(new DefaultItemAnimator());
        chatsrecycler.setAdapter(chatAdapter);
        chatsrecycler.scrollToPosition(chats.size()-1);
    }

    private void setchatslist(String lastseenstr) {
        id = lastseenstr;
        String type[] = {"current user","friend"};
        System.out.println(name.getText().toString());
        //retrieve chat between current user and friend from database
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if(dbHelper.isTableExists("c"+lastseenstr)){
            Cursor cursor = dbHelper.getAllMessages(lastseenstr);
            while (cursor.moveToNext()){
                chats.add(new me.discretesolutions.string.ui.chat(cursor.getString(1),type[cursor.getInt(3)],cursor.getString(5),cursor.getString(6)));
            }
        }
    }

    public void onback()
    {
        startActivity(new Intent(getApplicationContext(), me.discretesolutions.string.ui.Home.class));
        User.currentChat="0";
        finish();
    }

    public void send(View view) throws JSONException {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String msg = typemessage.getText().toString();
        if(msg.equals(""))
        { }
        else
        {
            ApiHandler.send(id,msg, User.getId(),getApplicationContext(),nm);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(chatactivity.this,Home.class));
        User.currentChat="0";
        finish();
    }
}