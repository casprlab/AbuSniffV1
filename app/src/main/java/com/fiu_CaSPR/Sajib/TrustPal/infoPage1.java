package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class infoPage1 extends Activity {

    public static WebView wv;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public static WebSettings webSettings = null;
    public String username;
    public String url2;
    public String html;


    //*******************************//

    // Finds the Birthday DIV
    public static Pattern birthdayDiv = Pattern.compile(">Birthday.+?<[/]div><[/]span>");

    // Finds the Birthday Level 2
    public static Pattern birthdayLevel2 = Pattern.compile("<div>.+?<[/]div>");

    // Finds the Birthday
    public static Pattern bday = Pattern.compile(">.+?<");


    // Finds the Living DIV
    public static Pattern livingDiv = Pattern.compile("Lives in.+?<[/]a>");

    // Finds the Living Level 2 Div
    public static Pattern livingLevel2 = Pattern.compile(">.+?<");


    public static String birthday=null;
    public static String gender=null;
    public static String location=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(friendsPage.this, "Entered friendPage", Toast.LENGTH_SHORT).show();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        url2= "https://www.facebook.com/"+FacebookRegexPatternPool.userName+"/about?section=overview&pnref=about";
        System.out.println("URL2: "+ url2);
        //Toast.makeText(infoPage.this, "URL: "+url1, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.wait_result);
        //progressBar = (ProgressBar) findViewById(R.id.myProgressBar);

        // Initialize the WebView
        wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient());
        wv.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");

//********************************************
        try {
            wv.loadUrl(url2);
            wv.setWebViewClient(new WebViewClient() {
                private boolean isRedirected;
                private boolean pageFinished = false;

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    System.out.println("shouldOverrideUrlLoading: " + url);
                    view.loadUrl(url);
                    isRedirected = true;
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    if (!isRedirected) {
                        //Do something you want when starts loading
                        System.out.println("onPageStarted: " + url);
                    }

                    isRedirected = false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    if (!isRedirected) {
                        //Do something you want when finished loading
                        System.out.println("onPageFinished: " + url);
                        if (url.equals(url2) && !pageFinished) {
                            pageFinished = true;
                            wv.loadUrl("javascript:window.HTMLOUT.saveHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        }
                    }
                }

            });

            /********************/
        }
        catch(Exception e)
        {
            System.out.println(e);
            start();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fetch_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadListener {
        @JavascriptInterface
        public void saveHTML(String html) {
            //Toast.makeText(friendsPage.this, "Started Data Capturing in friends page", Toast.LENGTH_SHORT).show();
            //Toast.makeText(infoPage.this, html, Toast.LENGTH_SHORT).show();
            try {

                final Matcher birthdayMatch = birthdayDiv.matcher(html);
                if(birthdayMatch.find()){
                    System.out.println("Match Found: "+ birthdayMatch.group(0).toString());
                    final Matcher match21 = birthdayLevel2.matcher(birthdayMatch.group());
                    if (match21.find()) {
                        System.out.println("Match Found: "+ match21.group(0).toString());
                        final Matcher match22 = bday.matcher(match21.group());
                        if (match22.find()) {
                            System.out.println("Match Found: "+ match22.group(0).toString());
                            birthday = match22.group(0).toString();
                            birthday = birthday.replace("<", "");
                            birthday = birthday.replace(">", "");
                        }
                    }

                    //friendsMap.put(user_id,user_full_name);
                    System.out.println("Birthday: "+ birthday);
                }

                final Matcher livingMatch = livingDiv.matcher(html);
                if(livingMatch.find()){
                    System.out.println("Match Found: "+ livingMatch.group(0).toString());
                    final Matcher match11 = livingLevel2.matcher(livingMatch.group());
                    if (match11.find()) {
                        System.out.println("Match Found: "+ match11.group(0).toString());
                        location = match11.group(0).toString();
                        location = location.replace("<", "");
                        location = location.replace(">", "");
                    }
                    //friendsMap.put(user_id,user_full_name);
                    System.out.println("Location: "+ location);
                }

                start();

            } catch (Exception e) {
                //Toast.makeText(friendsPage2.this, "File Creation Failed. Reason:" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }

    }
    public void start()
    {
        /*if(friendsCount<20)
        {
            Intent intent = new Intent(getApplicationContext(), UnqualifiedUser.class);
            startActivity(intent);
        }
        else
        {*/
        Intent intent = new Intent(getApplicationContext(), ResultSummary.class);
        intent.putExtra("username", FacebookRegexPatternPool.userName);
        startActivity(intent);
        //}
    }

}