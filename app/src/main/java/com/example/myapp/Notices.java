package com.example.myapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.media.JetPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.CompoundButton;

import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class Notices extends AppCompatActivity {

    //the recyclerView
    RecyclerView recyclerView;

    Vector<String> allBookmark = new Vector<String>();


    //database reference to get uploads data
    DatabaseReference mDatabaseReference ;
    DatabaseReference studReference;

    //list to store uploads data
    List<UploadPDF> uploadList;
    ToggleButton toggleButton;

    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uploadList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //displaying it to list
        adapter = new MyAdapter(uploadList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        studReference = FirebaseDatabase.getInstance().getReference("Stud");
        toggleButton = (ToggleButton) findViewById(R.id.button_favorite);

        //retrieving upload data from firebase database
       // toggleButton = (ToggleButton) findViewById(R.id.button_favorite);
        loadData();

    }

    public void loadData(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                uploadList.clear();
                progressDialog.dismiss();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    UploadPDF upload = postSnapshot.getValue(UploadPDF.class);
                    uploadList.add(upload);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

}
