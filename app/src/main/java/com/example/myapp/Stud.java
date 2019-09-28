package com.example.myapp;
//file for storing information of student and teacher
public class Stud {
    public String fullname,id,department,category;
    public Stud()
    {

    }

    public Stud(String fullname, String id, String department, String category) {
        this.fullname = fullname;
        this.id = id;

        this.department = department;
        this.category = category;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
