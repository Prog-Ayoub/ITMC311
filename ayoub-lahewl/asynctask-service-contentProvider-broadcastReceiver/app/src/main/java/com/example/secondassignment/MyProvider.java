package com.example.secondassignment;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.ArrayList;

public class MyProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.secondassignment.provider";
    private static final String TABLE_NAME = "items";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final String[] COLUMNS = {"id", "value"};

    private static final ArrayList<String> data = new ArrayList<>();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(COLUMNS);
        for (int i = 0; i < data.size(); i++) {
            cursor.addRow(new Object[]{i, data.get(i)});
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String value = values.getAsString("value");
        data.add(value);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(data.size() - 1));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        data.clear();
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
    }
}
