package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class DisscusionAdapter extends RecyclerView.Adapter<DisscusionAdapter.ViewHolder> {

    private List<DiscussionPost> listItems;
    private String mfullname;
    private String uid;
    private FirebaseDatabase firebaseDataRef = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = firebaseDataRef.getReference("DiscussionPost");
    private List<List<Comment>>commentlist;
    private Context context;
    private RecyclerView recyclerComment;
    private CommentAdapter commentAdapter;

    public DisscusionAdapter(
            List<DiscussionPost> listItems,
            String name, String uid,
            List<List<Comment>>commentlist,
            Context context
    ) {
        this.listItems = listItems;
        this.mfullname = name;
        this.uid = uid;
        this.commentlist = commentlist;
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
        holder.textPostTime.setText(listItem.getMtime());
        holder.commentUser.setText(mfullname);

        holder.addComment(position);

        commentAdapter = new CommentAdapter(commentlist.get(position));
        recyclerComment.setAdapter(commentAdapter);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewUserName;
        TextView textViewContent;
        TextView textPostTime;
        TextView commentUser;
        EditText editComment;
        Button addCommentButt;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.postTitle);
            textViewUserName = itemView.findViewById(R.id.postUsername);
            textViewContent = itemView.findViewById(R.id.postContent);
            textPostTime = itemView.findViewById(R.id.postTime);
            commentUser = itemView.findViewById(R.id.currentUser);
            addCommentButt = itemView.findViewById(R.id.addCommentButt);
            editComment = itemView.findViewById(R.id.addComment);

            recyclerComment = itemView.findViewById(R.id.recyclerComments);

            recyclerComment.setHasFixedSize(true);
            recyclerComment.setLayoutManager(new LinearLayoutManager(context));

        }

        public void addComment(final int position){
            addCommentButt.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    DiscussionPost post = listItems.get(position);

                    String comment = editComment.getText().toString().trim();
                    Comment c = new Comment(uid, mfullname, comment);

                    mDatabaseReference.child(post.getPostId()).child("Comments").push().setValue(c);
                }
            });

        }
    }
}
