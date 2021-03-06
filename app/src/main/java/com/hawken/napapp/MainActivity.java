package com.hawken.napapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean running;
    private TextView time;
    private TextView micro;
    private Button left;
    private Button right;
    private SimpleDateFormat format;
    private Handler h;
    private long timestamp = 0;
    private long t = 0;
    private long add = 0;

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            t = add + System.nanoTime() - timestamp;
            long micros = (t / 1000) % 1000000;
            long millis = t / 1000000;
            if (running) {
                time.setText(format.format(millis));
                micro.setText(String.format(Locale.ENGLISH, "%06d", micros));
                h.post(this);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        left = findViewById(R.id.pauseButton);
        right = findViewById(R.id.stopButton);
        right.setClickable(false);
        time =  findViewById(R.id.time);
        micro = findViewById(R.id.micro);
        h = new Handler();
    }

    public void toggle(View v) {
        if (!running) {
            running = toggleRunning(false);
            timestamp = System.nanoTime();
            h.post(update);
        } else {
            running = toggleRunning(true);
            add = t;
        }
    }

    public void stop(View v) {
        if (!running) {
            right.setTextColor(getResources().getColor(R.color.colorAccent));
            right.setClickable(false);
            add = 0;
            time.setText(R.string.default_upper_text);
            micro.setText(R.string.default_lower_text);
        }
    }

    public boolean toggleRunning(boolean running) {
        if (running) {
            left.setText(R.string.Start);
            right.setTextColor(getResources().getColor(R.color.colorText));
            right.setClickable(true);
            return false;
        } else {
            left.setText(R.string.Pause);
            right.setTextColor(getResources().getColor(R.color.colorAccent));
            right.setClickable(false);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        return true;
    }
}
