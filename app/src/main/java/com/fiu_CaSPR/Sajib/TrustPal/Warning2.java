package com.fiu_CaSPR.Sajib.TrustPal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hathy.trustpal.R;

public class Warning2 extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning2);
        Button nextbutton = (Button) findViewById(R.id.next);
        nextbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), FriendSelectorView16.class);
                startActivity(intent);
            }
        });
    }

}