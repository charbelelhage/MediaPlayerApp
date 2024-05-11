package com.example.mediaplayerapp;

public class Song {
    private String id;
    private String title;
    private long albumId;
    private String path;
    private String image;
    private String description;

    private String duration;
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

    private String artistName;

    public Song(String id, String title,String url,String artistName,String duration,String path) {
        this.id = id;
        this.title = title;
        this.image=url;
        this.artistName=artistName;
        this.duration=duration;
        this.path=path;
    }
    public Song(String id, String title, long albumId, String path, String imagePath, String description) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.path = path;
        this.image = imagePath;
        this.description = description;
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
}
