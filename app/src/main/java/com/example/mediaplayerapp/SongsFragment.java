package com.example.mediaplayerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    private ListView listViewSongs;
    private FirestoreHelper firestoreHelper;
    private SongAdapter songAdapter;

    public static SongsFragment newInstance(String sector) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString("sector", sector);
        Log.e("secotr",sector);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        listViewSongs = view.findViewById(R.id.listView_songs);

        String sector = getArguments().getString("sector", "");
        if (!sector.isEmpty()) {
            fetchSongs(sector);
        } else {
            Log.e("SongsFragment", "Sector not provided or empty");
        }

        return view;
    }

    private void fetchSongs(String sector) {
        firestoreHelper = new FirestoreHelper();
        firestoreHelper.fetchSongsBySector(sector, new FirestoreHelper.FirestoreCallback<List<MusicPreference>>() {
            @Override
            public void onCallback(List<MusicPreference> musicPreferences) {
                List<Song> songs = convertMusicPreferencesToSongs(musicPreferences);
                updateListView(songs);
            }

            @Override
            public void onError(Exception e) {
                Log.e("SongsFragment", "Error fetching songs: ", e);
            }
        });
    }

    private List<Song> convertMusicPreferencesToSongs(List<MusicPreference> musicPreferences) {
        List<Song> songs = new ArrayList<>();
        for (MusicPreference pref : musicPreferences) {
            Song song = new Song(
                    pref.getId(),
                    pref.getTitle(),
                    pref.getAlbumId(),
                    pref.getPath(),
                    pref.getImage(),
                    pref.getDescription(),
                    pref.getDuration(),
                    pref.getArtistName()
            );
            songs.add(song);
        }
        return songs;
    }

    private void updateListView(List<Song> songs) {
        if (getActivity() != null) {
            songAdapter = new SongAdapter(getActivity(), songs);
            listViewSongs.setAdapter(songAdapter);
        }
    }
}
