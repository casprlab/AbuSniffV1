package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;


public class AfterLogin extends Activity {

    int serverResponseCode = 0;

    TextView messageText;
    String upLoadServerUri = null;
    String uploadFileName = "folder_cache";
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";

    public static boolean finishedLoggingIn = false;
    public static WebView webView;
    public static String id=null;
    public static String Name=null;
    public static String gender=null;

    private ProgressDialog dialog = null;
    // We won't navigate to this URL, we simply use it as an indicator of
    // when in the OAuth flow we should go through the finalize routines
    private final static String FORWARDING_URL = "http://temboo.placeholder.url";
    //final MyTimerTask myTask = new MyTimerTask(this);
    final Timer myTimer = new Timer();
    public static WebSettings webSettings =null;

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
        setContentView(R.layout.wait_screen1);

        //Toast.makeText(AfterLogin.this, "After Login Page Loaded", Toast.LENGTH_SHORT).show();

        if (isNetworkAvailable()) {
            Toast.makeText(this, "Loading Please Wait", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG);
            this.finish();
            System.exit(0);
        }


        // Initialize the WebView
        //FacebookRegexPatternPool.id = "10200577169468421";
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        webView.loadUrl("https://www.facebook.com/me");
        //RequestData();
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient() {

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
                    if(url.contains("facebook.com") && !url.contains("login.php") && !pageFinished)
                    {
                        pageFinished=true;

                        if (webView != null) {
                            String s = webView.getUrl();
                            //Toast.makeText(AfterLogin.this, "URL LOADED: " + s, Toast.LENGTH_SHORT).show();
                            if (s.contains("facebook.com")) {
                                System.out.println("PageLoadURL:"+ webView.getUrl());
                                String id=null;
                                String part1=null;
                                String part2=null;
                                String[] urlCut = s.split("/");
                                StringBuilder builder = new StringBuilder();

                                for (int i = 0; i < urlCut[urlCut.length - 1].length(); ++i) {
                                    if (urlCut[urlCut.length - 1].charAt(i) != '?') {
                                        builder.append(urlCut[urlCut.length - 1].charAt(i));
                                    } else break;
                                }
                                if(builder.toString().equals("profile.php"))
                                {
                                    String[] parts = urlCut[urlCut.length - 1].split("=");
                                    part1 = parts[0]; // 004
                                    part2 = parts[1]; // 03455
                                    FacebookRegexPatternPool.id = part2;
                                    FacebookRegexPatternPool.userName = part2;
                                    System.out.println("userID: "+FacebookRegexPatternPool.id);
                                }
                                else
                                {
                                    FacebookRegexPatternPool.userName = builder.toString();
                                    System.out.println("userName: "+FacebookRegexPatternPool.userName);
                                }
                                //new FacebookRegexPatternPool().storeFriends();
                                start();
                            }

                        }
                    }
                }
            }
        });


    }

    public void start()
    {
        Intent intent = new Intent(getApplicationContext(),Consent.class);
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

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        System.out.println(json);
                        id = json.getString("id");
                        Name = json.getString("name");
                        gender = json.getString("gender");
                        System.out.println("Data: " + id + ", " + Name + ", " + gender);
                        System.out.println("AccessToken:" + AccessToken.getCurrentAccessToken().getToken());

                        FacebookRegexPatternPool.id = id;
                        FacebookRegexPatternPool.Name = Name;
                        webView.loadUrl("https://www.facebook.com/" + FacebookRegexPatternPool.id+"/?access_token=" + AccessToken.getCurrentAccessToken().getToken());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void doLastJob() {

        /************* File Upload Code ****************/
        upLoadServerUri = "http://users.cs.fiu.edu/~stalu001/upload.php";

        dialog = ProgressDialog.show(AfterLogin.this, "", "", true);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("uploading started.....");
                    }
                });

                uploadFile(uploadFilePath + "" + uploadFileName);

            }
        }).start();
        /************* File Upload Code Ends ****************/

        Intent intent = new Intent(getApplicationContext(),FinishJob.class);
        startActivity(intent);
    }
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed";

                            //messageText.setText(msg);
                            //Toast.makeText(FriendResult.this, "File Upload Complete.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(AfterLogin.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(AfterLogin.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }


}


