package com.fiu_CaSPR.Sajib.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Sajib.DataStructures.FriendRequest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.regex.Matcher;

/**
 * Created by fhu004 on 6/12/2015.
 * We run this thread at the beginning of the application as soon as the user
 * has logged into their profile.
 * We run this to get all the friend requests and to begin processing them as well.
 */
public class RetrieveFriendRequestInfo extends AsyncTask<String, Void, String> {
    @Deprecated
    private Exception exception;
    private String htmlSource = "";
    @Deprecated
    private String url = "";
    private String cookie;
    @Deprecated
    private Dialog dialog;
    private Context mContext;

    /**
     * The Constructor is here to copy over the context so that we can push dialogs to the UI Thread.
     * @param context
     */
    public RetrieveFriendRequestInfo(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Begun Processing Friend Requests")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        // Shows alert
        alert.show();
        //Create progress dialog here and show it
        //dialog = ProgressDialog.show(mContext, "Please Wait", "Loading");
        super.onPreExecute();
    }


    /**
     * This method is ran when the thread has begun. It begins getting every single friend requests from the facebook friend request page,
     * which is passed in when the thread is executed: it is in the parameter urls. Urls[1] = the url and urls[0] = cookies for JSoup to connect to
     * the facebook friend request page.
     * @param urls
     * @return
     */
    protected String doInBackground(String... urls) {
        try {
            int numberItem = 0;

            // Finished Don't need to process again, it probably will never run here, because the thread is never ran again.
            if (FacebookRegexPatternPool.processed) {
                return "Finished";
            }
            this.url = urls[1];
            this.cookie = urls[0];
            // We connect to the facebook page with urls[1] and use the cookie urls[0].
            Connection.Response response = Jsoup.connect(urls[1]).cookie(urls[1], urls[0]).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            Log.i("Info Element: ", response.body());
            Log.i("Info Element: ", response.url().toString());
            htmlSource = response.body();
            // Here we get the style
            final Matcher styleMatcher = FacebookRegexPatternPool.style.matcher(htmlSource);
            if (styleMatcher.find())
                Log.d("style :", styleMatcher.group());
            // Here we run the r regex pattern which is described in the faceboookregex pattern pool class.
            final Matcher m = FacebookRegexPatternPool.r.matcher(htmlSource);
            FacebookRegexPatternPool.styleDiv = styleMatcher.group();
            FacebookRegexPatternPool.div = styleMatcher.group();
            // If we find r, then we run the facebookregex pattern pool methods that are process current friend request overviews, and etc...
            if (m.find()) {
                String friendRequestDivSource = m.group();
                final Matcher friendRequestMatcher = FacebookRegexPatternPool.friendRequestPattern.matcher(friendRequestDivSource);
                String friendDivSource = "";


                // While we have another friend request to parse.
                while (friendRequestMatcher.find()) {
                    // We create a new friend request data structure for each friend request in the friend request page, and then run the methods on the friend request profile
                    FacebookRegexPatternPool.currentFriendRequest = new FriendRequest();
                    // TODO: create algorithm to determine safety of Facebook Friend
                    // friendRequestSourceList.add("<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "</div>");
                    // TODO: code below must be revised later because it will not work with algorithm
                    // friendDivSource += "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "</div>";
                    String levelOfSafety;
                    FacebookRegexPatternPool.findNames(friendRequestMatcher.group());
                    //Log.i("ID of :", FacebookRegexPatternPool.currentFriendRequest.getName() + " " + FacebookRegexPatternPool.currentFriendRequest.getId());
                    FacebookRegexPatternPool.processCurrentFriendRequestOverview(cookie);
                    FacebookRegexPatternPool.processCurrentFriendRequestPhotos(cookie);
                    FacebookRegexPatternPool.processCurrentFriendRequestLifeEvents(cookie);
                    levelOfSafety = FacebookRegexPatternPool.currentFriendRequest.getDangerScore();

                    // Below is the old code for the mockup that has the different background colors according to the danger level of a friend request
                    /*switch (levelOfSafety) {
                        case ("red"):
                            friendDivSource = "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                        case ("yellow"):
                            friendDivSource = "<div style=\"background-color: #FFFF00\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                        case ("green"):
                            friendDivSource = "<div style=\"background-color: #33CC33\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                    }
                    */
                    FacebookRegexPatternPool.currentFriendRequest.setNumberInPage(numberItem++);
                    FacebookRegexPatternPool.currentFriendRequest.setDiv(friendDivSource);
                    FacebookRegexPatternPool.friendRequestList.add(FacebookRegexPatternPool.currentFriendRequest);
                    FacebookRegexPatternPool.div += friendDivSource;
                }
                //final String divSource = friendDivSource;


         /*       AfterLogin.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        FacebookRegexPatternPool.div = styleMatcher.group() + divSource + "</body></html>";
                        AfterLogin.webView.loadData(styleMatcher.group() + divSource + "</body></html>", "text/html; charset=UTF-8", null);
                        FacebookRegexPatternPool.processed = true;
                    }
                });
                */

            } else {
                Log.d("d", "NO MATCH");
            }

        } catch (Exception e) {
            Log.i("Error: ", e.toString());
        }
        return "didn't work";
    }

    /**
     * On post execute, we set the processed value to true in the FacebookRegexPatternPool, and this means that the RetrieveFriendRequestInfo Thread has finished processing all the friend requests.
     * We also show the user a popup dialog that says that the application has finished processing all friend requests.
     * @param result
     */
    protected void onPostExecute(String result) {
        // We have finished running the entire thread and have processed all friend reuqests. So we
        // set the FacebookRegexPattern
        FacebookRegexPatternPool.processed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Finished processing all Friend Requests")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        super.onPostExecute(result);
    }
}

