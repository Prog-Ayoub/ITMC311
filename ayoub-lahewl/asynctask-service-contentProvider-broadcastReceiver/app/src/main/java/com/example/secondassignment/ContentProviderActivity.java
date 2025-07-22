package com.example.secondassignment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ContentProviderActivity extends AppCompatActivity {

    private static final Uri CONTENT_URI = Uri.parse("content://com.example.secondassignment.provider/items");
    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        Button insertBtn = findViewById(R.id.insertBtn);
        Button loadBtn = findViewById(R.id.loadBtn);
        TextView resultText = findViewById(R.id.resultText);

        insertBtn.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("value", "Example " + time);
            getContentResolver().insert(CONTENT_URI, values);
        });

        loadBtn.setOnClickListener(v -> {
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
            StringBuilder builder = new StringBuilder();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    builder.append(cursor.getString(0)).append(" - ").append(cursor.getString(1)).append("\n");
                }
                cursor.close();
            }
            resultText.setText(builder.toString());
        });
    }
}