package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HandlerActivity extends AppCompatActivity {

    private TextView textResult;
    private Handler handlerMessage;
    private Handler handlerRunnable;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        back = findViewById(R.id.back);
        Button btnRunnable = findViewById(R.id.btnRunnable);
        Button btnMessage = findViewById(R.id.btnMessage);
        textResult = findViewById(R.id.textResult);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HandlerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        handlerRunnable = new Handler();
        btnRunnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerRunnable.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textResult.setText("Updated using Runnable after 2 seconds");
                    }
                }, 2000);
            }
        });

        handlerMessage = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String data = (String) msg.obj;
                textResult.setText("Message received from background thread:\n" + data);
            }
        };

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread backgroundThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = "Hello from background thread!";
                        Message message = handlerMessage.obtainMessage();
                        message.obj = result;
                        handlerMessage.sendMessage(message);
                    }
                });
                backgroundThread.start();
            }
        });
    }
}