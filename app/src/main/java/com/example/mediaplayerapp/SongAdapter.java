package com.example.mediaplayerapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;


import androidx.appcompat.widget.AppCompatImageButton;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    private Context context;
    private List<Song> songsList;
    private MediaPlayer mediaPlayer;
    private String currentPlayingUrl;

    public SongAdapter(Context context, List<Song> songsList) {
        super(context, 0, songsList);
        this.context = context;
        this.songsList = songsList;
        this.mediaPlayer = new MediaPlayer();

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
//        int imageResource = context.getResources().getIdentifier(currentSong.getImage(), "drawable", context.getPackageName());
//        imageView.setImageResource(imageResource);
        Picasso.get().load(currentSong.getImage()).into(imageView);


        TextView textViewTitle = listItem.findViewById(R.id.textView_title);
        textViewTitle.setText(currentSong.getTitle());

        TextView textViewPath = listItem.findViewById(R.id.textView_duration);
        textViewPath.setText(formatDuration(Long.parseLong(currentSong.getDuration())));

        TextView textViewDescription = listItem.findViewById(R.id.textView_artistName);

        textViewDescription.setText(currentSong.getArtistName());

        AppCompatImageButton playButton = listItem.findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic_play);
                } else {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(currentSong.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        currentPlayingUrl = currentSong.getPath();
                        playButton.setImageResource(R.drawable.ic_pause);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        return listItem;
    }
    public String formatDuration(long durationInMillis) {
        long totalSeconds = durationInMillis / 1000; // Convert milliseconds to seconds
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String formattedDuration = String.format("%02d:%02d", minutes, seconds);

        return formattedDuration;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (MediaPlayerManager.isPlaying() && !MediaPlayerManager.getCurrentPlayingUrl().equals(currentPlayingUrl)) {
            MediaPlayerManager.stop();
        }
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
