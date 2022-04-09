package com.example.user.complaintanalyser;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1, b2;
    EditText t1, t2;
    ProgressDialog dialog;
    FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    public static final String LOGIN_ID = "login_id";
    public static final String USER_NAME = "user_name";
    public static final String PASS_WORD = "pass_word";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        t1 = (EditText) findViewById(R.id.editText);
        t2 = (EditText) findViewById(R.id.editText2);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        // firebase
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        sharedPreferences = this.getSharedPreferences(LOGIN_ID, MODE_PRIVATE);

        String user = sharedPreferences.getString(USER_NAME,"");  // second parameter is default value
        String pass = sharedPreferences.getString(PASS_WORD,"");
        if(!user.equals("") && !pass.equals("")) {
            startActivity(new Intent(this, Dashboard.class));
        }
    }

    @Override
    public void onClick(View v) {
        if(v == b1) {
            String user = t1.getText().toString();
            String pass = t2.getText().toString();
            if(isEmpty(user, pass)) {
                Toast.makeText(this, R.string.mandatory, Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.setMessage("Loading...");
            dialog.show();

            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        dialog.dismiss();
                        setSharedPreferences();
                        clearData();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login Failure: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(v == b2) {
            startActivity(new Intent(MainActivity.this, Register.class));
        }
    }
    private boolean isEmpty(String username, String password) {
        if (username.length() == 0 || password.length() == 0) {
            return true;
        }
        return false;
    }
    private void clearData() {
        t1.setText("");
        t2.setText("");
    }

    private void setSharedPreferences() {
        sharedPreferences = this.getSharedPreferences(LOGIN_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, t1.getText().toString());
        editor.putString(PASS_WORD, t2.getText().toString());
        editor.commit();
    }
}























































// GET METHOD

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    progressDialog.setMessage("Please wait...");
//                    progressDialog.show();
//
//                    data = response.getString("data");
//                    Log.e("MyError","Message: "+data);
//                    textView.setText(data);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//       }