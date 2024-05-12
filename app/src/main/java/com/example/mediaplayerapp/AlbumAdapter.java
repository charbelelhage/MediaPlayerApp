package com.example.mediaplayerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends ArrayAdapter<Album> {
    private Context context;
    private List<Album> albumsList;

    public AlbumAdapter(Context context, List<Album> albumsList) {
        super(context, 0, albumsList);
        this.context = context;
        this.albumsList = albumsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        }

        Album currentAlbum = albumsList.get(position);

        ImageView imageView = listItem.findViewById(R.id.imageView_album);
        // Using Picasso to load the album image from URL
        Picasso.get().load(currentAlbum.getImageUrl()).into(imageView);

        TextView textViewID = listItem.findViewById(R.id.textView_albumId);
        textViewID.setText(currentAlbum.getId());

        TextView textViewTitle = listItem.findViewById(R.id.textView_albumName);
        textViewTitle.setText(currentAlbum.getName());

        TextView textViewTracks = listItem.findViewById(R.id.textView_totalTracks);
        textViewTracks.setText("Total Tracks: " + currentAlbum.getTotalTracks());

        TextView artistName = listItem.findViewById(R.id.textView_artistName);
        artistName.setText(currentAlbum.getArtistName());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Action on clicking a specific album
                TextView textViewAlbumId = view.findViewById(R.id.textView_albumId);
                String albumId = textViewAlbumId.getText().toString();
                // Presumed method to fetch songs by album ID, implement or modify as needed
                List<Song> songsList = Spotify.getTracksOfAlbumById(albumId);
                // Debug or use the songsList as needed
                System.out.println("Songs: " + songsList.toString());
            }
        });

        return listItem;
    }
}
