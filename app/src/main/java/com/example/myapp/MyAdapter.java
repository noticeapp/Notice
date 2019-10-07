package com.example.myapp;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<UploadPDF> listItems;
    private Context context;

    DatabaseReference bookmarkReference = FirebaseDatabase.getInstance().getReference("Bookmarks");

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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final UploadPDF listItem = listItems.get(position);
        holder.textViewName.setText(listItem.getName());
        holder.textViewTag.setText(listItem.getTag());
        holder.textViewCreated.setText(listItem.getCreated());
        holder.textViewUploaded.setText(listItem.getUploaded());

        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                        bookmarkReference.child("noticeID").setValue(listItems.get(position).getNoticeid());
                }
                else {

                    bookmarkReference.orderByChild("noticeID").equalTo(listItems.get(position).getNoticeid());
                }
            }
        });

        holder.onClick(position);

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
            toggleButton = itemView.findViewById(R.id.button_favorite);

        }
        public void onClick(final int position)
        {
            textViewName.setOnClickListener(new View.OnClickListener() {
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

    }


}
