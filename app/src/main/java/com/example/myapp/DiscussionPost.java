package com.example.myapp;
//import java.time.LocalDate;
//import java.time.LocalTime;



public class DiscussionPost {
    private String mtitle;
    private String muser;
    private String mcontent;
//    private LocalDate mDate;
//    private LocalTime mTime;

    public DiscussionPost() {
    }

    public DiscussionPost(String mtitle, String muser, String mcontent) {
        this.mtitle = mtitle;
        this.muser = muser;
        this.mcontent = mcontent;
//        this.mDate = LocalDate.now();
//        this.mTime = LocalTime.now();
    }


    public String getMtitle() {
        return mtitle;
    }

    public String getMuser() {
        return muser;
    }

    public String getMcontent() {
        return mcontent;
    }
}
