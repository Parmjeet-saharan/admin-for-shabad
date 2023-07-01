package com.parmjeet.music_player_admin.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parmjeet.music_player_admin.DataType;

public class AddSong {
    AddSong.OnItemClick onItemClick;
    public void setOnItemClickForFetchData(AddSong.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public interface OnItemClick {
        void setStringData(String data); //pass any things
    }
    public void addUserWork(String path, DataType value, Context context,String singleValue){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(path);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(singleValue!= null && !singleValue.equals("")){
                    mDatabase.setValue(singleValue);
                }
                if(value!=null){
                    mDatabase.setValue(value);
                }
                if(singleValue.equals("delete")){
                    mDatabase.removeValue();
                }

                //    Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show();
                onItemClick.setStringData("true");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                onItemClick.setStringData("false");
            }
        });
    }
}
