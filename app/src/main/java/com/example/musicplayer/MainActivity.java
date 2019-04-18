package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    private ViewPager mViewPager;

    public static View currentView;
    public static int selectedColor;
    public static int unSelectedColor;

    //SongSelect variables
    static ArrayList<String> displayList;          //List to be displayed (Song name + artist name)
    static ArrayList<String> songNamesList;        //Only song names
    static ArrayList<String> pathList;
    static MediaPlayer mediaPlayer;
    static ArrayAdapter<String> SongSelectAdapter;


    //FavoritesFragment
    static ArrayList<String> Fav_displayList;          //List to be displayed (Song name + artist name)
    static ArrayList<String> Fav_songNamesList;        //Only song names
    static ArrayList<String> Fav_pathList;
    static ArrayAdapter<String> Fav_adapter;


    public static Timer timer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        displayList = new ArrayList<>();
        pathList = new ArrayList<>();
        songNamesList = new ArrayList<>();

        MainActivity.mediaPlayer = new MediaPlayer();
        selectedColor = Color.CYAN;
        unSelectedColor = Color.WHITE;
        currentView = new TextView(this);

        Fav_displayList = new ArrayList<>();
        Fav_pathList = new ArrayList<>();
        Fav_songNamesList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        else {
            mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }



        //When song finishes, play next song in list
        MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int index = SongSelectFragment.currentIndex;
                if (FavoritesFragment.isPlayingFrom)
                    index = FavoritesFragment.currentIndex;
                index = index + 1;

                String filePath = "";

                if (FavoritesFragment.isPlayingFrom) {
                    if (index < MainActivity.Fav_pathList.size()) {
                        filePath = MainActivity.Fav_pathList.get(index);
                        FavoritesFragment.currentIndex = index;
                        FavoritesFragment.playlist_list.setSelection(index);
                    }
                    else
                        return;
                }
                else {
                    if (index < MainActivity.pathList.size()) {
                        filePath = MainActivity.pathList.get(index);
                        SongSelectFragment.currentIndex = index;
                        SongSelectFragment.listView.setSelection(index);
                    }
                    else
                        return;
                }

                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(filePath);
                    mediaPlayer.prepare();
                    PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                    PlayerFragment.isPaused = false;
                    PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                    MainActivity.mediaPlayer.start();
                    PlayerFragment.playCycle();
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                String songName, artistName;

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

                PlayerFragment.songNameTV.setText(songName);
                PlayerFragment.artistNameTV.setText(artistName);

                if (songName.length() > 25)
                    PlayerFragment.songNameTV.setTextSize(20);
                else PlayerFragment.songNameTV.setTextSize(30);



            }
        });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    mViewPager = (ViewPager) findViewById(R.id.container);
                    setupViewPager(mViewPager);

                    TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }
                else {
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }





    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongSelectFragment(), "MUSIC");
        adapter.addFragment(new PlayerFragment(), "PLAYER");
        adapter.addFragment(new FavoritesFragment(), "FAVORITES");
        adapter.addFragment(new GuessSongFragment(), "SHOBHIT");
        viewPager.setAdapter(adapter);


    }








}
