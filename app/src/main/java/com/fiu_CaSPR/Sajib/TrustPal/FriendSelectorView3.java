package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hathy.trustpal.R;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FriendSelectorView3 extends Activity {

    public static ImageView propicView;
    public static String imageUrl;
    private TextView profileNameTextView;

    public static int currentPictureIndex;
    public static String responseString="";
    private Random randomGenerator;
    private int totalCount;
    private List<Integer> randomNumbers;

    private static int Q1=-1;
    private static int Q2=-1;
    private static int Q3=-1;
    private static int Q4=-1;
    private static int Q5=-1;
    //private static int answer6=0;

    private List<CheckBox> checkboxes;
    private long start_time;
    private long next_button_time;
    private long start_time_for_next_button;
    private long Q1_time;
    private long Q2_time;
    private long Q3_time;
    private long Q4_time;
    private long Q5_time;


    private LinearLayout layoutQuestion1;
    private LinearLayout layoutQuestion2;
    private LinearLayout layoutQuestion3;
    private LinearLayout layoutQuestion4;
    private LinearLayout layoutQuestion5;
    //private LinearLayout layoutQuestion6;

    private ScrollView scrollView1;
    private TextView textView1;

    int userpageindex = 1;
    private TextView frndCounttextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        start_time = System.nanoTime();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);
        checkboxes = new ArrayList<CheckBox>();
        totalCount = 24;

        //totalCountForReview = getTotalCount();
        currentPictureIndex = 2;
        try {
            loadViews();
            loadNextFriendView();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    private void loadViews() {


        propicView = (ImageView) findViewById(R.id.propicimageview);
        profileNameTextView = (TextView) findViewById(R.id.usernamenew);
        frndCounttextView = (TextView) findViewById(R.id.frndcount);

        textView1 = (TextView) findViewById(R.id.TextView01);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        //textView1.setText("Suppose you upload a picture of yourself like below on Facebook. How likely is it that "+friendsPage.friendsArray[currentPictureIndex][1]+" will post an inappropriate comment on this?");

        //Layouts for question 1 to 6
        layoutQuestion1 = (LinearLayout) findViewById(R.id.layoutquestion1);
        layoutQuestion2 = (LinearLayout) findViewById(R.id.layoutquestion2);
        layoutQuestion3 = (LinearLayout) findViewById(R.id.layoutquestion3);
        layoutQuestion4 = (LinearLayout) findViewById(R.id.layoutquestion4);
        layoutQuestion5 = (LinearLayout) findViewById(R.id.layoutquestion5);
        //layoutQuestion6 = (LinearLayout) findViewById(R.id.layoutquestion6);



        //Checkboxes for question1
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox11));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox12));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox13));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox14));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox15));


        //Checkboxes for question2
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox21));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox22));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox23));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox24));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox25));


        //Checkboxes for question3
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox31));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox32));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox33));


        //Checkboxes for question4
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox41));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox42));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox43));


        //Checkboxes for question5
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox51));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox52));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox53));




        for (int i = 0; i < checkboxes.size(); i++) {
            final CheckBox checkBox = checkboxes.get(i);

            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Boolean isChecked = checkBox.isChecked();

                    long end_time = System.nanoTime();

                    int index=-1;

                    //Getting the index of the current  checkbox
                    for (int j = 0; j < checkboxes.size(); j++) {
                        if(checkBox.getId() == checkboxes.get(j).getId()) index = j;
                    }

                    Log.d("sajib", "index " + index);

                    //Setting the other checkboxes false except the current checked one for question 1
                    if (index >= 0 && index <= 4) {
                        Q1_time=end_time;
                        double difference = (Q1_time - start_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][27] = Double.toString(difference);
                        Q1=index%5;
                        Log.d("sajib", "Q1:  " + difference);
                        //Toast.makeText(FriendSelectorView1.this, ""+ Q1, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i <= 4; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                            //currentFirndInfo.getCheckBoxValues().set(i, boxValue);
                        }
                        checkboxes.get(index).setChecked(true);

                        //checkboxes.get(index).setChecked(true);
                    }

                    //Setting the other checkboxes false except the current checked one for question 2
                    if (index >= 5 && index <= 9) {
                        Q2_time=end_time;
                        double difference = (Q2_time - Q1_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][28] = Double.toString(difference);
                        Q2=index%5;
                        Log.d("sajib", "Q2:  " + difference);
                        //Toast.makeText(FriendSelectorView1.this, ""+ Q2, Toast.LENGTH_SHORT).show();
                        for (int i = 5; i <= 9; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                        }
                        checkboxes.get(index).setChecked(true);
                    }

                    //Setting the other checkboxes false except the current checked one for question 3
                    if (index >= 10 && index <= 12) {
                        Q3_time=end_time;
                        double difference = (Q3_time - Q2_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][29] = Double.toString(difference);
                        Q3=(index-1)%3;
                        Log.d("sajib", "Q3:  " + difference);
                        for (int i = 10; i <= 12; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                        }
                        checkboxes.get(index).setChecked(true);
                    }

                    //Setting the other checkboxes false except the current checked one for question 4
                    if (index >= 13 && index <= 15) {
                        Q4_time=end_time;
                        double difference = (Q4_time - Q3_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][30] = Double.toString(difference);
                        Q4=(index-1)%3;
                        Log.d("sajib", "Q4:  " + difference);
                        for (int i = 13; i <= 15; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                        }
                        checkboxes.get(index).setChecked(true);
                    }

                    //Setting the other checkboxes false except the current checked one for question 5
                    if (index >= 16 && index <= 18) {
                        Q5_time=end_time;
                        double difference = (Q5_time - Q4_time) /(double) 1000000000;
                        friendsPage.friendsArray[currentPictureIndex][31] = Double.toString(difference);
                        Q5=(index-1)%3;
                        Log.d("sajib", "Q5:  " + difference);
                        for (int i = 16; i <= 18; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                        }
                        checkboxes.get(index).setChecked(true);
                    }


                    //currentFirndInfo.getCheckBoxValues().set(index, boxValue);

                    start_time = System.nanoTime();

                }
            });



            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {

                }
            });
        }


        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button doneButton = (Button) findViewById(R.id.doneButton);
        Button backButton = (Button) findViewById(R.id.backButton);

        doneButton.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevious();
            }
        });


        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int clickcount=0;
                for (int j = 0; j < checkboxes.size(); j++) {
                    if(checkboxes.get(j).isChecked()) {
                        clickcount++;
                    }
                }
                if(clickcount<5) {
                    Toast.makeText(FriendSelectorView3.this, "Please answer all questions before proceeding!", Toast.LENGTH_SHORT).show();
                }
                else {
                    next_button_time = System.nanoTime();
                    double difference = (next_button_time - start_time) / 1e6;
                    friendsPage.friendsArray[currentPictureIndex][8]= Integer.toString(Q1);
                    friendsPage.friendsArray[currentPictureIndex][9]= Integer.toString(Q2);
                    friendsPage.friendsArray[currentPictureIndex][10]= Integer.toString(Q3);
                    friendsPage.friendsArray[currentPictureIndex][11]= Integer.toString(Q4);
                    friendsPage.friendsArray[currentPictureIndex][12]= Integer.toString(Q5);
                    friendsPage.friendsArray[currentPictureIndex][24]= Double.toString(difference);
                    setAction();
                    friendsPage.friendsArray[currentPictureIndex][7]= Integer.toString(currentPictureIndex);
                    FriendSelectorView1.responseString += Q1+","+Q2+","+Q3+","+Q4+","+Q5+","+ friendsPage.friendsArray[currentPictureIndex][2]+","+difference;
                    FriendSelectorView1.responseString += "\n";
                    start_time=next_button_time;
                    showNext();
                }
            }
        });

        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doLastJob();
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

    public void calculateResult(int Q1,int Q2,int Q3,int Q4,int Q5,int answer6) {

    }


    private String getDateTime() {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);
        return reportDate;
    }


    private void resetFields() {
        propicView.setBackgroundResource(R.drawable.icon);
        profileNameTextView.setText("");

        for (int i = 0; i < checkboxes.size(); i++) {
            CheckBox checkBox = checkboxes.get(i);
            checkBox.setChecked(false);
        }
    }
    public static boolean isNullOrBlank(String s)
    {
        return (s==null || s.trim().equals(""));
    }

    protected void showNext() {

        saveMutualData();
    }

    protected void saveMutualData() {

        Intent intent = new Intent(getApplicationContext(),FriendSelectorView4.class);
        intent.putExtra("currentPictureIndex", currentPictureIndex);
        startActivity(intent);
    }

    protected void showPrevious() {

    }

    private void doLastJob() {

    }

    private void loadNextFriendView() {
        /*if (this.isNullOrBlank(friendsPage.friendsArray[currentPictureIndex][1]))
        {
            currentPictureIndex++;
            showNext();
        }
        else {*/

        resetFields();
        scrollView1.smoothScrollTo(0, 0);
        frndCounttextView.setText(currentPictureIndex + 1 + " of 25");
        new DownloadImageTask(FriendSelectorView3.propicView).execute("https://3.bp.blogspot.com/-Qi_neL7i4JU/WSaDCQZy4JI/AAAAAAAABPw/QkUqIzgyiHcfPgQL6Ac8qf8WWYoQVOv7QCLcB/s1600/5f.jpg");
        profileNameTextView.setText("Monica Samaras");
        start_time = System.nanoTime();
        start_time_for_next_button = System.nanoTime();

        //}
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

    public void setAction() {

        if(Q1==3 && Q2==3 && Q3!=0 && Q4!=0 && Q5!=0) //Indicates we should Sandbox
        {
            friendsPage.friendsArray[currentPictureIndex][2]="4"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="0"; //0->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q3==0 && Q4==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="1"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q3==0 && Q4==0) //Indicates we should Unfollow
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="2"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q4==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="2"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q3==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="2"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q3==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="3"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q4==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="3"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="3"; //1->highest priority, 9->lowest priority
        }
        else if(Q1==3 && Q2==3) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="4"; //1->highest priority, 9->lowest priority
        }
        else if(Q3==0 && Q4==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="5"; //1->highest priority, 9->lowest priority
        }
        else if(Q3==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="6"; //1->highest priority, 9->lowest priority
        }
        else if(Q4==0 && Q5==0) //Indicates we should Unfriend
        {
            friendsPage.friendsArray[currentPictureIndex][2]="3"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="6"; //1->highest priority, 9->lowest priority
        }
        else if(Q1!=3 && Q2!=3 && Q3==0 && Q4==0 && Q5!=0) //Indicates we should Restrict
        {
            friendsPage.friendsArray[currentPictureIndex][2]="2"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="7"; //1->highest priority, 9->lowest priority
        }
        else if(Q1!=3 && Q2!=3 && Q3==0 && Q4!=0 && Q5!=0) //Indicates we should Restrict
        {
            friendsPage.friendsArray[currentPictureIndex][2]="2"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="8"; //1->highest priority, 9->lowest priority
        }
        else if(Q1!=3 && Q2!=3 && Q3!=0 && Q4==0 && Q5!=0) //Indicates we should Restrict
        {
            friendsPage.friendsArray[currentPictureIndex][2]="2"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="8"; //1->highest priority, 9->lowest priority
        }
        else if(Q1!=3 && Q2!=3 && Q3!=0 && Q4!=0 && Q5==0) //Indicates we should Unfollow
        {
            friendsPage.friendsArray[currentPictureIndex][2]="1"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="9"; //1->highest priority, 9->lowest priority
        }
        else if(Q1!=3 && Q2!=3 && Q3!=0 && Q4!=0 && Q5!=0) //Indicates this friend is safe
        {
            friendsPage.friendsArray[currentPictureIndex][2]="0"; //0->Safe, 1->Unfollow, 2->Restricted, 3->Unfriend
            friendsPage.friendsArray[currentPictureIndex][4]="10"; //1->highest priority, 9->lowest priority
        }
    }


}