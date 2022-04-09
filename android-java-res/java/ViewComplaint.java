package com.example.user.complaintanalyser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class ViewComplaint extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog progressDialog;
    String key, email, title, body, output, time;
    boolean isManager, isResolved, isManager_isNotResolved;
    TextView t1, t2, t3, t4;
    Button b1;

    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint);

        Intent intent = this.getIntent();
        isManager = intent.getExtras().getBoolean("isManager");
        key = intent.getExtras().getString("key");
        email = intent.getExtras().getString("email");
        title = intent.getExtras().getString("title");
        body = intent.getExtras().getString("body");
        output = intent.getExtras().getString("output");
        time = intent.getExtras().getString("time");
        isResolved = intent.getExtras().getBoolean("isResolved");

//        time = new Date(intent.getLongExtra("time", 0)); // Firebase cannot store dates

        t1 = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        b1 = (Button) findViewById(R.id.button);

        t1.setText(title);
        t2.setText("Complaint:\n\n"+body);
        t3.setText("Date: "+time);
        t4.setText("Redirected To:\n\n"+output+" Dept."+"\n\nStatus: "+ (isResolved ? "Resolved" : "Not Resolved"));

        //Html.fromHtml("<font color='#2BCC94'>Resolved</font>")


        isManager_isNotResolved = isManager && !isResolved;

        if(isManager_isNotResolved) {
            b1.setText(R.string.mark_as_resolved);
            b1.setBackground(getResources().getDrawable(R.drawable.button_view_complaint));
            b1.setTextColor(Color.parseColor("#ffffff"));
        }

        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == b1) {
            // Update Database
            if(isManager_isNotResolved) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                databaseReference = FirebaseDatabase.getInstance().getReference("complaint_details").child(key);
                user = FirebaseAuth.getInstance().getCurrentUser();
                isResolved = true; // set to true
                ComplaintDetails complaintDetails = new ComplaintDetails(key, email, title, body, time, output, isResolved);
                databaseReference.setValue(complaintDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewComplaint.this, "Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewComplaint.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_complaints_menu, menu);

        if(!isManager) {
            MenuItem delete = menu.findItem(R.id.delete);
            delete.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.delete) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if(isResolved) {
                builder.setMessage("Are you sure?");
            }
            else {
                builder.setMessage("The complaint has not been resolved yet. Do you still want to delete?");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("complaint_details").child(key);
                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ViewComplaint.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewComplaint.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
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

        return super.onOptionsItemSelected(item);
    }
}
