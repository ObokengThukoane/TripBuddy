package com.example.mytripbuddymb.model;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mytripbuddymb.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class FullMemoryActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnPlay, btnPause, btnStop;
    private MediaPlayer mediaPlayer;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_memory);

        TextView title = findViewById(R.id.full_memory_title);
        TextView notes = findViewById(R.id.full_memory_notes);
        viewPager = findViewById(R.id.full_memory_viewpager);
        btnPlay = findViewById(R.id.btn_play_song);
        btnPause = findViewById(R.id.btn_pause_song);
        btnStop = findViewById(R.id.btn_stop_song);
        android.widget.ImageView btnBack = findViewById(R.id.btn_back);

        memory = (Memory) getIntent().getSerializableExtra("memory");

        String songUri = null;
        ArrayList<String> imageStrs;

        if (memory != null) {
            // Opened from an album/memory
            title.setText(memory.getTitle());
            notes.setText(memory.getNotes());
            songUri = memory.getSongUri();
            imageStrs = memory.getImagesUris();
        } else {

            title.setVisibility(View.GONE);
            notes.setVisibility(View.GONE);
            songUri = getIntent().getStringExtra("songUri");
            imageStrs = getIntent().getStringArrayListExtra("imageUris");
        }

        ArrayList<Uri> uriList = new ArrayList<>();
        if (imageStrs != null) {
            for (String s : imageStrs) uriList.add(Uri.parse(s));
        }

            ImagePageAdapter adapter = new ImagePageAdapter(uriList);
            viewPager.setAdapter(adapter);

        viewPager.setPageTransformer((page, position) -> {
            page.setAlpha(0f);
            page.animate().alpha(1f).setDuration(250).start();
        });
        final String finalSongUri = songUri;

        btnPlay.setOnClickListener(v -> {
            if (finalSongUri == null) return;
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, Uri.parse(finalSongUri));
            }
            mediaPlayer.start();
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });
        btnStop.setOnClickListener(v -> stopAndRelease());

        btnBack.setOnClickListener(v -> onBackPressed());
    }
private void stopAndRelease() {
    if (mediaPlayer != null) {
        try {
            mediaPlayer.stop();
        } catch (IllegalStateException ignored) {}
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
@Override
protected void onPause() {
    super.onPause();
    // lifecycle: pause music when leaving screen
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
        mediaPlayer.pause();
    }
}

@Override
protected void onDestroy() {
    stopAndRelease();
    super.onDestroy();
    }
}

