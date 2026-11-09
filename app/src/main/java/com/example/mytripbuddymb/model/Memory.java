package com.example.mytripbuddymb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Memory implements Serializable {

    private String title, notes;
    private String date;
    private ArrayList<String> imagesUris;
    private String songUri;

    public Memory(String title, String notes, String date, ArrayList<String> imagesUris, String songUri) {
        this.title = title;
        this.notes = notes;
        this.date = date;
        this.imagesUris = imagesUris;
        this.songUri = songUri;


    }


    // Getters and setters
    public String getTitle() {
        return title;
    }


    public String getNotes() {
        return notes;


    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getImagesUris() {
        return imagesUris;
    }

    public void setImages(ArrayList<String> images) {
        this.imagesUris = images;
    }

    public String getSongUri() {
        return songUri;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }
}