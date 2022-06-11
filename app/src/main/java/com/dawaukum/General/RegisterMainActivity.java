package com.dawaukum.General;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dawaukum.R;

public class RegisterMainActivity extends AppCompatActivity {

    Button userRegister, pharmacyRegister, doctorRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register_main);

        //Define Views
        userRegister = findViewById(R.id.user_register_btn);
        pharmacyRegister = findViewById(R.id.pharmacy_register_btn);
        doctorRegister = findViewById(R.id.doctor_register_btn);

        //Clicks Action
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterMainActivity.this, RegisterUserActivity.class);
                startActivity(i);
            }
        });

        pharmacyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterMainActivity.this, RegisterPharmacyActivity.class);
                startActivity(i);
            }
        });

        doctorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterMainActivity.this, RegisterDoctorActivity.class);
                startActivity(i);
            }
        });

    }
}