package com.rockykhan.rockysmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlayMusic extends AppCompatActivity {

    SeekBar seekBar;
    ImageView previous, play, next, fast_rewind, fast_forward, logo;
    TextView textView;
    TextView songDuration, totalSongDuration;
    ArrayList<File> songs;
    String songName;
    int position;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ObjectAnimator animation, anim;

    private static final String CHANNEL_ID = "Media Player";
    private static final int NOTIFICATION_ID = 001;

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent serviceIntent = new Intent(this, NotiForegroundService.class);

            seekBar = findViewById(R.id.seekBar);
            previous = findViewById(R.id.previous);
            play = findViewById(R.id.play);
            next = findViewById(R.id.next);
            textView = findViewById(R.id.songName);
            songDuration = findViewById(R.id.duration);
            totalSongDuration = findViewById(R.id.totalDuration);
            fast_rewind = findViewById(R.id.fast_rewind);
            fast_forward = findViewById(R.id.fast_forward);
            logo = findViewById(R.id.logo);

            animation = ObjectAnimator.ofFloat(logo, "rotation", 0, 3600);
            animation.setDuration(100000);
            animation.setRepeatCount(10000);
            animation.setRepeatMode(ObjectAnimator.RESTART);
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            songs = (ArrayList) extras.getParcelableArrayList("songsList");
            songName = extras.getString("currentSong");
            textView.setSelected(true);
            textView.setText(songName);
            position = intent.getIntExtra("position", 0);

            Uri uri = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();

        play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);

            animation.start();

            songDuration.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            int duration = mediaPlayer.getDuration();
            String totalDuration = convertFormat(duration);
            totalSongDuration.setText(totalDuration);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        mediaPlayer.seekTo(i);
                    }
                    songDuration.setText(convertFormat(mediaPlayer.getCurrentPosition()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            runnable = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 200);
                }
            };

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        pause();
                    } else {
                        play();
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    next();

                    anim = ObjectAnimator.ofFloat(logo, "rotation", 0, 360);
                    anim.setDuration(500);
                    anim.start();

//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            startForegroundService(serviceIntent);
//                        }

                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    previous();

                    anim = ObjectAnimator.ofFloat(logo, "rotation",  360, 0);
                    anim.setDuration(500);
                    anim.start();

//                    stopService(serviceIntent);

                }
            });

            fast_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    if (mediaPlayer.isPlaying() && duration != currentPosition) {
                        currentPosition += 5000;
                        songDuration.setText(convertFormat(currentPosition));
                        mediaPlayer.seekTo(currentPosition);
                    }
                }
            });

            fast_rewind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                        currentPosition -= 5000;
                        songDuration.setText(convertFormat(currentPosition));
                        mediaPlayer.seekTo(currentPosition);
                    }
                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    next();
                }
            });

    }

    public void previous(){
        mediaPlayer.stop();
        mediaPlayer.release();
        if (position != 0){
            position -= 1;
        }else{
            position = songs.size() - 1;
        }
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.postDelayed(runnable,0);
        songDuration.setText(convertFormat(mediaPlayer.getCurrentPosition()));
        totalSongDuration.setText(convertFormat(mediaPlayer.getDuration()));
        songName = songs.get(position).getName();
        textView.setText(songName);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });

        animation.start();

    }

    public void next(){
        mediaPlayer.stop();
        mediaPlayer.release();
        if (position != songs.size()-1){
            position += 1;
        }else{
            position = 0;
        }
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.postDelayed(runnable, 0);
        songDuration.setText(convertFormat(mediaPlayer.getCurrentPosition()));
        totalSongDuration.setText(convertFormat(mediaPlayer.getDuration()));
        songName = songs.get(position).getName();
        textView.setText(songName);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });

        animation.start();

    }

    public void pause(){
        mediaPlayer.pause();
        play.setImageResource(R.drawable.play);
        handler.removeCallbacks(runnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animation.pause();
        }
    }

    public void play(){
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());
        handler.postDelayed(runnable, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animation.resume();
        }
    }

    private String convertFormat(int duration){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        handler.removeCallbacks(runnable);
    }

}