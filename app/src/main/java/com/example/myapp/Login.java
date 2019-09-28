package com.example.myapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    // TODO: Add member variables here:
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.username_text);
        mPasswordView = (EditText) findViewById(R.id.password_text);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 200 || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth

    }

    // Executed when Sign in button pressed
    public void onClickLogin(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();
    }

    // Executed when Register button pressed
    public void onClickSignUp(View v) {
        Intent intent = new Intent(Login.this,SignUp.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

      //  Toast.makeText(Login.this,userid,Toast.LENGTH_LONG).show();
        // TODO: Use FirebaseAuth to sign in with email & password
        String email=mEmailView.getText().toString().trim();
        String password=mPasswordView.getText().toString().trim();
        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password))
        {
            Toast.makeText(Login.this,"Please fill fields",Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                            // Read from the database
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    getandshowdatd(dataSnapshot);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                        } else {
                            Toast.makeText(Login.this,"Wrong credentials or no user available",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });


    }
    private void getandshowdatd(DataSnapshot dataSnapshot)
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        userid=user.getUid();
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {
            Stud stud=new Stud();
            stud.setId(ds.child(userid).getValue(Stud.class).getId());
            stud.setFullname(ds.child(userid).getValue(Stud.class).getFullname());
            stud.setCategory(ds.child(userid).getValue(Stud.class).getCategory());
            stud.setDepartment(ds.child(userid).getValue(Stud.class).getDepartment());
            String cat=ds.child(userid).getValue(Stud.class).getCategory();
            Toast.makeText(Login.this,cat,Toast.LENGTH_LONG).show();
            if(cat.equals("Student"))
            {

                startActivity(new Intent(getApplicationContext(),Student.class));
            }
            else if(cat.equals("Teacher"))
            {
                startActivity(new Intent(getApplicationContext(),teacher.class));
            }
        }
    }

    // TODO: Show error on screen with an alert dialog



}