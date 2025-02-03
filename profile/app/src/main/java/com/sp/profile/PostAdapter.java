package com.sp.profile;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.postImage.setImageResource(postList.get(position).getImageResId());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}