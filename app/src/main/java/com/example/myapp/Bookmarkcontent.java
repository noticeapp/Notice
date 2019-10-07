package com.example.myapp;

public class Bookmarkcontent {

    String noticeid;
    String studentid;

    public Bookmarkcontent(String noticeid, String studentid) {
        this.noticeid = noticeid;
        this.studentid = studentid;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public Bookmarkcontent() {
    }
}
