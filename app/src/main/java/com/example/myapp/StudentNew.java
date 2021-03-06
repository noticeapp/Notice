package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.StrictMode;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView display;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;

    //database reference to get uploads data
    DatabaseReference mDatabaseReference ;

    //list to store uploads data
    List<UploadPDF> uploadList;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Stud");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        display = (TextView) headerView.findViewById(R.id.displayEmail);
        getUsername();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());





        uploadList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //displaying it to list
        adapter = new MyAdapter(uploadList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

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

    private void getUsername() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
          databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  if(dataSnapshot.child("fullname").getValue()!=null)
                  {
                      String name = dataSnapshot.child("fullname").getValue().toString();
                      display.setText(name);
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_notices) {
            // Handle the notice action

            Intent myIntent = new Intent(StudentNew.this,Notices.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);

        } else*/ if (id == R.id.nav_forum) {

            Intent myIntent = new Intent(getApplicationContext(),Discussion.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_bookmark) {

            Intent myIntent = new Intent(StudentNew.this,Bookmark.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_logOut) {

            firebaseAuth.signOut();
            Intent myIntent = new Intent(StudentNew.this,Login.class);
            startActivity(myIntent);

            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
