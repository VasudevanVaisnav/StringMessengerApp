package me.discretesolutions.string.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.ApiHandler;

public class login extends AppCompatActivity {

    static int backCounter = 2;

    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void showpass(View view) {
        if (view.getId() == R.id.hidepass) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) view).setImageResource(R.drawable.ic_action_show_pass);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Log.d("pass", password.toString() + "abcd");
            } else {
                ((ImageView) view).setImageResource(R.drawable.ic_action_hidepass);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Log.d("pass", password.toString());
            }
        }
    }
    public void login(View view) throws JSONException {
        EditText userEdit = (EditText) findViewById(R.id.mailid);
        EditText passEdit = (EditText) findViewById(R.id.password);
        Context context = getApplicationContext();
        ApiHandler.login(userEdit,passEdit,context);
    }
    public void signup(View view){
        startActivity(new Intent(getApplicationContext(),Signup.class));
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