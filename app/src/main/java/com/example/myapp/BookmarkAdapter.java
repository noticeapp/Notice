package com.example.myapp;

import android.app.LauncherActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;
import java.util.Vector;


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
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UploadPDF listItem = listItems.get(position);

            holder.textViewName.setText(listItem.getName());
            // holder.textViewTag.setText(listItem.getTag());
            holder.textViewCreated.setText(listItem.getCreated());
            holder.textViewUploaded.setText(listItem.getUploaded());

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

