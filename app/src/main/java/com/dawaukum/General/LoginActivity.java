package com.dawaukum.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawaukum.Doctor.MainDoctorActivity;
import com.dawaukum.Pharmacy.MainPharmacyActivity;
import com.dawaukum.R;
import com.dawaukum.User.MainUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView register;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_login);

        //Define Views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        register = findViewById(R.id.register);

        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //Clicks Action
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterMainActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                check(email.getText().toString(),password.getText().toString());

            }
        });






    }

    //check with firebase
    private void check(String email,String pass){

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            progressDialog.dismiss();

                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if(documentSnapshot.getString("email").equalsIgnoreCase(email)){
                                    if (documentSnapshot.getString("password").equals(pass)){
                                        if (documentSnapshot.getString("Type").equalsIgnoreCase("user")){
                                            editor.putString("name", documentSnapshot.getString("name"));
                                            Intent i = new Intent(LoginActivity.this, MainUserActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else if (documentSnapshot.getString("Type").equalsIgnoreCase("pharmacy")){
                                            editor.putString("name", documentSnapshot.getString("pharmacyName"));
                                            Intent i = new Intent(LoginActivity.this, MainPharmacyActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else if (documentSnapshot.getString("Type").equalsIgnoreCase("Doctor")){
                                            Intent i = new Intent(LoginActivity.this, MainDoctorActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                        editor.putString("email", documentSnapshot.getString("email"));
                                        try {
                                            editor.putString("imagelink", documentSnapshot.getString("imagelink").toString());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        editor.putString("type",documentSnapshot.getString("Type"));
                                        editor.putString("address",documentSnapshot.getString("Address").toString());
                                        editor.putString("gender",documentSnapshot.getString("gender"));
                                        editor.apply();

                                    }
                                }

                            }
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}