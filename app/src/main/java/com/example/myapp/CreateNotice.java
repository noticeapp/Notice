package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateNotice extends AppCompatActivity {

    Button upload;
    TextView txtuploadstatus;
    EditText noticecontent;
    EditText noticename;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);
        noticecontent=(EditText)findViewById(R.id.notice);
        upload=(Button)findViewById(R.id.btnupload);
        txtuploadstatus=(TextView)findViewById(R.id.uploadstatus);
        noticename=(EditText)findViewById(R.id.creatednoticename);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }
}
