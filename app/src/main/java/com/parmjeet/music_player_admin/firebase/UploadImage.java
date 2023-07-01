package com.parmjeet.music_player_admin.firebase;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parmjeet.music_player_admin.DataType;

public class UploadImage {
    UploadImage.OnItemClick onItemClick;
    public void setOnItemClickForFetchData(UploadImage.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public interface OnItemClick {
        void setImage(String data);
    }

    public void uploadImage(String savePath,Uri file, String filenmae, Context context){
        //      final ProgressDialog progressDialog = new ProgressDialog(context);
        //  progressDialog.setTitle("Uploading...");
        //    progressDialog.show();
        final String TAG = "UploadFile";
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(savePath);
        storageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //            progressDialog.dismiss();
                        //   Toast.makeText(context,"call sucess ",Toast.LENGTH_LONG).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DataType dataType = new DataType();
                                dataType.setUri(String.valueOf(uri));
                                dataType.setSonglastsegmant(filenmae);
                                String generatedString =getAlphaNumericString(20);
                                AddSong addSong = new AddSong();
                                String path = "audiosong/"+generatedString;
                                addSong.addUserWork(path,dataType,context,"");
                                addSong.setOnItemClickForFetchData(new AddSong.OnItemClick() {
                                    @Override
                                    public void setStringData(String data) {
                                        onItemClick.setImage("true");
                                    }
                                });
                            }
                        });
                    }

                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"fail photo upload "+e.getMessage(),Toast.LENGTH_LONG).show();
                        //            progressDialog.dismiss();
                        onItemClick.setImage("false");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0*snapshot.getBytesTransferred()/snapshot
                                .getTotalByteCount());
                        //               progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                }) ;
    }
    public String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
