package com.example.downloadaudio_ver2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadaudio_ver2.Adapter.AlbumAdapter;
import com.example.downloadaudio_ver2.Model.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class CollectionList extends Fragment implements AllSongFragment.createDataParse{
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    ArrayList<Album> albumArrayList;
    ArrayList<Album> album_Selected;
    private Toolbar toolbarAlbum;
    private TextView tvEdit, tvDel, tvMove, tvRename, tvNameAudio;
    private LinearLayout linearEdit;
    private ImageView imgback;
    public boolean check_Edit = false, playContinueFlag = false, check_play = false;
    public Button btn_shuffle;
    private static CollectionList instane;
    public LinearLayout linear_status;
    SharedPreferences sharedPreferences;
    private ImageView img_play, img_skip_next, img_shuffle, imgAudio;
    private FrameLayout frameLayout;
    private MediaPlayer mediaPlayer;
    private Album album;
    private int currentPosition;
    private int nRandomItem = 0;
    private Random random = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_audio_list, container, false);
        setControl(view);
        fragmentListview();
        setEvent();


//        if (sharedPreferences.getBoolean("CHECK_AUDIO_PLAY", true)){
////            linear_status.setVisibility(View.VISIBLE);
//        }


        return view;
    }

    private void fragmentListview() {
        AllSongFragment allSongFragment = new AllSongFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container1, allSongFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        checkSharePreEdit();
        super.onPause();
    }

    private void checkSharePreEdit() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CheckEdit", false);
        editor.apply();
    }

    public static CollectionList getInstance() {
        return instane;
    }



    private void setEvent() {
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogDelete();

            }
        });
        tvRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogRename();
            }
        });
        tvMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogMove();
            }
        });
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int n = ((MainActivity)getActivity()).allSongLength;
//                Toast.makeText(getContext(), n + "", Toast.LENGTH_SHORT).show();
//                nRandomItem = random.nextInt(n);
//                Log.d("randoma", nRandomItem + "");
//                onDataPass(albumArrayList.get(nRandomItem).getsNameAlbum(), albumArrayList.get(nRandomItem).getImgAlbum());
                ((MainActivity)getActivity()).randomItem();
            }
        });
    }

    private void Edit() {
        Handler handler = new Handler();
        CollectionList task = new CollectionList();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!check_Edit) {
            linearEdit.setVisibility(View.VISIBLE);
            btn_shuffle.setVisibility(View.GONE);
            tvEdit.setText("Done");
            editor.putBoolean("CheckEdit", true);
            editor.apply();
            check_Edit = true;

            ArrayList<Integer> n = new ArrayList<>();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < albumArrayList.size(); i++) {
                        if (albumArrayList.get(i).isSelected()) {
                            n.add(i);
                        }
                    }
                    Log.d("adsad", n.size() + "");
                    if (n.size() >= 2) {
                        tvRename.setVisibility(View.GONE);
                    } else
                        tvRename.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, 500);
                }
            });

        } else {
            linearEdit.setVisibility(View.GONE);
            btn_shuffle.setVisibility(View.VISIBLE);
            tvEdit.setText("Edit");
            editor.putBoolean("CheckEdit", false);
//            getActivity().recreate();
            editor.apply();
            check_Edit = false;
        }
    }

    private void addSelected() {
        album_Selected = new ArrayList<>();
//        for (Album album : albumArrayList) {
//            if (album.isSelected()) {
//                Log.d("aaaaa1a", album.getsNameAlbum());
//                album_Selected.add(new Album(album.getImgAlbum(), album.getsNameAlbum()));
////                albumArrayList.remove(0);
////                notifyAll();
//            }
//        }
//        Log.d("aaaaa1a", album_Selected.size() + "");

        ArrayList<Integer> n = new ArrayList<>();
        for (int i = 0; i < albumArrayList.size(); i++) {
            if (albumArrayList.get(i).isSelected()) {
//                Log.d("aaaaa1a", i +"");
                n.add(i);
//                albumArrayList.remove(i);
//                albumAdapter.notifyDataSetChanged();
//
            }
        }

        for (int i = 0; i < n.size(); i++) {
            albumArrayList.remove(n.get(i));
            albumAdapter.notifyDataSetChanged();
            Log.d("aaaaa1a", n.get(i) + "");
        }
    }

    private void ShowDialogMove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_remove, null);
        builder.setView(view);
        TextView tvCancel1 = view.findViewById(R.id.btnCancel_move);
        TextView tvDone1 = view.findViewById(R.id.btnDone_move);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tvDone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "move", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowDialogRename() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater1.inflate(R.layout.dialog_rename, null);
        builder1.setView(view1);
        EditText editText = view1.findViewById(R.id.edit_rename);
        TextView tvCancel1 = view1.findViewById(R.id.btnCancel);
        TextView tvDone1 = view1.findViewById(R.id.btnDone);
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();

        tvCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
            }
        });
        tvDone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "rename", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowDialogDelete() {
//        addSelected();

        ArrayList<Integer> n = new ArrayList<>();
        for (int i = 0; i < albumArrayList.size(); i++) {
            if (albumArrayList.get(i).isSelected()) {
//                Log.d("aaaaa1a", i +"");
                n.add(i);
//                albumArrayList.remove(i);
//                albumAdapter.notifyDataSetChanged();
//
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.diaglog_del, null);
        builder.setView(view);
        TextView tvCancel = view.findViewById(R.id.btnCancel_del);
        TextView title_del = view.findViewById(R.id.title_del);
        TextView tvDone = view.findViewById(R.id.btnDelete);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                for (int i = 0; i < n.size(); i++) {

                    albumArrayList.remove(Integer.parseInt(n.get(i).toString()));
                    albumAdapter.notifyDataSetChanged();

                    n.remove(i);
//                            n.clear();

//                            Log.d("aaaaa1a", n.get(i) + "");
                }
//                    }
//                });

                Toast.makeText(getContext(), "del", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();

            }
        });
    }


    @Override
    public void onDataPass(String name, String img) {
        Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void fullSongList(ArrayList<Album> albumArrayList, int position) {
        this.albumArrayList = albumArrayList;
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
//        this.allSongLength = length;
    }

    private void setControl(View view) {
        albumArrayList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        img_play = view.findViewById(R.id.img_play);
        img_shuffle = view.findViewById(R.id.img_shuffle);
        img_skip_next = view.findViewById(R.id.img_skip_next);
        linear_status = view.findViewById(R.id.status_audio);
//        recyclerView = view.findViewById(R.id.recycleAlbum);
        tvNameAudio = view.findViewById(R.id.tvNameAudio);
        imgAudio = view.findViewById(R.id.img_audio);
        tvEdit = view.findViewById(R.id.tvEdit);
        linearEdit = view.findViewById(R.id.linearEdit);
        imgback = view.findViewById(R.id.imgBack);
        tvDel = view.findViewById(R.id.tvDel);
        tvMove = view.findViewById(R.id.tvMove);
        tvRename = view.findViewById(R.id.tvRename);
        btn_shuffle = view.findViewById(R.id.btn_shuffle);
        sharedPreferences = getActivity().getSharedPreferences("AudioDownload", MODE_PRIVATE);
    }
}