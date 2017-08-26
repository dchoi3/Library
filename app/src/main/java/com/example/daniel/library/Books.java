package com.example.daniel.library;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Daniel on 6/22/2017.
 */

public class Books {

    private String title;
    private String author;
    private String date;
    private String description;
    private Bitmap img;
    private String previewURL;

    public Books(String title, String author, String date, String description, Bitmap img, String previewURL){
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;
        this.img = img;
        this.previewURL = previewURL;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
