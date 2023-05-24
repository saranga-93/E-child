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
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar;

public class ParentLogin extends AppCompatActivity {
    Button signin,signup;
    TextView greet,ask;
    EditText inputmail,inputpass;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        greet=findViewById(R.id.textView2);
        ask=findViewById(R.id.tvCreate);
        inputmail=findViewById(R.id.etMail);
        inputpass=findViewById(R.id.etpass);
        signin=findViewById(R.id.btnlogin);
        signup=findViewById(R.id.btnSignup);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),Registrationform.class);
                startActivity(next);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,pass;
                mail=inputmail.getText().toString();
                pass=inputpass.getText().toString();

                if(mail.isEmpty() ||pass.isEmpty())
                {
                    Toast.makeText(ParentLogin.this, "All field must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ParentLogin.this, "Logging in", Toast.LENGTH_SHORT).show();
                            Intent next = new Intent(getApplicationContext(),ParentDashboard.class);
                            startActivity(next);
                            finish();
                        }else{
                            Toast.makeText(ParentLogin.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
//    protected void onStart() {
//        super.onStart();
//
//        if(mAuth.getCurrentUser()!=null)
//        {
//            startActivity(new Intent(ParentLogin.this,ParentDashboard.class));
//            finish();
//        }else{
//            Toast.makeText(this, "Login now", Toast.LENGTH_SHORT).show();
//        }
//    }
}