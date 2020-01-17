package project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.erfan.prostd.R;
import project.activity.ShowPostActivity;
import project.model.Comment;
import project.model.Post;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public CommentListAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        String sender = comment.getUsername() + " گفته: ";
        holder.txt_whoWriteComment.setText(sender);
        holder.txt_commentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void update(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_whoWriteComment;
        TextView txt_commentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_whoWriteComment = itemView.findViewById(R.id.txt_whoWriteComment);
            txt_commentText = itemView.findViewById(R.id.txt_commentText);
        }
    }
}
