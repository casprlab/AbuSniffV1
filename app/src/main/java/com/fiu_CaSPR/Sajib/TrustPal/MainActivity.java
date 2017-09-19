package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hathy.trustpal.R;

public class MainActivity extends Activity {
    private static final String target_url="https://www.facebook.com";
    private Context mContext;
    private WebView mWebview;
    private WebView mWebviewPop;
    private FrameLayout mContainer;
    private long mLastBackPressTime = 0;
    private Toast mToast;
    CookieManager cookieManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        mWebview = (WebView) findViewById(R.id.webView);
        //mWebviewPop = (WebView) findViewById(R.id.webviewPop);
        mContainer = (FrameLayout) findViewById(R.id.webview_frame);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        mWebview.setWebViewClient(new UriWebViewClient());
        mWebview.loadUrl(target_url);
        mContext=this.getApplicationContext();
    }
    private class UriWebViewClient extends WebViewClient {
        private boolean isRedirected;
        private boolean pageFinished=false;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            System.out.println("shouldOverrideUrlLoading: " + url);
            if(url.contains("facebook.com/home.php") || url.contains("facebook.com/?_rdr"))
            {
                String cookie1 = cookieManager.getCookie("facebook.com");
                String cookie2 = cookieManager.getCookie(".facebook.com");
                String cookie3 = cookieManager.getCookie("https://facebook.com");
                String cookie4 = cookieManager.getCookie("https://.facebook.com");
                if (cookie1 != null || cookie2 != null || cookie3 != null || cookie4 != null) {
                    Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                    startActivity(intent);
                }

            }
            else {
                mWebview.loadUrl(url);
            }
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

            /*if (!isRedirected) {
                //Do something you want when finished loading
                System.out.println("onPageFinished: " + url);
                if(!pageFinished)
                {
                    pageFinished=true;
                    Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                    startActivity(intent);
                }
            }*/
        }
    }
}