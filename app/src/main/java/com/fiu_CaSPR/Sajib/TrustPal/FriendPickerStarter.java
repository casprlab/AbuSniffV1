package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Sajib.Utility.MyTimerTask;
import com.hathy.trustpal.R;

import java.util.Timer;


public class FriendPickerStarter extends Activity {


    public static boolean finishedLoggingIn = false;
    final Context context = this;
    public static String htmlSource = "";
    public static String urlLoad = "";
    public static WebView webView;
    public static int filecount=0;
    public static int currentPictureIndex = 0;
    public static boolean pageFinished = false;
    public static boolean isRedirected = true;
    public static int position = 0;
    String url;

    private ProgressDialog dialog = null;
    // We won't navigate to this URL, we simply use it as an indicator of
    // when in the OAuth flow we should go through the finalize routines
    private final static String FORWARDING_URL = "http://temboo.placeholder.url";
    @Deprecated
    public static String friendRequestDataString = "";
    @Deprecated
    public static boolean alreadyLoadedFriendsURL = false;
    final MyTimerTask myTask = new MyTimerTask(this);
    final Timer myTimer = new Timer();
    public static WebSettings webSettings =null;
    String userId;
    String userName;
    Integer friendIndex;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_screen3);

        //Toast.makeText(FriendPickerStarter.this, "After Login Page Loaded", Toast.LENGTH_SHORT).show();

        if (isNetworkAvailable()) {
            Toast.makeText(this, "Loading Please Wait", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG);
            this.finish();
            System.exit(0);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPictureIndex = extras.getInt("currentPictureIndex");
        }

        // Initialize the WebView
        //FacebookRegexPatternPool.id = "10200577169468421";
        webView = friendsPage.wv;
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        System.out.println("Current Index in Loop: " + currentPictureIndex);
        url = "https://www.facebook.com/" + friendsPage.friendsArray[currentPictureIndex][0];
        //System.out.println("First: " + url);
        webView.loadUrl(url);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                System.out.println("shouldOverrideUrlLoading: " + url);
                view.stopLoading();
                if (url.contains("facebook.com")) {
                    String id = null;
                    String part1 = null;
                    String part2 = null;
                    String[] urlCut = url.split("/");
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < urlCut[urlCut.length - 1].length(); ++i) {
                        if (urlCut[urlCut.length - 1].charAt(i) != '?') {
                            builder.append(urlCut[urlCut.length - 1].charAt(i));
                        } else break;
                    }
                    if (builder.toString().equals("profile.php")) {
                        String[] parts = urlCut[urlCut.length - 1].split("=");
                        part1 = parts[0]; // 004
                        part2 = parts[1]; // 03455
                        userId = part2;
                        userName = part2;
                        friendsPage.friendsArray[currentPictureIndex][26] = userName;
                        System.out.println("userID: " + userName);
                    } else {
                        userName = builder.toString();
                        friendsPage.friendsArray[currentPictureIndex][26] = userName;
                        System.out.println("userName: " + userName);
                    }
                    //new FacebookRegexPatternPool().storeFriends();
                    currentPictureIndex++;
                    loopFriends(currentPictureIndex);
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //Do something you want when finished loading

            }
        });


    }

    public void loopFriends(int currentPictureIndex)
    {
        if(currentPictureIndex>=32)
        {
            start();
        }
        else
        {
            Intent intent= getIntent();
            //finish();
            intent.putExtra("currentPictureIndex", currentPictureIndex);
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.onCreate(null);
    }

    public void start()
    {
        String output="";
        for (int i=0; i<32 ; i++) {
            output += "Username "+i+":"+ friendsPage.friendsArray[i][26]+"\n";
        }
        System.out.print(output);

        Intent intent = new Intent(getApplicationContext(), FriendSelectorView1.class);
        intent.putExtra("username", FacebookRegexPatternPool.userName);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}


