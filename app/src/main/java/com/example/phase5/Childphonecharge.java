package com.example.phase5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Childphonecharge extends AppCompatActivity {
    TextView tvMoto, tvOutput;
    DatabaseReference reff;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childphonecharge);
        tvMoto=findViewById(R.id.tvChildPhoneCHragedisplay);
        tvOutput=findViewById(R.id.tvChildPhoneChargeper);

        reff= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Charge");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Charge = snapshot.child("charge").getValue().toString();


               tvOutput.setText(Charge);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}