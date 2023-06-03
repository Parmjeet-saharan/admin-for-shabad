package com.parmjeet.music_player_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parmjeet.music_player_admin.firebase.getPendingSongList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class AllSong extends AppCompatActivity {
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    MediaPlayer music = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_song);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout = (LinearLayout) findViewById(R.id.liner);
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getPendingSongList fetchAllCommenSong = new getPendingSongList();
        fetchAllCommenSong.getData("audiosong",AllSong.this);
        fetchAllCommenSong.setOnItemClickForFetchData(new getPendingSongList.OnItemClick() {
            @Override
            public void setStringData(String data) throws JSONException {
                //     Toast.makeText(Approve_Song.this, data, Toast.LENGTH_SHORT).show();
                HelperFunction helperFunc = new HelperFunction();
                String encString = helperFunc.replacecharfromstring(data);
                ArrayList<DataType> allData = helperFunc.stringToJsonAllSong(encString);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                AllSongAdapter songAdapter = new AllSongAdapter(AllSong.this, allData);
                recyclerView.setAdapter(songAdapter); // set the Adapter to RecyclerView
                songAdapter.setOnItemClick(new AllSongAdapter.OnItemClick() {
                    @Override
                    public void getPosition(String link) throws IOException {
                        Toast.makeText(AllSong.this, link, Toast.LENGTH_SHORT).show();
                        Log.d("allSong","link is "+link);
                        Uri uri = Uri.parse(link);
                        if(music.isPlaying()){
                            Toast.makeText(AllSong.this, "already play song", Toast.LENGTH_SHORT).show();
                            music.stop();
                            music.release();
                            music = null;
                            music = new MediaPlayer();
                            music.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        }
                        if(!music.isPlaying()){
                            Toast.makeText(AllSong.this, "song not playing", Toast.LENGTH_SHORT).show();
                            music.setDataSource(AllSong.this,uri);
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