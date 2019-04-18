package com.example.musicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.nio.file.attribute.PosixFileAttributeView;

public class PlayerFragment extends Fragment {
    private static final String TAG = "PlayerFragment";

    public static Button btnPlay, btnRewind, btnFast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player, container, false);


        btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnRewind = (Button)view.findViewById(R.id.btnRewind);
        btnFast = (Button)view.findViewById(R.id.btnFast);

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
                    MainActivity.mediaPlayer.pause();
                    b.setText("PLAY");
                }
            }
        });
//
//        btnRewind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        btnFast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (SongSelectFragment.isPlayingFrom) {
//                    SongSelectFragment.currentIndex += 1;
//                    if (MainActivity.mediaPlayer.isPlaying())
//                        MainActivity.mediaPlayer.stop();
//                    String filePath = MainActivity.pathList.get(SongSelectFragment.currentIndex);
//                    try {
//                        MainActivity.mediaPlayer.reset();
//                        MainActivity.mediaPlayer.setDataSource(filePath);
//                        MainActivity.mediaPlayer.prepare();
//                        FavoritesFragment.isPlayingFrom = false;
//                        SongSelectFragment.isPlayingFrom = true;
//                        MainActivity.mediaPlayer.start();
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    FavoritesFragment.currentIndex += 1;
//                    if (MainActivity.mediaPlayer.isPlaying())
//                        MainActivity.mediaPlayer.stop();
//                    String filePath = MainActivity.Fav_pathList.get(FavoritesFragment.currentIndex);
//                    try {
//                        MainActivity.mediaPlayer.reset();
//                        MainActivity.mediaPlayer.setDataSource(filePath);
//                        MainActivity.mediaPlayer.prepare();
//                        SongSelectFragment.isPlayingFrom = false;
//                        FavoritesFragment.isPlayingFrom = true;
//                        MainActivity.mediaPlayer.start();
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });



        return view;
    }

    public void btnPlayClick (View view) {

    }

    public void btnRewindClick (View view) {

    }


}
