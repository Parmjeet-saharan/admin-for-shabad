package com.parmjeet.music_player_admin.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

public class getPendingSongList {
    getPendingSongList.OnItemClick onItemClick;
    public void setOnItemClickForFetchData(getPendingSongList.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public interface OnItemClick {
        void setStringData(String data) throws JSONException; //pass any things
    }
    public void getData(String completePath, Context context){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(completePath);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = String.valueOf(snapshot.getValue());
                try {
                    onItemClick.setStringData(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               //          Toast.makeText(context,value, Toast.LENGTH_SHORT).show();
                Log.d("somglist", "onDataChange: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
