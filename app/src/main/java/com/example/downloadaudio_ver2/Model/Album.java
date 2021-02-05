package com.example.downloadaudio_ver2.Model;

public class Album {
    public int id;
    String sNameAlbum;
    String imgAlbum;

    String sUrl;
    boolean isSelected;

    public Album(int id, String sNameAlbum, String imgAlbum, String sUrl) {
        this.id = id;
        this.sNameAlbum = sNameAlbum;
        this.imgAlbum = imgAlbum;
        this.sUrl = sUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImgAlbum() {
        return imgAlbum;
    }

    public void setImgAlbum(String imgAlbum) {
        this.imgAlbum = imgAlbum;
    }

    public String getsNameAlbum() {
        return sNameAlbum;
    }

    public void setsNameAlbum(String sNameAlbum) {
        this.sNameAlbum = sNameAlbum;
    }
}
