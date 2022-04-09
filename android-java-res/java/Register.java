package com.example.user.complaintanalyser;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    EditText t1, t2, t3;

    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialise button, edittext

        b1 = (Button) findViewById(R.id.button);
        t1 = (EditText) findViewById(R.id.editText);
        t2 = (EditText) findViewById(R.id.editText2);
        t3 = (EditText) findViewById(R.id.editText3);

//        // Change Action Bar color
//        ActionBar actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#183693"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        // inititalise firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        b1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == b1) {
            if(v == b1) {
                String user = t1.getText().toString();
                String pass = t2.getText().toString();
                String pass2 = t3.getText().toString();
                if(isEmpty(user, pass, pass2)) {
                    Toast.makeText(this, R.string.mandatory, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (pass.equals(pass2)) {
                        dialog.setMessage("Registering...");
                        dialog.show();

                        firebaseAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(Register.this, "Registeration Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this,MainActivity.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(Register.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.do_not_match, Toast.LENGTH_SHORT).show();
                        t2.setText("");
                        t3.setText("");
                        t2.requestFocus();
                    }
                }
            }
        }
    }
    private void clearData() {
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t1.requestFocus();
    }

    private boolean isEmpty(String username, String password, String password2) {
        if (username.length() == 0 || password.length() == 0 || password2.length() == 0) {
            return true;
        }
        return false;
    }
}