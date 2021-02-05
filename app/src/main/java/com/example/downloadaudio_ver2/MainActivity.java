package com.example.downloadaudio_ver2;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.downloadaudio_ver2.Model.Album;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AllSongFragment.createDataParse {
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private ImageView img_play, img_skip_next, img_shuffle, imgAudio;
    private MediaPlayer mediaPlayer;
    private int length = 0, currentPosition;;
    private boolean checkPlay = false;
    private SharedPreferences sharedPreferences;
    public LinearLayout linear_status;
    private TextView tvNameAudio;
    public ArrayList<Album> albumArrayList;
    private Album album;
    public int allSongLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        checkStatusAudio();

    }

    private void checkStatusAudio() {
        if (sharedPreferences.getBoolean("CHECK_AUDIO_PLAY", true)){
            linear_status.setVisibility(View.VISIBLE);
        }
        else if (!sharedPreferences.getBoolean("CHECK_AUDIO_PLAY", true))
            linear_status.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
//        super.finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CHECK_AUDIO_PLAY", false);
        editor.apply();
        super.onDestroy();
    }

    public void setFragmentAudioList(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(getSupportFragmentManager().findFragmentById(R.id.frame_container));
        fragmentTransaction.add(R.id.frame_container, new CollectionList());
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void setFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setFragment(new HomeFragment());
                break;
            case R.id.nav_collection:
                setFragment(new CollectionFragment());
                break;
            case R.id.nav_setting:
                setFragment(new SettingFragment());
                break;
        }
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private int nRandomItem = 0;
    private Random random = new Random();
    public void randomItem(){
        nRandomItem = random.nextInt(allSongLength);
        Toast.makeText(this, nRandomItem + "", Toast.LENGTH_SHORT).show();
        onDataPass(albumArrayList.get(1).getsNameAlbum(), albumArrayList.get(1).getImgAlbum());
    }


    @Override
    public void onDataPass(String name, String img) {
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//        CollectionList collectionList = (CollectionList) getSupportFragmentManager().findFragmentById(R.id.audio_list);
//        collectionList.btn_shuffle.setVisibility(View.GONE);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(String.valueOf(Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + name + ".mp3")));
            mediaPlayer.prepare();
            setEventStatusAudio(name, img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                img_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                if (currentPosition + 1 < albumArrayList.size()) {
                    onDataPass(albumArrayList.get(currentPosition + 1).getsNameAlbum(), albumArrayList.get(currentPosition + 1).getImgAlbum());
                    currentPosition += 1;
                } else {
                    Toast.makeText(getApplicationContext(), "PlayList Ended", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setEventStatusAudio(String name, String img) {
        linear_status.setVisibility(View.VISIBLE);
        img_play.setImageResource(R.drawable.ic_baseline_pause_24);
        tvNameAudio.setText(name);
        tvNameAudio.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvNameAudio.setSelected(true);
        Picasso.get()
                .load(img)
                .into(imgAudio);
        mediaPlayer = MediaPlayer.create(MainActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + name + ".mp3"));
        mediaPlayer.setLooping(false);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.start();
//        check_play = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CHECK_AUDIO_PLAY", true);
        editor.apply();
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    img_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    img_play.setImageResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });

        img_skip_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition + 1 < albumArrayList.size()) {
                    onDataPass(albumArrayList.get(currentPosition + 1).getsNameAlbum(), albumArrayList.get(currentPosition + 1).getImgAlbum());
                    currentPosition += 1;
                } else {
                    Toast.makeText(getApplicationContext(), "Playlist Ended", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = random.nextInt(allSongLength - 1);
                onDataPass(albumArrayList.get(pos).getsNameAlbum(), albumArrayList.get(pos).getImgAlbum());

            }
        });
    }

    @Override
    public void fullSongList(ArrayList<Album> songList, int position) {
        this.albumArrayList = songList;
        this.currentPosition = position;
//        this.playlistFlag = albumArrayList.size() == allSongLength;
//        this.playContinueFlag = !playlistFlag;
    }

    @Override
    public String queryText() {
        return null;
    }

    @Override
    public void currentSong(Album Album) {
        this.album = Album;
    }



    @Override
    public void getLength(int length) {
        this.allSongLength = length;
//        Toast.makeText(this, allSongLength + "", Toast.LENGTH_SHORT).show();
    }

    private void setControl() {
        mediaPlayer = new MediaPlayer();
        sharedPreferences = getSharedPreferences("AudioDownload", MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.nav_views);
        img_play = findViewById(R.id.img_play);
        img_shuffle = findViewById(R.id.img_shuffle);
        img_skip_next = findViewById(R.id.img_skip_next);
        linear_status = findViewById(R.id.status_audio);
        tvNameAudio = findViewById(R.id.tvNameAudio);
        imgAudio = findViewById(R.id.img_audio);
    }
}