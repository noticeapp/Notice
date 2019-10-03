package com.example.myapp;

public class UploadPDF {

    public String name;
    public String url;
    public String uploaded;

  //  public String tag;
    public String created;

    public UploadPDF() {
    }

    public UploadPDF(String name, String url, String created) {
        this.name = name;
        this.url = url;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeacherid() {
        return uploaded;
    }

    public void setTeacherid(String teacherid) {
        this.uploaded= teacherid;
    }

   /* public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }*/

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UploadPDF(String name, String url, String teacherid, String created) {
        this.name = name;
        this.url = url;
        this.uploaded = teacherid;

        this.created = created;
    }



}