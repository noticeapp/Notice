package com.example.myapp;

import android.widget.RadioButton;

public class UploadPDF {

    public String name;
    public String url;
    public String tag;
    public String uploaded;
    public String created;

    public UploadPDF() {
    }

    public UploadPDF(String name, String url, String tag, String uploaded, String created) {
        this.name = name;
        this.url = url;
        this.tag = tag;
        this.uploaded = uploaded;
        this.created = created;

    }

    public UploadPDF(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }


    public String getUploaded() {
        return uploaded;
    }

    public String getCreated() {
        return created;
    }



}