package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Discussion extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDataRef = FirebaseDatabase.getInstance();
    RecyclerView recyclerView;
    DatabaseReference mDatabaseReference = firebaseDataRef.getReference("DiscussionPost");
    DatabaseReference mDatabaseRefStud = firebaseDataRef.getReference("Stud");
    List<DiscussionPost> uploadList;
    DisscusionAdapter adapter;

    EditText mtitle;
    EditText mcontent;
    Button mpost;
    String uid = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        mtitle = findViewById(R.id.editPostTitle);
        mcontent = findViewById(R.id.editPostContent);
        mpost = findViewById(R.id.postButton);

        recyclerView = findViewById(R.id.discussRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }


    public void loadData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                uploadList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    DiscussionPost upload = postSnapshot.getValue(DiscussionPost.class);
                    uploadList.add(upload);
                }


                //displaying it to list
                adapter = new DisscusionAdapter(uploadList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickCreatePost(View v) {

        mDatabaseRefStud.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = mtitle.getText().toString().trim();
                String content = mcontent.getText().toString().trim();
                String fullname;

                fullname = dataSnapshot.child("fullname").getValue().toString();
//                Log.d("FULL", dataSnapshot.child("fullname").getValue().toString());

                DiscussionPost x = new DiscussionPost(uid, title, fullname,content);
                mDatabaseReference.push().setValue(x);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}