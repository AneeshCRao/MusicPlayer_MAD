package com.example.musicplayer;

import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class YoutubeData extends AsyncTask<String, Void, JSONObject> {

    private Exception exception;
    TextView tv;
    String ans="";
    YoutubeData(TextView t){
        tv=t;
    }
    public String getAns(){
        return ans;
    }

    protected JSONObject doInBackground(String... urls) {
        String ytres="";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            Log.d("youtubedata_input",urls[0]);
            url = new URL("https://www.googleapis.com/youtube/v3/search?part=id&q="+urls[0]+"&key=AIzaSyBwQprnPBSNUiUzg6WxUshRxJEn8R51beQ");
            Log.d("youtubedata","a2");

            Log.d("youtubedata","a");
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
//            urlConnection.setRequestProperty ("Authorization", "Bearer AIzaSyBwQprnPBSNUiUzg6WxUshRxJEn8R51beQ");
            urlConnection.setRequestProperty ("Accept", "application/json");
            Log.d("youtubedatanum",Integer.toString(urlConnection.getResponseCode()));
            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                ytres=ytres+current;
                System.out.print(current);
            }
            Log.d("youtubedata",ytres);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        JSONObject jo=null ;

        try {
             jo = new JSONObject(ytres);
        }
        catch (Exception e){
            Log.e("youtube_jsonerror",e.toString());
            e.printStackTrace();
        }
        return jo;
    }

    protected void onPostExecute(JSONObject res) {
        // TODO: check this.exception
        // TODO: do something with the feed
        try{
            JSONArray ja = (JSONArray)(res.get("items"));
            String vid = (String)((JSONObject)(((JSONObject)(ja.get(0))).get("id"))).get("videoId");
            ans=vid;
            Log.d("video_id",ans);
            String fromthere=tv.getText().toString();
            fromthere = fromthere + "Listen to it here: \"https://www.youtube.com/watch/"+ans+"\"";

            int a1=fromthere.indexOf("Album");
            int ar1=fromthere.indexOf("Artist");
            SpannableStringBuilder str = new SpannableStringBuilder(fromthere);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), a1, a1+5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), ar1, ar1+6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(str);
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
