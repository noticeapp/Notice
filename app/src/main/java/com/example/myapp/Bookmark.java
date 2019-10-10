package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Bookmark extends AppCompatActivity {

    RecyclerView recyclerView;
    BookmarkAdapter bookmarkAdapter;
    List<UploadPDF> bookmarkList;
    DatabaseReference uploadReference;
    DatabaseReference bookmarkReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        uploadReference = FirebaseDatabase.getInstance().getReference("uploads");
        bookmarkReference = FirebaseDatabase.getInstance().getReference("Bookmark");

        bookmarkList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.bookmarkRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookmarkAdapter = new BookmarkAdapter(bookmarkList,getApplicationContext());

        recyclerView.setAdapter(bookmarkAdapter);

        ArrayList arrayList = getData();
        loadData(arrayList);

    }

    private ArrayList getData() {


        final ArrayList arrayList = new ArrayList();
        bookmarkReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.child("studentid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        final String noticeid = snapshot.child("noticeid").getValue().toString();
                        arrayList.add(noticeid);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return arrayList;

    }

    private void loadData(final ArrayList arrayList)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        uploadReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarkList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot1: dataSnapshot.getChildren()) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).equals(snapshot1.child("noticeid").getValue().toString())) {
                            UploadPDF uploadPDF = snapshot1.getValue(UploadPDF.class);
                            bookmarkList.add(uploadPDF);
                        }
                    }
                }
                bookmarkAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
