package com.example.phase5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class ChildLogin extends AppCompatActivity {
    Button btnSignin;
    TextView moto;
    EditText inputmail, inputpass;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);
        btnSignin=findViewById(R.id.btnChildlogin);
        moto=findViewById(R.id.tvChildMoto);
        inputmail=findViewById(R.id.etChildmail);
        inputpass=findViewById(R.id.etChildpass);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,pass;
                mail=inputmail.getText().toString();
                pass=inputpass.getText().toString();

                if(mail.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(ChildLogin.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ChildLogin.this, "Logging in", Toast.LENGTH_SHORT).show();
                            Intent next = new Intent(getApplicationContext(),ChildDashboard.class);
                            startActivity(next);
                        }else{
                            Toast.makeText(ChildLogin.this, "Authetication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(ChildLogin.this,ChildDashboard.class));
            finish();
        }else{
            Toast.makeText(this, "Login now", Toast.LENGTH_SHORT).show();
        }
    }
}