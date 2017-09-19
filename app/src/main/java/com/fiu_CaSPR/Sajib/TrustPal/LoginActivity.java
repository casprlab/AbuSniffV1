/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fiu_CaSPR.Sajib.TrustPal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hathy.trustpal.R;

import java.security.MessageDigest;
import java.util.Arrays;

public class LoginActivity extends FragmentActivity {

    private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
    private LoginButton loginButton;
    private PendingAction pendingAction = PendingAction.NONE;
    boolean loggedIn=false;
    CallbackManager callbackManager;
    private Button startGameButton;
    private TextView welcome;
    private LinearLayout layout1;
    private String keyhash="";

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fiu_CaSPR.Sajib.TrustPal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyhash= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                System.out.println("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.d("KeyHash Error1", e.toString());

        }

        if (savedInstanceState != null) {
            String name = savedInstanceState
                    .getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        welcome = (TextView) findViewById(R.id.welcome);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        //startGameButton.setText("Welcome to PalSpy");
        //startGameButton.setEnabled(false);
        //loginButton.setReadPermissions(permissions)
        loginButton
                .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        layout1.setPadding(0, 300, 0, 0);
                        /*startGameButton.setText("Start");
                        welcome.setText("Please click on start to continue");
                        startGameButton.setEnabled(true);*/
                        startFriendListActivity();
                        Log.i("ran here:", "success");
                        System.out.println("KEYHASH: "+keyhash);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.i("ran here:", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i("ran here error:", "" + exception);
                    }
                });

        //profilePictureView = (ImageView) findViewById(R.id.profilePicture);
        //greeting = (TextView) findViewById(R.id.greeting);

        startGameButton = (Button) findViewById(R.id.getInvitableFriends);
        startGameButton.setVisibility(View.INVISIBLE);
        startGameButton.setEnabled(false);
        if(AccessToken.getCurrentAccessToken()!=null) {
            loggedIn = true;
            /*layout1.setPadding(0,300,0,0);
            startGameButton.setText("Start");
            welcome.setText("Please click on start to continue");
            startGameButton.setEnabled(true);*/
            startFriendListActivity();
        }

        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startFriendListActivity();
            }
        });

        //controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in
        // analytics and advertising reporting. Do so in
        // the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in
        // analytics and advertising
        // reporting. Do so in the onPause methods of the primary Activities
        // that an app may be launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void startFriendListActivity() {
        //Toast.makeText(MainActivity.this, "Access Token: " + AccessToken.getCurrentAccessToken().getToken(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
        startActivity(intent);
    }

}
