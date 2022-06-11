package com.dawaukum.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dawaukum.R;
import com.dawaukum.User.MainUserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    EditText name, email, birthdate, identifierNumber, password, confirmPassword, address;
    RadioGroup gender;
    Spinner identifierType;
    Button register;

    String genSelected="Male";

    RadioButton male_selection,female_selection;

    DatePickerDialog datePickerDialog;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";
    public static final  String value = "key";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register_user);

        //Define Views
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        birthdate = findViewById(R.id.birthdate);
        identifierNumber = findViewById(R.id.identifier_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.commercial_number);
        identifierType = findViewById(R.id.identifier_type);
        register = findViewById(R.id.register_btn);
        male_selection=findViewById(R.id.male_selection);
        female_selection=findViewById(R.id.female_selection);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressDialog=new ProgressDialog(RegisterUserActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");



        //Set Identifier Type
        final String[] types={"Type","Passport","Identity"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_spinner, types);
        spinnerArrayAdapter.setDropDownViewResource( R.layout.layout_spinner_drop_down);
        identifierType.setAdapter(spinnerArrayAdapter);

        //Initial Date Picker to get Birthdate
        initialDatePicker();

        //Clicks Action
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });





        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                int checkedRadioButtonId = gender.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.male_selection) {
                    // No item selected
                    genSelected="Male";
                } else {
                    if (checkedRadioButtonId == R.id.female_selection) {
                        // Do something with the button
                        genSelected="Female";
                    }
                }

                if (password.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {

                    if(name.getText().toString().isEmpty()){
                        name.setError("Required");
                    }else if (email.getText().toString().isEmpty()){
                        email.setError("Required");
                    }else if(birthdate.getText().toString().isEmpty()){
                        birthdate.setError("Required");
                    }else if(genSelected.equalsIgnoreCase("null")){
                        Toast.makeText(RegisterUserActivity.this,"Please Select Gender",Toast.LENGTH_LONG).show();
                    }else if(identifierNumber.getText().toString().isEmpty()){
                        identifierNumber.setError("Required");
                    }else if (password.getText().toString().isEmpty()){
                        password.setError("Required");
                    }else if (address.getText().toString().isEmpty()){
                        address.setError("Required");
                    }else {
                        storeData(name.getText().toString(), email.getText().toString(), birthdate.getText().toString(), identifierNumber.getText().toString(), identifierType.getSelectedItem().toString(),
                                password.getText().toString(), address.getText().toString(), genSelected);
                    }

                }else {
                    Toast.makeText(RegisterUserActivity.this,"Password not match",Toast.LENGTH_LONG).show();
                }

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

    private void storeData(String name,String email,String birthdate,String identifierNumber,String identtype,String password,String address,String Gender){

        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("birthday", birthdate);
        user.put("identifierNumber",identifierNumber);
        user.put("identifierType",identtype);
        user.put("password",password);
        user.put("Address",address);
        user.put("gender",Gender);
        user.put("Type","user");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("type","user");
                        editor.putString("address",address);
                        editor.putString("gender",Gender);
                        editor.apply();

                        Intent intent=new Intent(RegisterUserActivity.this, MainUserActivity.class);
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