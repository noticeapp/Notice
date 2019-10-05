package com.example.myapp;

import java.util.ArrayList;

public class DiscussionPost {
    private String postId;
    private String mtitle;
    private String muser;
    private String mcontent;
    private String muid;

    public DiscussionPost() {
    }

    public DiscussionPost(String postId, String uid, String mtitle, String muser, String mcontent) {
        this.postId = postId;
        this.muid = uid;
        this.mtitle = mtitle;
        this.muser = muser;
        this.mcontent = mcontent;
    }


    public String getMtitle() {
        return mtitle;
    }

    public String getMuser() { return muser; }

    public String getMcontent() {
        return mcontent;
    }

    public String getMuid() {return muid;}

    public String getPostId() { return postId; }

}
