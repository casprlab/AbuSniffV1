package com.fiu_CaSPR.Sajib.Utility;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fhu004 on 6/9/2015.
 *
 */
@Deprecated
public class AnalyzeFacebookFriend {

    private WebView webView;
    // pass in the div here to parse and get the url to the user's page
    // it must be a single div of
    String urlLoad = "";
    String whichFunction = "";
    public static String analysis = "";
    Document doc = null;

    public AnalyzeFacebookFriend(String url, boolean divRequiresFurtherParse, WebView webView){
        if(divRequiresFurtherParse){
            this.urlLoad = parseDiv(url);
        }else {
            this.urlLoad = url;
        }
        try {
            doc = Jsoup.connect(urlLoad)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();
            String htmlBody = doc.body().select("<div class=\"_4qm1\">[^*].+?</ul></div>").toString();
            Log.e("HTMLBody from JSOUP: " , htmlBody);
        }
        catch(Exception e){
            Log.e("Error Jsoup:" , e.toString());
        }


    }

    private String parseDiv(String div){
        String regexToFindUserID = "https://www.facebook.com/[^*].+?\"";
        Pattern pattern = Pattern.compile(regexToFindUserID);
        final Matcher matcher = pattern.matcher(div);
        if(matcher.find()) {
            String url = matcher.group();
            url.substring(0,url.length()-1);
            return url;
        }else
            return "Could Not find";
    }

    private void getLocation(){
        whichFunction = "getLocation";
        this.webView.loadUrl(urlLoad+"/about?section=living&pnref=about");
    }

    private void getAddress(){
        whichFunction = "getAddress";
        this.webView.loadUrl(urlLoad+"/about?section=overview&pnref=about");

    }

    private void getAllOverView(){
        whichFunction = "overView";
        this.webView.loadUrl(urlLoad+"/about?section=overview&pnref=about");
    }


    class LoadListener{
        @JavascriptInterface
        public void processHTML(String html){
        //  Log.e("html", html);

            switch(whichFunction){
                case "getLocation": {
                    String regex = "<div class=\"_4qm1\">[^*].+?</ul></div>";
                    Pattern regexPattern = Pattern.compile(regex);
                    final Matcher regexMatcher = regexPattern.matcher(html);
                    if (regexMatcher.find()) {
                        String innerRegex = "<span class=\"_5[^*].+?<";
                        Pattern innerRegexPatt = Pattern.compile(innerRegex);
                        final Matcher regexMatcher2 = innerRegexPatt.matcher(regexMatcher.group());
                        if(regexMatcher2.find())
                            analysis = regexMatcher2.group();
                    }
                    break;
                }
                case "getAddress": {
                    String regex = "Lives in[^*].+?</a>";
                    Pattern regexPattern = Pattern.compile(regex);
                    final Matcher regexMatcher = regexPattern.matcher(html);
                    if (regexMatcher.find()) {
                        analysis = regexMatcher.group();
                    }
                    break;
                }
             //   case "overView":{
               //     final Matcher regexMatcher = regexPattern.matcher(html);
                 //   analysis = "";
                  //  while (regexMatcher.find()) {
                   //     analysis += regexMatcher.group();
                    //}
                    //break;
               // }
            }
        }
    }
}
