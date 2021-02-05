package com.example.downloadaudio_ver2;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadaudio_ver2.Adapter.CollectionAdapter;
import com.example.downloadaudio_ver2.Model.Collection;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Collection> collectionArrayList;
    private CollectionAdapter collectionAdapter;
    private ImageView imgAddCollection;
    private SearchView searchView;
    private String searchText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment, container, false);

        setControl(view);
        addCollection();
        setEventCollection();
        searchCollection();
        return view;
    }

    private void addCollection() {
        collectionArrayList = new ArrayList<>();
        collectionAdapter = new CollectionAdapter(getActivity(), collectionArrayList);
        recyclerView.setAdapter(collectionAdapter);
        collectionArrayList.add(new Collection(R.drawable.rectangle108, "Download"));
//        collectionArrayList.add(new Collection(R.drawable.rectangle120, "Download1"));
//        collectionArrayList.add(new Collection(R.drawable.rectangle110, "Download2"));
//        collectionArrayList.add(new Collection(R.drawable.rectangle111, "Download3"));
        collectionAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
    }

//    private void createData() {
//        collectionAdapter = new CollectionAdapter(getActivity(), collectionArrayList);
//        recyclerView.setAdapter(collectionAdapter);
//        database = new Database(getActivity(), "Memory.sqlite", null, 1);
//        database.QueryData("CREATE TABLE IF NOT EXISTS Memory(Id INTEGER PRIMARY KEY AUTOINCREMENT, Date VARCHAR(50), Content VARCHAR(2000), Imgages BLOB)");
//
//        Cursor cursor = database.GetData("SELECT * FROM Memory");
//        while (cursor.moveToNext()) {
//            collectionArrayList.add(new Collection(
//                    cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getString(2),
//                    cursor.getBlob(3)
//            ));
//        }
//        collectionAdapter.notifyDataSetChanged();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
////        Log.d("abcc", String.valueOf(cursor.getInt(0)));
//    }


    private void setEventCollection() {
        imgAddCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_create_collection, null);
                builder.setView(view);
                TextView tvCancel1 = view.findViewById(R.id.btnCancel_create);
                TextView tvDone1 = view.findViewById(R.id.btnDone_create);
                EditText edit_create_name = view.findViewById(R.id.edit_create_name);
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
                        Toast.makeText(getContext(), "create", Toast.LENGTH_SHORT).show();
//                        collectionArrayList = new ArrayList<>();
//                        collectionAdapter = new CollectionAdapter(getActivity(), collectionArrayList);
//                        recyclerView.setAdapter(collectionAdapter);
//                        collectionArrayList.add(new Collection(R.drawable.rectangle108, edit_create_name.getText().toString()));
//
//                        collectionAdapter.notifyDataSetChanged();
//                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
                    }
                });
            }
        });
    }

    private void searchCollection() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Collection> newList = new ArrayList<>();
                for (Collection collection : collectionArrayList) {
                    if (collectionArrayList.size() > 0) {
                        searchText = collection.getsNameCollection().toLowerCase();
                        if (searchText.contains(newText))
                            newList.add(collection);
                    }
                }
                collectionAdapter = new CollectionAdapter(getContext(), newList);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(collectionAdapter);
                return true;
            }
        });
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.recycleCollection);
        imgAddCollection = view.findViewById(R.id.imgAddCollection);
        searchView = view.findViewById(R.id.search);
    }

}