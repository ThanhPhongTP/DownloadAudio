package com.example.downloadaudio_ver2.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {


    @GET("oldtest.php?")
    Call<String> getLinkAPI(
            @Query(value = "url")  String url
    );

}
