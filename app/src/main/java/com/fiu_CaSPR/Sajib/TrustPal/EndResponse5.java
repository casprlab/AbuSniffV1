package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EndResponse5 extends Activity {

    public static ImageView propicView;
    public static String imageUrl;
    private TextView profileNameTextView;

    public static String responseString="";
    public static String code="";


    static int answer=-1;

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
        setContentView(R.layout.end_response5);
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


        for (int i = 0; i < checkboxes.size(); i++) {
            final CheckBox checkBox = checkboxes.get(i);

            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Boolean isChecked = checkBox.isChecked();

                    long end_time = System.nanoTime();
                    double difference = (end_time - start_time) / 1e6;

                    int index=-1;

                    //Getting the index of the current  checkbox
                    for (int j = 0; j < checkboxes.size(); j++) {
                        if(checkBox.getId() == checkboxes.get(j).getId()) index = j;
                    }

                    Log.d("sajib", "index " + index);

                    //Setting the other checkboxes false except the current checked one for question 1
                    if (index >= 0 && index <= 4) {
                        answer=index%5;
                        Log.d("sajib", "Q3:  " + answer);
                        //Toast.makeText(FriendSelectorView1.this, ""+ Q1, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i <= 4; i++) {
                            if (i == index) continue;
                            checkboxes.get(i).setChecked(false);
                            //currentFirndInfo.getCheckBoxValues().set(i, boxValue);
                        }
                        checkboxes.get(index).setChecked(true);

                        //checkboxes.get(index).setChecked(true);
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
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int clickcount=0;
                for (int j = 0; j < checkboxes.size(); j++) {
                    if(checkboxes.get(j).isChecked()) {
                        clickcount++;
                    }
                }
                if(clickcount<1) {
                    Toast.makeText(EndResponse5.this, "Please select an option before proceeding!", Toast.LENGTH_SHORT).show();
                }
                else {
                    EndResponse4.responseString+=answer+",";
                    Intent intent = new Intent(getApplicationContext(),EndResponse6.class);
                    startActivity(intent);
                    }
                }
        });

    }


    private void saveJsonStringToFile() {
        String createJsonString = createJsonString();
        try {
            FileOutputStream outputStream;
            //Create Folder
            File folder = new File(Environment.getExternalStorageDirectory().toString());
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();

            File file1 = new File(extStorageDirectory, FacebookRegexPatternPool.userName+"_"+cDateTime+".txt");
            outputStream = new FileOutputStream(file1);
            outputStream.write(createJsonString.getBytes());
            outputStream.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String createJsonString() {

        responseString+=answer+",";

        return responseString;
    }

    private void doLastJob() {

        saveJsonStringToFile();

        /************* File Upload Code ****************/
        upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload5.php";

        dialog = ProgressDialog.show(EndResponse5.this, "", "Uploading file...", true);

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
                            Toast.makeText(EndResponse5.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    dialog.dismiss();
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("Got Exception : see logcat ");
                            Toast.makeText(EndResponse5.this, "Got Exception : see logcat ",
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