package com.example.marcel.musicplayerapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnPlay1 , btnPause1 ;
    ImageButton btnPlay2 , btnPause2;
    AudioManager mAudioManager;
    MediaPlayer mMediaPlayer;
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                // Puase playback
                mMediaPlayer.pause();
                //mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Resume playback
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }

        }
    };
    View.OnClickListener play = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    };

    View.OnClickListener pause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMediaPlayer.pause();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaPlayer  = MediaPlayer.create(this , R.raw.song5);
        btnPlay1 = (Button)findViewById(R.id.buttonPlay1);
        btnPause1 = (Button)findViewById(R.id.buttonPause1);
        btnPlay1.setOnClickListener(play);
        btnPause1 = (Button)findViewById(R.id.buttonPause1);
        btnPause1.setOnClickListener(pause);
        btnPlay2 = (ImageButton)findViewById(R.id.buttonPlay2);
        btnPause2 = (ImageButton)findViewById(R.id.buttonPause2);
        btnPlay2.setOnClickListener(play);
        btnPause2.setOnClickListener(pause);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE) ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer.start();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }
}
