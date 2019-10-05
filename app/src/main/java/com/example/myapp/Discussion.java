package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

public class Discussion extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDataRef = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = firebaseDataRef.getReference("DiscussionPost");
    DatabaseReference mDatabaseRefStud = firebaseDataRef.getReference("Stud");

    List<List<Comment>>commentList;
    HashMap<DiscussionPost, List<Comment>>uploadList;
    List<DiscussionPost>uploadPosts;

    DisscusionAdapter adapter;

    RecyclerView recyclerView;

    String fullname;
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

        setName();
        loadData();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void loadData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                uploadList = new HashMap<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    List<Comment>comments = new ArrayList<>();

                    DiscussionPost upload = postSnapshot.getValue(DiscussionPost.class);

                    if(postSnapshot.hasChild("Comments")){
                        for(DataSnapshot commentShot : postSnapshot.child("Comments").getChildren()){
                            Comment c = commentShot.getValue(Comment.class);
                            comments.add(c);
                        }
                    }

                    uploadList.put(upload, comments);

                }

                uploadPosts = new ArrayList<>(uploadList.keySet());
                commentList = new ArrayList<>(uploadList.values());
                //displaying it to list
                adapter = new DisscusionAdapter(uploadPosts, fullname,uid, commentList,getApplicationContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setName(){
        mDatabaseRefStud.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fullname = dataSnapshot.child("fullname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickCreatePost(View v) {

        String title = mtitle.getText().toString().trim();
        String content = mcontent.getText().toString().trim();

        String postId = mDatabaseReference.push().getKey();
        Log.d("POSTID", postId);
        DiscussionPost x = new DiscussionPost(postId, uid, title, fullname,content);
        mDatabaseReference.child(postId).setValue(x);
    }

}