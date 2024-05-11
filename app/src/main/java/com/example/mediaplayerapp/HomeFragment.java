package com.example.mediaplayerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ListView listViewSongs;
    private List<Song> songsList;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        listViewSongs = view.findViewById(R.id.listView_songs);
        songsList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity()); // Corrected context usage

        // Fetch all songs from the database
        songsList = databaseHelper.getAllSongs();

        // Create a custom adapter to display songs with images
        SongAdapter adapter = new SongAdapter(getActivity(), songsList); // Corrected context usage
        listViewSongs.setAdapter(adapter);

        return view; // Return the view at the end
    }


}
