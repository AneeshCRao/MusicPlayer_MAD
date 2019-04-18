package com.example.musicplayer;

public class YTVideo {
    String track,album,artist,id;

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getTrack() {
        return track;
    }
}
