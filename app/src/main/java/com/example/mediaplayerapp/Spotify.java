package com.example.mediaplayerapp;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Spotify {
    private static final String TAG = "Spotify";
    private static String accessToken;
    private static List<Song> likedSongs;
    private static List<Album> newReelasedAlbums;
    private static List<Song> resultSongs;

    public static void getAccessToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://accounts.spotify.com/api/token");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String postData = "grant_type=client_credentials&client_id=858bfe10644d446db2db9da406e10980&client_secret=2bcf1d08fea249cfaf1fd4345145ad2a";
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(postData.getBytes());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    parseAccessToken(response.toString());
                } catch (IOException e) {
                    Log.e(TAG, "Error fetching access token: " + e.getMessage());
                }
            }
        });
        thread.start();
    }

    private static void parseAccessToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            accessToken = jsonObject.getString("access_token");
            Log.d(TAG, "Access Token: " + accessToken);

            // Now you can use accessToken globally in your application
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
        }
    }

    public static String getAccessTokenValue() {
        return accessToken;
    }


    public static List<Song> searchSpotify(String query) {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spotify.com/v1/search?q="+query+"&type=track");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        System.out.println("BEFOREEE" +response.toString());
                        // Parse the response using parseSongs method
                        resultSongs = parseSongsFromSearch(response.toString());
                        System.out.println("AFTERRR");
                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultSongs;
    }

    public static List<Song> getLikedTracks() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spotify.com/v1/recommendations?seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_genres=classical%2Ccountry&seed_tracks=0c6xIDDpzE81m2q797ordA");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse the response using parseSongs method
                        likedSongs = parseSongs(response.toString());
                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return likedSongs; // Return the fetched liked songs
    }


    public static List<Album> getNewReleasedAlbums() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(" https://api.spotify.com/v1/browse/new-releases");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);


                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse the response using parseSongs method
                        newReelasedAlbums = parseAlbums(response.toString());

                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return newReelasedAlbums;
    }



    public static List<Song> getTracksOfAlbumById(String id) {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(" https://api.spotify.com/v1/albums/"+id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);


                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse the response using parseSongs method
                        likedSongs = parseSongsFromSearch(response.toString());

                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return likedSongs;
    }


    public static List<Song> getTracksRecommendations() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spotify.com/v1/recommendations?seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_genres=classical%2Ccountry&seed_tracks=0c6xIDDpzE81m2q797ordA");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse the response using parseSongs method
                        likedSongs = parseSongs(response.toString());
                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return likedSongs; // Return the fetched liked songs
    }



    public static List<Song> getAllPlaylists() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spotify.com/v1/me/playlists");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer "+ accessToken);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        System.out.println("Heyyooo "+jsonObject);
                        // Parse the response using parseSongs method
                        //playlists = parseSongs(response.toString());
                    } else {
                        System.out.println("GET request not worked. " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Count down the latch to indicate thread completion
                }
            }
        });
        thread.start();

        try {
            latch.await(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return likedSongs; // Return the fetched liked songs
    }

    private static List<Album> parseAlbums(String jsonResponse) throws JSONException {
        List<Album> albums = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject albumsObject = jsonObject.getJSONObject("albums");
        JSONArray itemsArray = albumsObject.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);
            String albumName = item.getString("name");
            String albumid = item.getString("id");
            String totalTracks=item.getString("total_tracks");

            JSONArray imagesArray = item.getJSONArray("images");
            JSONObject firstImageObject = imagesArray.getJSONObject(0);
            String imageUrl = firstImageObject.getString("url");

            JSONArray artistsObject = item.getJSONArray("artists");
            JSONObject firstArtistObject = artistsObject.getJSONObject(0);
            String artistName = firstArtistObject.getString("name");

            // Create an Album object and add it to the list
            Album album = new Album(albumid,albumName,imageUrl,"test",artistName,totalTracks);
            albums.add(album);
        }
        return albums;
    }


    private static List<Song> parseSongs(String jsonResponse) throws JSONException {
        List<Song> songs = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray tracksArray = jsonObject.getJSONArray("tracks");

        for (int i = 0; i < tracksArray.length(); i++) {
            JSONObject trackObject = tracksArray.getJSONObject(i);
            JSONObject albumObject = trackObject.getJSONObject("album");
            JSONArray imagesArray = albumObject.getJSONArray("images");
            JSONObject firstImageObject = imagesArray.getJSONObject(0);
            String imageUrl = firstImageObject.getString("url");

            JSONArray artistsObject = trackObject.getJSONArray("artists");
            JSONObject firstArtistObject = artistsObject.getJSONObject(0);
            String artistName = firstArtistObject.getString("name");


            String songName = trackObject.getString("name");
            String songId = trackObject.getString("id");
            String songDuration=trackObject.getString("duration_ms");
            String path=trackObject.getString("preview_url");

            // Create a Song object and add it to the list
            Song song = new Song(songId, songName,imageUrl,artistName,songDuration,path);
            songs.add(song);
        }

        return songs;
    }
    private static List<Song> parseSongsFromSearch(String jsonResponse) throws JSONException {
        List<Song> songs = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject tracksObject = jsonObject.getJSONObject("tracks");
        JSONArray itemsArray = tracksObject.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject trackObject = itemsArray.getJSONObject(i);
            JSONObject albumObject = trackObject.getJSONObject("album");
            JSONArray imagesArray = albumObject.getJSONArray("images");
            JSONObject firstImageObject = imagesArray.getJSONObject(0);
            String imageUrl = firstImageObject.getString("url");

            JSONArray artistsObject = trackObject.getJSONArray("artists");
            JSONObject firstArtistObject = artistsObject.getJSONObject(0);
            String artistName = firstArtistObject.getString("name");


            String songName = trackObject.getString("name");
            String songId = trackObject.getString("id");
            String songDuration=trackObject.getString("duration_ms");
            String path=trackObject.getString("preview_url");

            // Create a Song object and add it to the list
            Song song = new Song(songId, songName,imageUrl,artistName,songDuration,path);
            songs.add(song);
        }

        return songs;
    }



}

