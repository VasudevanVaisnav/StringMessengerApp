package me.discretesolutions.string.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.DBHelper;
import me.discretesolutions.string.helpers.User;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    public static int backCounter = 2;
    public static Context context;
    public EditText search_participant;
    static private ArrayList<me.discretesolutions.string.ui.chatfriends> chatfriends,chatfriends2;
    private RecyclerView recyclerView;
    private chatfriendadapter.chatclicklistener chatclicklistener;
    static chatfriendadapter adapter;
    ArrayList<me.discretesolutions.string.ui.chatfriends> filteredchats;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView textView = (TextView) findViewById(R.id.heytext);
        String txt = "Hey, "+ User.getName();
        context = getApplicationContext();
        textView.setText(txt);
        search_participant=(EditText)findViewById(R.id.search_edit);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //Go to contacts to chat contacts
                startActivity(new Intent(getApplicationContext(), me.discretesolutions.string.ui.yourcontacts_activity.class));
            }
        });
        search_participant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if(b)
                {
                    fab.setVisibility(View.INVISIBLE);
                    search_participant.clearFocus();
                }
                else
                {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        search_participant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filterChats(s.toString());
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.chatview);
        chatfriends=new ArrayList<>();
        chatfriends.clear();
        setfriendslist();
        filteredchats = chatfriends;
        setadapter();
    }
//me.discretesolutions.string.ui.chatfriends
    private void filterChats(String s) {

        for(me.discretesolutions.string.ui.chatfriends item : chatfriends) {
            if(item.getFriendname().toLowerCase().contains(s.toLowerCase()))
            {
                filteredchats.add(item);
            }
        }
        adapter.filterchatlist(filteredchats);


    }

    private void setadapter() {
        setOnclicklistener();
        adapter = new chatfriendadapter(chatfriends,chatclicklistener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnclicklistener() {
        chatclicklistener = new chatfriendadapter.chatclicklistener() {
            @Override
            public void onClick(View v, int position) {
                User.currentChat = filteredchats.get(position).getNumber();
                Intent i = new Intent(getApplicationContext(),chatactivity.class);
                String username = filteredchats.get(position).getFriendname();
                String number = filteredchats.get(position).getNumber();
                i.putExtra("username",username);
                i.putExtra("no",number);
                i.putExtra("dp",R.drawable.woman);
                startActivity(i);
            }
        };
    }

    private void setfriendslist() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        chatfriends.clear();
        Cursor cursor = dbHelper.getAllChats();
        while (cursor.moveToNext()){
            chatfriends.add(new me.discretesolutions.string.ui.chatfriends(cursor.getString(1),cursor.getString(6),String.valueOf(cursor.getInt(5)),cursor.getString(2),R.drawable.man,R.id.noticationcircle,cursor.getString(0)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setfriendslist();
    }

    @Override
    public void onBackPressed() {
        backCounter-=1;
        if (backCounter==0){
            System.exit(0);
        }
        Toast.makeText(getApplicationContext(),"Press Back Again To Exit",Toast.LENGTH_LONG).show();
    }
    public static void refreshList(){
        DBHelper dbHelper = new DBHelper(context);
        chatfriends.clear();
        Cursor cursor = dbHelper.getAllChats();
        while (cursor.moveToNext()){
            chatfriends.add(new me.discretesolutions.string.ui.chatfriends(cursor.getString(1),cursor.getString(6),String.valueOf(cursor.getInt(5)),cursor.getString(2),R.drawable.man,R.id.noticationcircle,cursor.getString(0)));
        }
        adapter.notifyDataSetChanged();
    }
}