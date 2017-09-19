package com.fiu_CaSPR.Sajib.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Sajib.DataStructures.FriendRequest;
//import com.fiu_CaSPR.Sajib.Intents.PostActivity;
import com.fiu_CaSPR.Sajib.TrustPal.AfterLogin;

import java.util.Collections;
import java.util.TimerTask;

/**
 * Created by frankhu on 6/16/15.
 * This timer task begins as soon as the user has finished logging in. Once the user has logged in and we have the Facebook username,
 * It also determines to push the beginning dialog please wait loading. We use this dialog to show that we haven't finished finding the username
 * of the Facebook user which is extremely important. Without the Facebook user's Facebook given username, (which is different from the id and the login username),
 * the entire app does not work correctly and has incorrect results.
 */
public class MyTimerTask extends TimerTask {

    private Context mContext = null;
    private Dialog dialog;
    private boolean alreadyLoading = false;
    private boolean foundUserAlready = false;
    private boolean alreadyBegunThread = false;

    /**
     * Constructor, we wish to copy the context so we can push dialogs to the main UI Thread.
     * @param context
     */
    public MyTimerTask(Context context) {
        mContext = context;
    }

    /**
     * This run thread is constantly ran throughout the app. It detects whether or not the facebook username has been found and if it's
     * on the post or friend request page. It determines this byt seeing if the facebook page url contains soft=composer (for post) and
     * soft=requests (for friend requests)
     */
    public void run() {
        // ERROR
        // how update TextView in link below
        // http://android.okhelp.cz/timer-task-timertask-run-cancel-android-example/

        if (AfterLogin.webView != null)

            AfterLogin.webView.post(new Runnable() {
                                          @Override
                                          public void run() {
                                              // AfterLogin.webView.loadData("<html><body>Please Click one of the buttons at the top right to see Friend Requests and to post to your wall.</body></html>", "text/html; charset=UTF-8", null);
                                              // If the username has not been found, and we have the access token we pus hte hloading dialog, and that means the user has logged in and we need to find the user's facebook given username
                                              if (FacebookRegexPatternPool.userName.equals("") && !foundUserAlready && !FacebookRegexPatternPool.accessToken.equals("")) {
                                                  dialog = ProgressDialog.show(mContext, "Please Wait", "Loading");
                                                  foundUserAlready = true;
                                              }
                                              Log.i("username:", FacebookRegexPatternPool.userName + " user  ; founduser" +foundUserAlready +" acces" + FacebookRegexPatternPool.accessToken);
                                              // If we have found the facebook username, then we dismiss the dialog and begin the background thread retrieve friend reuqest info so that the user can
                                              // perform facebook tasks while the application process the friend requests in the background.
                                              if (!FacebookRegexPatternPool.userName.equals("") && foundUserAlready && !alreadyBegunThread) {
                                                  alreadyBegunThread = true;
                                                  dialog.dismiss();
                                                  // This method retrieves all the information from facebook and processes it with the cookies given.
                                                  new RetrieveFriendRequestInfo(mContext).execute(FacebookRegexPatternPool.cookie, "https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                                              }
                                              Log.i("Timer : ", "Url : " + AfterLogin.webView.getUrl());
                                              Log.i("URL : ", "Equals?: " + "https://m.facebook.com/" + FacebookRegexPatternPool.userName + "?soft=requests");

                                              // Post to
                                              if (AfterLogin.webView != null)
                                                  if (AfterLogin.webView.getUrl().contains("soft=composer") && !alreadyLoading) {
                                                      Log.i("Posting ", "Posting Page");
                                                      AfterLogin.webView.loadUrl("https://www.facebook.com/" + FacebookRegexPatternPool.userName);
                                                      //Intent intent = new Intent(mContext, PostActivity.class);
                                                      //mContext.startActivity(intent);
                                                  } else if (AfterLogin.webView.getUrl().contains("soft=requests") && !alreadyLoading) {
                                                      // This is the friend requests page, we want to do a javascript injection

                                                      alreadyLoading = true;
                                                      // this sorts the friend request list
                                                      Collections.sort(FacebookRegexPatternPool.friendRequestList);



                                                      // If the facebook regex pattern pool has not been processed completely, then we have to tell the user not all friend requests have been processed and we will only show the processed friend
                                                      // requests
                                                      if (!FacebookRegexPatternPool.processed) {
                                                          AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                          builder.setMessage("Not finished processing all friend requests, some may not be shown. Please come back when it is done. A dialog will pop up when all friend requests have been processed.")
                                                                  .setCancelable(false)
                                                                  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int id) {
                                                                          //do things
                                                                      }
                                                                  });
                                                          AlertDialog alert = builder.create();
                                                          alert.show();
                                                      }

                                                      // Here we push an action to the webview UI Thread. This is where we perform the javascript injections that change the color and add information accordingly to if a
                                                      // friend request is red, yellow, or green..
                                                      AfterLogin.webView.post(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {

                                                                                        for (FriendRequest fr : FacebookRegexPatternPool.friendRequestList) {

                                                                                            AfterLogin.webView.loadUrl("javascript:void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].style.width=\"43px\")");
                                                                                            switch (fr.getDangerScore()) {
                                                                                                case "red":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    AfterLogin.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"red solid 3px\")");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://www.readableblog.com/wp-content/uploads/warning2.gif\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var x = document.getElementsByClassName(\"_55wq _4g33 _5pxa _4l9h _5vbx\")["+ fr.getNumberInPage() + "]; void(x.getElementsByClassName(\"_54k8 _56bs _56b_ _3cqr _5uc2 _56bu\")[0].className=\"_54k8 _56bs _56b_ _5uc3 _3cqr _56bt\")");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var x = document.getElementsByClassName(\"_55wq _4g33 _5pxa _4l9h _5vbx\")["+ fr.getNumberInPage() + "]; void(x.getElementsByClassName(\"_54k8 _56bs _56b_ _5uc3 _3cqr _56bt\")[1].className=\"_54k8 _56bs _56b_ _3cqr _5uc2 _56bu\")");
                                                                                                    break;
                                                                                                case "yellow":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    AfterLogin.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"yellow solid 3px\")");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://thumb101.shutterstock.com/display_pic_with_logo/93851/268648007/stock-photo-yellow-traffic-label-with-question-mark-pictogram-268648007.jpg\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var x = document.getElementsByClassName(\"_55wq _4g33 _5pxa _4l9h _5vbx\")["+ fr.getNumberInPage() + "]; void(x.getElementsByClassName(\"_54k8 _56bs _56b_ _3cqr _5uc2 _56bu\")[0].className=\"_54k8 _56bs _56b_ _5uc3 _3cqr _56bt\")");
                                                                                                    break;
                                                                                                case "green":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    AfterLogin.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"green solid 3px\")");
                                                                                                    AfterLogin.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://images.all-free-download.com/images/graphiclarge/check_mark_clip_art_9677.jpg\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                      );

                                                      //  new RetrieveFriendRequestInfo(mContext).execute(FacebookRegexPatternPool.cookie, "https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                                                  } else if (alreadyLoading)

                                                  {
                                                      if (!AfterLogin.webView.getUrl().contains("soft=requests"))
                                                          alreadyLoading = false;
                                                  }
                                          }
                                      }

            );
    }
}
