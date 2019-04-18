package com.example.musicplayer;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class GuessSongClass {
    public void getDetails(Context context, final TextView tv, String lyric, final String artist){

// ...
        final YTVideo v1 = new YTVideo();
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://api.musixmatch.com/ws/1.1/track.search?apikey=91d9d078ea2fcb46055848af9cf2b40f&q_lyrics="+lyric+"&q_artist="+artist+"&page_size=3&page=1&s_track_rating=desc";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        tv.setText(response);
                        String ans="";
                        try{
                            JSONObject jo = new JSONObject(response);
                            JSONArray songs =(JSONArray) ((JSONObject)((JSONObject)jo.get("message")).get("body")).get("track_list");

                            for(int i=0;i<songs.length();i++){
                                JSONObject jo1 = (JSONObject) (((JSONObject)(songs.get(i))).get("track"));

                                String tname=(String)jo1.get("track_name");
                                String alname=(String)jo1.get("album_name");
                                String arname=(String)jo1.get("artist_name");

                                ans=ans+"Track\n"+tname+"\nAlbum\n"+alname+"\nArtist\n"+arname+"\n\n";
                                if(i==0){
                                    v1.setAlbum(alname);
                                    v1.setArtist(arname);
                                    v1.setTrack(tname);break;
                                }

                            }


                            tv.setText(ans);

//                            tv.setText(Html.fromHtml(ans));
                            Log.d("waiting","3");
                            YoutubeData ytd = new YoutubeData(tv);

                            ytd.execute(v1.getArtist()+" "+v1.getTrack()).get();
                            Log.d("waiting","2");
                            Log.d("finalres",ytd.getAns());

                            Log.d("waiting","1");
                        }
                        catch (Exception e){
                            Log.e("guess_error1",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");

                tv.setText("error. Check what you entered.");
                Log.e("guess_error",error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}
