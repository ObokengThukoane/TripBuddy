package com.example.mytripbuddymb.model;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytripbuddymb.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



    public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {
        private Context context;
        private List<String> imageUris;
        private Animation fadeInAnim;

        public PhotoGridAdapter(Context context, ArrayList<String> imageUris) {
            this.context = context;
            this.imageUris = imageUris;
            this.fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_grid_photo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String uriString = imageUris.get(position);
            if (uriString != null && !uriString.isEmpty()) {
                Glide.with(context)
                        .load(uriString)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.imageView);
                

                holder.itemView.setAlpha(0f);
                holder.itemView.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .setStartDelay(position * 100)
                        .start();
            } else {
                holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        @Override
        public int getItemCount() {
            return imageUris.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.grid_photo);
            }
        }
    }
