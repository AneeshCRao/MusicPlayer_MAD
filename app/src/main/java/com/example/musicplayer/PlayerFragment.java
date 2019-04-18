package com.example.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment {
    private static final String TAG = "PlayerFragment";

    public static Button btnPlay, btnRewind, btnFast;

    public static TextView timerTV;
    public static TextView fullTimerTV;

    public static SeekBar seekBar;

    static Handler handler;
    static Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player, container, false);

        btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnRewind = (Button)view.findViewById(R.id.btnRewind);
        btnFast = (Button)view.findViewById(R.id.btnFast);


        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        timerTV = (TextView)view.findViewById(R.id.timerTV);
        fullTimerTV = (TextView)view.findViewById(R.id.fullTimerTV);


        handler = new Handler();



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input) {
                    MainActivity.mediaPlayer.seekTo(progress);
                    if (!MainActivity.mediaPlayer.isPlaying()) {
                        MainActivity.mediaPlayer.start();
                        btnPlay.setText("PAUSE");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        if (MainActivity.mediaPlayer.isPlaying())
            btnPlay.setText("PAUSE");

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                if (b.getText().equals("PLAY")) {
                    MainActivity.mediaPlayer.start();
                    b.setText("PAUSE");
                }
                else {
                    MainActivity.mediaPlayer.stop();
                    b.setText("PLAY");
                }
            }
        });




        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long playerPosition = MainActivity.mediaPlayer.getCurrentPosition();

                if (playerPosition > 2500) {
                    MainActivity.mediaPlayer.pause();
                    MainActivity.mediaPlayer.seekTo(0);
                    MainActivity.mediaPlayer.start();
                }
                else {
                    if (SongSelectFragment.isPlayingFrom) {
                        SongSelectFragment.currentIndex -= 1;
                        if (MainActivity.mediaPlayer.isPlaying())
                            MainActivity.mediaPlayer.stop();
                        String filePath = MainActivity.pathList.get(SongSelectFragment.currentIndex);
                        try {
                            MainActivity.mediaPlayer.reset();
                            MainActivity.mediaPlayer.setDataSource(filePath);
                            MainActivity.mediaPlayer.prepare();
                            FavoritesFragment.isPlayingFrom = false;
                            SongSelectFragment.isPlayingFrom = true;
                            MainActivity.mediaPlayer.start();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        FavoritesFragment.currentIndex -= 1;
                        if (MainActivity.mediaPlayer.isPlaying())
                            MainActivity.mediaPlayer.stop();
                        String filePath = MainActivity.Fav_pathList.get(FavoritesFragment.currentIndex);
                        try {
                            MainActivity.mediaPlayer.reset();
                            MainActivity.mediaPlayer.setDataSource(filePath);
                            MainActivity.mediaPlayer.prepare();
                            SongSelectFragment.isPlayingFrom = false;
                            FavoritesFragment.isPlayingFrom = true;
                            MainActivity.mediaPlayer.start();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }



            }
        });




        btnFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SongSelectFragment.isPlayingFrom) {
                    SongSelectFragment.currentIndex += 1;
                    if (MainActivity.mediaPlayer.isPlaying())
                        MainActivity.mediaPlayer.stop();
                    String filePath = MainActivity.pathList.get(SongSelectFragment.currentIndex);
                    try {
                        MainActivity.mediaPlayer.reset();
                        MainActivity.mediaPlayer.setDataSource(filePath);
                        MainActivity.mediaPlayer.prepare();
                        FavoritesFragment.isPlayingFrom = false;
                        SongSelectFragment.isPlayingFrom = true;
                        MainActivity.mediaPlayer.start();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    FavoritesFragment.currentIndex += 1;
                    if (MainActivity.mediaPlayer.isPlaying())
                        MainActivity.mediaPlayer.stop();
                    String filePath = MainActivity.Fav_pathList.get(FavoritesFragment.currentIndex);
                    try {
                        MainActivity.mediaPlayer.reset();
                        MainActivity.mediaPlayer.setDataSource(filePath);
                        MainActivity.mediaPlayer.prepare();
                        SongSelectFragment.isPlayingFrom = false;
                        FavoritesFragment.isPlayingFrom = true;
                        MainActivity.mediaPlayer.start();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        btnFast.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int progress = MainActivity.mediaPlayer.getCurrentPosition();
                progress += 4000;
                MainActivity.mediaPlayer.seekTo(progress);
                return false;
            }
        });

        btnRewind.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int progress = MainActivity.mediaPlayer.getCurrentPosition();
                if (progress < 4000)
                    progress = 0;
                else
                    progress -= 4000;
                MainActivity.mediaPlayer.seekTo(progress);
                return false;
            }
        });



        return view;
    }


    public static void playCycle() {
        int full = MainActivity.mediaPlayer.getDuration();
        String fullTime = String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(full) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(full)), TimeUnit.MILLISECONDS.toSeconds(full) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(full)));
        fullTimerTV.setText(fullTime);


        int curr = MainActivity.mediaPlayer.getCurrentPosition();
        seekBar.setProgress(curr);
        String currTime = String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(curr) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(curr)), TimeUnit.MILLISECONDS.toSeconds(curr) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(curr)));
        timerTV.setText(currTime);

        if (MainActivity.mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

}
