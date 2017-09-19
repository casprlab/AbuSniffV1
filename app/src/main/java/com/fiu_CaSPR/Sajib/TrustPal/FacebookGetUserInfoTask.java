package com.fiu_CaSPR.Sajib.TrustPal;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.fiu_CaSPR.Sajib.Constants.Dictionary;
import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.temboo.Library.Facebook.Reading.User;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by sajib on 1/19/16.
 */
class FacebookGetUserInfoTask extends AsyncTask<Void, Void, String> {
    private AccessToken accesstoken;
    // Replace with your Temboo credentials.
    private static final String TEMBOO_ACCOUNT_NAME = "fiuseclab";
    private static final String TEMBOO_APP_KEY_NAME = "myFirstApp";
    private static final String TEMBOO_APP_KEY_VALUE = "fea7fa83813941fc8b445a5b4ed22bb0";
    String AccessTokenString= AccessToken.getCurrentAccessToken().getToken();
    private TembooSession session;
    @Override
    protected String doInBackground(Void... params) {

        try {
            try {

                session = new TembooSession(TEMBOO_ACCOUNT_NAME, TEMBOO_APP_KEY_NAME, TEMBOO_APP_KEY_VALUE);
            } catch (TembooException te) {
                Log.d("", "Error1");
                Log.e("FacebookOAuthHelpder", te.getMessage());
            }
            FacebookRegexPatternPool.accessToken = AccessTokenString;
            // Get user info. Instantiate the choreo, using a session object.
            User userChoreo = new User(session);

            // Get an InputSet object for the Facebook.Reading.User choreo.
            User.UserInputSet userInputs = userChoreo.newInputSet();

            // Pass access token to the Facebook.Reading.User choreo.
            Log.d("", "Access Token: " + AccessTokenString);
            AfterLogin.finishedLoggingIn = true;
            userInputs.set_AccessToken(AccessTokenString);


            // Execute Facebook.Reading.User choreo.
            User.UserResultSet userResults = userChoreo.execute(userInputs);

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
