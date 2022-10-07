package com.example.travelist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelist.Mekanlar;
import com.example.travelist.Model.PostModel;
import com.example.travelist.PostDetailsActivity;
import com.example.travelist.R;
import com.example.travelist.TurlerActivity;

import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {
    Context context;
    List<PostModel> postModelList;

    public PostAdapter(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.home_post,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.postTitle.setText(postModelList.get(position).getpTitle());
        Glide.with(context).load(postModelList.get(position).getpImage()).into(holder.postImage);


       /* String title=postModelList.get(position).getpTitle();
        String description=postModelList.get(position).getpDescription();
        String image=postModelList.get(position).getpImage();

        holder.postTitle.setText(title);
        holder.postDescription.setText(description);

        Glide.with(context).load(image).into(holder.postImage);*/

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView postTitle,postemail;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            postImage=itemView.findViewById(R.id.postImage);
            postTitle=itemView.findViewById(R.id.postTitle);
         //   postemail=itemView.findViewById(R.id.postemail);
            //postDescription=itemView.findViewById(R.id.postDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent postDetailActivity=new Intent(context,PostDetailsActivity.class);
                    int position=getAdapterPosition();

                    postDetailActivity.putExtra("pImage",postModelList.get(position).getpImage());
                    postDetailActivity.putExtra("pTitle",postModelList.get(position).getpTitle());
                    postDetailActivity.putExtra("uEmail",postModelList.get(position).getuEmail());
                    postDetailActivity.putExtra("pDescription",postModelList.get(position).getpDescription());

                    postDetailActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(postDetailActivity);





                }
            });


        }
    }

}
