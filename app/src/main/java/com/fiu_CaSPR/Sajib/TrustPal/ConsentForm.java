package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hathy.trustpal.R;

public class ConsentForm extends Activity {

    public static String country="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consent_form);
        Button agreebutton = (Button) findViewById(R.id.agree);
        Button disagreebutton = (Button) findViewById(R.id.disagree);

       /* final Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Algeria","Australia","Bangladesh","Bulgaria","Canada","Colombia","Egypt","Ghana","India","Indonesia","Italy","Kenya","Morocco","Nepal","Nigeria","Pakistan","Poland","Romania","Serbia","Switzerland","UK","USA","Venezuela","Vietnam","Other","<Please select your country>"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(25);
        //dropdown.setPrompt("Please select your country");*/

        agreebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
        });
        disagreebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
            }
        });

    }


}
