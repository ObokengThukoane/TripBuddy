package com.example.mytripbuddymb.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.example.mytripbuddymb.MainActivity;
import com.example.mytripbuddymb.R;
import com.example.mytripbuddymb.database.MemoryDatabaseHelper;
import com.example.mytripbuddymb.model.FullMemoryActivity;
import com.example.mytripbuddymb.model.Memory;
import com.example.mytripbuddymb.model.PhotoGridAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;

public class CreateMemory extends AppCompatActivity {

    private EditText editTitle, editNotes;
    private Button btnAddImages, btnAddSong, btnSave, btnPlay, btnPause, btnStop, btnBack;
    private CardView headerCard, photoUploadCard, musicCard, detailsCard;
    private LinearLayout musicControls;
    private ImageView musicIcon, dateIcon;
    private TextView headerTitle, headerSubtitle, musicTitle, memoryDate;
    private RecyclerView recyclerView;
    private PhotoGridAdapter photoGridAdapter;

    private String selectedSongUri = null;
    private static final int PICK_IMAGES = 1;
    private static final int PICK_SONG = 2;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> selectedImages = new ArrayList<>();

    // Animation objects
    private Animation fadeInAnim, slideUpAnim, scaleInAnim, buttonClickAnim, buttonReleaseAnim, loadingAnim;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_memory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize animations
        initializeAnimations();
        
        // Initialize views
        initializeViews();
        
        // Setup click listeners with animations
        setupClickListeners();
        

        startEntranceAnimations();
    }

    private void initializeAnimations() {
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        scaleInAnim = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        buttonClickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click);
        buttonReleaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release);
        loadingAnim = AnimationUtils.loadAnimation(this, R.anim.loading_rotation);
    }

    private void initializeViews() {
        // Initialize form elements
        editTitle = findViewById(R.id.memory_title);
        editNotes = findViewById(R.id.memory_description);
        btnAddImages = findViewById(R.id.btn_upload_photos);
        btnAddSong = findViewById(R.id.btn_upload_song);
        btnSave = findViewById(R.id.button_save_memory);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnBack = findViewById(R.id.Back);

        // Initialize cards
        headerCard = findViewById(R.id.header_card);
        photoUploadCard = findViewById(R.id.photo_upload_card);
        musicCard = findViewById(R.id.music_card);
        detailsCard = findViewById(R.id.details_card);

        // Initialize other UI elements
        musicControls = findViewById(R.id.music_controls);
        musicIcon = findViewById(R.id.music_icon);
        dateIcon = findViewById(R.id.date_icon);
        headerTitle = findViewById(R.id.header_title);
        headerSubtitle = findViewById(R.id.header_subtitle);
        musicTitle = findViewById(R.id.music_title);
        memoryDate = findViewById(R.id.memory_date);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.photo_grid_recycler);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 3));
        photoGridAdapter = new PhotoGridAdapter(this, selectedImages);
        recyclerView.setAdapter(photoGridAdapter);

        // Set current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        memoryDate.setText("Date: " + currentDate);
    }

    private void setupClickListeners() {
        // Back button with animation
        btnBack.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                Intent intent = new Intent(CreateMemory.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_up);
            });
        });


        btnAddImages.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES);
            });
        });

        // Upload song button with animation
        btnAddSong.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_SONG);
            });
        });

        // Music control buttons with animations
        btnPlay.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                if (selectedSongUri != null) {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(CreateMemory.this, Uri.parse(selectedSongUri));
                    }
                    mediaPlayer.start();
                    // Animate music icon
                    musicIcon.startAnimation(loadingAnim);
                }
            });
        });

        btnPause.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    musicIcon.clearAnimation();
                }
            });
        });

        btnStop.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    musicIcon.clearAnimation();
                }
            });
        });

        // Save button with loading animation
        btnSave.setOnClickListener(v -> {
            animateButtonClick(v, () -> {
                btnSave.setText("Saving...");
                btnSave.setEnabled(false);
                btnSave.startAnimation(loadingAnim);
                
                // Simulate save process with delay
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    saveMemory();
                }, 1500);
            });
        });


        addTouchFeedback(btnAddImages, btnAddSong, btnPlay, btnPause, btnStop, btnSave, btnBack);
        

        setupScrollAnimations();
    }

    private void startEntranceAnimations() {
        // Stagger the entrance animations
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            headerCard.startAnimation(fadeInAnim);
        }, 100);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            photoUploadCard.startAnimation(slideUpAnim);
        }, 300);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            musicCard.startAnimation(slideUpAnim);
        }, 500);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            detailsCard.startAnimation(slideUpAnim);
        }, 700);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            btnSave.startAnimation(scaleInAnim);
        }, 900);
    }

    private void animateButtonClick(View button, Runnable action) {
        button.startAnimation(buttonClickAnim);
        buttonClickAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(buttonReleaseAnim);
                action.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void addTouchFeedback(Button... buttons) {
        for (Button button : buttons) {
            button.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(buttonClickAnim);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(buttonReleaseAnim);
                        break;
                }
                return false; // Let the normal click listener handle the action
            });
        }
    }

    private void setupScrollAnimations() {
        androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.scroll_container);
        scrollView.setOnScrollChangeListener(new androidx.core.widget.NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(androidx.core.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Trigger animations when elements come into view
                checkAndAnimateOnScroll();
            }
        });
    }

    private void checkAndAnimateOnScroll() {
        // Get the scroll view
        androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.scroll_container);
        LinearLayout contentContainer = findViewById(R.id.content_container);
        
        // Check if elements are visible and animate them
        animateIfVisible(headerCard, scrollView, contentContainer);
        animateIfVisible(photoUploadCard, scrollView, contentContainer);
        animateIfVisible(musicCard, scrollView, contentContainer);
        animateIfVisible(detailsCard, scrollView, contentContainer);
        animateIfVisible(btnSave, scrollView, contentContainer);
    }

    private void animateIfVisible(View view, androidx.core.widget.NestedScrollView scrollView, View contentContainer) {
        if (view.getTag() != null && (Boolean) view.getTag()) {
            return; // Already animated
        }
        
        int[] viewLocation = new int[2];
        view.getLocationInWindow(viewLocation);
        
        int scrollViewTop = scrollView.getTop();
        int scrollViewBottom = scrollView.getBottom();
        
        // Check if the view is visible in the scroll view
        if (viewLocation[1] >= scrollViewTop && viewLocation[1] <= scrollViewBottom) {
            view.setTag(true); // Mark as animated
            view.startAnimation(fadeInAnim);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    imageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                        } catch (Exception ignore) {}
                        selectedImages.add(imageUri.toString());
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    try {
                        getContentResolver().takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    } catch (Exception ignore) {}
                    selectedImages.add(imageUri.toString());
                }
                
                // Animate the photo grid update
                recyclerView.startAnimation(fadeInAnim);
                photoGridAdapter.notifyDataSetChanged();

            } else if (requestCode == PICK_SONG) {
                Uri songUri = data.getData();
                if (songUri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(
                                songUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    } catch (Exception ignore) {}
                    selectedSongUri = songUri.toString();
                    
                    // Animate music controls appearance
                    musicControls.startAnimation(scaleInAnim);
                    Toast.makeText(this, "Song selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveMemory() {
        String title = editTitle.getText().toString();
        String notes = editNotes.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        MemoryDatabaseHelper helper = new MemoryDatabaseHelper(this);
        android.database.sqlite.SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            android.content.ContentValues m = new android.content.ContentValues();
            m.put("title", title);
            m.put("notes", notes);
            m.put("date", date);
            m.put("songUri", selectedSongUri);
            m.put("mood", (String) null);
            m.put("location", (String) null);
            m.put("createdAt", System.currentTimeMillis());
            long memoryId = db.insertOrThrow("memories", null, m);

            for (String uri : selectedImages) {
                android.content.ContentValues i = new android.content.ContentValues();
                i.put("memoryId", memoryId);
                i.put("imageUri", uri);
                db.insertOrThrow("memory_images", null, i);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        // Stop loading animation and show success
        btnSave.clearAnimation();
        btnSave.setText("Memory Saved!");
        btnSave.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // Green

        Toast.makeText(this, "Memory saved!", Toast.LENGTH_SHORT).show();

        // Navigate to gallery with transition animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, ViewGallery.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_up);
            finish();
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                mediaPlayer.stop();
            } catch (Exception ignore) {}
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}



