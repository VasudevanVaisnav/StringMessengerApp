package me.discretesolutions.string.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import me.discretesolutions.string.R;
import me.discretesolutions.string.crypto.CryptoHandler;
import me.discretesolutions.string.ui.ErrorPage;
import me.discretesolutions.string.ui.Signup;
import me.discretesolutions.string.ui.SuccessRegister;
import me.discretesolutions.string.ui.chatactivity;

public class ApiHandler {
    public static void login(EditText userEdit, EditText passEdit, Context context) throws JSONException {
        //Do login if success generate key pair and update and then notif token true
        String un = userEdit.getText().toString();
        String pw = passEdit.getText().toString();
        JSONObject req = new JSONObject();
        req.put("uname",un);
        req.put("pass",pw);
        req.put("mod","dummy");
        req.put("exp","dummy");
        req.put("device","dummy");
        req.put("notifToken","dummy");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://3.131.61.2:2021/client/login",
                req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("error")){
                                String errMsg = response.getString("message");
                                if (errMsg.equalsIgnoreCase("invalid credentials")){
                                    Intent i = new Intent(context, ErrorPage.class);
                                    i.putExtra("errorCode",5);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("extra","Login Successful Redirecting to home in 5 seconds");
                                    context.startActivity(i);
                                }
                                else if(errMsg.equalsIgnoreCase("login failed")){
                                    Intent i = new Intent(context, ErrorPage.class);
                                    i.putExtra("errorCode",6);
                                    i.putExtra("extra","Login Successful Redirecting to home in 5 seconds");
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }
                                else {
                                    Intent i = new Intent(context, ErrorPage.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("errorCode",7);
                                    i.putExtra("extra","Login Successful Redirecting to home in 5 seconds");
                                    context.startActivity(i);
                                }
                                //invalid credentials
                                //login failed
                                //user doesnt exists
                            }
                            else{
                                //Write User and to shared preferences
                                String userId = "";
                                postLogin(userEdit,passEdit,context);
                                Intent i = new Intent(context, ErrorPage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("errorCode",4);
                                i.putExtra("extra","Login Successful Redirecting to home in 5 seconds");
                                context.startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent(context, ErrorPage.class);
                        i.putExtra("errorCode",3);
                        i.putExtra("extra","Unable to reach server");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private static void postLogin(EditText userEdit, EditText passEdit, Context context) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Intent i = new Intent(context, ErrorPage.class);
                            i.putExtra("errorCode",3);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("extra","Unable to reach server");
                            context.startActivity(i);
                        }
                        String address = task.getResult();
                        String device = Build.MODEL;
                        try {
                            JSONObject keyStore = CryptoHandler.assist();
                            String PubMod = keyStore.getString("PublicKeyMod");
                            String PubExp = keyStore.getString("PublicKeyExp");
                            String PriMod = keyStore.getString("PrivateKeyMod");
                            String PriExp = keyStore.getString("PrivateKeyExp");
                            String un = userEdit.getText().toString();
                            String pw = passEdit.getText().toString();
                            JSONObject req = new JSONObject();
                            req.put("uname",un);
                            req.put("pass",pw);
                            req.put("mod",PubMod);
                            req.put("exp",PubExp);
                            req.put("device",device);
                            req.put("notifToken",address);
                            JsonObjectRequest request = new JsonObjectRequest(
                                    Request.Method.POST,
                                    "http://3.131.61.2:2021/client/login",
                                    req,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.getBoolean("error")){
                                                    String errMsg = response.getString("message");
                                                    if (errMsg.equalsIgnoreCase("invalid credentials")){
                                                        //5
                                                    }
                                                    else if(errMsg.equalsIgnoreCase("login failed")){
                                                        //6
                                                    }
                                                    else {
                                                        //7
                                                    }
                                                    //invalid credentials
                                                    //login failed
                                                    //user doesnt exists
                                                }
                                                else{
                                                    //Write User and to shared preferences
                                                    JSONObject userdata = response.getJSONObject("message");
                                                    SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.clear();
                                                    editor.putString("name",userdata.getString("name"));
                                                    editor.putString("id",userdata.getString("mobile"));
                                                    editor.putString("privateKeyMod",PriMod);
                                                    editor.putString("privateKeyExp",PriExp);
                                                    editor.putString("notifToken",address);
                                                    editor.putBoolean("user",true);
                                                    editor.commit();
                                                    User.load(userdata.getString("name"),userdata.getString("mobile"),PriExp,PriMod,address);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Intent i = new Intent(context, ErrorPage.class);
                                            i.putExtra("errorCode",3);
                                            i.putExtra("extra","Unable to reach server");
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                        }
                                    }
                            );
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(request);

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });
        //KeyGen
        //WriteInDB
        //WriteInApp
    }

    public static void signup(Signup signup, JSONObject userData, Context applicationContext) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://3.131.61.2:2021/client/signup",
                userData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("error")){
                                SuccessRegister.signupStatus=false;
                            }
                            else{
                                SuccessRegister.signupStatus=true;
                            }
                            signup.startSuccessRegister();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent(applicationContext, ErrorPage.class);
                        i.putExtra("errorCode",3);
                        i.putExtra("extra","Unable to reach server");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationContext.startActivity(i);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.add(request);
    }


    public static void send(String recId, String msg, String senId,Context applicationContext, String nm) throws JSONException {
        JSONObject req1 = new JSONObject();
        req1.put("toId",recId);
        JsonObjectRequest request1 = new JsonObjectRequest(
                Request.Method.POST,
                "http://3.131.61.2:2021/client/getkey",
                req1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("error")==true){
                                String errResponse = response.getString("message");
                                Intent i = new Intent(applicationContext, ErrorPage.class);
                                i.putExtra("errorCode",0);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("extra","User not found");
                                applicationContext.startActivity(i);
                                //user nhi hai
                            }
                            else{
                                JSONObject keyData = response.getJSONObject("publicKey");
                                System.out.println(keyData);
                                String mod = keyData.getString("mod");
                                String exp = keyData.getString("exp");
                                System.out.println(mod);
                                System.out.println(exp);
                                String[] cipherResult = CryptoHandler.assist(msg,mod,exp);
                                String cipherKey = cipherResult[0];
                                String cipherText = cipherResult[1];
                                JSONObject req2 = new JSONObject();
                                req2.put("from",senId);
                                req2.put("to",recId);
                                req2.put("key",cipherKey);
                                req2.put("message",cipherText);
                                JsonObjectRequest request2 = new JsonObjectRequest(
                                        Request.Method.POST,
                                        "http://3.131.61.2:2021/client/sendmsg",
                                        req2,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject responselat) {
                                                try {
                                                    if (!responselat.getBoolean("error")){
                                                        Date date = new Date();
                                                        String dt = String.valueOf(date.getDate())+"-"+String.valueOf(date.getMonth())+"-"+String.valueOf(date.getYear());
                                                        String tm = String.valueOf(date.getHours())+":"+String.valueOf(date.getMinutes())+":"+String.valueOf(date.getSeconds());
                                                        DBHelper dbHelper = new DBHelper(applicationContext);
                                                        dbHelper.insertMessage(recId,msg,nm,0,dt,tm);
                                                        Intent i = new Intent(applicationContext, chatactivity.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        String username = nm;
                                                        String number = recId;
                                                        i.putExtra("username",username);
                                                        i.putExtra("no",number);
                                                        i.putExtra("dp", R.drawable.woman);
                                                        applicationContext.startActivity(i);
                                                    }
                                                    else{
                                                        Intent i = new Intent(applicationContext, ErrorPage.class);
                                                        i.putExtra("errorCode",2);
                                                        i.putExtra("extra","Unable to send message");
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        applicationContext.startActivity(i);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Intent i = new Intent(applicationContext, ErrorPage.class);
                                                i.putExtra("errorCode",1);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.putExtra("extra","Unable to reach server");
                                                applicationContext.startActivity(i);
                                            }
                                        }
                                );
                                RequestQueue queue = Volley.newRequestQueue(applicationContext);
                                queue.add(request2);
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent(applicationContext, ErrorPage.class);
                        i.putExtra("errorCode",1);
                        i.putExtra("extra","Unable to reach server");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationContext.startActivity(i);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.add(request1);
    }

    public static void checkAddress(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        String currAddress = sharedPreferences.getString("notifToken","error");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(Task<String> task) {
                String newNotifToken = task.getResult();
                if (!task.isSuccessful()){
                    //Internet Error
                }
                if (newNotifToken.equals(currAddress)){
                    JSONObject req = new JSONObject();
                    try {
                        req.put("userid",User.getId());
                        req.put("address",newNotifToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://3.131.61.2:2021/client/refreshtoken",
                            req,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("error")){
                                            //Unable to set
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Unable to connect
                                }
                            }
                    );
                    RequestQueue queue = Volley.newRequestQueue(applicationContext);
                    queue.add(request);
                }
                else{
                    JSONObject req = new JSONObject();
                    try {
                        req.put("userid",User.getId());
                        req.put("address",newNotifToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://3.131.61.2:2021/client/refreshtoken",
                            req,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("error")){
                                            //Unable to set
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Unable to connect
                                }
                            }
                    );
                    RequestQueue queue = Volley.newRequestQueue(applicationContext);
                    queue.add(request);
                }
            }
        });
    }
}
