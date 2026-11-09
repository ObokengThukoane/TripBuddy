package com.example.mytripbuddymb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tripbuddy.db";
    private static final int DATABASE_VERSION = 3;

    // Table + columns
    public static final String TABLE_MEMORIES = "memories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE_URIS = "imageUris";
    public static final String COLUMN_SONG_URI = "songUri";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TRIPID = "tripId";
    public static final String COLUMN_CREATEDAT = "createdAt";

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_TRIP_LINE_ITEMS = "trip_line_items";
    public static final String TABLE_MEMORY_IMAGES = "memory_images";

    private static final String CREATE_TABLE_MEMORIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MEMORIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_NOTES + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_IMAGE_URIS + " TEXT," +
                    COLUMN_SONG_URI + " TEXT," +
                    COLUMN_MOOD + " TEXT," +
                    COLUMN_LOCATION + " TEXT," +
                    COLUMN_CREATEDAT + " INTEGER," +
                    COLUMN_TRIPID + " INTEGER" +
                    ");";

    private static final String CREATE_TABLE_MEMORY_IMAGES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MEMORY_IMAGES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "memoryId INTEGER NOT NULL," +
                    "imageUri TEXT NOT NULL," +
                    "FOREIGN KEY(memoryId) REFERENCES " + TABLE_MEMORIES + "(id) ON DELETE CASCADE" +
                    ");";

    private static final String CREATE_TABLE_TRIPS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "destination TEXT NOT NULL," +
                    "startDate TEXT," +
                    "endDate TEXT," +
                    "notes TEXT," +
                    "createdAt INTEGER" +
                    ");";

    private static final String CREATE_TABLE_TRIP_LINE_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP_LINE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tripId INTEGER NOT NULL," +
                    "label TEXT NOT NULL," +
                    "amountCents INTEGER NOT NULL," +
                    "type TEXT NOT NULL," + // 'predefined' or 'custom'
                    "FOREIGN KEY(tripId) REFERENCES " + TABLE_TRIPS + "(id) ON DELETE CASCADE" +
                    ");";

    public MemoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all tables
        db.execSQL(CREATE_TABLE_MEMORIES);
        db.execSQL(CREATE_TABLE_MEMORY_IMAGES);
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_TRIP_LINE_ITEMS);

        // Create indexes
        createIndexes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
        if (oldVersion < 2) {

            db.execSQL("ALTER TABLE " + TABLE_MEMORIES + " ADD COLUMN " + COLUMN_MOOD + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_MEMORIES + " ADD COLUMN " + COLUMN_LOCATION + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_MEMORIES + " ADD COLUMN " + COLUMN_CREATEDAT + " INTEGER;");
            db.execSQL("ALTER TABLE " + TABLE_MEMORIES + " ADD COLUMN " + COLUMN_TRIPID + " INTEGER;");

            // Create memory_images table
            db.execSQL(CREATE_TABLE_MEMORY_IMAGES);
        }

        if (oldVersion < 3) {

            db.execSQL(CREATE_TABLE_TRIPS);
            db.execSQL(CREATE_TABLE_TRIP_LINE_ITEMS);
        }


        createIndexes(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void createIndexes(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_memory_images_memoryId ON " + TABLE_MEMORY_IMAGES + "(memoryId);");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_memories_tripId ON " + TABLE_MEMORIES + "(" + COLUMN_TRIPID + ");");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_memories_createdAt ON " + TABLE_MEMORIES + "(" + COLUMN_CREATEDAT + ");");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_trip_line_items_tripId ON " + TABLE_TRIP_LINE_ITEMS + "(tripId);");
    }
}

