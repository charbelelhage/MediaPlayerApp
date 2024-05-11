package com.example.mediaplayerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class SearchFragment extends Fragment {
    private EditText searchField;
    private ListView listViewSongs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        listViewSongs = view.findViewById(R.id.listView_songs);
        List<Song> likedSongsList = Spotify.getTracksRecommendations();
        // Create a custom adapter to display songs with images
        SongAdapter adapter = new SongAdapter(getActivity(), likedSongsList); // Corrected context usage
        listViewSongs.setAdapter(adapter);

        searchField = view.findViewById(R.id.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform action when text changes
                String updatedText = charSequence.toString();
                if(updatedText.isEmpty()){
                    List<Song> likedSongsList = Spotify.getTracksRecommendations();
                    // Create a custom adapter to display songs with images
                    SongAdapter adapter = new SongAdapter(getActivity(), likedSongsList);
                    TextView textViewTitle = view.findViewById(R.id.textView_recommendation);
                    textViewTitle.setText("Recommendations");
                    listViewSongs.setAdapter(adapter);
                }else {
                List<Song> likedSongsList = Spotify.searchSpotify(updatedText);
                // Update the adapter with the new data
                SongAdapter adapter = new SongAdapter(getActivity(), likedSongsList);
                TextView textViewTitle = view.findViewById(R.id.textView_recommendation);
                textViewTitle.setText("Search Results");
                listViewSongs.setAdapter(adapter);
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this example
            }
        });
        return view; // Return the view at the end
    }

//    private void updateRecommendations(String searchText) {
//        // Perform your search logic or update recommendations based on the search text
//        // You can update the ListView or TextView accordingly
//        // For example:
//        List<Song> searchedSongs = Spotify.searchSpotify(searchText);
//        SongAdapter adapter = new SongAdapter(getActivity(), searchedSongs);
//        listViewSongs.setAdapter(adapter);
//        recommendationText.setVisibility(View.GONE); // Hide the recommendation text if search results are shown
//    }

//    private void clearRecommendations() {
//        // Clear the search results or recommendations
//        listViewSongs.setAdapter(null);
//        recommendationText.setVisibility(View.VISIBLE); // Show the recommendation text if no search text
//    }
}
