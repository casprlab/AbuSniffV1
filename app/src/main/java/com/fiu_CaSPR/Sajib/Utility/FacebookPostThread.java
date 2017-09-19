package com.fiu_CaSPR.Sajib.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.temboo.Library.Facebook.Publishing.Post;
import com.temboo.core.TembooSession;

/**
 * Created by frankhu on 6/17/15.
 * This Thread Class is used in order to send the post to Facebook in a background thread.
 * We execute this thread in the PostActivity and when it has finished we want the postActivity to close, so we end the activity
 * on post execute.
 */
public class FacebookPostThread extends AsyncTask<Void,Void,String> {

    private Dialog dialog;
    private Context mContext;
    private String post ="";
    private TembooSession session;
    private int dangerText = 0;

    /**
     * Constructor, we simply copy over the information from the PostActivity so that we can created dialogs as well as
     * end the activity at the end.
     * @param contxt
     * @param text
     * @param tembooSession
     * @param danger
     */
    public FacebookPostThread(Context contxt, String text, TembooSession tembooSession, int danger){
        mContext = contxt;
        post = text;
        session = tembooSession;
        dangerText = danger;

    }
    @Override
    protected void onPreExecute() {
        //Create progress dialog here and show it
        if(dangerText == Color.GREEN)
            dialog = ProgressDialog.show(mContext, "Please Wait", "Posting");
        else if(dangerText == Color.YELLOW){
            dialog = ProgressDialog.show(mContext, "Please Wait", "Posting your mildly dangerous post");
        }else {
            dialog = ProgressDialog.show(mContext, "Please Wait", "Posting your dangerous post");
        }
    }
    @Override
    protected String doInBackground(Void... params){
        Post postChoreo = new Post(session);
        // Get an InputSet object for the choreo
        Post.PostInputSet postInputs = postChoreo.newInputSet();
        postInputs.set_AccessToken(FacebookRegexPatternPool.accessToken);
        postInputs.set_Message(post);
        // Set inputs
        // Execute Choreo
        try {
            Post.PostResultSet postResults = postChoreo.execute(postInputs);
        } catch (Exception e) {
            Log.e("Error:", e.toString());
        }
        return "finished";
    }

    @Override
    protected void onPostExecute(String s){
        dialog.dismiss();
        ((Activity)mContext).finish();
    }
}
