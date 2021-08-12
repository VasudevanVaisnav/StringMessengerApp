package me.discretesolutions.string.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import me.discretesolutions.string.R;
import me.discretesolutions.string.helpers.User;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class ContactModel {
    public String id;
    public String name;
    public String mobileNumber;
    public Bitmap photo;
    public Uri photoURI;
}


public class yourcontacts_activity extends AppCompatActivity implements me.discretesolutions.string.ui.yourcontactsAdapter.contactclick{
    private ArrayList<me.discretesolutions.string.ui.yourcontacts> yourcontacts;
    private ArrayList<me.discretesolutions.string.ui.yourcontacts> filteredcontacts;
    private RecyclerView contactlist;
    public static ArrayList<yourcontacts> listContacts;
    static Boolean fetched = false;
    EditText search_contact;
    public static String[] nameArray,numArray;
    me.discretesolutions.string.ui.yourcontactsAdapter yourcontactsAdapter;
    private me.discretesolutions.string.ui.yourcontactsAdapter.contactclick contactclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourcontacts_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        search_contact=(EditText)findViewById(R.id.search_contact) ;
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshContacts);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetched = false;
                startActivity(new Intent(yourcontacts_activity.this,yourcontacts_activity.class));
            }
        });
        search_contact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        search_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    setContactFilter(s.toString());
            }
        });
        //recycler content
        contactlist=(RecyclerView)findViewById(R.id.contactslist);
        yourcontacts=new ArrayList<>();
        if (!fetched){
            setcontactlist();
        }
        yourcontacts = listContacts;
        filteredcontacts = yourcontacts;
        setAdapter();
    }

    private void setContactFilter(String s) {
        filteredcontacts = new ArrayList<>();
        for(me.discretesolutions.string.ui.yourcontacts item: yourcontacts)
        {
            if(item.getUsername().toLowerCase().contains(s.toLowerCase()))
            {
                filteredcontacts.add(item);
            }
        }
//        yourcontacts.clear();
        yourcontactsAdapter.filtercontactlist(filteredcontacts);

    }

    private void setAdapter() {

        yourcontactsAdapter = new me.discretesolutions.string.ui.yourcontactsAdapter(yourcontacts,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contactlist.setLayoutManager(layoutManager);
        contactlist.setItemAnimator(new DefaultItemAnimator());
        contactlist.setAdapter(yourcontactsAdapter);
    }

    private void setOnclicklistener() {
//        contactclick = new yourcontactsAdapter.contactclick() {
//            @Override
//            public void onClick(View v, int position) {
//                Intent i = new Intent(getApplicationContext(),chatactivity.class);
//                i.putExtra("username",yourcontacts.get(position).getUsername());
//                i.putExtra("lastseen","position "+position);
//                i.putExtra("dp",yourcontacts.get(position).getDpimg());
//                startActivity(i);
//            }
//        };

    }

    private void setcontactlist() {

        List<ContactModel> list = new ArrayList<>();
        Context ctx = getApplicationContext();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        info.photo = photo;
                        info.photoURI= pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        Map<String,String> contactsList = new HashMap<String, String>();
        String num;
        for (int j=0;j<list.size();j++){
            String value = contactsList.get(list.get(j).mobileNumber);
            if (value == null) {
                num=list.get(j).mobileNumber;
                while(num.contains(" ")){
                    num = num.replace(" ","");
                }
                while(num.contains("-")){
                    num = num.replace("-","");
                }
                if (num.length()<10){
                    continue;
                }
                num = num.substring(num.length()-10,num.length());
                contactsList.put(num, list.get(j).name);
            }
        }
        System.out.println("------------------------------------------------------------------");
        String[] numArr = contactsList.keySet().toArray(new String[contactsList.keySet().size()]);
        String[] names = contactsList.values().toArray(new String[contactsList.values().size()]);

//        Map<String,String> map = new HashMap<>();
//        for (int i=0;i<names.length;i++){
//            map.put(names[i],numArr[i]);
//        }
//        Arrays.sort(names);
//        String result = Arrays.toString(names);
//        System.out.println("_______________________________\n_______________________________\n_______________________________\n_______________________________\n_______________________________\n_______________________________\n");
//        System.out.println(result);
//        System.out.println(map);
        TextView textView = (TextView) findViewById(R.id.ncontacts);
        String cnt = String.valueOf(contactsList.size())+" Contacts";
        textView.setText(cnt);
        nameArray = names;
        numArray = numArr;
        for(int i=0;i<contactsList.size();i++){
            yourcontacts.add(new yourcontacts(names[i],"Hola, I am on String",R.drawable.man,numArr[i]));
        }
        listContacts = yourcontacts;
        fetched = true;
        System.out.println("=====================================================================");
    }

    @Override
    public void onClick(int position) {
        System.out.println("position"+position);
        Intent i = new Intent(getApplicationContext(),chatactivity.class);
        String username = filteredcontacts.get(position).getUsername();
        String number = filteredcontacts.get(position).getNumber();
        i.putExtra("username",username);
        i.putExtra("no",number);
        i.putExtra("dp",R.drawable.woman);
        User.currentChat=filteredcontacts.get(position).getNumber();
        startActivity(i);
                //apicall(number,username);startActivity(i);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(yourcontacts_activity.this,Home.class));
        User.currentChat="0";
        finish();
    }
}