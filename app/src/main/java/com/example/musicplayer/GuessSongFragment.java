package com.example.musicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GuessSongFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "GuessSongFragment";
    TextView tv;
    EditText ly,ar;
    Button bt;
    YoutubeData ytd;
    String lyric="",artist="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guess_song, container, false);

        tv=view.findViewById(R.id.guess_result);
        ly=view.findViewById(R.id.guess_lyric);
        ar=view.findViewById(R.id.guess_artist);
        bt=view.findViewById(R.id.guess_bt);
        bt.setOnClickListener(this);




        return view;
    }



    @Override
    public void onClick(View view) {
        GuessSongClass gsc = new GuessSongClass();
        gsc.getDetails(getContext(),tv,ly.getText().toString(),ar.getText().toString());


//        ytd = new YoutubeData();
//        Log.d("sending",video.getArtist()+ " "+ video.getTrack());
//        ytd.execute(video.getArtist()+ " "+ video.getTrack());



    }
}
