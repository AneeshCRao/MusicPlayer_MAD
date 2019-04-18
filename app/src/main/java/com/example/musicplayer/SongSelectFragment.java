package com.example.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SongSelectFragment extends Fragment {
    private static final String TAG = "SongSelectFragment";

    public static boolean isPlayingFrom = false;

    public static ListView listView;
    ArrayAdapter<String> adapter;

    int titleIndex, songPathIndex, artistIndex;


    public static int currentIndex = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_select, container, false);

        listView = (ListView)view.findViewById(R.id.lv1);

        DisplaySongs();
        return view;
    }


    //Context menu - when he long presses an option in the list of songs -> Play, Add to playlist
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu1, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int list_index = (int)info.id;
        switch(item.getItemId()) {
            case R.id.play:
                listView.setSelection(list_index);
                String filePath = MainActivity.pathList.get(list_index);
                if (MainActivity.mediaPlayer.isPlaying())
                    MainActivity.mediaPlayer.stop();
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(filePath);
                    MainActivity.mediaPlayer.prepare();
                    FavoritesFragment.isPlayingFrom = false;
                    isPlayingFrom = true;
                    PlayerFragment.isPaused = false;
                    PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                    PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                    MainActivity.mediaPlayer.start();
                    PlayerFragment.playCycle();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                currentIndex = list_index;
                return true;
            case R.id.add_playlist:
                if (MainActivity.Fav_displayList.indexOf(MainActivity.displayList.get(list_index)) >= 0) {
                    Toast.makeText(getContext(), "This song is already added to the playlist", Toast.LENGTH_SHORT).show();
                    return true;
                }
                MainActivity.Fav_displayList.add(MainActivity.displayList.get(list_index));
                MainActivity.Fav_songNamesList.add(MainActivity.songNamesList.get(list_index));
                MainActivity.Fav_pathList.add(MainActivity.pathList.get(list_index));

                Toast.makeText(getContext(), MainActivity.songNamesList.get(list_index) + " added to playlist", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    public void DisplaySongs () {
        getMusic();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MainActivity.displayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex = i;
                String filePath = MainActivity.pathList.get(i);
                listView.setSelection(i);
                MainActivity.currentView = view;
                if (MainActivity.mediaPlayer.isPlaying())
                    MainActivity.mediaPlayer.stop();
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(filePath);
                    MainActivity.mediaPlayer.prepare();
                    FavoritesFragment.isPlayingFrom = false;
                    isPlayingFrom = true;
                    PlayerFragment.isPaused = false;
                    PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                    PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                    MainActivity.mediaPlayer.start();
                    PlayerFragment.playCycle();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        registerForContextMenu(listView);
    }

    public void getMusic () {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            titleIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            artistIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            songPathIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(titleIndex);
                String currentArtist = songCursor.getString(artistIndex);
                String currentPath = songCursor.getString(songPathIndex);
                MainActivity.displayList.add(currentTitle + "\n" + currentArtist);
                MainActivity.songNamesList.add(currentTitle);
                MainActivity.pathList.add(currentPath);
            } while (songCursor.moveToNext());

        }
    }







}
