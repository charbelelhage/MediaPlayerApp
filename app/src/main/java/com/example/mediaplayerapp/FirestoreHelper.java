package com.example.mediaplayerapp;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {

    private static FirebaseFirestore db;
    private List<String> sectorNames;
    private static List<Song> songsList;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        sectorNames = new ArrayList<>();
    }

    public void fetchSectors(FirestoreCallback<List<String>> callback) {
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

    public void fetchUserSector(String userId, FirestoreCallback<String> callback) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("sector")) {
                        String sector = documentSnapshot.getString("sector");
                        if (sector != null) {
                            callback.onCallback(sector);
                        } else {
                            callback.onError(new Exception("Sector is null for user: " + userId));
                        }
                    } else {
                        callback.onError(new Exception("User document is missing or does not contain a sector field."));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

//    public void saveMusicPreference(MusicPreference musicPreference) {
//        db.collection("music_preferences")
//                .add(musicPreference)
//                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Music preference successfully saved!"))
//                .addOnFailureListener(e -> Log.e("Firestore", "Error saving music preference", e));
//    }

    public void saveMusicPreference(MusicPreference musicPreference) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference musicPrefsCollection = db.collection("music_preferences");

        // Construct a unique key or identifier for each preference based on sector and songId
        String documentKey = musicPreference.getSector() + "_" + musicPreference.getId();

        DocumentReference docRef = musicPrefsCollection.document(documentKey);

        // Using Firestore transactions to safely increment the count
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(docRef);
                    if (snapshot.exists()) {
                        // If the document exists, increment the count
                        long newCount = snapshot.getLong("count") + 1;
                        transaction.update(docRef, "count", newCount);
                    } else {
                        // If it does not exist, set the new MusicPreference with count 1
                        transaction.set(docRef, musicPreference);
                    }
                    return null; // Transaction must return null if void
                }).addOnSuccessListener(aVoid -> Log.d("Firestore", "Transaction success"))
                .addOnFailureListener(e -> Log.e("Firestore", "Transaction failure", e));
    }

    public void fetchSongsBySector(String sector, FirestoreCallback<List<MusicPreference>> callback) {
        db.collection("music_preferences")
                .whereEqualTo("sector", sector)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<MusicPreference> musicPreferences = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MusicPreference musicPreference = document.toObject(MusicPreference.class);
                            musicPreferences.add(musicPreference);
                        }
                        // Optionally, sort musicPreferences by 'count' here in Java if needed
                        musicPreferences.sort((mp1, mp2) -> Integer.compare(mp2.getCount(), mp1.getCount()));
                        callback.onCallback(musicPreferences);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }
    public static void checkSongIfLiked(String userId, Song currentSong){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference musicPrefsCollection = db.collection("users_liked_songs");

        Query query = musicPrefsCollection.whereEqualTo("userId", userId)
                .whereEqualTo("Id", currentSong.getId());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    // Record exists for the userId and songId, delete it
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        document.getReference().delete();
                        Log.d("TAG", "Deleted existing record");
                    }
                } else {
                    // No record found for the userId and songId, add it
                    Map<String, Object> songData = new HashMap<>();
                    songData.put("userId", userId);
                    songData.put("Id", currentSong.getId());
                    songData.put("Title", currentSong.getTitle());
                    songData.put("AlbumId", currentSong.getAlbumId());
                    songData.put("Path", currentSong.getPath());
                    songData.put("Image", currentSong.getImage());
                    songData.put("Description", currentSong.getDescription());
                    songData.put("Duration", currentSong.getDuration());
                    songData.put("ArtistName", currentSong.getArtistName());

                    musicPrefsCollection.add(songData)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("TAG", "Record added with ID: " + documentReference.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e("TAG", "Error adding document", e);
                            });
                }
            } else {
                // Handle any errors
                Log.e("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    public static void getSongsByUserId(String userId, FirestoreCallback<List<Song>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Song> songsList = new ArrayList<>();

        db.collection("users_liked_songs")
                .whereEqualTo("userId", userId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null) {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                Song song = new Song(
                                        document.getString("Id"),
                                        document.getString("Title"),
                                        document.getLong("AlbumId"),
                                        document.getString("Path"),
                                        document.getString("Image"),
                                        document.getString("Description"),
                                        document.getString("Duration"),
                                        document.getString("ArtistName")
                                );
                                songsList.add(song);
                            }
                        }
                        callback.onCallback(songsList);
                    } else {
                        Log.e("TAG", "Error getting documents: ", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }



    public interface FirestoreCallback<T> {
        void onCallback(T result);
        void onError(Exception e);
    }
}
