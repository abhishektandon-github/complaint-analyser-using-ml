package com.example.user.complaintanalyser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends AppCompatActivity {

    TextView t1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t1 = (TextView) findViewById(R.id.textView2);
        b1 = (Button) findViewById(R.id.button);

        t1.setText("This app is a collaborative effort of our team of three members. It is a part of "+
                   "our BTech Final Year Project 'Complaint Analyser Using Machine Learning' in the Department of "+
                   "Computer Science and Engineering at Kamla Nehru Institute of Technology, Sultanpur "+
                   "and is completed under the guidance of Prof. (Dr.) Arvind Kumar Tiwari.\n"+
                   "\nTeam members:\nAbhishek Tandon (17208)\nAditya Singh Rathore (17209)\nRitik Singh (17243)");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
