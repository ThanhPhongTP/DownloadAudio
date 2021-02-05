package com.example.downloadaudio_ver2.Model;

public class Collection {
    int id, idAlbum;
    int imgCollection;
    String sNameCollection;


    public Collection() {
    }

    public Collection(int imgCollection, String sNameCollection) {
        this.imgCollection = imgCollection;
        this.sNameCollection = sNameCollection;
    }

    public Collection(int imgCollection, String sNameCollection, int id, int idAlbum) {
        this.imgCollection = imgCollection;
        this.sNameCollection = sNameCollection;
        this.id = id;
        this.idAlbum = idAlbum;
    }

    public int getImgCollection() {
        return imgCollection;
    }

    public void setImgCollection(int imgCollection) {
        this.imgCollection = imgCollection;
    }

    public String getsNameCollection() {
        return sNameCollection;
    }

    public void setsNameCollection(String sNameCollection) {
        this.sNameCollection = sNameCollection;
    }
}
