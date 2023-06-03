package com.parmjeet.music_player_admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import com.parmjeet.music_player_admin.firebase.AddSong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AllSongAdapter extends RecyclerView.Adapter<AllSongAdapter.MyViewHolder>  {
    private Context context;
    public ArrayList<DataType> allData;
    AllSongAdapter.OnItemClick onItemClick;
    public void setOnItemClick(AllSongAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public interface OnItemClick {
        void getPosition(String link) throws IOException; //pass any things

    }
    public AllSongAdapter(Context context, ArrayList<DataType> allData) {
        this.context = context;
        this.allData = allData;
    }

    @NonNull
    @Override
    public AllSongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        AllSongAdapter.MyViewHolder vh = new AllSongAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AllSongAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataType require_detail = allData.get(position);
        String songName = require_detail.getSonglastsegmant();
        String link = require_detail.getUri();

        holder.name.setText(songName);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onItemClick.getPosition(link);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.aprove.setVisibility(View.GONE);
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("ARE YOU SURE YOU WANT TO DELETE SONG")
                        .setTitle("DELETE FILE");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String path = require_detail.getPath();
                        AddSong addSong = new AddSong();
                        addSong.addUserWork(path,null,context,"delete");
                        addSong.setOnItemClickForFetchData(new AddSong.OnItemClick() {
                            @Override
                            public void setStringData(String data) {
                                if(data.equals("true")){
                                    Toast.makeText(context,   "delete sucessfully", Toast.LENGTH_LONG).show();
                                    allData.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, allData.size()-position);

                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        //   Toast.makeText(context, require_detail + " is here", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return allData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,aprove,reject;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.songs);
            aprove = (TextView) itemView.findViewById(R.id.approve);
            reject = (TextView) itemView.findViewById(R.id.reject);
        }
    }
}


