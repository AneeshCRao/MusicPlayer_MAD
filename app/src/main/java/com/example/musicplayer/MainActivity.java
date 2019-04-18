package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
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
