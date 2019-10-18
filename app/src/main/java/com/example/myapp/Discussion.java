package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    CalendarView calender;
    LinearLayout createpost;

    Date today = new Date();
    String day;
    String fullname;
    EditText mtitle;
    EditText mcontent;
    TextView noposts;
    Button mpost;
    String uid = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        mtitle = findViewById(R.id.editPostTitle);
        mcontent = findViewById(R.id.editPostContent);
        mpost = findViewById(R.id.postButton);
        calender = findViewById(R.id.calender);
        noposts = findViewById(R.id.noposts);
        createpost = findViewById(R.id.createpost);

        noposts.setVisibility(View.INVISIBLE);

        day = getDay(today);

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                day = dayOfMonth + "-" + month + "-" + year;
//                Log.d("YEET", day);

                if(!day.equals(getDay(today))){
                    createpost.setVisibility(View.GONE);

                }
                else{
                    createpost.setVisibility(View.VISIBLE);
                }

                loadData();
            }

        });


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



        mDatabaseReference.orderByChild("mday").equalTo(day).addValueEventListener(new ValueEventListener() {
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

                if(uploadList.isEmpty())
                    noposts.setVisibility(View.VISIBLE);
                else
                    noposts.setVisibility(View.INVISIBLE);

                commentList = new ArrayList<>();

                Collections.sort(uploadPosts, new Comparator<DiscussionPost>() {
                    @Override
                    public int compare(DiscussionPost o1, DiscussionPost o2) {
                        return o1.getPostTime().compareTo(o2.getPostTime());
                    }
                });

                Collections.reverse(uploadPosts);

                for(int i = 0; i < uploadPosts.size(); ++i){
                    commentList.add(uploadList.get(uploadPosts.get(i)));
                }

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

        if(title.equals("")){
            mtitle.setError("Required");
            return;
        }

        if(content.equals("")){
            mcontent.setError("Required");

            return;
        }

        String postId = mDatabaseReference.push().getKey();
//        Log.d("POSTID", postId);

        if(postId != null){
            DiscussionPost x = new DiscussionPost(postId, uid, title, fullname,
                    content, getTime(today), getDay(today));

            mDatabaseReference.child(postId).setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mtitle.setText("");
                    mcontent.setText("");
                }
            });
        }
    }

    public String getTime(Date d){
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        return time.format(d);
    }

    public String getDay(Date d){
        DateFormat day = new SimpleDateFormat("dd-MM-yyyy");
        return day.format(d);
    }

}