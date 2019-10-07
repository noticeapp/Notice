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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.*;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<UploadPDF> listItems;
    private Context context;

    DatabaseReference uploadref= FirebaseDatabase.getInstance().getReference("uploads");
    DatabaseReference studref= FirebaseDatabase.getInstance().getReference("Stud");
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    public Vector<String> allbookmarks=new Vector<String>();

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

        UploadPDF listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getName());
       // holder.textViewTag.setText(listItem.getTag());
        holder.textViewCreated.setText(listItem.getCreated());
        holder.textViewUploaded.setText(listItem.getUploaded());

        //holder.onClick(position);
        //holder.toggle(position);

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        studref.child(firebaseAuth.getCurrentUser().getUid()).child("allbookmarks").child(listItems.get(position).getNoticeid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    holder.toggleButton.setChecked(true);
                }else{
                    holder.toggleButton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                 //   allbookmarks.add(listItems.get(position).getNoticeid());
                    Map<String,Object> bookmark = new HashMap<>();
                    bookmark.put("key",listItems.get(position).getNoticeid());

                    studref.child(firebaseAuth.getCurrentUser().getUid()).child("allbookmarks").child(listItems.get(position).getNoticeid())
                            .setValue(bookmark);
                }else{
                    studref.child(firebaseAuth.getCurrentUser().getUid()).child("allbookmarks").child(listItems.get(position).getNoticeid())
                            .removeValue();
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
