package com.example.phase5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrationform extends AppCompatActivity {
    Button Register;
    EditText inputname,inputmail,inputphone,inputpass,inputconf;
    TextView regform;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mroot;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationform);
        regform=findViewById(R.id.tvregform);
        inputname=findViewById(R.id.etRegname);
        inputmail=findViewById(R.id.etRegmail);
        inputphone=findViewById(R.id.etRegphone);
        inputpass=findViewById(R.id.etRegPass);
        inputconf=findViewById(R.id.etConf);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        Register=findViewById(R.id.btnRegistration);
        mroot=FirebaseDatabase.getInstance();
        reference=mroot.getReference("User");


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mail,phone,pass,conf;
                name=inputname.getText().toString();
                mail=inputmail.getText().toString();
                phone=inputphone.getText().toString();
                pass=inputpass.getText().toString();
                conf=inputconf.getText().toString();

                if(name.isEmpty()|| mail.isEmpty()|| phone.isEmpty()||pass.isEmpty()||conf.isEmpty())
                {
                    Toast.makeText(Registrationform.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(conf))
                {
                    Toast.makeText(Registrationform.this, "Passwords aren't matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Registrationform.this, "Registration succesfull", Toast.LENGTH_SHORT).show();

                            Reghelper helper = new Reghelper(name,mail,phone,pass);

                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helper);

                            finish();
                        }else{
                            Toast.makeText(Registrationform.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}