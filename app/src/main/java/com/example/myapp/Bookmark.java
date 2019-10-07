package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Bookmark extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference mdatabaseReference;
    List<UploadPDF> uploadList;
    BookmarkAdapter bookmarkAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);


    }
}
