package com.example.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment {
    private static final String TAG = "PlayerFragment";

    public static Button btnPlay, btnRewind, btnFast;

    public static TextView timerTV;
    public static TextView fullTimerTV;
    static TextView artistNameTV;
    static TextView songNameTV;

    public static SeekBar seekBar;

    public static boolean isPaused = true;

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
        artistNameTV = (TextView)view.findViewById(R.id.ArtistNameTV);
        songNameTV = (TextView)view.findViewById(R.id.SongNameTV);


        handler = new Handler();



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input) {
                    MainActivity.mediaPlayer.seekTo(progress);
                    if (!MainActivity.mediaPlayer.isPlaying()) {
                        MainActivity.mediaPlayer.start();
//                        btnPlay.setText("PAUSE");
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



        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                if (isPaused) {
                    MainActivity.mediaPlayer.start();
                    isPaused = false;
                    b.setBackgroundResource(R.drawable.pausebutton);
                }
                else {
                    MainActivity.mediaPlayer.pause();
                    isPaused = true;
                    b.setBackgroundResource(R.drawable.playbutton);
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
                        SongSelectFragment.listView.setSelection(SongSelectFragment.currentIndex);
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
                        FavoritesFragment.playlist_list.setSelection(FavoritesFragment.currentIndex);
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
                    SongSelectFragment.listView.setSelection(SongSelectFragment.currentIndex);
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
                    FavoritesFragment.playlist_list.setSelection(FavoritesFragment.currentIndex);
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
        PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
        seekBar.setProgress(curr);
        String currTime = String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(curr) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(curr)), TimeUnit.MILLISECONDS.toSeconds(curr) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(curr)));
        timerTV.setText(currTime);

        String artistName, songName;

        if (FavoritesFragment.isPlayingFrom) {
            songName = MainActivity.Fav_songNamesList.get(FavoritesFragment.currentIndex);
            String temp = MainActivity.Fav_displayList.get(FavoritesFragment.currentIndex);
            int x = temp.indexOf("\n");
            artistName = temp.substring(x + 1);
        }
        else {
            songName = MainActivity.songNamesList.get(SongSelectFragment.currentIndex);
            String temp = MainActivity.displayList.get(SongSelectFragment.currentIndex);
            int x = temp.indexOf("\n");
            artistName = temp.substring(x + 1);
        }
        songNameTV.setText(songName);
        artistNameTV.setText(artistName);

        if (songName.length() > 25)
            songNameTV.setTextSize(20);
        else songNameTV.setTextSize(30);



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
