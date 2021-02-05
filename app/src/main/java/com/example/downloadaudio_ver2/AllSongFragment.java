package com.example.downloadaudio_ver2;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;


import com.example.downloadaudio_ver2.Adapter.AlbumAdapter;
import com.example.downloadaudio_ver2.Database.Database;
import com.example.downloadaudio_ver2.Model.Album;

import java.util.ArrayList;

import static com.example.downloadaudio_ver2.Database.Database.database;


public class AllSongFragment extends ListFragment {


    private static ContentResolver contentResolver1;

    public ArrayList<Album> albums;
    public ArrayList<Album> newList;
    private SharedPreferences sharedPreferences;
    private ListView listView;
    private createDataParse createDataParse;
    private ContentResolver contentResolver;

    public static Fragment getInstance(int position, ContentResolver mcontentResolver) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        AllSongFragment tabFragment = new AllSongFragment();
        tabFragment.setArguments(bundle);
        contentResolver1 = mcontentResolver;
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDataParse = (createDataParse) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        sharedPreferences = getActivity().getSharedPreferences("AudioDownload", Context.MODE_PRIVATE);
        contentResolver = contentResolver1;
        setContent();
    }


    public void setContent() {
        boolean searchedList = false;
        albums = new ArrayList<>();
        newList = new ArrayList<>();
        getMusic();
        AlbumAdapter adapter = new AlbumAdapter(getContext(), albums);
//        if (!createDataParse.queryText().equals("")) {
//            adapter = onQueryTextChange();
//            adapter.notifyDataSetChanged();
//            searchedList = true;
//        } else {
//            searchedList = false;
//        }
        createDataParse.getLength(albums.size());
        listView.setAdapter(adapter);

        final boolean finalSearchedList = searchedList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = albums.get(position);
                if (sharedPreferences.getBoolean("CheckEdit", false)) {
                    albums.get(position).setSelected(!albums.get(position).isSelected());
                    if (albums.get(position).isSelected())
                        adapter.imgCheck.setVisibility(View.VISIBLE);
                    else
                        adapter.imgCheck.setVisibility(View.INVISIBLE);
//                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//                    listView.setItemsCanFocus(false);

                } else {
                    createDataParse.onDataPass(albums.get(position).getsNameAlbum(), albums.get(position).getImgAlbum());
                    createDataParse.fullSongList(albums, position);
                }


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(position);
                return true;
            }
        });
    }


    public void getMusic() {
        database = new Database(getContext(), "AudioDownload.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Collection(Id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(1000)," +
                " thumbnail VARCHAR(1000), url LONGTEXT)");
        Cursor cursor = database.GetData("SELECT * FROM Collection");
        while (cursor.moveToNext()) {
            albums.add(new Album(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
    }

    public AlbumAdapter onQueryTextChange() {
        String text = createDataParse.queryText();
        for (Album songs : albums) {
            String title = songs.getsNameAlbum().toLowerCase();
            if (title.contains(text)) {
                newList.add(songs);
            }
        }
        return new AlbumAdapter(getContext(), newList);

    }

    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("play_next")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createDataParse.currentSong(albums.get(position));
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface createDataParse {
        public void onDataPass(String name, String img);

        public void fullSongList(ArrayList<Album> songList, int position);

        public String queryText();

        public void currentSong(Album Album);

        public void getLength(int length);
    }

}
