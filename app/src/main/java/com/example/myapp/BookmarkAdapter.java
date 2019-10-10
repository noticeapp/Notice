package com.example.myapp;
import android.content.ActivityNotFoundException;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;


public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<UploadPDF> listItems;
    private Context context;

    DatabaseReference uploadref= FirebaseDatabase.getInstance().getReference("uploads");
    DatabaseReference studref= FirebaseDatabase.getInstance().getReference("Stud");
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    public BookmarkAdapter(List<UploadPDF> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_items,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        UploadPDF listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getName());
        // holder.textViewTag.setText(listItem.getTag());
        holder.textViewCreated.setText(listItem.getCreated());
        holder.textViewUploaded.setText(listItem.getUploaded());

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


        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.BookmarkNoticeName);
            textViewTag = (TextView) itemView.findViewById(R.id.Bookmarktag);
            textViewCreated = (TextView) itemView.findViewById(R.id.BookmarkCreated);
            textViewUploaded = (TextView) itemView.findViewById(R.id.Bookmarkupload);


        }

    }

}


