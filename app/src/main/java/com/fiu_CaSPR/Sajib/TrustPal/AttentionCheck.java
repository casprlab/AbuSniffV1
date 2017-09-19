package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.fiu_CaSPR.Sajib.Constants.FacebookRegexPatternPool;
import com.hathy.trustpal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttentionCheck extends Activity {

    public static ImageView propicView;
    public static String imageUrl;
    private TextView profileNameTextView;

    public static String responseString="";
    public static String code="";

    public static int index=-1;
    private static int answer=-1;

    private List<CheckBox> checkboxes;
    private long start_time;
    private long next_button_time;
    private long start_time_for_next_button;

    private LinearLayout layoutQuestion1;
    private LinearLayout layoutQuestion2;
    private LinearLayout layoutQuestion3;
    private LinearLayout layoutQuestion4;

    private ScrollView scrollView1;
    private TextView textView1;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        start_time = System.nanoTime();
        start_time_for_next_button = System.nanoTime();
        uploadFileName = FacebookRegexPatternPool.userName+"_"+cDateTime+".txt";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_check);
        checkboxes = new ArrayList<CheckBox>();
        loadViews();
    }

    private void loadViews() {

        textView1 = (TextView) findViewById(R.id.TextView01);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        //textView1.setText("Suppose you upload a picture of yourself like below on Facebook. How likely is it that "+friendsPage.friendsArray[currentPictureIndex][1]+" will post an inappropriate comment on this?");

        //Layouts for question 1 to 6
        layoutQuestion1 = (LinearLayout) findViewById(R.id.layoutquestion1);



        //Checkboxes for question1
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox11));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox12));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox13));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox14));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox15));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox16));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox17));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox18));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox19));
        checkboxes.add((CheckBox) findViewById(R.id.CheckBox10));


        for (int i = 0; i < checkboxes.size(); i++) {
            final CheckBox checkBox = checkboxes.get(i);

            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Boolean isChecked = checkBox.isChecked();

                    long end_time = System.nanoTime();
                    double difference = (end_time - start_time) / 1e6;

                    //Getting the index of the current  checkbox
                    for (int j = 0; j < checkboxes.size(); j++) {
                        if(checkBox.getId() == checkboxes.get(j).getId()) index = j;
                    }

                    Log.d("sajib", "index " + index);

                    start_time = System.nanoTime();

                    //Setting the other checkboxes false except the current checked one for question 1
                    if (index >= 0 && index <= 9) {
                        answer=index%10;
                        if (answer == 9) {
                            for (int i = 0; i <= 9; i++) {
                                if (i == 9) continue;
                                checkboxes.get(i).setChecked(false);
                                //currentFirndInfo.getCheckBoxValues().set(i, boxValue);
                            }
                        }
                        checkboxes.get(index).setChecked(true);

                        //checkboxes.get(index).setChecked(true);
                    }

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
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int clickcount = 0;
                for (int j = 0; j < checkboxes.size(); j++) {
                    if (checkboxes.get(j).isChecked()) {
                        clickcount++;
                    }
                }
                if (clickcount < 1) {
                    Toast.makeText(AttentionCheck.this, "Please select some option before proceeding!", Toast.LENGTH_SHORT).show();
                } else {
                    if(answer!=9) {
                        Intent intent = new Intent(getApplicationContext(), UnqualifiedUser.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), ConsentForm.class);
                        intent.putExtra("username", FacebookRegexPatternPool.userName);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    }