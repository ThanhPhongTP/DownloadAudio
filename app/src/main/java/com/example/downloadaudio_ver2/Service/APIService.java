package com.example.downloadaudio_ver2.Service;

public class APIService {
    private static final String BASE_URL = "https://downloader28.banabatech.com/";
    public static DataService getService(){
        return APIRetrofitClient.getClient(BASE_URL).create(DataService.class);
    }
}
