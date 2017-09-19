package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class FetchData extends Activity {

    public static WebView wv;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public static WebSettings webSettings = null;
    public static int position = 0;
    public static int filecount = 0;
    public static String url;
    public static String filename;
    public static boolean pageSaved=false;
    public static String currenturl="";
    public static Iterator entries;
    public String url0 = "https://www.facebook.com/" + FacebookRegexPatternPool.userName + "/about";
    public String url1 = "https://www.facebook.com/" + FacebookRegexPatternPool.userName + "/about?section=relationship&pnref=about";
    public String url2 = "https://www.facebook.com/" + FacebookRegexPatternPool.userName + "/about?section=year-overviews&pnref=about";
    public String url3 = "https://www.facebook.com/" + FacebookRegexPatternPool.userName + "/places_visited";
    public String url4 = "https://www.facebook.com/" + FacebookRegexPatternPool.userName + "/map";

    Map<String, String> myMap = new HashMap<String, String>();
    String s = url0+";about,"+url1+";relationship,"+url2+";overview,"+url3+";places,"+url4+";map";
    String[] pairs = s.split(",");

    /**
     * ****************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fetch_data);
        progressBar = (ProgressBar) findViewById(R.id.myProgressBar);

        // Initialize the WebView
        wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient());
        wv.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");

        for (int i=0;i<pairs.length;i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(";");
            myMap.put(keyValue[0], keyValue[1]);
        }

        entries = myMap.entrySet().iterator();
        Map.Entry entry = (Map.Entry) entries.next();
        url = (String) entry.getKey();
        filename = (String) entry.getValue();
        System.out.println("Key = " + url + ", Value = " + filename);
        wv.loadUrl(url);
        Toast.makeText(FetchData.this, "page " + filename + " started", Toast.LENGTH_SHORT).show();

        if (entries.hasNext() && FacebookRegexPatternPool.userName!="") {
            wv.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    Toast.makeText(FetchData.this, "progress changed. position: " + position, Toast.LENGTH_SHORT).show();
                        while (entries.hasNext() && position<5) {
                            progressBar.setProgress(position * 20);
                            Map.Entry entry = (Map.Entry) entries.next();
                            url = (String) entry.getKey();
                            filename = (String) entry.getValue();
                            System.out.println("Key = " + url + ", Value = " + filename);
                            wv.loadUrl(url);
                            Toast.makeText(FetchData.this, "page " + filename + " started", Toast.LENGTH_SHORT).show();
                        }
                    if(position>=5) {
                        Intent intent = new Intent(getApplicationContext(), FriendSelectorView1.class);
                        startActivity(intent);
                    }

                    }

                });
        }


        // Set up WebView for OAuth2 login - intercept redirect when the redirect
        // URL matches our FORWARDING_URL, in which case we will complete the OAuth
        // flow using Temboo
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("LOADING");
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("PageStarted: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                System.out.println("PageFinished: " + url);
                currenturl=wv.getUrl();
                wv.loadUrl("javascript:window.HTMLOUT.saveHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        /********************/


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
            Toast.makeText(FetchData.this, "JS function called", Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream outputStream;

                String prevurl="";



                    //Create Folder
                    File folder = new File(Environment.getExternalStorageDirectory().toString() + "/SafeBuk/"+FacebookRegexPatternPool.Name);
                    folder.mkdirs();

                    //Save the path as a string value
                    String extStorageDirectory = folder.toString();

                        File file = new File(extStorageDirectory, FacebookRegexPatternPool.userName + "_" + filename + ".txt");
                        outputStream = new FileOutputStream(file);
                        outputStream.write(html.getBytes());
                        outputStream.close();
                        filecount++;
                        position++;
                        pageSaved=true;
                        Toast.makeText(FetchData.this, filename + " file Saved Successfully. File: " + filecount + " for url: " + currenturl, Toast.LENGTH_SHORT).show();



            } catch (IOException e) {
                Toast.makeText(FetchData.this, "File Creation Failed. Reason:" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }

    }
}
