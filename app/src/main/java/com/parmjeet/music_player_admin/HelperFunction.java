package com.parmjeet.music_player_admin;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HelperFunction {
    public ArrayList<DataType> stringToJsonAllSong(String data )throws JSONException {
        JSONObject obj=new JSONObject();
        ArrayList<DataType> result = new ArrayList<DataType>();
        try {
            data = data.replace("=",":");
            Log.d("requestlist", "setStringData: "+data);
            obj= new JSONObject(data);
            //  obj = obj.getJSONObject("RbA9w4KTaKS3ZkbZdvJyvvXKgg93");
            Iterator iterator = obj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                JSONObject newUser= obj.getJSONObject(key);
                String songName = getrealvaluefromjson(newUser.getString("songlastsegmant"));
                String link = getrealvaluefromjson(newUser.getString("uri"));
                String path = "audiosong/"+key;
                DataType dataType = new DataType();
                dataType.setSonglastsegmant(songName);
                dataType.setUri(link);
                dataType.setPath(path);
                Log.d("requestlist",key+" value "+newUser.getString("uri"));
                result.add(dataType);
            }
            return result;
            //
        } catch (JSONException e) {
            e.printStackTrace();
            ArrayList<DataType> result1 = new ArrayList<DataType>();

            String songName = "SomeThing Went Wrong";
            String link =   "SomeThing Went Wrong";
            DataType dataType = new DataType();
            dataType.setSonglastsegmant(songName);
            dataType.setUri(link);
            result.add(dataType);
            //     textView.setText(data);
            Log.d("requestlist","error is "+ e.getMessage());
            return result1;
            //       Toast.makeText(RequestList.this,"failed to convert json", Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<UserSongDataType> stringtojsonconvert(String data) throws JSONException {
        JSONObject obj=new JSONObject();
        ArrayList<UserSongDataType> result = new ArrayList<UserSongDataType>();
        try {
            data = data.replace("=",":");
            Log.d("requestlist", "setStringData: "+data);
            obj= new JSONObject(data);
            //  obj = obj.getJSONObject("RbA9w4KTaKS3ZkbZdvJyvvXKgg93");
            Iterator iterator = obj.keys();
            String key = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                JSONObject newUserAll= obj.getJSONObject(key);
                JSONObject newUser = newUserAll.getJSONObject("songs");
                Log.d("requestlist",key+" value "+newUser.toString());
                Iterator iterator2 = newUser.keys();
                String key2 = null;
                while (iterator2.hasNext()) {
                    key2 = (String) iterator2.next();
                    JSONObject song = newUser.getJSONObject(key2);
                    Log.d("requestlist",key2+" value 2"+song.toString());
                    String songName = getrealvaluefromjson(song.getString("songlastsegmant"));
                    String link = getrealvaluefromjson(song.getString("uri"));
                    String status = getrealvaluefromjson(song.getString("status"));
                    String path = "Users/"+key+"/songs/"+key2;
                    if(status.equals("pending")) {
                        UserSongDataType dataType = new UserSongDataType();
                        dataType.setSonglastsegmant(songName);
                        dataType.setUri(link);
                        dataType.setStatus("pending");
                        dataType.setPath(path);
                        result.add(dataType);
                    }
                }
            }
            return result;
            //
        } catch (JSONException e) {
            e.printStackTrace();
            ArrayList<UserSongDataType> result1 = new ArrayList<UserSongDataType>();

            String songName = "SomeThing Went Wrong";
            String link =   "SomeThing Went Wrong";
            UserSongDataType dataType = new UserSongDataType();
            dataType.setSonglastsegmant(songName);
            dataType.setUri(link);
            result.add(dataType);
            //     textView.setText(data);
            Log.d("requestlist","error is "+ e.getMessage());
            return result1;
            //       Toast.makeText(RequestList.this,"failed to convert json", Toast.LENGTH_SHORT).show();
        }
    }

    public String replacecharfromstring(String data){
        data=data.replace(", ",",");
        data=data.replace(" ","_");
        data=data.replace("/","'");
        data=data.replace("alt=","alt|");
        data=data.replace("token=","token|");
        data=data.replace("#","@");
        data=data.replace(":","$");
        data=data.replace(";","*");
        Log.d("jsonstring", "setStringData: "+data);

        return data;
    }

    public String getrealvaluefromjson(String data){
        data=  data.replace("_"," ");
        data=  data.replace("'","/");
        data=  data.replace("|","=");
        data=  data.replace("@","#");
        data=  data.replace("$",":");
        data=    data.replace("*",";");
        return data;
    }
}
