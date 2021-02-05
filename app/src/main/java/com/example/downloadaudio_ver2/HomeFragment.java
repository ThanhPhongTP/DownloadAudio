package com.example.downloadaudio_ver2;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.downloadaudio_ver2.Database.Database;
import com.example.downloadaudio_ver2.Service.APIService;
import com.example.downloadaudio_ver2.Service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.downloadaudio_ver2.Database.Database.database;

public class HomeFragment extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 3;
    private Button btnDownload;
    private EditText edtLink;
    private String sTitle, sThumbnail, sUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Đã thêm vào Download", Toast.LENGTH_SHORT).show();
                getDataFromLink();
//                checkDownload();
            }
        });
    }



    private void getDataFromLink() {
        database = new Database(getContext(), "AudioDownload.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Collection(Id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(1000)," +
                " thumbnail VARCHAR(1000), url LONGTEXT)");
        DataService dataService = APIService.getService();
        dataService.getLinkAPI(edtLink.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        sTitle = jsonObject.getString("title");
                        sThumbnail = jsonObject.getString("thumbnail");
                        JSONArray jsonArray = jsonObject.getJSONArray("links");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(object.getString("type").equals("mp3") &&
                                    object.getString("quality").equals("AUDIO_QUALITY_MEDIUM")){
                                sUrl = object.getString("url");
                                Log.d("sasas", sUrl);
                                break;
                            }
                        }
                        database.insertData(sTitle, sThumbnail, sUrl);
                        checkDownload();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void checkDownload(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String [] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            }
            else {
                startDownload();
            }
        } else {
            startDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startDownload();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownload() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(sUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Audio Download")
                .setDescription("Download file...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, sTitle + ".mp3");
        Log.d("aaas", sTitle);
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null){
            downloadManager.enqueue(request);
        }
    }

    private void setControl(View view) {
        btnDownload = view.findViewById(R.id.btnDownload);
        edtLink = view.findViewById(R.id.edtLink);
    }
}