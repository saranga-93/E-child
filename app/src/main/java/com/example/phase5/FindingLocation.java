package com.example.phase5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class FindingLocation extends AppCompatActivity {
    TextView tvlati,tvlongi,tvtime,tvdate;
    DatabaseReference reff;
    Button btnshow;
    Double latituded,longituded;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_location);
        tvlati=findViewById(R.id.tvLocateLatitude);
        tvlongi=findViewById(R.id.tvLocateLongitude);
        tvtime=findViewById(R.id.tvLocateTime);
        tvdate=findViewById(R.id.tvLocateDate);
        btnshow=findViewById(R.id.btnshowmap);

        reff= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Location");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String latitude=snapshot.child("latitude").getValue().toString();
                String longitude=snapshot.child("longitude").getValue().toString();
                String time= snapshot.child("time").getValue().toString();
                String date=snapshot.child("date").getValue().toString();
                latituded=(double)snapshot.child("latitude").getValue();
                longituded=(double)snapshot.child("longitude").getValue();

                tvlati.setText("Latitude: "+latitude);
                tvlongi.setText("Longitude: "+longitude);
                tvtime.setText("Time: "+time);
                tvdate.setText("Date: "+date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent5 = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("google.navigation:q=latitude,longitude&mode=l"));
//
//                intent5.setPackage("com.google.android.apps.maps");
//                if(intent5.resolveActivity(getPackageManager())!= null)
//                {
//                    startActivity(intent5);
//                }

                String uri = String.format(Locale.ENGLISH,"http://maps.google.com/maps?q=loc:%f,%f&mode=l",latituded,longituded);
                Intent intent4=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
                startActivity(intent4);

            }
        });
    }
}