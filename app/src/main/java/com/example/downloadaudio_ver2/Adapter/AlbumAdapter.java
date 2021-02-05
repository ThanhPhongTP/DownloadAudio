package com.example.downloadaudio_ver2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.downloadaudio_ver2.Model.Album;
import com.example.downloadaudio_ver2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<Album> implements Filterable {

    private Context mContext;
    private ArrayList<Album> AlbumList = new ArrayList<>();
    public ImageView imgCheck, imgMore;

    public AlbumAdapter(Context mContext, ArrayList<Album> AlbumList) {
        super(mContext, 0, AlbumList);
        this.mContext = mContext;
        this.AlbumList = AlbumList;
    }

    @android.support.annotation.NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_audio, parent, false);
        }
        Album album = AlbumList.get(position);
        TextView tvNameAlbum = listItem.findViewById(R.id.tvNameAudio);
        ImageView imgAlnum = listItem.findViewById(R.id.img_audio);
        imgCheck = listItem.findViewById(R.id.img_check);
        imgMore = listItem.findViewById(R.id.btnMore);
        tvNameAlbum.setText(album.getsNameAlbum());

        Picasso.get()
                .load(album.getImgAlbum())
                .into(imgAlnum);

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, imgMore);
                popup.getMenuInflater().inflate(R.menu.popup_audio, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.del:
                                break;
                            case R.id.rename:
                                break;
                            case R.id.move:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        return listItem;
    }
}