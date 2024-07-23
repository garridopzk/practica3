package com.example.practica3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPause, btnStop, btnRewind, btnForward, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        videoView = findViewById(R.id.videoView);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnRewind = findViewById(R.id.btnRewind);
        btnForward = findViewById(R.id.btnForward);
        btnMenu = findViewById(R.id.menu);

        // Reemplaza "tu_video.mp4" con la ruta de tu video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video);
        videoView.setVideoURI(videoUri);
        videoView.start();

        btnPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                btnPause.setText("Reanudar");
            } else {
                videoView.start();
                btnPause.setText("Pausar");
            }
        });

        btnStop.setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.setVideoURI(videoUri);
            videoView.start();
        });

        btnRewind.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int rewindPosition = currentPosition - 10000; // Retrocede 10 segundos
            videoView.seekTo(Math.max(rewindPosition, 0));
        });

        btnForward.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int forwardPosition = currentPosition + 10000; // Avanza 10 segundos
            videoView.seekTo(Math.min(forwardPosition, videoView.getDuration()));
        });

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
