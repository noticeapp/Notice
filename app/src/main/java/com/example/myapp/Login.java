package com.example.myapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.*;

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

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(getApplicationContext(),Discussion.class));

            return;
        }

        mEmailView = (EditText) findViewById(R.id.username_text);
        mPasswordView = (EditText) findViewById(R.id.password_text);
        firebaseAuth=FirebaseAuth.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference("Stud");
        //  myRef=FirebaseDatabase.getInstance().getReference("Stud").child("Stud");




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


        //  Toast.makeText(Login.this,userid,Toast.LENGTH_LONG).show();
        // TODO: Use FirebaseAuth to sign in with email & password
        String email=mEmailView.getText().toString().trim();
        String password=mPasswordView.getText().toString().trim();
        if(!validateForm())
        {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                     // Sign in success, update UI with the signed-in user's information
                            //   Log.d(TAG, "signInWithEmail:success");


                            Toast.makeText(Login.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                myRef.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("category").getValue()!=null) {
                                            String category = dataSnapshot.child("category").getValue().toString();
                                            if (category.equals("Student")) {
                                                startActivity(new Intent(getApplicationContext(), StudentNew.class));
                                            } else {
                                                startActivity(new Intent(getApplicationContext(), Teachernew.class));
                                            }
                                        }



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                        }else
                        {

                            Log.d("Login","Failed");
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }




                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Required.");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Required.");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }


    // TODO: Show error on screen with an alert dialog



}