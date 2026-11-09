package com.example.mytripbuddymb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TripRepository {
    private final MemoryDatabaseHelper dbHelper;

    public TripRepository(Context context) {
        this.dbHelper = new MemoryDatabaseHelper(context);
    }

    public static class LineItem {
        public long id;
        public String label;
        public int amountCents;
        public String type;
    }

    public long insertTripWithLineItems(String destination,
                                        String startDateIso,
                                        String endDateIso,
                                        String notes,
                                        List<LineItem> itemsCents) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues t = new ContentValues();
            t.put("destination", destination);
            t.put("startDate", startDateIso);
            t.put("endDate", endDateIso);
            t.put("notes", notes);
            t.put("createdAt", System.currentTimeMillis());
            long tripId = db.insertOrThrow("trips", null, t);

            if (itemsCents != null) {
                for (LineItem item : itemsCents) {
                    ContentValues li = new ContentValues();
                    li.put("tripId", tripId);
                    li.put("label", item.label);
                    li.put("amountCents", item.amountCents);
                    li.put("type", item.type);
                    db.insertOrThrow("trip_line_items", null, li);
                }
            }

            db.setTransactionSuccessful();
            return tripId;
        } finally {
            db.endTransaction();
        }
    }

    public List<LineItem> getLineItemsForTrip(long tripId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<LineItem> items = new ArrayList<>();
        try (Cursor c = db.query(
                "trip_line_items",
                new String[]{"id","label","amountCents","type"},
                "tripId = ?",
                new String[]{ String.valueOf(tripId) },
                null, null, "id ASC"
        )) {
            while (c.moveToNext()) {
                LineItem li = new LineItem();
                li.id = c.getLong(0);
                li.label = c.getString(1);
                li.amountCents = c.getInt(2);
                li.type = c.getString(3);
                items.add(li);
            }
        }
        return items;
    }
}


