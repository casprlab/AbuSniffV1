package com.fiu_CaSPR.Sajib.Utility;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Created by fhu004 on 6/15/2015.
 */
@Deprecated
public class FindFriendRequestThread extends AsyncTask<String, Void, String> {
    private Exception exception;
    private String htmlSource = "";

    protected String doInBackground(String... urls) {
        try {
            Connection.Response response = Jsoup.connect(urls[1]).cookie(urls[1], urls[0]).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            Log.i("Info Element: ", response.body());
            Log.i("Info Element: ", response.url().toString());
            htmlSource =  response.body();

        }catch(Exception e){
            Log.i("Error: ", e.toString());
        }
        return "didn't work";
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed

        super.onPostExecute(result);

    }
}
