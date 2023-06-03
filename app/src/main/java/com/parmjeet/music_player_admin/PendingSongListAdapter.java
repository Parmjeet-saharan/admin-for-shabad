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

public class PendingSongListAdapter extends RecyclerView.Adapter<PendingSongListAdapter.MyViewHolder>  {
    private Context context;
    public  ArrayList<UserSongDataType> allData;
    PendingSongListAdapter.OnItemClick onItemClick;
    public void setOnItemClick(PendingSongListAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public interface OnItemClick {
        void getPosition(String link) throws IOException; //pass any things

    }
    public PendingSongListAdapter(Context context, ArrayList<UserSongDataType> allData) {
        this.context = context;
        this.allData = allData;
    }

    @NonNull
    @Override
    public PendingSongListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        PendingSongListAdapter.MyViewHolder vh = new PendingSongListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull PendingSongListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserSongDataType require_detail = allData.get(position);
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
        holder.aprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("ARE YOU SURE YOU WANT TO APPROVE SONG")
                        .setTitle("APPROVE FILE");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String path = require_detail.getPath()+"/status";
                        AddSong addSong = new AddSong();
                        addSong.addUserWork(path,null,context,"approve");
                        addSong.setOnItemClickForFetchData(new AddSong.OnItemClick() {
                            @Override
                            public void setStringData(String data) {
                                if(data.equals("true")){
                                    Toast.makeText(context,   "approve sucessfully", Toast.LENGTH_LONG).show();
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

