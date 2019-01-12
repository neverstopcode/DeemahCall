package com.example.dmitry.deemahcall;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String sendName;
    String email;
    String company;
    String contactID;
    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phonenumber;
    EditText inputSearch;
    public  static final int RequestPermissionCode  = 1;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listview1);
        StoreContacts = new ArrayList<String>();
        EnableRuntimePermission();
        GetContactsIntoArrayList();
        arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                        R.layout.contact_items_listview,
                        R.id.textView, StoreContacts
        );
        inputSearch =(EditText)findViewById(R.id.filter);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String number=(String)listView.getItemAtPosition(pos);
                number=number.substring(number.lastIndexOf(": ")+1);
                String gotname=(String)listView.getItemAtPosition(pos);
                gotname=gotname.substring(0,gotname.indexOf(":"));

                //test
                cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +"=?", new String[]{gotname}, null);
                while (cursor.moveToNext()) {
                    sendName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                }
                cursor.close();
                //end of test
        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
                myIntent.putExtra("number", number);
                myIntent.putExtra("name", sendName);
        MainActivity.this.startActivity(myIntent);
                return true;
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MainActivity.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String number=(String)listView.getItemAtPosition(position);
                number=number.substring(number.lastIndexOf(": ")+1);
//                Toast.makeText(getApplicationContext(),
//                        "Number :"+number, Toast.LENGTH_LONG)
//                        .show();
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void GetContactsIntoArrayList(){
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
            StoreContacts.add(name +":" + " " + phonenumber);
        }
        cursor.close();
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS))
        {
            Toast.makeText(MainActivity.this,"Contacts permission need for list Contacts", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,"Access granted.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"Access denied.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}