package me.discretesolutions.string.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import me.discretesolutions.string.R;


public class SuccessRegister extends AppCompatActivity {
    public TextView registered, redirect, registeragain;
    public static boolean signupStatus;
    public LottieAnimationView fail, success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_success_register);
        registered = (TextView) findViewById(R.id.Registeredtext);
        redirect = (TextView) findViewById(R.id.Redirectingtext);
        success = (LottieAnimationView) findViewById(R.id.sucessreg);
        fail = (LottieAnimationView) findViewById(R.id.failreg);
        registeragain = (TextView) findViewById(R.id.Registeragaintext);
        Log.d("signupstatus", String.valueOf(signupStatus));
        if (signupStatus)
            onsuccess();
        else
            onfail();


    }

    public void onsuccess() {
        Handler han = new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                success.setVisibility(View.VISIBLE);
                MediaPlayer ring = MediaPlayer.create(SuccessRegister.this, R.raw.successaudio);
                ring.start();
            }
        }, 2000);
        //maillay.setVisibility(View.INVISIBLE);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                registered.setText("Signed Up Successfully");
                registered.setVisibility(View.VISIBLE);
            }
        }, 3000);
        Handler x = new Handler();
        x.postDelayed(new Runnable() {
            @Override
            public void run() {
                redirect.setVisibility(View.VISIBLE);
            }
        }, 5000);
        Handler y = new Handler();
        y.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SuccessRegister.this, login.class);
                startActivity(intent);
                finish();
            }
        }, 8000);
    }

    public void onfail() {
        Handler han = new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                fail.setVisibility(View.VISIBLE);
            }
        }, 2000);
        //maillay.setVisibility(View.INVISIBLE);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                registered.setText("Failed to Signup");
                registeragain.setVisibility(View.VISIBLE);
                registered.setVisibility(View.VISIBLE);
            }
        }, 3000);
        Handler x = new Handler();
        x.postDelayed(new Runnable() {
            @Override
            public void run() {
                redirect.setVisibility(View.VISIBLE);
            }
        }, 5000);
        Handler y = new Handler();
        y.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SuccessRegister.this, login.class);
                startActivity(intent);
                finish();
            }
        }, 8000);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(SuccessRegister.this,"Not allowed",Toast.LENGTH_LONG).show();
    }
}