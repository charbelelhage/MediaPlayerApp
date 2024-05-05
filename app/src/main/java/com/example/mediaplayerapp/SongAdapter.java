package com.example.mediaplayerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    private Context context;
    private List<Song> songsList;

    public SongAdapter(Context context, List<Song> songsList) {
        super(context, 0, songsList);
        this.context = context;
        this.songsList = songsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        }

        Song currentSong = songsList.get(position);

        ImageView imageView = listItem.findViewById(R.id.imageView_song);
        // Load image from the drawable resources based on the image name
        int imageResource = context.getResources().getIdentifier(currentSong.getImage(), "drawable", context.getPackageName());
        imageView.setImageResource(imageResource);

        TextView textViewTitle = listItem.findViewById(R.id.textView_title);
        textViewTitle.setText(currentSong.getTitle());

        TextView textViewPath = listItem.findViewById(R.id.textView_path);
        textViewPath.setText(currentSong.getPath());

        TextView textViewDescription = listItem.findViewById(R.id.textView_description);
        textViewDescription.setText(currentSong.getDescription());

        return listItem;
    }
}
