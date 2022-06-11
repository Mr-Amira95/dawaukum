package com.dawaukum.General;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.dawaukum.R;

import java.util.Calendar;

public class RegisterDoctorActivity extends AppCompatActivity {

    EditText name, email, birthdate, medicalAssociationID, password, confirmPassword, address;
    RadioGroup gender;
    Button register;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register_doctor);

        //Define Views
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        birthdate = findViewById(R.id.birthdate);
        medicalAssociationID = findViewById(R.id.medical_association_id);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.commercial_number);
        register = findViewById(R.id.register_btn);

        //Initial Date Picker to get Birthdate
        initialDatePicker();

        //Clicks Action
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

    }

    private void initialDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                birthdate.setText(i1+"/"+(i1+1)+"/"+i);
            }
        };

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) ;
        int day = c.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }


}