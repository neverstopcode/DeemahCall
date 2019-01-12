package com.example.dmitry.deemahcall;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
String number,name,company,email,id;
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            number= null;
            name=null;

        } else {
            number= extras.getString("number");
            name= extras.getString("name");

        }
        Cursor cursor;
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +"=?", new String[]{name}, null);
        while (cursor.moveToNext()) {
            id =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//            img=(ImageView)findViewById(R.id.imageView);
//            img.setImageBitmap(uriToBitmap(this,thumbnailByContactId(this,id)));
        }
        cursor.close();
        TextView tvid=findViewById(R.id.textView6);
        TextView tvname=findViewById(R.id.textView7);
        TextView tvnumber=findViewById(R.id.textView8);
        tvid.setText(id);
        tvname.setText(name);
        tvnumber.setText(number);
    }
    public void Call(View v){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }
//
//    public static final String thumbnailByContactId(Context ctx, String contactId) {
//        String uri = null;
//        if (contactId != null) {
//            ContentResolver cr = ctx.getContentResolver();
//            String[] projection = new String[] { ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
//                    ContactsContract.CommonDataKinds.Photo.PHOTO_URI };
//            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone._ID + " = ?",
//                    new String[] { contactId }, null);
//            String imageUri = null;
//            String thumbnailUri = null;
//            while (cur.moveToNext()) {
//                imageUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
//                thumbnailUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
//            }
//            uri = thumbnailUri != null ? thumbnailUri : imageUri;
//        }
//        return uri;
//    }
//
//
//    public static final Bitmap uriToBitmap(Context ctx, String uri) {
//        Bitmap bitmap = null;
//        if (uri != null) {
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.parse(uri));
//            } catch (IOException e) {
//                // Do nothing
//                e.printStackTrace();
//            }
//        }
//        return(bitmap);
//    }

}
