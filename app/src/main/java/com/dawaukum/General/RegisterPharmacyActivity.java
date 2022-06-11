package com.dawaukum.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dawaukum.Pharmacy.MainPharmacyActivity;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterPharmacyActivity extends AppCompatActivity {

    EditText pharmacyName, responsiblePharmacist, commercialNumber, email, openingDate, password, confirmPassword, address;
    Button register;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";
    public static final  String value = "key";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register_pharmacy);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //Define Views
        pharmacyName = findViewById(R.id.pharmacy_name);
        responsiblePharmacist = findViewById(R.id.responsible_pharmacist_name);
        commercialNumber = findViewById(R.id.commercial_number);
        email = findViewById(R.id.email);
        openingDate = findViewById(R.id.opening_date_pha);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        address = findViewById(R.id.address);
        register = findViewById(R.id.register_btn);

        progressDialog=new ProgressDialog(RegisterPharmacyActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        //Clicks Action
        openingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) ;
                day = c.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG_ID);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();


                if (password.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {

                    if(pharmacyName.getText().toString().isEmpty()){
                        pharmacyName.setError("Required");
                    }else if (responsiblePharmacist.getText().toString().isEmpty()){
                       responsiblePharmacist.setError("Required");
                    }else if(commercialNumber.getText().toString().isEmpty()){
                        commercialNumber.setError("Required");
                    }else if(email.getText().toString().isEmpty()){
                        email.setError("Required");
                    }else if(openingDate.getText().toString().isEmpty()){
                        openingDate.setError("Required");
                    }else if (password.getText().toString().isEmpty()){
                        password.setError("Required");
                    }else if (address.getText().toString().isEmpty()){
                        address.setError("Required");
                    }else {
                        storeData(pharmacyName.getText().toString(), responsiblePharmacist.getText().toString(), commercialNumber.getText().toString(), email.getText().toString(), openingDate.getText().toString(),
                                password.getText().toString(), address.getText().toString());
                    }

                }else {
                    Toast.makeText(RegisterPharmacyActivity.this,"Password not match",Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, datePickerLisner, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerLisner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int SelectedYear, int SelectedMonth, int SelectedDay) {
            year= SelectedYear;
            month= SelectedMonth;
            day = SelectedDay;

            openingDate.setText(SelectedYear+"/"+(SelectedMonth+1)+"/"+SelectedDay);
        }
    };

    private void storeData(String pharmacyName,String responsiblePharmacy,String commercialNumber,String email,String OpeningDate,String password,String address){


        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("pharmacyName", pharmacyName);
        user.put("responsiblePharmacy", responsiblePharmacy);
        user.put("commercialNumber", commercialNumber);
        user.put("email",email);
        user.put("OpeningDate",OpeningDate);
        user.put("password",password);
        user.put("Address",address);
        user.put("Type","Pharmacy");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("name", pharmacyName);
                        editor.putString("email", email);
                        editor.putString("type","pharmacy");
                        editor.apply();

                        Intent intent=new Intent(RegisterPharmacyActivity.this, MainPharmacyActivity.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_LONG).show();

                    }
                });

    }



}