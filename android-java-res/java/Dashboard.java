package com.example.user.complaintanalyser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemClickListener {

    FirebaseAuth auth;
    FirebaseUser user;


    DatabaseReference databaseReference;

    ListView listView;
    List<ComplaintDetails> list;

    TextView t1;
    String email;

    boolean isManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NewComplaint.class);
                startActivity(intent);

            }
        });

        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        t1 = (TextView) findViewById(R.id.textView);
        t1.setText("Welcome, "+user.getEmail()+"!");

        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();

        listView.setOnItemClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("complaint_details");
        databaseReference.addValueEventListener(this);

        isManager = email.contains("manager");

        if(isManager) {

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Manager Dashboard");

            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fab.setLayoutParams(p);
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        list.clear();

        if(isManager) {
            for(DataSnapshot data: dataSnapshot.getChildren()) {
                ComplaintDetails complaintDetails = data.getValue(ComplaintDetails.class);
                if(email.contains("savingsaccount")) {
                    if(complaintDetails.getOutput().equals("Savings Account")) {
                        list.add(complaintDetails);
                    }
                }
                else if(email.contains("creditcard")) {
                    if(complaintDetails.getOutput().equals("Credit Card")) {
                        list.add(complaintDetails);
                    }
                }
                else if(email.contains("creditreporting")) {
                    if(complaintDetails.getOutput().equals("Credit Reporting")) {
                        list.add(complaintDetails);
                    }
                }
                else if(email.contains("mortgage")) {
                    if(complaintDetails.getOutput().equals("Mortgage")) {
                        list.add(complaintDetails);
                    }
                }
                else if(email.contains("studentloan")) {
                    if(complaintDetails.getOutput().equals("Student Loan")) {
                        list.add(complaintDetails);
                    }
                }

            }
        }
        else {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                ComplaintDetails complaintDetails = data.getValue(ComplaintDetails.class);
                if (email.equals(complaintDetails.getEmail()))
                    list.add(complaintDetails);
            }
        }



        ListViewAdapter listViewclass = new ListViewAdapter(this,list);
        listView.setAdapter(listViewclass);

        if(!list.isEmpty()) {
            findViewById(R.id.imagePanel).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.imagePanel).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ComplaintDetails complaintDetails = list.get(position);
        Intent intent = new Intent(this, ViewComplaint.class);
        intent.putExtra("key", complaintDetails.getKey());
        intent.putExtra("email", complaintDetails.getEmail());
        intent.putExtra("title", complaintDetails.getTitle());
        intent.putExtra("body", complaintDetails.getBody());
        intent.putExtra("output", complaintDetails.getOutput());
        intent.putExtra("time", complaintDetails.getTime());
        intent.putExtra("isResolved", complaintDetails.getIsResolved());
        intent.putExtra("isManager", isManager);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sign_out) {
            logout();
        }
        if(id == R.id.about) {
            startActivity(new Intent(this, About.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void logout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSharedPreferences();
                auth.signOut();

                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void clearSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("login_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
