package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> listItems;

    public CommentAdapter(List<Comment> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comment listItem = listItems.get(position);
        holder.mcommentContent.setText(listItem.getMcomment());
        holder.mcommenter.setText(listItem.getMname());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mcommenter;
        TextView mcommentContent;


        public ViewHolder(View itemView) {
            super(itemView);

            mcommenter = itemView.findViewById(R.id.commenter);
            mcommentContent = itemView.findViewById(R.id.commentContent);
        }

    }
}
