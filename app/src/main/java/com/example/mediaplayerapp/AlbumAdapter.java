package com.example.mediaplayerapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

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
        // Load image from the drawable resources based on the image name
//        int imageResource = context.getResources().getIdentifier(currentSong.getImage(), "drawable", context.getPackageName());
//        imageView.setImageResource(imageResource);
        Picasso.get().load(currentAlbum.getImageUrl()).into(imageView);

        TextView textViewID = listItem.findViewById(R.id.textView_albumId);
        textViewID.setText(currentAlbum.getId());

        TextView textViewTitle = listItem.findViewById(R.id.textView_albumName);
        textViewTitle.setText(currentAlbum.getName());

        TextView textViewPath = listItem.findViewById(R.id.textView_totalTracks);
        textViewPath.setText("Total Tracks: "+currentAlbum.getTotalTracks());

        TextView artistName = listItem.findViewById(R.id.textView_artistName);
        artistName.setText(currentAlbum.getArtistName());
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve the album ID from textView_albumId
                TextView textViewAlbumId = view.findViewById(R.id.textView_albumId);
                String albumId = textViewAlbumId.getText().toString();
                System.out.println("AlbumID: "+ albumId);
                List<Song> songsList = Spotify.getTracksOfAlbumById(albumId);
                System.out.println("sONGS: "+ songsList.toString());


            }
        });
        return listItem;
    }
}
