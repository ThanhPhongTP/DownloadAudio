package com.example.downloadaudio_ver2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.downloadaudio_ver2.CollectionList;
import com.example.downloadaudio_ver2.MainActivity;
import com.example.downloadaudio_ver2.Model.Collection;
import com.example.downloadaudio_ver2.R;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.myViewHolder>{
    Context context;
    List<Collection> collectionList;

    public CollectionAdapter(Context context, List<Collection> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
    }

    @NonNull

    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Collection collection = collectionList.get(position);

        holder.tvName.setText(collection.getsNameCollection());
        holder.imgCollection.setImageResource(collection.getImgCollection());

        holder.imgCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent(context, CollectionList.class));
                ((MainActivity)context).setFragmentAudioList();
            }
        });

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.imgMore);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.del:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.diaglog_del, null);
                                builder.setView(view);
                                TextView tvCancel = view.findViewById(R.id.btnCancel_del);
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
                                        Toast.makeText(context, "del", Toast.LENGTH_SHORT).show();
                                        removeAt(position);
                                        alertDialog.cancel();
                                    }
                                });
                                break;
                            case R.id.rename:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                                        Toast.makeText(context, "rename", Toast.LENGTH_SHORT).show();
                                        holder.tvName.setText(editText.getText().toString());
                                        alertDialog1.cancel();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgCollection;
        public TextView tvName;
        public ImageView imgMore;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCollection = itemView.findViewById(R.id.imgCollection);
            tvName = itemView.findViewById(R.id.tvName);
            imgMore = itemView.findViewById(R.id.btnMore);
        }
    }

    public void removeAt(int position) {
        collectionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, collectionList.size());
    }

}
