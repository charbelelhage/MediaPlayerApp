package com.example.mediaplayerapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment {
    private RecyclerView recyclerView;
    private SectorAdapter adapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlists_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();  // Initialize Firestore
        fetchSectors();

        return view;
    }

    private void fetchSectors() {
        db.collection("sectors").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> sectors = new ArrayList<>();
                task.getResult().forEach(document -> {
                    sectors.add(document.getString("name"));  // Assuming 'name' is the field where sector names are stored
                });
                adapter = new SectorAdapter(getContext(), sectors, this::navigateToSongsFragment);
                recyclerView.setAdapter(adapter);
            } else {
                // Handle failure
                System.out.println("Error getting documents: " + task.getException());
            }
        });
    }

    private void navigateToSongsFragment(String sector) {
        SongsFragment songsFragment = SongsFragment.newInstance(sector);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, songsFragment)
                .addToBackStack(null)
                .commit();
    }
}
