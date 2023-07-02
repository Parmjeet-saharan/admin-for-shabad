package com.parmjeet.music_player_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parmjeet.music_player_admin.firebase.getPendingSongList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Approve_Song extends AppCompatActivity {
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    ArrayList<UserSongDataType> allData;
    PendingSongListAdapter songAdapter;
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
                allData = helperFunc.stringtojsonconvert(encString);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                 songAdapter = new PendingSongListAdapter(Approve_Song.this, allData);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search_iteam = menu.findItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search){
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // inside on query text change method we are
                    // calling a method to filter our recycler view.
                    filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
    public void filter(String text){
        ArrayList<UserSongDataType> filteredlist = new ArrayList<UserSongDataType>();
        for(UserSongDataType dataType : allData){
            if(dataType.getSonglastsegmant().toLowerCase().contains(text.toLowerCase())){
                //        Toast.makeText(this, " Found.."+dataType.getSonglastsegmant(), Toast.LENGTH_SHORT).show();
                filteredlist.add(dataType);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            //     Toast.makeText(this, "total match "+filteredlist.size(), Toast.LENGTH_SHORT).show();
            songAdapter.filterList(filteredlist);
        }
    }
}