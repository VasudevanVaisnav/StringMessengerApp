package me.discretesolutions.string.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;

import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.User;

public class ErrorPage extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animDisplay);
        TextView textView = (TextView) findViewById(R.id.errorMessage);
        Bundle bundle = new Bundle();
        int error_code = bundle.getInt("errorCode");
        String extraField = bundle.getString("extra");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        switch (error_code){
            case 0:
                lottieAnimationView.setAnimation(R.raw.user);
                textView.setText("User Not Found");
                intent = new Intent(ErrorPage.this, Home.class);
                break;
            case 1:
                lottieAnimationView.setAnimation(R.raw.offline);
                textView.setText("Unable to reach server");
                intent = new Intent(ErrorPage.this, Home.class);
                break;
            case 2:
                lottieAnimationView.setAnimation(R.raw.errordoing);
                textView.setText("Message sending failed");
                intent = new Intent(ErrorPage.this, Home.class);
                break;
            case 3:
                lottieAnimationView.setAnimation(R.raw.offline);
                textView.setText("Unable to reach server");
                intent = new Intent(ErrorPage.this, login.class);
                break;
            case 4:
                lottieAnimationView.setAnimation(R.raw.user);
                textView.setText("Login Successfull Redirecting in 5 seconds");
                User.currentChat="0";
                intent = new Intent(ErrorPage.this, Home.class);
                break;
            case 5:
                lottieAnimationView.setAnimation(R.raw.errordoing);
                textView.setText("Invalid Credentials");
                intent = new Intent(ErrorPage.this, login.class);
                break;
            case 6:
                lottieAnimationView.setAnimation(R.raw.errordoing);
                textView.setText("Login failed");
                intent = new Intent(ErrorPage.this, login.class);
                break;
            case 7:
                lottieAnimationView.setAnimation(R.raw.user);
                textView.setText("User Not Found");
                intent = new Intent(ErrorPage.this, login.class);
                break;
            default:
                System.out.println("1");
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ErrorPage.this,"Not allowed",Toast.LENGTH_LONG).show();
    }
}