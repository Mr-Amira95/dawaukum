package com.dawaukum.General;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dawaukum.Doctor.MainDoctorActivity;
import com.dawaukum.Pharmacy.MainPharmacyActivity;
import com.dawaukum.R;
import com.dawaukum.User.MainUserActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_splash);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (prefs.getString("type","").equalsIgnoreCase("user")){
                    Intent i = new Intent(SplashActivity.this, MainUserActivity.class);
                    startActivity(i);
                    finish();
                } else if (prefs.getString("type","").equalsIgnoreCase("pharmacy")){
                    Intent i = new Intent(SplashActivity.this, MainPharmacyActivity.class);
                    startActivity(i);
                    finish();
                } else if (prefs.getString("type","").equalsIgnoreCase("Doctor")){
                    Intent i = new Intent(SplashActivity.this, MainDoctorActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);

    }
}