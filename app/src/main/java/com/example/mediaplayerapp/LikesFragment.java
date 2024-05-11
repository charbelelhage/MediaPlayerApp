package com.example.mediaplayerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class LikesFragment extends Fragment {
    private ListView listViewSongs;
    private List<Song> likedSongsList;
    private DatabaseHelper databaseHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.likes_fragment, container, false);
        System.out.println("My variable value: " + Spotify.getAccessTokenValue());


        listViewSongs = view.findViewById(R.id.listView_songs);
        likedSongsList = Spotify.getLikedTracks();
        System.out.println("Likes Fragment " + likedSongsList);
        for (Song song : likedSongsList) {
            System.out.println(song.getTitle());
            System.out.println(song.getId());
        }
//        databaseHelper = new DatabaseHelper(getActivity()); // Corrected context usage
//
//        // Fetch all songs from the database
//        songsList = databaseHelper.getAllSongs();

        // Create a custom adapter to display songs with images
        SongAdapter adapter = new SongAdapter(getActivity(), likedSongsList); // Corrected context usage
        listViewSongs.setAdapter(adapter);

        return view; // Return the view at the end

    }
}
