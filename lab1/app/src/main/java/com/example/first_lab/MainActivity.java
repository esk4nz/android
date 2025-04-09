package com.example.first_lab;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_AUDIO = 1;
    private static final int REQUEST_CODE_PICK_VIDEO = 2;

    private VideoView videoView;
    private PlayerView playerView;
    private EditText editUrl;

    private MediaPlayer audioPlayer;
    private ExoPlayer exoPlayer;

    private Uri currentAudioUri = null;
    private Uri currentVideoUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
        playerView = findViewById(R.id.player_view);
        editUrl = findViewById(R.id.edit_url);

        Button pickAudioBtn = findViewById(R.id.button_pick_audio);
        Button pickVideoBtn = findViewById(R.id.button_pick_video);
        Button playBtn = findViewById(R.id.button_play);
        Button pauseBtn = findViewById(R.id.button_pause);
        Button stopBtn = findViewById(R.id.button_stop);

        pickAudioBtn.setOnClickListener(v -> {
            stopAllMedia();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("audio/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO);
        });

        pickVideoBtn.setOnClickListener(v -> {
            stopAllMedia();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("video/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
        });

        playBtn.setOnClickListener(v -> {
            String url = editUrl.getText().toString().trim();

            if (audioPlayer != null && !audioPlayer.isPlaying()) {
                audioPlayer.start();
                return;
            }

            if (videoView.getVisibility() == View.VISIBLE && !videoView.isPlaying() && videoView.canSeekForward()) {
                videoView.start();
                return;
            }

            if (exoPlayer != null && exoPlayer.getPlaybackState() == ExoPlayer.STATE_READY && !exoPlayer.isPlaying()) {
                exoPlayer.play();
                return;
            }

            stopAllMedia();

            // Якщо URL
            if (!url.isEmpty()) {
                if (url.endsWith(".mp3")) {
                    playAudioFromUrl(url);
                } else if (url.endsWith(".mp4")) {
                    playVideoFromUrl(url);
                } else {
                    Toast.makeText(this, "Непідтримуваний формат URL", Toast.LENGTH_SHORT).show();
                }
            } else if (currentAudioUri != null) {
                playAudioFromFile();
            } else if (currentVideoUri != null) {
                playVideoFromFile();
            } else {
                Toast.makeText(this, "Файл або URL не вибрано", Toast.LENGTH_SHORT).show();
            }
        });


        pauseBtn.setOnClickListener(v -> {
            if (audioPlayer != null && audioPlayer.isPlaying()) {
                audioPlayer.pause();
            } else if (videoView.getVisibility() == View.VISIBLE && videoView.isPlaying()) {
                videoView.pause();
            } else if (exoPlayer != null && exoPlayer.isPlaying()) {
                exoPlayer.pause();
            }
        });

        stopBtn.setOnClickListener(v -> stopAllMedia());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            editUrl.setText("");

            if (requestCode == REQUEST_CODE_PICK_AUDIO) {
                currentAudioUri = uri;
                currentVideoUri = null;
                videoView.setVisibility(View.GONE);
                playerView.setVisibility(View.GONE);
                Toast.makeText(this, "Аудіо вибрано", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CODE_PICK_VIDEO) {
                currentVideoUri = uri;
                currentAudioUri = null;
                videoView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                Toast.makeText(this, "Відео вибрано", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playAudioFromFile() {
        try {
            if (audioPlayer != null) audioPlayer.release();
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(this, currentAudioUri);
            audioPlayer.prepare();
            audioPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не вдалося відтворити аудіо", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideoFromFile() {
        videoView.setVideoURI(currentVideoUri);
        videoView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        videoView.start();
    }

    private void playAudioFromUrl(String url) {
        try {
            if (audioPlayer != null) audioPlayer.release();
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(url);
            audioPlayer.prepare();
            audioPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не вдалося відтворити аудіо з URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideoFromUrl(String url) {
        if (exoPlayer != null) exoPlayer.release();
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        playerView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
    }

    private void stopAllMedia() {
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }

        if (videoView.isPlaying()) videoView.stopPlayback();
        videoView.setVisibility(View.GONE);

        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        playerView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllMedia();
    }
}
