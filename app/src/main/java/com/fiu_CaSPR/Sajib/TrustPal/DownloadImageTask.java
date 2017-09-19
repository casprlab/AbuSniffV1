package com.fiu_CaSPR.Sajib.TrustPal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sajib on 3/24/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        String defaulturl = "https://scontent-mia1-1.xx.fbcdn.net/v/t1.0-1/c59.0.200.200/p200x200/10354686_10150004552801856_220367501106153455_n.jpg?oh=2c85fa504d0284186fef69a81036bd6c&amp;oe=58E5D025";
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //wLog.e("Error", e.getMessage());
            e.printStackTrace();
            InputStream in = null;
            try {
                in = new java.net.URL(defaulturl).openStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mIcon11 = BitmapFactory.decodeStream(in);
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        try {
            bmImage.setImageBitmap(result);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}