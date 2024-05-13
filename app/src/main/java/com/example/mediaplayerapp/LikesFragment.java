package com.example.mediaplayerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LikesFragment extends Fragment {
    private ListView listViewSongs;
    private List<Song> likedSongsList;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.likes_fragment, container, false);
        System.out.println("My variable value: " + Spotify.getAccessTokenValue());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listViewSongs = view.findViewById(R.id.listView_songs);
        likedSongsList = new ArrayList<>();

        adapter = new SongAdapter(getActivity(), likedSongsList); // Corrected context usage
        listViewSongs.setAdapter(adapter);

        FirestoreHelper.getSongsByUserId(userId, new FirestoreHelper.FirestoreCallback<List<Song>>() {
            @Override
            public void onCallback(List<Song> songs) {
                likedSongsList.clear();
                likedSongsList.addAll(songs);
                adapter.notifyDataSetChanged();
                System.out.println("Likes Fragment " + likedSongsList);
                for (Song song : likedSongsList) {
                    System.out.println(song.getTitle());
                    System.out.println(song.getId());
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        return view; // Return the view at the end
    }
}
