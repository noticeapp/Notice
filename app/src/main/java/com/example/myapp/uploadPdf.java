package com.example.myapp;

public class uploadPdf {
    public String name;
    public String url;

    public uploadPdf() {
    }

    public uploadPdf(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }

}
