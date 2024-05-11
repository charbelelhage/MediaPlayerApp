package com.example.mediaplayerapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MediaPlayerDB";
    private static final int DATABASE_VERSION = 1;

    // User table
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Albums table
    private static final String TABLE_ALBUMS = "albums";
    private static final String KEY_ALBUM_ID = "id";
    private static final String KEY_ALBUM_NAME = "name";
    private static final String KEY_ALBUM_IMAGE = "image";
    private static final String KEY_ALBUM_DESCRIPTION = "description";

    // Songs table
    private static final String TABLE_SONGS = "songs";
    private static final String KEY_SONG_ID = "id";
    private static final String KEY_SONG_TITLE = "title";
    private static final String KEY_SONG_ALBUM_ID = "album_id";
    private static final String KEY_SONG_PATH = "path";
    private static final String KEY_SONG_IMAGE = "image";
    private static final String KEY_SONG_DESCRIPTION = "description";

    // Artists table
    private static final String TABLE_ARTISTS = "artists";
    private static final String KEY_ARTIST_ID = "id";
    private static final String KEY_ARTIST_NAME = "name";
    private static final String KEY_ARTIST_IMAGE = "image";
    private static final String KEY_ARTIST_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override

    public synchronized void onOpen(SQLiteDatabase db) {

        super.onOpen(db);

        db.execSQL("PRAGMA synchronous = FULL");

        db.enableWriteAheadLogging();

        db.setForeignKeyConstraintsEnabled(true);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_ALBUMS_TABLE = "CREATE TABLE " + TABLE_ALBUMS + "("
                + KEY_ALBUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ALBUM_NAME + " TEXT,"
                + KEY_ALBUM_IMAGE + " TEXT,"
                + KEY_ALBUM_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_ALBUMS_TABLE);

        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SONG_TITLE + " TEXT,"
                + KEY_SONG_ALBUM_ID + " INTEGER,"
                + KEY_SONG_PATH + " TEXT,"
                + KEY_SONG_IMAGE + " TEXT,"
                + KEY_SONG_DESCRIPTION + " TEXT,"
                + "FOREIGN KEY (" + KEY_SONG_ALBUM_ID + ") REFERENCES " + TABLE_ALBUMS + "(" + KEY_ALBUM_ID + ")" + ")";
        db.execSQL(CREATE_SONGS_TABLE);

        String CREATE_ARTISTS_TABLE = "CREATE TABLE " + TABLE_ARTISTS + "("
                + KEY_ARTIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ARTIST_NAME + " TEXT,"
                + KEY_ARTIST_IMAGE + " TEXT,"
                + KEY_ARTIST_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_ARTISTS_TABLE);

        // Insert dummy data
        insertDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
        onCreate(db);
    }

    public void addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        db.insert(TABLE_USER, null, values);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
                        + COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password});
        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount > 0;
    }

    private void insertDummyData(SQLiteDatabase db) {
        // Insert dummy artists
        ContentValues artistValues = new ContentValues();
        artistValues.put(KEY_ARTIST_NAME, "Justin Bieber");
        artistValues.put(KEY_ARTIST_IMAGE, "justin_bieber.jpg");
        artistValues.put(KEY_ARTIST_DESCRIPTION, "Canadian singer-songwriter.");
        db.insert(TABLE_ARTISTS, null, artistValues);

        artistValues.put(KEY_ARTIST_NAME, "Taylor Swift");
        artistValues.put(KEY_ARTIST_IMAGE, "taylor_swift.jpg");
        artistValues.put(KEY_ARTIST_DESCRIPTION, "American singer-songwriter.");
        db.insert(TABLE_ARTISTS, null, artistValues);

        // Insert dummy albums
        ContentValues albumValues = new ContentValues();
        albumValues.put(KEY_ALBUM_NAME, "Greatest Hits");
        albumValues.put(KEY_ALBUM_IMAGE, "greatest_hits");
        albumValues.put(KEY_ALBUM_DESCRIPTION, "Collection of best songs.");
        long albumId = db.insert(TABLE_ALBUMS, null, albumValues);

        albumValues.put(KEY_ALBUM_NAME, "Weekly Top 10");
        albumValues.put(KEY_ALBUM_IMAGE, "weekly_top_10");
        albumValues.put(KEY_ALBUM_DESCRIPTION, "Best songs for this week.");
        albumId = db.insert(TABLE_ALBUMS, null, albumValues);

        albumValues.put(KEY_ALBUM_NAME, "Top Arabic");
        albumValues.put(KEY_ALBUM_IMAGE, "Top_Arabic");
        albumValues.put(KEY_ALBUM_DESCRIPTION, "Collection of best Arabic Songs.");
        albumId = db.insert(TABLE_ALBUMS, null, albumValues);

        // Insert dummy songs linked to artists and albums
        ContentValues songValues = new ContentValues();
        songValues.put(KEY_SONG_TITLE, "Song One");
        songValues.put(KEY_SONG_ALBUM_ID, albumId);
        songValues.put(KEY_SONG_PATH, "songone.mp3");
        songValues.put(KEY_SONG_IMAGE, "song_one");
        songValues.put(KEY_SONG_DESCRIPTION, "First song in the album.");
        db.insert(TABLE_SONGS, null, songValues);

        songValues.put(KEY_SONG_TITLE, "Song Two");
        songValues.put(KEY_SONG_ALBUM_ID, albumId);
        songValues.put(KEY_SONG_PATH, "songtwo.mp3");
        songValues.put(KEY_SONG_IMAGE, "song_two");
        songValues.put(KEY_SONG_DESCRIPTION, "Second song in the album.");
        db.insert(TABLE_SONGS, null, songValues);
    }

    public void addSong(String songName, long albumId, String path, String image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SONG_TITLE, songName);
        values.put(KEY_SONG_ALBUM_ID, albumId);
        values.put(KEY_SONG_PATH, path);
        values.put(KEY_SONG_IMAGE, image);
        values.put(KEY_SONG_DESCRIPTION, description);
        db.insert(TABLE_SONGS, null, values);
    }

    public void addAlbum(String albumName, String image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALBUM_NAME, albumName);
        values.put(KEY_ALBUM_IMAGE, image);
        values.put(KEY_ALBUM_DESCRIPTION, description);
        db.insert(TABLE_ALBUMS, null, values);
    }

    public void addArtist(String artistName, String image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_NAME, artistName);
        values.put(KEY_ARTIST_IMAGE, image);
        values.put(KEY_ARTIST_DESCRIPTION, description);
        db.insert(TABLE_ARTISTS, null, values);
    }

    @SuppressLint("Range")
    public List<String> getAllArtists() {
        List<String> artists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ARTISTS, null);
        if (cursor.moveToFirst()) {
            do {
                artists.add(cursor.getString(cursor.getColumnIndex(KEY_ARTIST_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return artists;
    }

    @SuppressLint("Range")
    public List<String> getAllAlbums() {
        List<String> albums = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALBUMS, null);
        if (cursor.moveToFirst()) {
            do {
                albums.add(cursor.getString(cursor.getColumnIndex(KEY_ALBUM_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return albums;
    }

    @SuppressLint("Range")
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SONGS, null);
        if (cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getLong(cursor.getColumnIndex(KEY_SONG_ID)));
                String title = cursor.getString(cursor.getColumnIndex(KEY_SONG_TITLE));
                long albumId = cursor.getLong(cursor.getColumnIndex(KEY_SONG_ALBUM_ID));
                String path = cursor.getString(cursor.getColumnIndex(KEY_SONG_PATH));
                String image = cursor.getString(cursor.getColumnIndex(KEY_SONG_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(KEY_SONG_DESCRIPTION));

                Song song = new Song(id,title, albumId, path, image, description);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

}
