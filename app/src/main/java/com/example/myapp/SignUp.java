package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    // UI references.
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private EditText mname;
    private EditText mid;
    private EditText mpassword;
    private EditText mconfirmpassword;
    RadioButton rstudent;
    RadioButton rteacher;
    RadioButton rcomp;
    RadioButton rit;
    RadioButton rentc;
    String  department="";
    String category="",named="",emailid="";

    // Firebase instance variables
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

         mname= (EditText) findViewById(R.id.txtname);
        mid = (EditText) findViewById(R.id.txtid);
        mpassword= (EditText) findViewById(R.id.txtpassword);
        mconfirmpassword = (EditText) findViewById(R.id.txtconfirmPassword);
        rstudent=(RadioButton)findViewById(R.id.rbstudent);
        rteacher=(RadioButton)findViewById(R.id.rbteacher);
        rcomp=(RadioButton)findViewById(R.id.rbCOMP);
        rit=(RadioButton)findViewById(R.id.rbIT);
        rentc=(RadioButton)findViewById(R.id.rbENTC);
        databaseReference= FirebaseDatabase.getInstance().getReference("Stud");

        // Keyboard sign in action
        mconfirmpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 200 || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });


        // TODO: Get hold of an instance of FirebaseAuth


    }

    // Executed when Sign Up button is pressed.
    public void signupclick(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mid.setError(null);
        mpassword.setError(null);
        mname.setError(null);
        mconfirmpassword.setError(null);


        // Store values at the time of the login attempt.
        String email = mid.getText().toString();
        String password = mpassword.getText().toString();
        String confirmpassword=mconfirmpassword.getText().toString();
        String name=mname.getText().toString();

        named=name;
        emailid=email;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password,confirmpassword)) {
            mpassword.setError(getString(R.string.error_invalid_password));
            focusView = mpassword;
            cancel = true;
        }
        if(!rstudent.isChecked() || !rteacher.isChecked())
        {
            cancel=true;
        }
        if(!rentc.isChecked() || !rit.isChecked() || !rcomp.isChecked())
        {
            cancel=true;
        }
        //radio buttons
        if(rstudent.isChecked())
        {
            category="Student";
        }
        if(rteacher.isChecked())
        {
            category="Teacher";
        }

        if(rcomp.isChecked())
        {
            department="COMP";
        }

        if(rit.isChecked())
        {
            department="IT";
        }

        if(rentc.isChecked())
        {
            department="ENTC";
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mid.setError(getString(R.string.error_field_required));
            focusView = mid;
            cancel = true;
        }
        if(!isEmailValid(email)) {
            mid.setError(getString(R.string.error_invalid_email));
            focusView = mid;
            cancel = true;
        }
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(SignUp.this,"Please enter full name",Toast.LENGTH_LONG).show();
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Toast.makeText(SignUp.this,"Registration failed due to  invalid constraints or empty fields",Toast.LENGTH_LONG).show();

            focusView.requestFocus();
        } else {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Stud info=new Stud(named,emailid,department,category);

                                FirebaseDatabase.getInstance().getReference("Stud").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(SignUp.this,"Registered successfully",Toast.LENGTH_LONG).show();
                                        if(category.equals("Student"))
                                        {
                                            startActivity(new Intent(getApplicationContext(),Student.class));
                                        }
                                        else if(category.equals("Teacher"))
                                        {
                                            startActivity(new Intent(getApplicationContext(),teacher.class));
                                        }
                                       // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }
                                });

                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));


                            } else {
                                Toast.makeText(SignUp.this,"Registration failed",Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });


        }//else

    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password,String cp) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        if(password.equals(cp)&& password.length()>4) {
            return true;
        }
        else
            return false;
    }
}
