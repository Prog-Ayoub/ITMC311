package com.example.secondassignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AsyncTaskActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView percentage;
    TextView time;
    Button process;
    long startTime;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_async_task);
        progressBar = findViewById(R.id.progressBar);
        percentage = findViewById(R.id.percentage);
        time = findViewById(R.id.time);
        process = findViewById(R.id.asyncbutton);

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new MyAsyncTask().execute();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class MyAsyncTask extends AsyncTask <Void,Integer,Void>
    {
        int progress_status;
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            percentage.setVisibility(View.VISIBLE);
            progress_status = 0;
            percentage.setText("Processing 0%");
            startTime = SystemClock.uptimeMillis();
        }
        protected Void doInBackground(Void... params)
        {
            while(progress_status<100) {
                progress_status += 20;
                publishProgress (progress_status);
                SystemClock.sleep (1000);
            }
            return null;
        }
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            percentage.setText("Processing " + values[0] + "%");
        }
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            percentage.setText("Processing complete");
            process.setEnabled(true);
            endTime = SystemClock.uptimeMillis();
            long totaltime = (endTime - startTime) / 1000;
            time.setVisibility(View.VISIBLE);
            time.setText("It took " + totaltime + " seconds!");
        }
    }
}