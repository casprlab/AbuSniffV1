package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hathy.trustpal.R;


public class SaveFriends extends Activity {

    public static int friendListCounter = 0;
    public static String userId;
    Button savebutton;
    TextView saveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_friends);
        savebutton = (Button) findViewById(R.id.finish);
        saveText = (TextView) findViewById(R.id.saveText);
        int counter = friendListCounter + 1;
        saveText.setText("Now Saving Friend " + counter);
        //getMessage();
    }

    public void myClickMethod(View v) {
        // more stuff
        //Toast.makeText(SaveFriends.this, "Button Clicked. Counter " + friendListCounter, Toast.LENGTH_SHORT).show();
        getMessage();
    }

    //Do all the source page saving for the 20 friends here
    public void getMessage() {
        // Create The  Intent and Start The Activity to get The message
        Intent intent = new Intent(SaveFriends.this, SaveMutualData.class);
        if (friendListCounter < 20) {
            if (friendListCounter == 0) {

                userId = friendsPage.friendsArray[0][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 1) {

                userId = friendsPage.friendsArray[1][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 2) {

                userId = friendsPage.friendsArray[2][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;


            } else if (friendListCounter == 3) {

                userId = friendsPage.friendsArray[3][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 4) {

                userId = friendsPage.friendsArray[4][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 5) {

                userId = friendsPage.friendsArray[5][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 6) {

                userId = friendsPage.friendsArray[6][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 7) {

                userId = friendsPage.friendsArray[7][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 8) {

                userId = friendsPage.friendsArray[8][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 9) {

                userId = friendsPage.friendsArray[9][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 10) {

                userId = friendsPage.friendsArray[10][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 11) {

                userId = friendsPage.friendsArray[11][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 12) {

                userId = friendsPage.friendsArray[12][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;
            } else if (friendListCounter == 13) {

                userId = friendsPage.friendsArray[13][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 14) {

                userId = friendsPage.friendsArray[14][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 15) {

                userId = friendsPage.friendsArray[15][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 16) {

                userId = friendsPage.friendsArray[16][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 17) {

                userId = friendsPage.friendsArray[17][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 18) {

                userId = friendsPage.friendsArray[18][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;

            } else if (friendListCounter == 19) {

                userId = friendsPage.friendsArray[19][0];
                intent.putExtra("userId", userId);
                intent.putExtra("friendIndex", friendListCounter);
                startActivity(intent);// Activity is started with requestCode=1
                friendListCounter++;
            } else {
                friendListCounter = 0;
            }
        }


    }
}
