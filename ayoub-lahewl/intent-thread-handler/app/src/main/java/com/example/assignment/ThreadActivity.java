package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThreadActivity extends AppCompatActivity {

    private Button btn4scd;
    private TextView textView4;
    private Button messagebtn;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thread2);
        back = findViewById(R.id.back2);
        btn4scd = findViewById(R.id.btn4scd);
        textView4 = findViewById(R.id.textView4);
        messagebtn = findViewById(R.id.messagebtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ThreadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn4scd.setOnClickListener(v -> {
            Thread thread = new Thread(() -> {
                for(int i=1; i<=5; i++)
                {
                    int number = i;
                    runOnUiThread(() -> textView4.setText(number+""));

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        });

        messagebtn.setOnClickListener(view ->
                Toast.makeText(this, "Thread is working very well!", Toast.LENGTH_SHORT).show());





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}