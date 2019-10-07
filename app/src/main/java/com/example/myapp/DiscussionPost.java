package com.example.myapp;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscussionPost {
    private String postId;
    private String mtitle;
    private String muser;
    private String mcontent;
    private String muid;
    private String mday;
    private String mtime;

    public DiscussionPost() {
    }

    public DiscussionPost(String postId, String uid, String mtitle,
                          String muser, String mcontent, String time, String day) {
        this.postId = postId;
        this.muid = uid;
        this.mtitle = mtitle;
        this.muser = muser;
        this.mcontent = mcontent;
        this.mtime = time;
        this.mday = day;
    }


    public String getMtitle() { return mtitle; }

    public String getMuser() { return muser; }

    public String getMcontent() { return mcontent; }

    public String getPostId() { return postId; }

    public String getMuid() { return muid; }

    public String getMtime() { return mtime; }

    public String getMday() { return mday; }

    @Exclude
    public Date getPostTime() {
        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");

        try{
            Date date = sdf.parse(mtime);

            return  date;
        }

        catch (java.text.ParseException e) {
            return new Date();
        }
    }
}