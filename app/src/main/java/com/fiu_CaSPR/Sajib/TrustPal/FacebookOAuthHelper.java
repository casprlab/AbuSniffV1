package com.fiu_CaSPR.Sajib.TrustPal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.fiu_CaSPR.Sajib.Constants.Dictionary;
import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.Library.Facebook.Reading.FriendLists;
import com.temboo.Library.Facebook.Reading.Picture;
import com.temboo.Library.Facebook.Reading.User;
import com.temboo.Library.Facebook.Reading.User.UserInputSet;
import com.temboo.Library.Facebook.Reading.User.UserResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;

/**
 * An AsyncTask that will be used to retrieve and display video query
 * results from Youtube.
 */
public class FacebookOAuthHelper{

private com.facebook.AccessToken AccessToken;
    private String accessToken;
    private String forwardingURL;
    private String stateToken;
    private TembooSession session;
    private WebView webView;
    public static String pictureURL=new String();
    public static JSONObject graphObject= new JSONObject();

    private final static String FACEBOOK_APP_ID = "1429957367301208";
    private final static String FACEBOOK_APP_SECRET = "a8f104eb7601f6ad197b22f474b049f9";
    private Context mContext;
    // Replace with your Temboo credentials.
    private static final String TEMBOO_ACCOUNT_NAME = "fiuseclab";
    private static final String TEMBOO_APP_KEY_NAME = "safebuk";
    private static final String TEMBOO_APP_KEY_VALUE = "Xa31WNulJ4EzrlkT08rkyNkiiUvzWNvT";

    public FacebookOAuthHelper(WebView webView, String forwardingURL, Context ctx) {
        this.forwardingURL = forwardingURL;
        //this.webView = webView;
        mContext = ctx;
        // Initialize Temboo session
        try {
            session = new TembooSession(TEMBOO_ACCOUNT_NAME, TEMBOO_APP_KEY_NAME, TEMBOO_APP_KEY_VALUE);
        } catch (TembooException te) {
            Log.d("", "Error1");
            Log.e("FacebookOAuthHelpder", te.getMessage());
        }

        // Generates a secure custom callback ID
        SecureRandom random = new SecureRandom();
        stateToken = "facebook-" + random.nextInt();
    }

    public void initOauth() {
        new FacebookGetUserInfoTask().execute(null, null, null);
    }

    public void getUserInfo() {
        // Finalize OAuth, which in turn retrieves/displays the user's info via FacebookGetUserInfoTask
        new FacebookFinalizeOAuthTask().execute(null, null, null);
    }

    public void getFriends() {
        new FacebookGetFriendListTask().execute(null, null, null);
    }

    public String getProfilePicture() {
        new FacebookFriendPicture().execute(null, null, null);
        return pictureURL;
    }

    private class FacebookInitOAuthTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Instantiate the InitializeOAuth choreo, using a session object.
                InitializeOAuth initializeOauthChoreo = new InitializeOAuth(session);

                // Get an input set for InitializeOAuth.
                InitializeOAuthInputSet initializeOauthInputs = initializeOauthChoreo.newInputSet();

                // Set inputs for InitializeOAuth, use a state token as the custom callback id
                initializeOauthInputs.set_AppID(FACEBOOK_APP_ID);
                initializeOauthInputs.set_CustomCallbackID(stateToken);
                initializeOauthInputs.set_ForwardingURL(forwardingURL);
                initializeOauthInputs.set_Scope("publish_actions, user_friends, read_custom_friendlists, user_tagged_places");
                // Execute InitializeOAuth choreo.
                InitializeOAuthResultSet initializeOauthResults = initializeOauthChoreo.execute(initializeOauthInputs);
                Log.d("", initializeOauthResults.get_AuthorizationURL());
                // This is the URL that the user will be directed to in order to login to FB and allow access.
                return initializeOauthResults.get_AuthorizationURL();
            } catch (Exception e) {
                // if an exception occurred, log it
                Log.d("", "Error2");
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String authURL) {
            try {
                // Redirect the user to the authorization (Facebook) URL
                webView.loadUrl(authURL);
            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
    }

    private class FacebookFinalizeOAuthTask extends AsyncTask<Void, Void, String> {
        // fiuseclab:Sajib45*45
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Instantiate the FinalizeOAuth choreo, using a session object.
                FinalizeOAuth finalizeOauthChoreo = new FinalizeOAuth(session);
                FinalizeOAuthInputSet finalizeOauthInputs = finalizeOauthChoreo.newInputSet();

                // Set input for FinalizeOAuth choreo.
                finalizeOauthInputs.set_AppID(FACEBOOK_APP_ID);
                finalizeOauthInputs.set_AppSecret(FACEBOOK_APP_SECRET);
                finalizeOauthInputs.set_LongLivedToken("1");

                final String customCallbackID = TEMBOO_ACCOUNT_NAME + "/" + stateToken;
                finalizeOauthInputs.set_CallbackID(customCallbackID);

                // Execute FinalizeOAuth choreo and retrieve the access token
                FinalizeOAuthResultSet finalizeOauthResults = finalizeOauthChoreo.execute(finalizeOauthInputs);

                accessToken = finalizeOauthResults.get_AccessToken();
                FacebookRegexPatternPool.accessToken = accessToken;
                return "Retrieved access token: " + accessToken;
            } catch (Exception e) {
                // if an exception occurred, log it
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String accessToken) {
            try {
                new FacebookGetUserInfoTask().execute();
            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
    }


    private class FacebookGetUserInfoTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Get user info. Instantiate the choreo, using a session object.
                User userChoreo = new User(session);

                // Get an InputSet object for the Facebook.Reading.User choreo.
                UserInputSet userInputs = userChoreo.newInputSet();

                // Pass access token to the Facebook.Reading.User choreo.
                Log.d("", "Access Token: " + accessToken);
                AfterLogin.finishedLoggingIn = true;
                userInputs.set_AccessToken(AccessToken.getToken());


                // Execute Facebook.Reading.User choreo.
                UserResultSet userResults = userChoreo.execute(userInputs);

                return userResults.get_Response();
            } catch (Exception e) {
                // if an exception occurred, log it
                //Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String userInfo) {
            try {

                JSONObject nytJSON = new JSONObject(userInfo);
                final String id = nytJSON.get("id").toString();
                final String userName = nytJSON.get("name").toString();
                Log.i("ID: ", id);
                Iterator<String> iter = nytJSON.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        if(!nytJSON.get(key).toString().contains("{"))
                            Dictionary.privateInformation.add(nytJSON.get(key).toString());
                        else{
                            String[] s = nytJSON.get(key).toString().split(",");
                            Dictionary.privateInformation.add(s[0].substring(1,s[0].length()-1));
                            for(int i =1;i <s.length-1; ++i){
                                Dictionary.privateInformation.add(s[i]);
                            }
                            Dictionary.privateInformation.add(s[s.length-1].substring(1,s[s.length-1].length()-1));
                        }

                        Log.i("Private Info: ", nytJSON.get(key) + "");
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }

                FacebookRegexPatternPool.id = id.toString();
                FacebookRegexPatternPool.Name = userName.toString();
                getFriends();

                AfterLogin.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        // AfterLogin.webView.loadData("<html><body>Please Click one of the buttons at the top right to see Friend Requests and to post to your wall.</body></html>", "text/html; charset=UTF-8", null);
                        AfterLogin.webView.loadUrl("https://www.facebook.com/" + FacebookRegexPatternPool.id);


                        //AfterLogin.webView.loadUrl("https://www.google.com/");
                    }
                });


            } catch (Exception e) {
                // if an exception occurred, show an error message
                //Log.e("error:", e.toString());
            }
        }
    }

    private class FacebookGetFriendListTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {

                // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
                // TembooSession session = new TembooSession("fiuseclab", "safebuk", "Xa31WNulJ4EzrlkT08rkyNkiiUvzWNvT");

                FriendLists friendListsChoreo = new FriendLists(session);

                // Get an InputSet object for the choreo
                FriendLists.FriendListsInputSet friendListsInputs = friendListsChoreo.newInputSet();

                // Set inputs
                friendListsInputs.set_AccessToken(accessToken);
                friendListsInputs.set_Fields("id");

                // Execute Choreo
                FriendLists.FriendListsResultSet friendListsResults = friendListsChoreo.execute(friendListsInputs);

                return friendListsResults.get_Response();
            } catch (Exception e) {
                // if an exception occurred, log it
                //Toast.makeText(FriendPickerStarter.this, "Graph Request Called", Toast.LENGTH_SHORT).show();
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String friendsInfo) {
            try {

                graphObject = new JSONObject(friendsInfo);
                try {
                    FileOutputStream outputStream;
                        //Create Folder
                        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/SafeBuk/"+FacebookRegexPatternPool.Name);
                        folder.mkdirs();
                    //Save the path as a string value
                    String extStorageDirectory = folder.toString();

                if(graphObject.toString().isEmpty()) {
                }
                else
                {
                    File file1 = new File(extStorageDirectory, "FriendList" + ".txt");
                    outputStream = new FileOutputStream(file1);
                    outputStream.write(graphObject.toString().getBytes());
                    outputStream.close();
                }
                    new FacebookFriendPicture().execute(null, null, null);
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e("error:", e.toString());
            }
        }
    }

    private class FacebookFriendPicture extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {

                // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
                // TembooSession session = new TembooSession("fiuseclab", "safebuk", "Xa31WNulJ4EzrlkT08rkyNkiiUvzWNvT");

                Picture pictureChoreo = new Picture(session);

                // Get an InputSet object for the choreo
                Picture.PictureInputSet pictureInputs = pictureChoreo.newInputSet();

                // Set inputs
                pictureInputs.set_Redirect("false");
                pictureInputs.set_AccessToken(accessToken);
                pictureInputs.set_ProfileID("1734570992");

                // Execute Choreo
                Picture.PictureResultSet pictureResults = pictureChoreo.execute(pictureInputs);
                return pictureResults.get_Response();

            } catch (Exception e) {
                // if an exception occurred, log it
                //Toast.makeText(FriendPickerStarter.this, "Graph Request Called", Toast.LENGTH_SHORT).show();
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String friendsInfo) {
            try {

                JSONObject nytJSON = new JSONObject(friendsInfo);
                pictureURL = nytJSON.get("url").toString();
//pictureURL.replaceAll("\", "");

            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e("error:", e.toString());
            }
        }
    }
}
