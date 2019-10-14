package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.myapp.Teachernew.PICK_PDF_CODE;

public class CreateNotice extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    Button upload;
    TextView txtuploadstatus;
    EditText noticecontent;
    EditText noticename;

   String mid="";
    String matter="";
    String name="";
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference uploadReference;
    DatabaseReference studreference;
    DateFormat dateFormat=new SimpleDateFormat();
    Date date=new Date();
    String createdtime=dateFormat.format(date);
    UploadTask uploadTask;

    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);
        noticecontent=(EditText) findViewById(R.id.notice);
        upload=(Button)findViewById(R.id.btnupload);
        txtuploadstatus=(TextView)findViewById(R.id.uploadstatus);
        noticename=(EditText)findViewById(R.id.creatednoticename);
        progressBar=(ProgressBar)findViewById(R.id.progressBarnotice);


      //  matter=noticecontent.getText().toString();
        uploadReference=FirebaseDatabase.getInstance().getReference("uploads");
        studreference=FirebaseDatabase.getInstance().getReference("Stud");
        getUsername();
        if (Build.VERSION.SDK_INT >= 23)
        {

            if(ContextCompat.checkSelfPermission(CreateNotice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(CreateNotice.this,noticecontent.getText().toString(),Toast.LENGTH_LONG).show();
                        createandDisplayPdf(noticecontent.getText().toString());
                    }
                });
            }
            else
            {
                ActivityCompat.requestPermissions(CreateNotice.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},9);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //Toast.makeText(CreateNotice.this,noticecontent.getText().toString(),Toast.LENGTH_LONG).show();
                    createandDisplayPdf(noticecontent.getText().toString());
                }
            });
        }
        else
        {
            Toast.makeText(CreateNotice.this,"Please provide permissions",Toast.LENGTH_LONG).show();
        }
    }

    public void createandDisplayPdf(String text) {

       Document doc=new Document();


        try {
            String path = Environment.getExternalStorageDirectory().getPath()+"/mypdf/";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, noticename.getText().toString()+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);


            PdfWriter.getInstance(doc,fOut);
            doc.open();
            Paragraph p1 = new Paragraph(text);
            doc.add(p1);
            //open the document
           // doc.open();
            //Paragraph p1 = new Paragraph(text);
            //doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf(noticename.getText().toString()+".pdf","mypdf");
    }
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        final String x=noticename.getText().toString();
         progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference("uploads/");
        storageReference= storageReference.child(x+".pdf");
        uploadTask = storageReference.putFile(path);

               uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                progressBar.setVisibility(View.GONE);
                                txtuploadstatus.setText("File Uploaded Successfully");


                                mid=uploadReference.push().getKey();

                                UploadPDF upload = new UploadPDF(noticename.getText().toString(), uri.toString(),name,createdtime,mid);
                                uploadReference.child(mid).setValue(upload);
                                noticename=null;


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
                        txtuploadstatus.setText((int) progress + "% Uploading...");
                    }
                });




    }


    public void getUsername() {

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            studreference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("fullname").getValue() != null) {
                        Toast.makeText(CreateNotice.this, dataSnapshot.child("fullname").getValue().toString(), Toast.LENGTH_LONG).show();
                        name = dataSnapshot.child("fullname").getValue().toString();
                       // display.setText(name);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }




}
