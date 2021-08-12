package me.discretesolutions.string.ui;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.ApiHandler;
import me.discretesolutions.string.helpers.MyFirebaseMessagingService;
import me.discretesolutions.string.helpers.User;

public class Splash extends AppCompatActivity {

    static int backCounter = 2;
    static int activityId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4500);
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)){
                        //Move further
                    } else {
                        ActivityCompat.requestPermissions(Splash.this,new String[] {Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET},101);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ActivityStart(activityId);
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getApplicationContext(),Splash.class));
            }
        });
        if (checkLogin(Splash.this)){
            //ApiHandler.UpdateNotifToken(getApplicationContext());
            //Redirect to Home
            activityId = 2;
            ApiHandler.checkAddress(getApplicationContext());
        }
        else{
            activityId = 1;
        }
    }

    private void ActivityStart(int activityId) {
        if (activityId==1){
            startActivity(new Intent(getApplicationContext(),login.class));
        }
        else{
            User.currentChat = "0";
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
    }

    private boolean checkLogin(Context context) {
        SharedPreferences s = context.getSharedPreferences("user",MODE_PRIVATE);
        if (s.getBoolean("user",false)==true){
            //
            User.load(s.getString("name","error"),s.getString("id","error"),s.getString("privateKeyExp","error"),s.getString("privateKeyMod","error"),s.getString("notifToken","error"));
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        backCounter-=1;
        if (backCounter==0){
            System.exit(0);
        }
        Toast.makeText(getApplicationContext(),"Press Back Again To Exit",Toast.LENGTH_LONG).show();
    }
}