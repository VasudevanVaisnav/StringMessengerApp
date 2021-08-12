package me.discretesolutions.string.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.ApiHandler;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

public class Signup extends AppCompatActivity {
    static int backCounter = 2;
    public EditText mailid, password, confirmpassword, name, mobileno;
    public String value = "", cvalue = "", clgvalue = "", yearvalue = "", deptvalue = "";
    public LinearLayout  namelayout;
    ImageView hidepass, hidepass2;
    public Button register;
    public RadioButton rb;
    public RelativeLayout signuplay;
    public ConstraintLayout successanimelay;
    public TextView registered, redirect;
    public ScrollView scrollView;
    public RadioGroup gendergrp;
    int f1 = 0, f = 0;
    public int s = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //  Toast.makeText(getApplicationContext(),"U R IN SIGNUP",Toast.LENGTH_LONG).show();
        scrollView = (ScrollView) findViewById(R.id.signup);
        mailid = (EditText) findViewById(R.id.mail);
        name = (EditText) findViewById(R.id.entername);
        hidepass = (ImageView) findViewById(R.id.hidepass1);
        hidepass2 = (ImageView) findViewById(R.id.hidepass2);
        signuplay = (RelativeLayout) findViewById(R.id.signuplay);
        register = (Button) findViewById(R.id.registerbutton);
        mobileno = (EditText) findViewById(R.id.mobileno);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        password = (EditText) findViewById(R.id.passwordin);
        namelayout = (LinearLayout) findViewById(R.id.namelayout);


        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    name.setBackground(getResources().getDrawable(R.drawable.purpback));
                    f1 = 1;
                }
                if (!hasFocus) {
                    name.setBackground(getResources().getDrawable(R.drawable.rect));
                    f1 = 0;
                }
            }
        });



        mailid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearF(3);
                if (hasFocus) {
                    mailid.setBackground(getResources().getDrawable(R.drawable.purpback));
                    checkvalid(3);
                    f1 = 2;
                }
                if (!hasFocus) {
                    mailid.setBackground(getResources().getDrawable(R.drawable.rect));
                    f1 = 0;
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearF(4);
                if (hasFocus) {
                    password.setBackground(getResources().getDrawable(R.drawable.purpback));
                    f1 = 3;
                    checkvalid(5);
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //passwordtoggle
                            hidepass.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            value = s.toString();
                            Log.d("log", value);
                        }
                    });

                }
                if (!hasFocus) {
                    f1 = 0;
                }
            }
        });

        confirmpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    clearF(5);
                    password.setBackground(getResources().getDrawable(R.drawable.rect));
                    confirmpassword.setBackground(getResources().getDrawable(R.drawable.purpback));

                    f1 = 4;
                    if (value.length() >= 6 || value.length() == 0) {
                        f = 1;
                        checkvalid(6);
                        confirmpassword.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                hidepass2.setVisibility(View.VISIBLE);
                            }

                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void afterTextChanged(Editable s) {
                                cvalue = s.toString();
                                Log.d("cpass", cvalue + "-----" + value);
                            }
                        });
                    } else if (value != "" & value.length() < 6) {
                        password.setError("Password should have atleast 6 characters");
                        password.requestFocus();
                    }


                }
                if (!hasFocus) {
                    f1 = 0;
                }
            }
        });
        mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    mobileno.setBackground(getResources().getDrawable(R.drawable.purpback));
                    clearF(7);
                    confirmpassword.setBackground(getResources().getDrawable(R.drawable.rect));
                    Log.d("mob", "akjgsdja");
                    f1 = 5;
                    namelayout.clearFocus();
                    checkvalid(7);
                }
                if (!hasFocus) {
                    mobileno.setBackground(getResources().getDrawable(R.drawable.rect));
                    f1 = 0;
                    if (mobileno.getText().length() < 10) {
                        mobileno.setError("Please enter valid phone number");
                    }
                }
            }
        });



    }

    public int checkvalid(int flag) {
        int i = 1, j = 0;
        while (i < flag) {
            switch (i) {
                case 1:
                    if (name.getText().length() == 0) {
                        if (j == 0) {
                            j = i;
                        }
                        name.setError("This is required field");
                    }
                    break;
                case 2:
                    if (mailid.getText().length() == 0) {
                        if (j == 0) {
                            j = i;
                        }
                        mailid.setError("This is required field");
                    }
                        else
                        {
                            Boolean status = Patterns.EMAIL_ADDRESS.matcher((CharSequence) mailid.getText().toString()).matches();
                            if(!status)
                            {
                                if(j==0)
                                {
                                    j=i;
                                }
                                mailid.setError("Enter valid email Id");
                                Log.d("matches", "checkvalid: "+status);
                            }
                        }
                    break;
                case 3:
                    if (value.equals("")) {
                        if (j == 0) {
                            j = i;
                        }
                        hidepass.setVisibility(View.INVISIBLE);
                        password.setError("Fill this field first");
                        if (f == 1)
                            password.requestFocus();
                    }
                    break;

                case 4:
                    if (cvalue.equals("")) {
                        if (j == 0) {
                            j = i;
                        }
                        hidepass2.setVisibility(View.INVISIBLE);
                        confirmpassword.setError("This is required field");
                    }
                    break;

                case 5:
                    if (mobileno.getText().length() == 0 | mobileno.getText().length() < 10) {
                        if (j == 0) {
                            j = i;
                        }
                        if (mobileno.getText().length() == 0)
                            mobileno.setError("This is required field");
                        if (mobileno.getText().length() != 0 & mobileno.getText().length() < 10) {
                            mobileno.setError("Please enter a valid phone number");
                        }
                    }
                    break;
            }
            i++;
        }
        return j;
    }

    public int checkpass() {

        if (!(value.equals(cvalue))) {
            //confirmpasslay.setPasswordVisibilityToggleEnabled(false);
            hidepass2.setVisibility(View.INVISIBLE);
            confirmpassword.setError("Passwords didn't match");
            confirmpassword.requestFocus();
            return 0;
        }
        return 1;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void register(View view) {
        clearF(10);
        password.clearFocus();
        confirmpassword.clearFocus();
        mobileno.clearFocus();
        int valid = checkvalid(10);
        Log.d("Valid", valid + "");
        if (valid != 0) {
            switch (valid) {
                case 1:
                    name.requestFocus();
                    Log.d("Passs", "in name");
                    break;
                case 2:
                    mailid.requestFocus();
                    break;

                case 3:
                    password.requestFocus();
                    break;

                case 4:
                    confirmpassword.requestFocus();
                    break;

                case 5:
                    mobileno.requestFocus();
                    break;
            }
        } else {
            Log.d("Success", "sdasa");
            signuplay.clearFocus();
            name.clearFocus();
            if (checkpass() == 1) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                setenabled();
                String Name = name.getText().toString().trim();
                String Email = mailid.getText().toString().trim().toLowerCase();
                String Password = password.getText().toString().trim();
                String PNO = mobileno.getText().toString().trim();
                org.json.JSONObject userData = new org.json.JSONObject();
                try {
                    userData.put("name", Name);
                    userData.put("uname", Email);
                    userData.put("pass", Password);
                    userData.put("pno", PNO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiHandler.signup(Signup.this, userData, getApplicationContext());
            }

        }
    }

    public int clearF(int flag) {
        for (int i = 1; i < 10; i++) {
            if (i != flag) {
                switch (flag) {
                    case 1:
                        name.setBackground(getResources().getDrawable(R.drawable.rect));
                        break;
                    case 2:
                        mailid.setBackground(getResources().getDrawable(R.drawable.rect));
                        break;
                    case 3:
                        break;
                    case 4:
                        confirmpassword.setBackground(getResources().getDrawable(R.drawable.rect));
                        break;
                    case 5:
                        mobileno.setBackground(getResources().getDrawable(R.drawable.rect));
                        break;
                    case 6:
                        Log.d("yess", "switch");
                        break;

                }
            }
        }
        return 0;
    }

    public void startSuccessRegister() {
        Intent intent = new Intent(Signup.this, SuccessRegister.class);
        startActivity(intent);
        finish();
    }

    void setenabled() {
        name.setEnabled(false);
        mailid.setEnabled(false);
        password.setEnabled(false);
        confirmpassword.setEnabled(false);
        mobileno.setEnabled(false);
    }

    public void showpass(View view) {
        if (view.getId() == R.id.hidepass1) {
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

    public void showcpass(View view) {
        if (view.getId() == R.id.hidepass2) {
            if (confirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) view).setImageResource(R.drawable.ic_action_show_pass);
                confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Log.d("pass", password.toString() + "abcd");
            } else {
                ((ImageView) view).setImageResource(R.drawable.ic_action_hidepass);
                confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Log.d("pass", confirmpassword.toString());
            }
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
