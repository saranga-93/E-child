package com.example.phase5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChildDashboard extends AppCompatActivity implements IBaseGPSListener {
    Button btnout, help;
    TextView greet;
    FirebaseDatabase mroot;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser mUser;
    DatabaseReference reff;
    String tokendata;
    String token;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    private Handler mHandler = new Handler();


    private double latitude, longitude;
    private static final int PERMISSION_LOCATION = 1000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        btnout = findViewById(R.id.btnOut);
        greet = findViewById(R.id.textViewkidgreet);
        help = findViewById(R.id.btnaskhelp);


        mroot = FirebaseDatabase.getInstance();
        reference = mroot.getReference("Location");
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }


        reff = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Parent");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tokendata = snapshot.child("parenttoken").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }


                        token = task.getResult();
                        Tokenhelper tokenhelper = new Tokenhelper(token);
                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Child").setValue(tokenhelper);


                    }
                });


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Integer integerbattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int charge = (int) integerbattery;
                if (charge < 20 && charge>17) {

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokendata, "Child's Phone battery low", "Hey parent, seems like your child's phone battery might die soon", getApplicationContext(), ChildDashboard.this);

                    notificationsSender.SendNotifications();
                }


                Batteryhelper batteryhelper = new Batteryhelper(charge);

                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Charge").setValue(batteryhelper);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    float x_accl = sensorEvent.values[0];
                    float y_accl = sensorEvent.values[1];
                    float z_accl = sensorEvent.values[2];

                    float floatsum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl);


                    // if (x_accl > 2 ||
                    //       x_accl < -2 ||
                    //     y_accl > 12 ||
                    //   y_accl < -12 ||
                    // z_accl > 2 ||
                    // z_accl < -2){
                    if (floatsum > 50) {

                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokendata, "Child's Motion alert", "Seems like your child is facing some issue, connect immediately.", getApplicationContext(), ChildDashboard.this);

                        notificationsSender.SendNotifications();

                        try {
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setOutputFile(getRecordingFilePath());
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.prepare();
                            mediaRecorder.start();

//

                            mHandler.postDelayed(mToastRunnable, 30000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } else {

                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        sensorManager.registerListener(sensorEventListener, sensorShake, sensorManager.SENSOR_DELAY_NORMAL);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
        } else {
            showLocation();
        }

        Intent intent = new Intent(this, LocationForegroundService.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            stopService(intent);
        }


        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokendata, "Child's asking help alert", "Seems like your child is facing some issue, connect immediately!!!", getApplicationContext(), ChildDashboard.this);

                notificationsSender.SendNotifications();

                try {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(getRecordingFilePath());
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                    mediaRecorder.start();

//                    Toast.makeText(ChildDashboard.this, "Recording is started", Toast.LENGTH_LONG).show();

                    mHandler.postDelayed(mToastRunnable, 30000);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            mediaRecorder.stop();
            mediaRecorder.release();
//            mediaRecorder = null;

        }
    };

    public void onRequestPermissionsResult(int request_code, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(request_code, permissions, grantResults);
        if (request_code == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();
            }
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @SuppressLint("MissingPermission")
    private void showLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0F, this);
        } else {
            Toast.makeText(this, "Enable GPS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        Date dateandtime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        String date = dateFormat.format(dateandtime);
        String time = timeFormat.format(dateandtime);


        latitude = location.getLatitude();
        longitude = location.getLongitude();
//        Toast.makeText(this, "latitude: "+location.getLatitude(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();

        LocationHelper locationhelper = new LocationHelper(latitude, longitude, date, time);

        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Location").setValue(locationhelper);

    }

    @Override
    public void onProviderDisabled(String provider) {
//empty
    }

    @Override
    public void onProviderEnabled(String provider) {
//empty
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//empty
    }

    @Override
    public void onGpsStatusChanged(int event) {

    }


    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecoderFile" + ".mp3");
        return file.getPath();
    }



}
