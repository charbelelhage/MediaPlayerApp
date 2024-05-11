package com.example.mediaplayerapp;

import static com.example.mediaplayerapp.Spotify.getAllPlaylists;

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
    private ListView listViewAlbums;
    private List<Song> likedSongsList;
    private List<Album> newRelesedAlbumsList;
    private DatabaseHelper databaseHelper;
    private String mp3Url = "https://p.scdn.co/mp3-preview/8196f1d0cb4c14a5a5bd1ab078c6893b30dab102?cid=cfe923b2d660439caf2b557b21f31221";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        System.out.println("My variable value: " + Spotify.getAccessTokenValue());

        listViewSongs = view.findViewById(R.id.listView_songs);
        likedSongsList = Spotify.getLikedTracks();

        SongAdapter adapter = new SongAdapter(getActivity(), likedSongsList); // Corrected context usage
        listViewSongs.setAdapter(adapter);

        listViewAlbums = view.findViewById(R.id.listView_albums);
        newRelesedAlbumsList = Spotify.getNewReleasedAlbums();
        AlbumAdapter albumAdapter = new AlbumAdapter(getActivity(), newRelesedAlbumsList); // Corrected context usage
        listViewAlbums.setAdapter(albumAdapter);

        return view;
    }


}
