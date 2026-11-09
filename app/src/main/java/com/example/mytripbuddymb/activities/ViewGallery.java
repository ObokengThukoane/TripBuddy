package com.example.mytripbuddymb.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytripbuddymb.MainActivity;
import com.example.mytripbuddymb.R;
import com.example.mytripbuddymb.database.MemoryDatabaseHelper;
import com.example.mytripbuddymb.model.FullMemoryActivity;
import com.example.mytripbuddymb.model.Memory;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewGallery extends AppCompatActivity {
    private final List<Album> albums = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_gallery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewGallery.this, MainActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadAlbums();
        recyclerView.setAdapter(new AlbumAdapter());
    }

    private void loadAlbums() {
        MemoryDatabaseHelper helper = new MemoryDatabaseHelper(this);
        android.database.sqlite.SQLiteDatabase db = helper.getReadableDatabase();


        try (android.database.Cursor m = db.query(
                "memories",
                new String[]{"id","title","notes","songUri"},
                null, null, null, null,
                "createdAt DESC"
        )) {
            while (m.moveToNext()) {
                long memoryId = m.getLong(0);
                String title = m.getString(1);
                String notes = m.getString(2);
                String songUri = m.getString(3);


                String cover = null;
                ArrayList<String> images = new ArrayList<>();
                try (android.database.Cursor c = db.query(
                        "memory_images",
                        new String[]{"imageUri"},
                        "memoryId=?",
                        new String[]{String.valueOf(memoryId)},
                        null, null,
                        "id ASC"
                )) {
                    while (c.moveToNext()) {
                        String uri = c.getString(0);
                        if (cover == null) cover = uri;
                        images.add(uri);
                    }
                }
                if (cover != null) {
                    albums.add(new Album(memoryId, title, notes, songUri, images, cover));
                }
            }
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
        @Override
        public AlbumViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_album, parent, false);
            return new AlbumViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AlbumViewHolder holder, int position) {
            Album a = albums.get(position);
            holder.title.setText(a.title);
            holder.caption.setText(a.notes);
            Glide.with(holder.cover.getContext()).load(a.coverUri).centerCrop().into(holder.cover);
            holder.itemView.setOnClickListener(v -> openAlbum(a));
        }

        @Override
        public int getItemCount() { return albums.size(); }
    }

    private static class AlbumViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView cover;
        android.widget.TextView title;
        android.widget.TextView caption;
        AlbumViewHolder(android.view.View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.album_cover);
            title = itemView.findViewById(R.id.album_title);
            caption = itemView.findViewById(R.id.album_caption);
        }
    }

    private void openAlbum(Album album) {
        // Pass to FullMemoryActivity via Memory model
        com.example.mytripbuddymb.model.Memory mem = new com.example.mytripbuddymb.model.Memory(
                album.title,
                album.notes,
                null,
                new ArrayList<>(album.images),
                album.songUri
        );
        Intent intent = new Intent(this, com.example.mytripbuddymb.model.FullMemoryActivity.class);
        intent.putExtra("memory", mem);
        startActivity(intent);
    }

    private static class Album {
        long memoryId;
        String title;
        String notes;
        String songUri;
        List<String> images;
        String coverUri;
        Album(long memoryId, String title, String notes, String songUri, List<String> images, String coverUri) {
            this.memoryId = memoryId;
            this.title = title;
            this.notes = notes;
            this.songUri = songUri;
            this.images = images;
            this.coverUri = coverUri;
        }
    }
}