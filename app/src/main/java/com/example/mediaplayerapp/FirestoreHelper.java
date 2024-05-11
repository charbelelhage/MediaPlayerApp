package com.example.mediaplayerapp;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {

    private FirebaseFirestore db;
    private List<String> sectorNames;

    public FirestoreHelper() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        sectorNames = new ArrayList<>();
    }

    public void fetchSectors(FirestoreCallback callback) {
        db.collection("sectors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sectorNames.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String sectorName = document.getString("name");
                            if (sectorName != null) {
                                sectorNames.add(sectorName);
                            }
                        }
                        callback.onCallback(sectorNames);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(List<String> sectorNames);
        void onError(Exception e);
    }
}
