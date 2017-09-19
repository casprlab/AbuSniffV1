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
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Sajib.Utility.MyTimerTask;
import com.hathy.trustpal.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Timer;


public class SaveMutualData extends Activity {


    static int commonPostCount=0;
    static int commonPhotoCount=0;
    static int commonCount=0;
    static int mutualFriends=0;
    static String currentCity=null;
    static String commonHometown=null;
    static String currentStudy=null;
    static String pastStudy=null;
    static String commonEducation=null;
    static String currentWork=null;
    static String pastWork=null;
    static String commonWork=null;
    static String classname=null;

    public static int currentPictureIndex;
    public static WebView wv;
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
    String username;
    String url0;
    static Document doc;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPictureIndex = extras.getInt("currentPictureIndex");
        }

        if(currentPictureIndex==24)
        {
            setContentView(R.layout.wait_screen3);
        }
        else
        {
            setContentView(R.layout.wait_processing);
        }

        if (isNetworkAvailable()) {
            Toast.makeText(this, "Loading Please Wait", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG);
            this.finish();
            System.exit(0);
        }
        reset();
        // Initialize the WebView
        wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient());
        wv.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");

        url0 = "https://www.facebook.com/friendship/" + FacebookRegexPatternPool.userName + "/" + friendsPage.friendsArray[currentPictureIndex][26];
        System.out.println("Mutual URL: "+url0);
        //Toast.makeText(com.fiu_CaSPR.Frank.safebuk.url0.this, "page 0 started", Toast.LENGTH_SHORT).show();
//********************************************
        wv.loadUrl(url0);
        wv.setVisibility(View.INVISIBLE);
        wv.setWebViewClient(new WebViewClient() {
            private boolean isRedirected;
            private boolean pageFinished=false;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                System.out.println("shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                isRedirected = true;
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (isRedirected) {
                    //Do something you want when starts loading
                    System.out.println("onPageStarted: " + url);
                }

                isRedirected = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (!isRedirected) {
                    //Do something you want when finished loading
                    if(url.equals(url0) && !pageFinished)
                    {
                        pageFinished=true;
                        System.out.println("onPageFinished: " + url);
                        wv.loadUrl("javascript:window.HTMLOUT.saveHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }
            }

        });

        /********************/


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
    class LoadListener {
        @JavascriptInterface
        public void saveHTML(String html) {
            //Toast.makeText(com.fiu_CaSPR.Frank.safebuk.url0.this, "JS function called", Toast.LENGTH_SHORT).show();
            try {
                doc = Jsoup.parse(html);
                //System.out.print(doc);
                setCommonValues();

                Intent intent = new Intent();
                if (currentPictureIndex == 0) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView2.class);
                } else if (currentPictureIndex == 1) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView3.class);
                } else if (currentPictureIndex == 2) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView4.class);
                } else if (currentPictureIndex == 3) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView5.class);
                } else if (currentPictureIndex == 4) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView6.class);
                } else if (currentPictureIndex == 5) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView7.class);
                } else if (currentPictureIndex == 6) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView8.class);
                } else if (currentPictureIndex == 7) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView9.class);
                } else if (currentPictureIndex == 8) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView10.class);
                } else if (currentPictureIndex == 9) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView11.class);
                } else if (currentPictureIndex == 10) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView12.class);
                } else if (currentPictureIndex == 11) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView13.class);
                } else if (currentPictureIndex == 12) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView14.class);
                } else if (currentPictureIndex == 13) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView15.class);
                } else if (currentPictureIndex == 14) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView16.class);
                } else if (currentPictureIndex == 15) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView17.class);
                } else if (currentPictureIndex == 16) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView18.class);
                } else if (currentPictureIndex == 17) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView19.class);
                } else if (currentPictureIndex == 18) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView20.class);
                } else if (currentPictureIndex == 19) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView21.class);
                } else if (currentPictureIndex == 20) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView22.class);
                } else if (currentPictureIndex == 21) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView23.class);
                } else if (currentPictureIndex == 22) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView24.class);
                } else if (currentPictureIndex == 23) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView25.class);
                } else if (currentPictureIndex == 24) {
                    intent = new Intent(getApplicationContext(), infoPage1.class);
                /*} else if (currentPictureIndex == 25) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView27.class);
                } else if (currentPictureIndex == 26) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView28.class);
                } else if (currentPictureIndex == 27) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView29.class);
                } else if (currentPictureIndex == 28) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView30.class);
                } else if (currentPictureIndex == 29) {
                    intent = new Intent(getApplicationContext(), infoPage1.class);*/
                }
                startActivity(intent);
            }
            catch(Exception e)
            {
                System.out.println("Error in SaveMutualData:"+ e);
                Intent intent = new Intent();
                if (currentPictureIndex == 0) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView2.class);
                } else if (currentPictureIndex == 1) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView3.class);
                } else if (currentPictureIndex == 2) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView4.class);
                } else if (currentPictureIndex == 3) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView5.class);
                } else if (currentPictureIndex == 4) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView6.class);
                } else if (currentPictureIndex == 5) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView7.class);
                } else if (currentPictureIndex == 6) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView8.class);
                } else if (currentPictureIndex == 7) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView9.class);
                } else if (currentPictureIndex == 8) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView10.class);
                } else if (currentPictureIndex == 9) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView11.class);
                } else if (currentPictureIndex == 10) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView12.class);
                } else if (currentPictureIndex == 11) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView13.class);
                } else if (currentPictureIndex == 12) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView14.class);
                } else if (currentPictureIndex == 13) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView15.class);
                } else if (currentPictureIndex == 14) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView16.class);
                } else if (currentPictureIndex == 15) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView17.class);
                } else if (currentPictureIndex == 16) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView18.class);
                } else if (currentPictureIndex == 17) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView19.class);
                } else if (currentPictureIndex == 18) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView20.class);
                } else if (currentPictureIndex == 19) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView21.class);
                } else if (currentPictureIndex == 20) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView22.class);
                } else if (currentPictureIndex == 21) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView23.class);
                } else if (currentPictureIndex == 22) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView24.class);
                } else if (currentPictureIndex == 23) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView25.class);
                } else if (currentPictureIndex == 24) {
                    intent = new Intent(getApplicationContext(), infoPage1.class);
                /*} else if (currentPictureIndex == 25) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView27.class);
                } else if (currentPictureIndex == 26) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView28.class);
                } else if (currentPictureIndex == 27) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView29.class);
                } else if (currentPictureIndex == 28) {
                    intent = new Intent(getApplicationContext(), FriendSelectorView30.class);
                } else if (currentPictureIndex == 29) {
                    intent = new Intent(getApplicationContext(), infoPage1.class);*/
                }
                startActivity(intent);
            }
        }

    }

    public void reset()
    {
        commonPostCount=0;
        commonPhotoCount=0;
        commonCount=0;
        mutualFriends=0;
        currentCity="";
        commonHometown="";
        currentStudy="";
        pastStudy="";
        commonEducation="";
        currentWork="";
        pastWork="";
        commonWork="";
    }

    public void countMutualPosts()
    {
        int count=0;
        try {
            Elements myin = doc.select("._4bl7._3_cw._4k2o");
            Elements posts = myin.select("._4-u2.mbm._5jmm._5pat._5v3q._4-u8");
            for (int i = 0; i < posts.size(); i++) {
                //System.out.println(posts.get(i).text());
                count++;
            }
            commonPostCount=count;
            //System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void countMutualPhotos()
    {
        int count=0;
        try {
            Elements myin = doc.select("._2a2q._bkt");
            Elements posts = myin.select("._5dec._xcx");
            for (int i = 0; i < posts.size(); i++) {
                //System.out.println(posts.get(i).text());
                count++;
            }
            commonPhotoCount=count;
            //System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void findCommon()
    {
        String commonText="";
        int commonCount=0;
        try {
            Elements myin = doc.select("ul.uiList._4kg");
            Elements commons = myin.select("._50f3");

            for (int i = 0; i < commons.size(); i++) {
                commonText = commons.get(i).text();
                commonCount++;
                if(commonText.contains("mutual friends including"))
                {
                    commonText=commonText.replaceAll("\\D+","");
                    mutualFriends = Integer.parseInt(commonText);
                }
                else if(commonText.contains("Live in"))
                {
                    currentCity="Yes";
                }
                else if(commonText.contains("From"))
                {
                    commonHometown="Yes";
                }
                else if(commonText.contains("Study at"))
                {
                    currentStudy="Yes";
                }
                else if(commonText.contains("Studied at"))
                {
                    pastStudy="Yes";
                }
                else if(commonText.contains("Education:"))
                {
                    commonEducation="Yes";
                }
                else if(commonText.contains("Work at"))
                {
                    currentWork="Yes";
                }
                else if(commonText.contains("Worked at"))
                {
                    pastWork="Yes";
                }
                else if(commonText.contains("Workplace:"))
                {
                    commonWork="Yes";
                }

                //System.out.println(commonText+"\n");
            }

            //System.out.println(commonText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setCommonValues() {

        countMutualPosts();
        countMutualPhotos();
        findCommon();

        friendsPage.friendsArray[currentPictureIndex][13] = Integer.toString(commonPostCount); //Initialize commonPostCount to -1
        friendsPage.friendsArray[currentPictureIndex][14] = Integer.toString(commonPhotoCount); //Initialize commonPhotoCount to -1
        friendsPage.friendsArray[currentPictureIndex][15] = Integer.toString(mutualFriends); //Initialize mutualFriends to -1
        if(currentCity.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][16] = currentCity; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][16] = "No"; //Initialize currentCity to -1
        }
        if(commonHometown.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][17] = commonHometown; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][17] = "No"; //Initialize currentCity to -1
        }
        if(currentStudy.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][18] = currentStudy; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][18] = "No"; //Initialize currentCity to -1
        }
        if(pastStudy.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][19] = pastStudy; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][19] = "No"; //Initialize currentCity to -1
        }
        if(commonEducation.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][20] = commonEducation; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][20] = "No"; //Initialize currentCity to -1
        }
        if(currentWork.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][21] = currentWork; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][21] = "No"; //Initialize currentCity to -1
        }
        if(pastWork.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][22] = pastWork; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][22] = "No"; //Initialize currentCity to -1
        }
        if(commonWork.equals("Yes")) {
            friendsPage.friendsArray[currentPictureIndex][23] = commonWork; //Initialize currentCity to -1
        }
        else
        {
            friendsPage.friendsArray[currentPictureIndex][23] = "No"; //Initialize currentCity to -1
        }

        System.out.println(currentPictureIndex + "\n" + "commonPostCount: " + commonPostCount + "\n" + "commonPhotoCount: " + commonPhotoCount + "\n" + "mutualFriends: " + mutualFriends + "\n" + "currentCity: " + currentCity + "\n" + "commonHometown: " + commonHometown + "\n" + "currentStudy: " + currentStudy + "\n" + "pastStudy: " + pastStudy + "\n" + "commonEducation: " + commonEducation + "\n" + "currentWork: " + currentWork + "\n" + "pastWork: " + pastWork + "\n" + "commonWork: " + commonWork);


    }

}

