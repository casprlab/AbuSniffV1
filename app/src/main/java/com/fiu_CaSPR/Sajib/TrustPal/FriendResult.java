package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import org.apache.commons.lang.RandomStringUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FriendResult extends Activity {

    public static ImageView propicView;
    public static String imageUrl;
    public static String code;
    private TextView profileNameTextView;

    public static int currentPictureIndex;
    public static String responseString="";

    private int totalCountForReview = 25;
    private Random randomGenerator;
    private int totalCount;
    private List<Integer> randomNumbers;

    private List<CheckBox> checkboxes;
    private long start_time;
    private long next_button_time;
    private long start_time_for_next_button;

    public static String unfriendReason="";
    public static String sandboxReason="";
    public static String restrictReason="";
    public static String unfollowReason="";

    private List<RadioButton> radioButtons;

    public RadioButton radio_u0;
    public RadioButton radio_u1;
    public RadioButton radio_u2;
    public RadioButton radio_u3;
    public RadioButton radio_u4;

    public RadioButton radio_s0;
    public RadioButton radio_s1;
    public RadioButton radio_s2;
    public RadioButton radio_s3;
    public RadioButton radio_s4;

    public RadioButton radio_r0;
    public RadioButton radio_r1;
    public RadioButton radio_r2;
    public RadioButton radio_r3;
    public RadioButton radio_r4;

    public RadioButton radio_f0;
    public RadioButton radio_f1;
    public RadioButton radio_f2;
    public RadioButton radio_f3;
    public RadioButton radio_f4;


    private LinearLayout reference;
    private LinearLayout unsafeDiv;
    private LinearLayout sandboxDiv;
    private LinearLayout unfriendDiv;
    private LinearLayout restrictDiv;
    private LinearLayout unfollowDiv;


    private TextView safeText;
    private TextView actionText;
    private TextView actionText12;
    private TextView actionText3;
    private TextView actionText4;
    private TextView actionText5;
    private TextView sandboxText;
    private TextView unfriendText;
    private TextView restrictText;
    private TextView unfollowText;
    //private ImageView safeIcon;
    private Button actionButton;
    private Button ignoreButton;
    private Button sandboxButton;
    private TextView txtProgress;
    private ProgressBar circularProgressBar;
    int userpageindex = 1;
    private TextView frndCounttextView;

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

    private String filepath = "MyFileStorage";
    static File myInternalFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_result);
        start_time = System.nanoTime();
        checkboxes = new ArrayList<CheckBox>();
        totalCount = 24;
        uploadFileName = FacebookRegexPatternPool.id+"_"+cDateTime+".txt";
        safeText = (TextView) findViewById(R.id.safeText);
        actionText = (TextView) findViewById(R.id.actionText);
        actionText12 = (TextView) findViewById(R.id.actionText12);
        actionText3 = (TextView) findViewById(R.id.actionText3);
        actionText4 = (TextView) findViewById(R.id.actionText4);
        actionText5 = (TextView) findViewById(R.id.actionText5);
        sandboxText = (TextView) findViewById(R.id.sandboxText);
        unfriendText = (TextView) findViewById(R.id.unfriendText);
        restrictText = (TextView) findViewById(R.id.restrictText);
        unfollowText = (TextView) findViewById(R.id.unfollowText);
        unsafeDiv = (LinearLayout) findViewById(R.id.unsafeDiv);

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        circularProgressBar = (ProgressBar) findViewById(R.id.circularProgressbar);

        int percentValue=0;
        percentValue=(int) Math.round((25 - ResultSummary.unsafeCount) * 4);
        txtProgress.setText(percentValue+"%");
        circularProgressBar.setProgress(percentValue);
        //actionButton = (Button) findViewById(R.id.actionButton);

        //totalCountForReview = getTotalCount();
        currentPictureIndex = 0;
        loadViews();

        if (totalCount > 0) {
            showNext();
        }

    }

    private void loadViews() {


        propicView = (ImageView) findViewById(R.id.propicimageview);
        profileNameTextView = (TextView) findViewById(R.id.usernamenew);
        frndCounttextView = (TextView) findViewById(R.id.frndcount);
        actionButton = (Button) findViewById(R.id.action);
        ignoreButton = (Button) findViewById(R.id.ignore);
        sandboxButton = (Button) findViewById(R.id.sandbox);

        ignoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }

            /***************************
             * Code for showing and handling popup
             **************************/
            private void showPopup() {

                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.action_reason, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);


                //unfriendReasons = (LinearLayout) popupView.findViewById(R.id.unfriendReasons);
                reference = (LinearLayout) findViewById(R.id.nameDiv);

                sandboxDiv = (LinearLayout) popupView.findViewById(R.id.sandboxLayer);
                unfriendDiv = (LinearLayout) popupView.findViewById(R.id.unfriendLayer);
                restrictDiv = (LinearLayout) popupView.findViewById(R.id.restrictLayer);
                unfollowDiv = (LinearLayout) popupView.findViewById(R.id.unfollowLayer);

                //neitherReasons for sandboxDiv
                radio_s0 = (RadioButton) popupView.findViewById(R.id.s0);
                radio_s1 = (RadioButton) popupView.findViewById(R.id.s1);
                radio_s2 = (RadioButton) popupView.findViewById(R.id.s2);
                radio_s3 = (RadioButton) popupView.findViewById(R.id.s3);
                radio_s4 = (RadioButton) popupView.findViewById(R.id.s4);

                //unfriendReasons for unfriendDiv
                radio_u0 = (RadioButton) popupView.findViewById(R.id.u0);
                radio_u1 = (RadioButton) popupView.findViewById(R.id.u1);
                radio_u2 = (RadioButton) popupView.findViewById(R.id.u2);
                radio_u3 = (RadioButton) popupView.findViewById(R.id.u3);
                radio_u4 = (RadioButton) popupView.findViewById(R.id.u4);

                //restrainReasons for restrainDiv
                radio_r0 = (RadioButton) popupView.findViewById(R.id.r0);
                radio_r1 = (RadioButton) popupView.findViewById(R.id.r1);
                radio_r2 = (RadioButton) popupView.findViewById(R.id.r2);
                radio_r3 = (RadioButton) popupView.findViewById(R.id.r3);
                radio_r4 = (RadioButton) popupView.findViewById(R.id.r4);

                //unfollowReasons for unfollowDiv
                radio_f0 = (RadioButton) popupView.findViewById(R.id.f0);
                radio_f1 = (RadioButton) popupView.findViewById(R.id.f1);
                radio_f2 = (RadioButton) popupView.findViewById(R.id.f2);
                radio_f3 = (RadioButton) popupView.findViewById(R.id.f3);
                radio_f4 = (RadioButton) popupView.findViewById(R.id.f4);

                //RadioButtons
                radioButtons = new ArrayList<RadioButton>();

                radioButtons.add(radio_s0);
                radioButtons.add(radio_s1);
                radioButtons.add(radio_s2);
                radioButtons.add(radio_s3);
                radioButtons.add(radio_s4);

                radioButtons.add(radio_u0);
                radioButtons.add(radio_u1);
                radioButtons.add(radio_u2);
                radioButtons.add(radio_u3);
                radioButtons.add(radio_u4);

                radioButtons.add(radio_r0);
                radioButtons.add(radio_r1);
                radioButtons.add(radio_r2);
                radioButtons.add(radio_r3);
                radioButtons.add(radio_r4);

                radioButtons.add(radio_f0);
                radioButtons.add(radio_f1);
                radioButtons.add(radio_f2);
                radioButtons.add(radio_f3);
                radioButtons.add(radio_f4);


                if (friendsPage.friendsArray[currentPictureIndex][2] == "4") {
                    sandboxDiv.setVisibility(View.VISIBLE);
                    unfriendDiv.setVisibility(View.INVISIBLE);
                    restrictDiv.setVisibility(View.INVISIBLE);
                    unfollowDiv.setVisibility(View.INVISIBLE);
                } else if (friendsPage.friendsArray[currentPictureIndex][2] == "3") {
                    sandboxDiv.setVisibility(View.INVISIBLE);
                    unfriendDiv.setVisibility(View.VISIBLE);
                    restrictDiv.setVisibility(View.INVISIBLE);
                    unfollowDiv.setVisibility(View.INVISIBLE);
                } else if (friendsPage.friendsArray[currentPictureIndex][2] == "2") {
                    sandboxDiv.setVisibility(View.INVISIBLE);
                    unfriendDiv.setVisibility(View.INVISIBLE);
                    restrictDiv.setVisibility(View.VISIBLE);
                    unfollowDiv.setVisibility(View.INVISIBLE);
                } else if (friendsPage.friendsArray[currentPictureIndex][2] == "1") {
                    sandboxDiv.setVisibility(View.INVISIBLE);
                    unfriendDiv.setVisibility(View.INVISIBLE);
                    restrictDiv.setVisibility(View.INVISIBLE);
                    unfollowDiv.setVisibility(View.VISIBLE);
                }


                for (int i = 0; i < radioButtons.size(); i++) {
                    final RadioButton radioButton = radioButtons.get(i);

                    radioButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            Boolean isChecked = radioButton.isChecked();

                            int index = -1;

                            //Getting the index of the current  radioButton
                            for (int j = 0; j < radioButtons.size(); j++) {
                                if (radioButton.getId() == radioButtons.get(j).getId()) index = j;
                            }

                            //Setting the other radioButtons false except the current checked one for sandboxReason
                            for (int i = 0; i <= 4; i++) {
                                if (i == index) {
                                    sandboxReason = "s" + index % 5;
                                    Log.d("sajib", "sandboxReason:" + sandboxReason);
                                    continue;
                                }
                                ;
                                radioButtons.get(i).setChecked(false);
                            }
                            radioButtons.get(index).setChecked(true);

                            //Setting the other radioButtons false except the current checked one for neitherReason
                            for (int i = 5; i <= 9; i++) {
                                if (i == index) {
                                    unfriendReason = "u" + index % 5;
                                    Log.d("sajib", "unfriendReason:" + unfriendReason);
                                    continue;
                                }
                                ;
                                radioButtons.get(i).setChecked(false);
                            }
                            radioButtons.get(index).setChecked(true);

                            //Setting the other radioButtons false except the current checked one for restrictReason
                            for (int i = 10; i <= 14; i++) {
                                if (i == index) {
                                    restrictReason = "r" + index % 5;
                                    Log.d("sajib", "restrictReason:" + restrictReason);
                                    continue;
                                }
                                ;
                                radioButtons.get(i).setChecked(false);
                            }
                            radioButtons.get(index).setChecked(true);

                            //Setting the other radioButtons false except the current checked one for unfollowReason
                            for (int i = 15; i <= 19; i++) {
                                if (i == index) {
                                    unfollowReason = "f" + index % 5;
                                    Log.d("sajib", "unfollowReason:" + unfollowReason);
                                    continue;
                                }
                                ;
                                radioButtons.get(i).setChecked(false);
                            }
                            radioButtons.get(index).setChecked(true);

                        }
                    });


                }


                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        next_button_time = System.nanoTime();
                        double difference = (next_button_time - start_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][5] = Double.toString(difference); //Setting the value whether the user took the action
                        Log.d("sajib", "Ignore:  " + difference);
                        start_time = next_button_time;

                        int clickcount = 0;
                        for (int j = 0; j < radioButtons.size(); j++) {
                            if (radioButtons.get(j).isChecked()) {
                                clickcount++;
                                //Toast.makeText(FriendResult.this, "Click Count:"+clickcount, Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (friendsPage.friendsArray[currentPictureIndex][2] == "4") //User selects Sandbox
                        {
                            if (clickcount < 1) {
                                Toast.makeText(FriendResult.this, "Please select any reason!", Toast.LENGTH_SHORT).show();
                            } else {
                                friendsPage.friendsArray[currentPictureIndex][3] = "Not Taken"; //Setting the value as the user takes Neither
                                friendsPage.friendsArray[currentPictureIndex][6] = sandboxReason; //Setting the value as the user took the action
                                Log.d("sajib", "Not Taken. reason: " + sandboxReason);

                                popupWindow.dismiss();
                                currentPictureIndex++;
                                showNext();
                            }

                        } else if (friendsPage.friendsArray[currentPictureIndex][2] == "3") //User selects Unfriend
                        {
                            if (clickcount < 1) {
                                Toast.makeText(FriendResult.this, "Please select any reason!", Toast.LENGTH_SHORT).show();
                            } else {
                                friendsPage.friendsArray[currentPictureIndex][3] = "Not Taken"; //Setting the value as the user takes Neither
                                friendsPage.friendsArray[currentPictureIndex][6] = unfriendReason; //Setting the value as the user took the action
                                Log.d("sajib", "Not Taken. reason: " + unfriendReason);

                                popupWindow.dismiss();
                                currentPictureIndex++;
                                showNext();
                            }

                        } else if (friendsPage.friendsArray[currentPictureIndex][2] == "2") //User selects Restrict
                        {
                            if (clickcount < 1) {
                                Toast.makeText(FriendResult.this, "Please select any reason!", Toast.LENGTH_SHORT).show();
                            } else {
                                friendsPage.friendsArray[currentPictureIndex][3] = "Not Taken"; //Setting the value as the user takes Neither
                                friendsPage.friendsArray[currentPictureIndex][6] = restrictReason; //Setting the value as the user took the action
                                Log.d("sajib", "Not Taken. reason: " + restrictReason);

                                popupWindow.dismiss();
                                currentPictureIndex++;
                                showNext();
                            }
                        } else if (friendsPage.friendsArray[currentPictureIndex][2] == "1") //User selects unfollow
                        {
                            if (clickcount < 1) {
                                Toast.makeText(FriendResult.this, "Please select any reason!", Toast.LENGTH_SHORT).show();
                            } else {
                                friendsPage.friendsArray[currentPictureIndex][3] = "Not Taken"; //Setting the value as the user takes Unfollow
                                friendsPage.friendsArray[currentPictureIndex][6] = unfollowReason; //Setting the value as the user took the action
                                Log.d("sajib", "Not Taken. reason: " + unfollowReason);

                                popupWindow.dismiss();
                                currentPictureIndex++;
                                showNext();
                            }
                        }

                    }
                });

                popupWindow.showAsDropDown(reference);

            }
        });


        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next_button_time = System.nanoTime();
                double difference = (next_button_time - start_time) / (double) 1000000000;
                friendsPage.friendsArray[currentPictureIndex][5] = Double.toString(difference); //Setting the value whether the user took the action
                Log.d("sajib", "Action:  " + difference);
                friendsPage.friendsArray[currentPictureIndex][3] = "Taken"; //Setting the value as the user took the action
                Log.d("sajib", "Taken");
                ResultSummary.unsafeCount--;
                currentPictureIndex++;
                start_time = next_button_time;
                showNext();
            }

        });

        sandboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next_button_time = System.nanoTime();
                double difference = (next_button_time - start_time) /(double) 1000000000;
                friendsPage.friendsArray[currentPictureIndex][5] = Double.toString(difference); //Setting the value whether the user took the action
                Log.d("sajib", "Sandbox:  " + difference);
                friendsPage.friendsArray[currentPictureIndex][3] = "Taken S"; //Setting the value as the user took the action
                Log.d("sajib", "TakenSandbox");
                ResultSummary.unsafeCount--;
                currentPictureIndex++;
                start_time = next_button_time;
                showNext();
            }

        });

    }


    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }


    private void resetFields() {
        propicView.setBackgroundResource(R.drawable.icon);
        profileNameTextView.setText("");
        int percentValue=0;
        percentValue=(int) Math.round((25 - ResultSummary.unsafeCount) * 4);
        txtProgress.setText(percentValue + "%");
        circularProgressBar.setProgress(percentValue);
    }

    public static boolean isNullOrBlank(String s)
    {
        return (s==null || s.trim().equals(""));
    }

    private void loadNextFriendView() {
        /*if (this.isNullOrBlank(friendsPage.friendsArray[currentPictureIndex][1]))
        {
            currentPictureIndex++;
            showNext();
        }
        else {*/
        Log.d("sajib", "currentPictureIndex:" + currentPictureIndex);
        resetFields();

        if(friendsPage.friendsArray[currentPictureIndex][2]=="1") { //Unfollow
            //safeText.setText("Our result indicates that this friend might be unsafe");
            safeText.setTextColor(Color.parseColor("#b50f15"));
            frndCounttextView.setTextColor(Color.parseColor("#b50f15"));
            unsafeDiv.setBackgroundColor(Color.YELLOW);
            actionText12.setVisibility(View.INVISIBLE);
            actionText3.setVisibility(View.INVISIBLE);
            actionText4.setVisibility(View.INVISIBLE);
            actionText5.setVisibility(View.VISIBLE);
            sandboxText.setVisibility(View.INVISIBLE);
            unfriendText.setVisibility(View.INVISIBLE);
            restrictText.setVisibility(View.INVISIBLE);
            unfollowText.setVisibility(View.VISIBLE);
            actionButton.setText("Unfollow");
            actionButton.setEnabled(true);
            sandboxButton.setVisibility(View.INVISIBLE);

        }
        else if(friendsPage.friendsArray[currentPictureIndex][2]=="2") { //Restrict
            //safeText.setText("Our result indicates that this friend is unsafe");
            safeText.setTextColor(Color.WHITE);
            unsafeDiv.setBackgroundColor(Color.parseColor("#FFF99A1D"));
            actionText12.setVisibility(View.INVISIBLE);
            actionText3.setVisibility(View.VISIBLE);
            actionText4.setVisibility(View.VISIBLE);
            actionText5.setVisibility(View.INVISIBLE);
            sandboxText.setVisibility(View.INVISIBLE);
            unfriendText.setVisibility(View.INVISIBLE);
            restrictText.setVisibility(View.VISIBLE);
            unfollowText.setVisibility(View.INVISIBLE);
            actionButton.setText("Restrict");
            actionButton.setEnabled(true);
            sandboxButton.setVisibility(View.INVISIBLE);
        }
        else if(friendsPage.friendsArray[currentPictureIndex][2]=="3") { //Unfriend
            //safeText.setText("Our result indicates that this friend is very unsafe");
            safeText.setTextColor(Color.WHITE);
            unsafeDiv.setBackgroundColor(Color.RED);

            actionText12.setVisibility(View.VISIBLE);
            actionText3.setVisibility(View.VISIBLE);
            actionText4.setVisibility(View.VISIBLE);
            actionText5.setVisibility(View.VISIBLE);

            //Show the reasoning as a function of the answers to the questions
            if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][10]=="0" && friendsPage.friendsArray[currentPictureIndex][11]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][10]=="0" && friendsPage.friendsArray[currentPictureIndex][11]=="0") //Indicates we should Unfollow
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.INVISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][11]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.INVISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][10]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.INVISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][10]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.INVISIBLE);
                actionText5.setVisibility(View.INVISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][11]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.INVISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.INVISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][8]=="3" && friendsPage.friendsArray[currentPictureIndex][9]=="3" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.VISIBLE);
                actionText3.setVisibility(View.INVISIBLE);
                actionText4.setVisibility(View.INVISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][10]=="0" && friendsPage.friendsArray[currentPictureIndex][11]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.INVISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][10]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.INVISIBLE);
                actionText3.setVisibility(View.VISIBLE);
                actionText4.setVisibility(View.INVISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            else if(friendsPage.friendsArray[currentPictureIndex][11]=="0" && friendsPage.friendsArray[currentPictureIndex][12]=="0") //Indicates we should Unfriend
            {
                actionText12.setVisibility(View.INVISIBLE);
                actionText3.setVisibility(View.INVISIBLE);
                actionText4.setVisibility(View.VISIBLE);
                actionText5.setVisibility(View.VISIBLE);
            }
            sandboxText.setVisibility(View.INVISIBLE);
            unfriendText.setVisibility(View.VISIBLE);
            restrictText.setVisibility(View.INVISIBLE);
            unfollowText.setVisibility(View.INVISIBLE);
            actionButton.setText("Unfriend");
            actionButton.setEnabled(true);
            sandboxButton.setVisibility(View.INVISIBLE);
        }
        else if(friendsPage.friendsArray[currentPictureIndex][2]=="4") { //Sandbox
            //safeText.setText("Our result indicates that this friend is unsafe");
            safeText.setTextColor(Color.WHITE);
            unsafeDiv.setBackgroundColor(Color.parseColor("#FFF99A1D"));
            actionText12.setVisibility(View.VISIBLE);
            actionText3.setVisibility(View.INVISIBLE);
            actionText4.setVisibility(View.INVISIBLE);
            actionText5.setVisibility(View.INVISIBLE);
            sandboxText.setVisibility(View.VISIBLE);
            unfriendText.setVisibility(View.INVISIBLE);
            restrictText.setVisibility(View.INVISIBLE);
            unfollowText.setVisibility(View.INVISIBLE);
            actionButton.setText("Unfriend");
            actionButton.setEnabled(true);
            sandboxButton.setVisibility(View.VISIBLE);
            sandboxButton.setEnabled(true);
        }

        frndCounttextView.setText(currentPictureIndex + 1 + " of " + totalCountForReview);
        try {
            if(currentPictureIndex==2)
            {
                new DownloadImageTask(FriendResult.propicView).execute("https://3.bp.blogspot.com/-Qi_neL7i4JU/WSaDCQZy4JI/AAAAAAAABPw/QkUqIzgyiHcfPgQL6Ac8qf8WWYoQVOv7QCLcB/s1600/5f.jpg");
            }
            else if(currentPictureIndex==16)
            {
                new DownloadImageTask(FriendResult.propicView).execute("https://4.bp.blogspot.com/-Nm2x9hwR0gk/WSaC_VRd4SI/AAAAAAAABPM/tzbwO_K3ms092Odfxvf2wvj3mWNBXiAWQCLcB/s1600/3.jpg");
            }
            else if(currentPictureIndex==24)
            {
                new DownloadImageTask(FriendResult.propicView).execute("https://4.bp.blogspot.com/-Gme6AwEE-PA/WSaC_rQgHwI/AAAAAAAABPU/8_8h9e4zUSEgmZMuLqq0VsZ38a8Trb5BACLcB/s1600/5.jpg");
            }
            else
            {
                new DownloadImageTask(FriendResult.propicView).execute(friendsPage.friendsArray[currentPictureIndex][25]);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        if(friendsPage.friendsArray[currentPictureIndex][1]=="Find Friends") {
            profileNameTextView.setText("Deactivated Account");
        }

        else {
            if(currentPictureIndex==2)
            {
                profileNameTextView.setText("Monica Samaras");
            }
            else if(currentPictureIndex==16)
            {
                profileNameTextView.setText("Ronny Yanev");
            }
            else if(currentPictureIndex==24)
            {
                profileNameTextView.setText("Thomas Fischer");
            }
            else
            {
                profileNameTextView.setText(friendsPage.friendsArray[currentPictureIndex][1]);
            }

        }

        //}
    }

    private void sortFriendlist() {

        //sort the friendsArray based on priority using bubble sort
        for(int i=0;i<25;i++){
            for(int j=i+1;j<25;j++) {
                //only checking the 5th column
                if (Integer.parseInt(friendsPage.friendsArray[i][7]) > Integer.parseInt(friendsPage.friendsArray[j][7])) {
                    //swap(friendsPage.friendsArray[i],friendsPage.friendsArray[j]);
                    String temp[] = friendsPage.friendsArray[j];
                    friendsPage.friendsArray[j] = friendsPage.friendsArray[i];
                    friendsPage.friendsArray[i] = temp;
                }
            }
        }
    }

    public String unicode2String(String unicode) {
        byte[] utf8Bytes       = null;
        String convertedString = null;
        try
        {
            utf8Bytes       = unicode.getBytes("UTF8");
            convertedString = new String(utf8Bytes,  "UTF8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return convertedString;
    }

    protected void showNext() {

        try {
            if (currentPictureIndex == totalCountForReview) {
                Intent intent = new Intent(getApplicationContext(),ResponseSummary.class);
                startActivity(intent);
                return;
            }
            if (friendsPage.friendsArray[currentPictureIndex][2] == "0") { //Safe
                currentPictureIndex++;
                showNext();
            } else {
                loadNextFriendView();
            }
        }
        catch (Exception e)
        {
        System.out.println(e);
        }
    }

    private void saveJsonStringToFile() {
        String createJsonString = createJsonString();

        try {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
            myInternalFile = new File(directory , FacebookRegexPatternPool.id+"_"+cDateTime+".txt");
            FileOutputStream fos = new FileOutputStream(myInternalFile);
            fos.write(createJsonString.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String createJsonString() {
        /*try {
            sortFriendlist();
        }catch (Exception e) {

        }*/
        String newString = "";
        for (int i = 0; i<25; i++)
        { //newString+=i+1;
            newString+=friendsPage.friendsArray[i][8];
            for(int j = 9; j<13; j++)
            {
                newString+=";"+friendsPage.friendsArray[i][j];
            }
            newString+=";"+friendsPage.friendsArray[i][24];
            newString+="\n";
        }
        newString+="\n\n\n";
        //newString+= Arrays.deepToString(friendsPage.friendsArray);
        // now let's print a two dimensional array in Java
        for (int i = 0; i<25; i++)
        { //newString+=i+1;
            newString+=friendsPage.friendsArray[i][0];
            for(int j = 1; j<7; j++)
            {
                newString+=";"+friendsPage.friendsArray[i][j];
            }
            newString+=";"+friendsPage.friendsArray[i][13];
            for(int j = 14; j<24; j++)
            {
                newString+=";"+friendsPage.friendsArray[i][j];
            }
            newString+="\n";
        }
        code = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        newString+="\n\nV3 Payment Code: "+code+";  ID:"+FacebookRegexPatternPool.id;
        newString+="\n\nName:"+FacebookRegexPatternPool.user_full_name+";  #Friends:"+friendsPage.number_of_friends+"; Time:"+getDateTime()+"; Birthday:"+infoPage1.birthday+"; Gender:"+AfterLogin.gender+"; Location:"+infoPage1.location;
        //newString+="\n\nMutual Data:\n"+ "commonPostCount: "+ commonPostCount + "\n" + "commonPhotoCount: "+ commonPhotoCount + "\n" + "mutualFriends: "+ mutualFriends + "\n" + "currentCity: "+ currentCity + "\n" + "commonHometown: "+ commonHometown + "\n" + "currentStudy: "+ currentStudy + "\n" + "pastStudy: "+ pastStudy + "\n" + "commonEducation: "+ commonEducation + "\n" + "currentWork: "+ currentWork+ "\n" + "pastWork: "+ pastWork+ "\n" + "commonWork: "+ commonWork;
        newString+="\n\n\n";
        //newString+=friendsPage.sourceHtml;
        return newString;
    }

    private String getDateTime() {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);
        return reportDate;
    }

    void doLastJob() {

        saveJsonStringToFile();

        /************* File Upload Code ****************/
        upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload.php";

        dialog = ProgressDialog.show(FriendResult.this, "", "", true);

        try {
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
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        /************* File Upload Code Ends ****************/

        Intent intent = new Intent(getApplicationContext(),ResponseSummary.class);
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
        //``        11File sourceFile = new File(sourceFileUri);

        if (!myInternalFile.isFile()) {

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
                FileInputStream fileInputStream = new FileInputStream(myInternalFile);
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
                        Toast.makeText(FriendResult.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(FriendResult.this, "Got Exception : see logcat ",
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