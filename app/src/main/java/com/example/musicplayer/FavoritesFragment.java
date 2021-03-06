package com.example.musicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FavoritesFragment extends Fragment {
    private static final String TAG = "FavoritesFragment";

    public static ListView playlist_list;

    public static int currentIndex = 0;
    public static boolean isPlayingFrom = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites, container, false);

        playlist_list = (ListView)view.findViewById(R.id.playlist_list);

        MainActivity.Fav_adapter = new ArrayAdapter<String>(getContext(), R.layout.list_layout,R.id.textView1, MainActivity.Fav_displayList);
        playlist_list.setAdapter(MainActivity.Fav_adapter);

        registerForContextMenu(playlist_list);

        //On clicking, play from that song
        playlist_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex = i;
                if (MainActivity.mediaPlayer.isPlaying() )
                    MainActivity.mediaPlayer.stop();
                String filePath = MainActivity.Fav_pathList.get(currentIndex);
                playlist_list.setSelection(currentIndex);
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(filePath);
                    MainActivity.mediaPlayer.prepare();
                    SongSelectFragment.isPlayingFrom = false;
                    isPlayingFrom = true;
                    PlayerFragment.isPaused = false;
                    PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                    PlayerFragment.seekBar.setProgress(0);
                    PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                    MainActivity.mediaPlayer.start();
                    PlayerFragment.playCycle();
                }catch(Exception e) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
        return view;
    }



    //Context menu - when he long presses an option in the list of songs -> Play, Add to playlist
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.playlist_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int list_index = (int)info.id;

        switch(item.getItemId()) {
            case R.id.play_playlist:
                String filePath = MainActivity.Fav_pathList.get(list_index);
                playlist_list.setSelection(list_index);
                if (MainActivity.mediaPlayer.isPlaying())
                    MainActivity.mediaPlayer.stop();
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(filePath);
                    MainActivity.mediaPlayer.prepare();
                    SongSelectFragment.isPlayingFrom = false;
                    isPlayingFrom = true;
                    PlayerFragment.isPaused = false;
                    PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                    PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                    MainActivity.mediaPlayer.start();
                    PlayerFragment.playCycle();
                }catch(Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                }
                currentIndex = list_index;
                return true;
            case R.id.remove_playlist:

                String removedSongName = MainActivity.Fav_songNamesList.get(list_index);
                String deleteName = MainActivity.Fav_displayList.get(list_index);
                MainActivity.Fav_pathList.remove(list_index);
                MainActivity.Fav_songNamesList.remove(list_index);
                MainActivity.Fav_displayList.remove(list_index);


                MainActivity.Fav_adapter.remove(deleteName);
                MainActivity.Fav_adapter.notifyDataSetChanged();

                if (MainActivity.Fav_displayList.size() == 0) return true;

                if (currentIndex == list_index) {       //Current playing song is removed
                    currentIndex = (currentIndex + 1) % (MainActivity.Fav_displayList.size());
                    filePath = MainActivity.Fav_pathList.get(currentIndex);
                    playlist_list.setSelection(currentIndex);
                    try {
                        MainActivity.mediaPlayer.reset();
                        MainActivity.mediaPlayer.setDataSource(filePath);
                        MainActivity.mediaPlayer.prepare();
                        SongSelectFragment.isPlayingFrom = false;
                        isPlayingFrom = true;
                        PlayerFragment.isPaused = false;
                        PlayerFragment.btnPlay.setBackgroundResource(R.drawable.pausebutton);
                        PlayerFragment.seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                        MainActivity.mediaPlayer.start();
                        PlayerFragment.playCycle();
                    }catch(Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                    }
                }

                Toast.makeText(getContext(), removedSongName + " removed from playlist", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
