package com.example.myapp;

import android.widget.RadioButton;

public class uploadPdf {
    public String name;
    public String url;
    public String created;
    public String uploaded;
    public String tag;
    public RadioButton bookmark;

    public uploadPdf(String name, String url, String created, String uploaded, String tag, RadioButton bookmark) {
        this.name = name;
        this.url = url;
        this.created = created;
        this.uploaded = uploaded;
        this.tag = tag;
        this.bookmark = bookmark;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCreated() {
        return created;
    }

    public String getUploaded() {
        return uploaded;
    }

    public String getTag() {
        return tag;
    }

    public RadioButton getBookmark() {
        return bookmark;
    }
}
