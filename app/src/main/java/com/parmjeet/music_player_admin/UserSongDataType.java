package com.parmjeet.music_player_admin;

import java.io.Serializable;

public class UserSongDataType  implements Serializable {
    private String songlastsegmant;
    private String uri;
    private String status;
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
