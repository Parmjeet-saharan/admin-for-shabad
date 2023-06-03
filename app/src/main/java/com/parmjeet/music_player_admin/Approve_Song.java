package com.parmjeet.music_player_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parmjeet.music_player_admin.firebase.getPendingSongList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Approve_Song extends AppCompatActivity {
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    MediaPlayer music = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_song);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout = (LinearLayout) findViewById(R.id.liner);
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getPendingSongList fetchAllCommenSong = new getPendingSongList();
        fetchAllCommenSong.getData("Users",Approve_Song.this);
        fetchAllCommenSong.setOnItemClickForFetchData(new getPendingSongList.OnItemClick() {
            @Override
            public void setStringData(String data) throws JSONException {
           //     Toast.makeText(Approve_Song.this, data, Toast.LENGTH_SHORT).show();
                HelperFunction helperFunc = new HelperFunction();
                String encString = helperFunc.replacecharfromstring(data);
                ArrayList<UserSongDataType> allData = helperFunc.stringtojsonconvert(encString);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                PendingSongListAdapter songAdapter = new PendingSongListAdapter(Approve_Song.this, allData);
                recyclerView.setAdapter(songAdapter); // set the Adapter to RecyclerView
                songAdapter.setOnItemClick(new PendingSongListAdapter.OnItemClick() {
                    @Override
                    public void getPosition(String link) throws IOException {
                        Uri uri = Uri.parse(link);
                        if(music.isPlaying()){
                            music.stop();
                            music.release();
                            music = null;
                            music = new MediaPlayer();
                            music.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        }
                        if(! music.isPlaying()){
                            music.setDataSource(Approve_Song.this,uri);
                            music.prepare();
                            music.start();
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(music != null && music.isPlaying()){
            music.stop();
            music.reset();
            music.release();
        }
    }
}