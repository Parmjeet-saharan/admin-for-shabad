package com.parmjeet.music_player_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parmjeet.music_player_admin.firebase.UploadImage;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button pick,upload,approve,allSong;
    Uri uri;
    String name;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pick = (Button) findViewById(R.id.pick);
        upload = (Button) findViewById(R.id.upload);
        approve = (Button) findViewById(R.id.approve);
        allSong = (Button) findViewById(R.id.gerenal_song);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(uri != null){
                      UploadImage uploadImage = new UploadImage();
                      uploadImage.uploadImage("audio/"+name,uri,name,MainActivity.this);
                      uploadImage.setOnItemClickForFetchData(new UploadImage.OnItemClick() {
                          @Override
                          public void setImage(String data) {
                              Toast.makeText(MainActivity.this, "file get uploaded sucessfully",
                                      Toast.LENGTH_SHORT).show();
                          }
                      });
                  }else{
                      Toast.makeText(MainActivity.this, "please select file first",
                              Toast.LENGTH_SHORT).show();
                  }
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this,Approve_Song.class);
                  startActivity(intent);
            }
        });
        allSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllSong.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
                 uri = data.getData();
                 name = getRealPathFromUri(uri);
                Log.d("filepath","path is "+name);
                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getRealPathFromUri(Uri contentURI) {
        Cursor returnCursor = getContentResolver().query(contentURI, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String result = returnCursor.getString(nameIndex);
        return result;
    }
}