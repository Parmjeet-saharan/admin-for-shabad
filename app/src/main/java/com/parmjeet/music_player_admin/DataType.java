package com.parmjeet.music_player_admin;

import java.io.Serializable;

public class DataType  implements Serializable {
    private String songlastsegmant;
    private String uri;
    private String path;
    private int like;

    public void setLike(int like) {
        this.like = like;
    }

    public int getLike() {
        return like;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setSonglastsegmant(String songlastsegmant) {
        this.songlastsegmant = songlastsegmant;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSonglastsegmant() {
        return songlastsegmant;
    }

    public String getUri() {
        return uri;
    }
}
