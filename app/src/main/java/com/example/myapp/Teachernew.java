package com.example.myapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Teachernew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static int PICK_PDF_CODE = 2342;

    //these are the views
    TextView textViewStatus;
    EditText TextFilename;
    ProgressBar progressBar;
    TextView display;
    Button selectfile;
    Button uploadbutton;
    TextView notification;
    //the firebase objects for storage and databas
    DatabaseReference studreference;
    StorageReference storageReference;
    String download="";
    String name="";
    String mid="";
    DateFormat dateFormat=new SimpleDateFormat();
    Date date=new Date();
    String createdtime=dateFormat.format(date);
    DatabaseReference uploadreference;
    FirebaseAuth firebaseAuth;
    Uri pdfUri;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachernew);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        display = (TextView) headerView.findViewById(R.id.emaildisplay);
        uploadreference= FirebaseDatabase.getInstance().getReference("uploads");
        studreference=FirebaseDatabase.getInstance().getReference("Stud");
        firebaseAuth=FirebaseAuth.getInstance();

        //getting the views
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        selectfile=(Button)findViewById(R.id.buttonChooseFile);
        notification=(TextView)findViewById(R.id.fileselected);
        uploadbutton=(Button)findViewById(R.id.buttonUploadFile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //attaching listeners to views

        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Teachernew.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
                    getPDF();
                }
                else
                {
                    ActivityCompat.requestPermissions(Teachernew.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }

            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfUri!=null)
                {
                        uploadFile(pdfUri);

                }
                else
                {
                    Toast.makeText(Teachernew.this,"Select a file",Toast.LENGTH_LONG).show();
                }
            }
        });



        getUsername();//to get username and show on welcome screen,so we need studreference

    }
    public void getUsername() {


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            studreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("fullname").getValue() != null) {
                        Toast.makeText(Teachernew.this, dataSnapshot.child("fullname").getValue().toString(), Toast.LENGTH_LONG).show();
                        name = dataSnapshot.child("fullname").getValue().toString();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getPDF();
        }
        else
        {
            Toast.makeText(Teachernew.this,"Please provide permissions",Toast.LENGTH_LONG).show();
        }
    }
    private void getPDF() {



        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,80);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == 80 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                pdfUri=data.getData();
                notification.setText("File selected:"+data.getData().getLastPathSegment()+" .pdf");
            }else{
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {

        TextFilename = (EditText) findViewById(R.id.editTextFileName);
        String x=TextFilename.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference("uploads/");
        storageReference= storageReference.child(x+ ".pdf");
        storageReference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                progressBar.setVisibility(View.GONE);
                                textViewStatus.setText("File Uploaded Successfully");


                                mid=uploadreference.push().getKey();

                                UploadPDF upload = new UploadPDF(TextFilename.getText().toString(), uri.toString(),name,createdtime,mid);
                                uploadreference.child(mid).setValue(upload);
                                TextFilename=null;


                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText((int) progress + "% Uploading...");
                    }
                });

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.discussforum) {
            //goto discussion forum page

            Intent myIntent = new Intent(getApplicationContext(),Discussion.class);
            startActivity(myIntent);

        } else if (id == R.id.logout) {

            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            //toggle logout state

        }else if(id==R.id.viewnotice)
        {
            //should view previous uploaded notices
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null)
            {

            }

        }
        else if(id==R.id.createnotice)
        {
            //should create edittext to create new notice txt document

            startActivity(new Intent(getApplicationContext(),CreateNotice.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
