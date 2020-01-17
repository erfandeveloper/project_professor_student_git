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
import project.model.Post;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public PostListAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home_posts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.txt_itemListPost.setText(post.getText());
        holder.lin_itemListPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowPostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void update(ArrayList<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_itemListPost;
        TextView txt_itemListPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_itemListPost = itemView.findViewById(R.id.lin_itemListPost);
            txt_itemListPost = itemView.findViewById(R.id.txt_itemListPost);
        }
    }
}
