package com.example.myapp;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Environment;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;


public class DisscusionAdapter extends RecyclerView.Adapter<DisscusionAdapter.ViewHolder> {

    private List<DiscussionPost> listItems;
    private Context context;

    public DisscusionAdapter(List<DiscussionPost> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DiscussionPost listItem = listItems.get(position);
        holder.textViewTitle.setText(listItem.getMtitle());
        holder.textViewUserName.setText(listItem.getMuser());
        holder.textViewContent.setText(listItem.getMcontent());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textViewTitle;
        public TextView textViewUserName;
        public TextView textViewContent;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.postTitle);
            textViewUserName = (TextView) itemView.findViewById(R.id.postUsername);
            textViewContent = (TextView) itemView.findViewById(R.id.postContent);

        }
    }
}
