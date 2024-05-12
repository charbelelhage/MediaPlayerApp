package com.example.mediaplayerapp;
public class MusicPreference {
    private String sector;
    private String id;
    private String title;
    private long albumId;
    private String path;
    private String image;
    private String description;
    private String duration;
    private String artistName;

    private int count;

    // Default constructor is necessary for Firestore data conversion
    public MusicPreference() {
    }

    // Constructor with all fields
    public MusicPreference(String sector, String songId, String title, long albumId, String path, String image, String description, String duration, String artistName, int count) {
        // Initialize fields
        this.sector = sector;
        this.id = songId;
        this.title = title;
        this.albumId = albumId;
        this.path = path;
        this.image = image;
        this.description = description;
        this.duration = duration;
        this.artistName = artistName;
        this.count = 1;  // Default count when creating a new preference
    }

    // Getters and setters for all fields
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
