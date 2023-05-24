package com.example.phase5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnParent,btnChild;
    TextView moto,soto;
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnParent=findViewById(R.id.btnParent);
        btnChild=findViewById(R.id.btnChild);
        moto=findViewById(R.id.tvMoto);
        soto=findViewById(R.id.tvask);
        img=findViewById(R.id.imageView);


        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),ParentLogin.class);
                startActivity(next);
            }
        });

        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),ChildLogin.class);
                startActivity(next);
            }
        });


    }

}