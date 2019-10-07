package com.example.myapp;

import android.widget.RadioButton;

public class UploadPDF {

    public String name;
    public String url;
    //public String tag;
    public String uploaded;
    public String created;
    public String noticeid;

    public UploadPDF()
    {
    }

    public UploadPDF(String name, String url, String uploaded, String created,String noticeid) {

        this.name = name;
        this.url = url;
        this.uploaded = uploaded;
        this.created = created;
        this.noticeid = noticeid;
    }

    public String getNoticeid() {
        return noticeid;
    }


    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
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

    public String getUploaded() {
        return uploaded;
    }

}