package com.example.myapp;

import java.util.Date;

public class Comment {
   private String muid;
   private String mname;
   private String mcomment;
   private Date mdate;

    public Comment() {
    }

    public Comment(String muid, String mname, String mcomment) {
        this.muid = muid;
        this.mname = mname;
        this.mcomment = mcomment;
        this.mdate = new Date();
    }

    public String getMuid() { return this.muid; }

    public String getMname() { return this.mname; }

    public String getMcomment() { return this.mcomment; }

    public Date getMdate() { return mdate; }
}
