package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class friendsPage extends Activity {

    public static WebView wv;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public static WebSettings webSettings = null;
    public String username;
    public String url0;
    public static String sourceHtml="";
    public static int number_of_friends=0;
    public static String friendsCount="";
    public static int friendsDivCount=-1;

    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    String uploadFileName;
    SimpleDateFormat dateFormat= new SimpleDateFormat("HHmmss_yyyyddMM", Locale.ENGLISH);
    final String cDateTime=dateFormat.format(new Date());

    //*******************************//

    // Finds the FriendsWithImage DIV
    public static Pattern friendsWithImageDiv = Pattern.compile("\\{id:\"\\d+\",name:.+?\",uri");

    // Finds the Friends DIV
    public static Pattern friendsDiv = Pattern.compile("\\{id:\"\\d+\",name:.+?\",fi");

    // Finds the ID Level 2 Div
    public static Pattern idLevel2Div = Pattern.compile("id:.+?\"");

    // Finds the USER ID
    public static Pattern idDiv = Pattern.compile("\\d.*\\d");

    // Finds the Name Level 2 Div
    public static Pattern nameLevel2Div = Pattern.compile("name:.+?\"");

    // Finds the Name
    public static Pattern nameDiv = Pattern.compile("[a-zA-Z\\\\\\\\][^:]*[\\\\da-zA-Z\\\\\\\\]{4}");

    // Finds the Image Div
    public static Pattern imageDiv = Pattern.compile("thumbSrc:.+?\"");

    // Finds the Friends Number DIV
    public static Pattern friendsNumberDiv = Pattern.compile("\"_gs6\">.+?<");

    // Finds the Friends NumberLevel 2
    public static Pattern friendsNumberLevel2 = Pattern.compile(">\\d.*\\d");

    // Finds the Friends Number
    public static Pattern friendsNumber = Pattern.compile("\\d.*\\d");

    // Finds the Friends Own ID DIV
    public static Pattern ownIDDiv = Pattern.compile("ORIGINAL_USER_ID:\"\\d+\"");

    // Finds the Friends Own Name DIV
    public static Pattern ownNameDiv = Pattern.compile("pageTitle\">.+?<");

    /*******************************/

    /*// Finds the Friends DIV
    public static Pattern friendsDiv = Pattern.compile("<a href=\"https:.+?fref=pb&amp;.+?<[/]a>");

    // Finds the ID Level 2 Div
    public static Pattern idLevel2Div = Pattern.compile("id=\\d*&amp;e");

    // Finds the USER ID
    public static Pattern idDiv = Pattern.compile("\\d.*\\d");

    // Finds the Name Level 2 Div
    public static Pattern nameLevel2Div = Pattern.compile(">.+?<");

    // Finds the Name
    public static Pattern nameDiv = Pattern.compile("[a-zA-Z][^<]*[a-zA-Z]");*/

    public static Map<String, String> friendsMap = new HashMap<String, String>();
    public static String[ ][ ] friendsArray = new String[200][32]; //friendsArray[][0]=userID,friendsArray[][1]=userName,friendsArray[][2]=safetyStatus,friendsArray[][3]=actionTakenStatus,friendsArray[][4]=actionPriority
    //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
    public String user_own_id;
    public String user_own_name;
    public String user_id;
    public String user_full_name;
    public String user_image;
    public static String birthday=null;
    public static String gender=null;
    public static String location=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(friendsPage.this, "Entered friendPage", Toast.LENGTH_SHORT).show();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        uploadFileName = FacebookRegexPatternPool.id+"_"+cDateTime+"_friends.txt";
        //Toast.makeText(friendsPage.this, "enter friendspage", Toast.LENGTH_SHORT).show();
        url0= "https://www.facebook.com/"+username+"/friends?source_ref=pb_friends_tl";
        sourceHtml+="Loaded URL: "+url0+"\n";
        System.out.println("URL0: " + url0);
        setContentView(R.layout.wait_screen2);
        //progressBar = (ProgressBar) findViewById(R.id.myProgressBar);

        // Initialize the WebView
        wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient());
        wv.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
//*******************************************

        for(int i=0; i<200; i++) {
            friendsArray[i][2] = "0"; //Initialize all friends safetyStatus as safe [0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend]
            friendsArray[i][3] = "0"; //Initialize all friends actionTakenStatus as not taken [0->not taken; 1->taken]
            friendsArray[i][4] = "0"; //Initialize all friends actionPriority as Null Priority [0->no priority; 1->highest priority; 9->lowest priority]
            friendsArray[i][5] = "0"; //Initialize all actionTime as 0
            friendsArray[i][6] = "-1"; //Initialize all action reason as None
            friendsArray[i][7] = "30"; //Initialize all friend order index as 30

            //The answers to all 5 questions
            friendsArray[i][8] = "-1"; //Initialize all Q1 to -1
            friendsArray[i][9] = "-1"; //Initialize all Q2 to -1
            friendsArray[i][10] = "-1"; //Initialize all Q3 to -1
            friendsArray[i][11] = "-1"; //Initialize all Q4 to -1
            friendsArray[i][12] = "-1"; //Initialize all Q5 to -1

            //Mutual Friendship values
            friendsArray[i][13] = "0"; //Initialize commonPostCount to -1 <Max 10>
            friendsArray[i][14] = "0"; //Initialize commonPhotoCount to -1 <Max 30>
            friendsArray[i][15] = "0"; //Initialize mutualFriends to -1
            friendsArray[i][16] = "0"; //Initialize currentCity to -1
            friendsArray[i][17] = "0"; //Initialize commonHometown to -1
            friendsArray[i][18] = "0"; //Initialize currentStudy to -1
            friendsArray[i][19] = "0"; //Initialize pastStudy to -1
            friendsArray[i][20] = "0"; //Initialize commonEducation to -1
            friendsArray[i][21] = "0"; //Initialize currentWork to -1
            friendsArray[i][22] = "0"; //Initialize pastWork to -1
            friendsArray[i][23] = "0"; //Initialize commonWork to -1

            //Question Time
            friendsArray[i][24] = "0"; //Initialize answerTime to 0

            //Image URL
            friendsArray[i][25] = "https://scontent-mia1-1.xx.fbcdn.net/v/t1.0-1/c59.0.200.200/p200x200/10354686_10150004552801856_220367501106153455_n.jpg?oh=2c85fa504d0284186fef69a81036bd6c&amp;oe=58E5D025"; //Initialize Image URL

            //Friend userName
            friendsArray[i][26] = null; //Initialize userName to null

            //Friend userName
            friendsArray[i][27] = "0"; //Initialize Q1 time to 0
            friendsArray[i][28] = "0"; //Initialize Q2 time to 0
            friendsArray[i][29] = "0"; //Initialize Q3 time to 0
            friendsArray[i][30] = "0"; //Initialize Q4 time to 0
            friendsArray[i][31] = "0"; //Initialize Q5 time to 0
        }

//********************************************
        wv.loadUrl(url0);
        wv.setVisibility(View.INVISIBLE);
        //RequestData();
        wv.setWebViewClient(new WebViewClient() {
            private boolean isRedirected;
            private boolean pageFinished=false;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                System.out.println("shouldOverrideUrlLoading: " + url);
                sourceHtml+="shouldOverrideUrlLoading: "+url+"\n";
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
                        System.out.println("onPageFinished: " + url);
                        sourceHtml+="onPageFinished: "+url+"\n";
                        pageFinished=true;
                        wv.loadUrl("javascript:window.HTMLOUT.saveHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }
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

            sourceHtml+="HTML: "+"\n"+html;
            //Toast.makeText(friendsPage.this, "Started Data Capturing in friends page", Toast.LENGTH_SHORT).show();
            //Toast.makeText(friendsPage.this, html, Toast.LENGTH_SHORT).show();
            try {

                final Matcher numberMatch = friendsNumberDiv.matcher(html);
                if(numberMatch.find()){
                    System.out.println("Match Found: "+ numberMatch.group(0).toString());
                    final Matcher match1 = friendsNumberLevel2.matcher(numberMatch.group());
                    if (match1.find()) {
                        System.out.println("Match Found: "+ match1.group(0).toString());
                        final Matcher match11 = friendsNumber.matcher(match1.group());
                        if (match11.find()) {
                            System.out.println("Match Found: "+ match11.group(0).toString());
                            number_of_friends = Integer.parseInt(match11.group(0).toString().replace(",",""));
                        }
                    }

                    //friendsMap.put(user_id,user_full_name);
                    System.out.println("NumberOfFriends: "+ number_of_friends);
                }

                final Matcher ownIDMatch = ownIDDiv.matcher(html);
                if(ownIDMatch.find()){
                    System.out.println("Match Found: "+ ownIDMatch.group(0).toString());

                    user_own_id = ownIDMatch.group(0).toString().replaceAll("[^0-9]", "");

                    FacebookRegexPatternPool.id = user_own_id;
                    System.out.println("User Own ID: "+ user_own_id);
                }

                final Matcher ownNameMatch = ownNameDiv.matcher(html);
                if(ownNameMatch.find()){
                    System.out.println("Match Found: "+ ownNameMatch.group(0).toString());

                    user_own_name = ownNameMatch.group(0).toString().replaceAll("pageTitle\">", "");
                    user_own_name = user_own_name.replaceAll("<", "");
                    FacebookRegexPatternPool.user_full_name = user_own_name;
                    System.out.println("User Own Name: "+ user_own_name);
                }

                System.out.println("Matching Starting");
                final Matcher match = friendsWithImageDiv.matcher(html);
                while(match.find()){
                    System.out.println("FriendsCount: "+friendsDivCount);
                    if(friendsDivCount>=0) {

                        //Toast.makeText(friendsPage.this, match.group(), Toast.LENGTH_SHORT).show();
                        final Matcher match1 = friendsDiv.matcher(match.group());
                        if (match1.find()) {
                            //Toast.makeText(friendsPage.this, "ID MATCH: "+match2.group(0), Toast.LENGTH_SHORT).show();
                            final Matcher match2 = idLevel2Div.matcher(match1.group());
                            if (match2.find()) {
                                //Toast.makeText(friendsPage.this, "ID MATCH: "+match2.group(0), Toast.LENGTH_SHORT).show();
                                final Matcher match3 = idDiv.matcher(match2.group(0));
                                if (match3.find()) {
                                    //Toast.makeText(friendsPage.this, "ID: "+match3.group(0), Toast.LENGTH_SHORT).show();
                                    user_id = match3.group(0).toString();
                                    friendsArray[friendsDivCount][0] = user_id;
                                    System.out.println(user_id);
                                    //Toast.makeText(friendsPage.this, "ID Saving: " + user_id, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        final Matcher match4 = nameLevel2Div.matcher(match.group());
                        if (match4.find()) {
                            //Toast.makeText(friendsPage.this, "NAME MATCH: " + match4.group(0), Toast.LENGTH_SHORT).show();
                            final Matcher match5 = nameDiv.matcher(match4.group(0));
                            if (match5.find()) {
                                //Toast.makeText(friendsPage.this, "NAME: "+match5.group(0), Toast.LENGTH_SHORT).show();
                                user_full_name = match5.group(0).toString();
                                friendsArray[friendsDivCount][1] = user_full_name;
                                System.out.println(user_full_name);
                                //Toast.makeText(friendsPage.this, "NAME Saving: " + user_full_name, Toast.LENGTH_SHORT).show();
                            }
                        }

                        final Matcher match6 = imageDiv.matcher(match.group());
                        if (match6.find()) {
                            //Toast.makeText(friendsPage.this, "NAME MATCH: " + match4.group(0), Toast.LENGTH_SHORT).show();
                            user_image = match6.group(0).toString().replace("\"", "");
                            user_image = user_image.replace("thumbSrc:", "");
                            friendsArray[friendsDivCount][25] = user_image;
                            System.out.println(user_image);
                        }
                    }
                    friendsDivCount++;

                }
                System.out.println("Matching Finishing");
                saveJsonStringToFile();
                Intent intent = new Intent(getApplicationContext(), FriendPickerStarter.class);
                intent.putExtra("currentPictureIndex", 0);
                startActivity(intent);

            } catch (Exception e) {
                //Toast.makeText(friendsPage.this, "File Creation Failed. Reason:" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }

    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {

                        JSONObject friendsObject = (JSONObject) json.get("friends");
                        JSONObject friendsNumberObject = (JSONObject) friendsObject.get("summary");
                        friendsCount = friendsNumberObject.getString("total_count").replace(",", "");

                        System.out.println("Friends: " + friendsCount);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveJsonStringToFile() {

        /*String output="";
        for (int i=0; i<40 ; i++) {
            if (friendsArray[i][0] == null)
            {
                break;
            }
            if (friendsArray[i][1] == "Find Friends")
                output += FacebookRegexPatternPool.id + "," + (i + 1) + "," + friendsArray[i][0] + "," + "Deactivated Account" + "\n";
            else
                output += FacebookRegexPatternPool.id + "," + (i + 1) + "," + friendsArray[i][0] + "," + friendsArray[i][1] + "\n";
        }

        String createJsonString = output+"\n\n"+sourceHtml;
        try {
            FileOutputStream outputStream;
            //Create Folder
            File folder = new File(Environment.getExternalStorageDirectory().toString());
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();

            File file1 = new File(extStorageDirectory, FacebookRegexPatternPool.id+"_"+cDateTime+"_friends.txt");
            outputStream = new FileOutputStream(file1);
            outputStream.write(createJsonString.getBytes());
            outputStream.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }*/
        String output="";
        for (int i=0; i<40 ; i++) {
            output += "ID:"+ friendsArray[i][0] + ", Name:" + friendsArray[i][1] + ", URL:" + friendsArray[i][25] + "\n";
        }
        System.out.print(output);
    }
}
