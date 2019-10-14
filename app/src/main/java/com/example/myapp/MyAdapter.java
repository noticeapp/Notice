package com.example.myapp;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.os.Environment;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.*;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<UploadPDF> listItems;
    private Context context;
    DatabaseReference bookmarkReference = FirebaseDatabase.getInstance().getReference("Bookmark");
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


    public MyAdapter(List<UploadPDF> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final UploadPDF listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getName());
        // holder.textViewTag.setText(listItem.getTag());
        holder.textViewCreated.setText(listItem.getCreated());
        holder.textViewUploaded.setText(listItem.getUploaded());

        final Bookmarkcontent bookmarkcontent = new Bookmarkcontent(listItem.getNoticeid(),firebaseAuth.getCurrentUser().getEmail());


        bookmarkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String noticeid =  snapshot.child("noticeid").getValue().toString();
                    String studentid = snapshot.child("studentid").getValue().toString();
                    if(noticeid.equals(listItem.getNoticeid()) && studentid.equals(firebaseAuth.getCurrentUser().getEmail())){
                        holder.toggleButton.setChecked(true);
                        snapshot.getRef().removeValue(); // jugad pathtime
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.toggleButton.setChecked(false);
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    bookmarkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                String noticeid =  snapshot.child("noticeid").getValue().toString();
                                String studentid = snapshot.child("studentid").getValue().toString();
                                if(noticeid.equals(listItem.getNoticeid()) && studentid.equals(firebaseAuth.getCurrentUser().getEmail())){
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    bookmarkReference.push().setValue(bookmarkcontent);
                }
                else
                {
                    bookmarkReference.orderByChild("noticeid").equalTo(listItem.getNoticeid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {
                                if(postSnapshot.child("studentid").getValue().toString().equals(firebaseAuth.getCurrentUser().getEmail()))
                                {
                                    postSnapshot.getRef().removeValue();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });


        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting the upload
                UploadPDF upload = listItems.get(position);

                String fileName = upload.getName();
                Log.d("File name " , fileName);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName+".pdf");

                Log.d("File  ",file.toString());
                if(file.exists()){
                    Toast.makeText(context,"File Already Downloaded",Toast.LENGTH_SHORT).show();
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfOpenintent.setDataAndType(path, "application/pdf");
                    try {
                        context.startActivity(pdfOpenintent);
                    }
                    catch (ActivityNotFoundException e) {

                    }

                }
                else {
                    //Opening the upload file in browser using the upload url
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(upload.getUrl()));
                    context.startActivity(intent);
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textViewName;
        public TextView textViewTag;
        public TextView textViewUploaded;
        public TextView textViewCreated;
        public ToggleButton toggleButton;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.noticeName);
            textViewTag = (TextView) itemView.findViewById(R.id.tag);
            textViewCreated = (TextView) itemView.findViewById(R.id.Created);
            textViewUploaded = (TextView) itemView.findViewById(R.id.upload);
            toggleButton=(ToggleButton)itemView.findViewById(R.id.button_bookmark);


        }


    }


}