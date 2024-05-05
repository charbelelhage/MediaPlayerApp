package com.example.mediaplayerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MediaPlayerActivity extends AppCompatActivity {

    private ListView listViewSongs;
    private List<Song> songsList;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        listViewSongs = findViewById(R.id.listView_songs);
        songsList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        // Fetch all songs from the database
        songsList = databaseHelper.getAllSongs();

        // Create a custom adapter to display songs with images
        SongAdapter adapter = new SongAdapter(this, songsList);
        listViewSongs.setAdapter(adapter);
    }
}
