package com.example.user.complaintanalyser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class NewComplaint extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText t1, t2;
    Button b1;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser user;

    List<ComplaintDetails> list;
    DatabaseReference databaseReference;

    String URL = "http://complaintanalyser.pythonanywhere.com/post";

    String key, email, title, data, output, formated_time;
    Date time;

    SimpleDateFormat sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        b1 = (Button) findViewById(R.id.button);
        t1 = (EditText) findViewById(R.id.editText);
        t2 = (EditText) findViewById(R.id.editText2);

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("complaint_details");
        databaseReference.addValueEventListener(this);


        list = new ArrayList<>();

        sd = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        sd.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == b1) {
            title = t1.getText().toString();
            data = t2.getText().toString();
            if(data.equals("") || title.equals("")) {
                Toast.makeText(this, R.string.mandatory, Toast.LENGTH_SHORT).show();
            }
            else {
                jsonObjReq();
            }
        }
    }

    public void jsonObjReq() {
        // String URL = "http://192.168.43.133:5000/post";

        // Log.e("error", "jsonObjReq was called");

        progressDialog.setMessage("Analysing Complaint using Machine Learning...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                output = response.toString();
                updateDatabase();
                alertDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(NewComplaint.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("data", data);
                return paramV;
            }
        };

        requestQueue.add(stringRequest);;
    }

    public void alertDialog() {

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.success_layout, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("\nYour Complaint has been Analysed and Redirected to:\n\n"+output+" Department");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCustomTitle(view);
        builder.show();
    }
    private void updateDatabase() {
        key = databaseReference.push().getKey();
//        Log.e("Myerror", "updateDatabase() called");

        time = Calendar.getInstance().getTime();

        formated_time = sd.format(time);

        ComplaintDetails complaintDetails = new ComplaintDetails(key, email, title, data, formated_time, output, false);
        databaseReference.child(key).setValue(complaintDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(NewComplaint.this, "Data Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewComplaint.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        list.clear();
        for(DataSnapshot data: dataSnapshot.getChildren()) {

            ComplaintDetails complaintDetails = data.getValue(ComplaintDetails.class);
            if(user.getEmail().equals(complaintDetails.getEmail())) {
                list.add(complaintDetails);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


}
