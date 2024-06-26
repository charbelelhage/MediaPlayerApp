package com.example.mediaplayerapp;

public class Song {
    private long id;
    private String title;
    private long albumId;
    private String path;
    private String image;
    private String description;

    public Song(long id, String title, long albumId, String path, String imagePath, String description) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.path = path;
        this.image = imagePath;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
