package com.example.mytripbuddymb.model;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mytripbuddymb.R;


import java.util.ArrayList;


public class ImagePageAdapter extends RecyclerView.Adapter<ImagePageAdapter.ImageViewHolder> {

    private ArrayList<Uri>images;

    public ImagePageAdapter(ArrayList<Uri> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_page, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = images.get(position);
        Glide.with(holder.imageView.getContext())
                .load(uri)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImage(Uri uri) {
        images.add(uri);
        notifyItemInserted(images.size() - 1);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_page_item);
        }
    }
}
