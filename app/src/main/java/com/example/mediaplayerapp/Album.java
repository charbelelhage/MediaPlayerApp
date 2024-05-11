package com.example.mediaplayerapp;

public class Album {
    private String id;
    private String name;
    private String imageUrl;
    private String spotifyUrl;
    private String artistName;
    private String totalTracks;



    public Album(String id, String name, String imageUrl, String spotifyUrl,String artistName,String totalTracks) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.spotifyUrl = spotifyUrl;
        this.artistName=artistName;
        this.totalTracks=totalTracks;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    // Getters
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(String totalTracks) {
        this.totalTracks = totalTracks;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
}
}