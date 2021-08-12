package me.discretesolutions.string.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import me.discretesolutions.string.R;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import me.discretesolutions.string.crypto.CryptoHandler;
import me.discretesolutions.string.ui.Home;
import me.discretesolutions.string.ui.chatactivity;
import me.discretesolutions.string.ui.yourcontacts_activity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("+===================-------------------=================");
        System.out.println("+===================-------------------=================");
        System.out.println("+===================-------------------=================");
        System.out.println("+===================-------------------=================");
        System.out.println("+===================-------------------=================");
        Map<String, String> data = remoteMessage.getData();
        System.out.println(data);
        String from = data.get("fromNum");
        String name1 = data.get("fromName");
        String cipherKey = data.get("key");
        String cipherText = data.get("message");
        String mod = User.getPrivateKeyMod();
        String exp = User.getPrivateKeyExp();
        String name = name1;
        try{
            String[] nameArray = yourcontacts_activity.nameArray;
            String[] numArray = yourcontacts_activity.numArray;
            for(int i = 0;i<numArray.length;i++){
                if (numArray[i]==from){
                    name = nameArray[i];
                    break;
                }
            }
        }
        catch (Exception e){
            name = name1;
        }
        try {
            String message = CryptoHandler.assist(cipherKey,cipherText,mod,exp);
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            Date date = new Date();
            String dt = String.valueOf(date.getDate())+"-"+String.valueOf(date.getMonth())+"-"+String.valueOf(date.getYear());
            String tm = String.valueOf(date.getHours())+":"+String.valueOf(date.getMinutes())+":"+String.valueOf(date.getSeconds());
            dbHelper.insertMessage(from,message,name,1,dt,tm);
            System.out.println("////////////////////////////////");
            System.out.println(from);
            System.out.println(User.currentChat);
            if (from.equals(User.currentChat)){
                Intent i = new Intent(getApplicationContext(),chatactivity.class);
                String username = name1;
                String number = from;
                i.putExtra("username",username);
                i.putExtra("no",number);
                i.putExtra("dp",R.drawable.woman);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            else if (User.currentChat=="0"){
                Intent i = new Intent(getApplicationContext(), Home.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            else{
                //Do nothing.
            }
            pushNotification(name,message,tm,from);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushNotification(String name1, String message, String tm, String number) {
        Context ctx = getApplicationContext();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            RemoteViews normalView = new RemoteViews(ctx.getPackageName(), R.layout.notification_collapsed);
            normalView.setTextViewText(R.id.number,name1+"  - "+tm);
            normalView.setTextViewText(R.id.message,message);
            Intent intent = new Intent(ctx, chatactivity.class);
            intent.putExtra("username",name1);
            intent.putExtra("no",number);
            intent.putExtra("dp",R.drawable.woman);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            Notification builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                    .setSmallIcon(R.drawable.woman)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(normalView)
                    .build();
            //.setCustomBigContentView(expandedView)
            notificationManager.notify(1, builder);
        }
        else{
            RemoteViews normalView = new RemoteViews(ctx.getPackageName(), R.layout.notification_collapsed);
            normalView.setTextViewText(R.id.number,name1+"  - "+tm);
            normalView.setTextViewText(R.id.message,message);

            Intent intent = new Intent(ctx, chatactivity.class);
            intent.putExtra("username",name1);
            intent.putExtra("no",number);
            intent.putExtra("dp",R.drawable.woman);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.woman)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(normalView)
                    .build();
            //.setCustomBigContentView(expandedView)
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.notify(Integer.valueOf(number),notification);
        }
    }
}
