package com.dawaukum.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.dawaukum.User.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PharmacyProfileActivity extends AppCompatActivity {

    TextView pharmacyName, pharmacyEmail, responsiblePharmacist,
            commercialNumber, openingDate, pharmacyAddress;
    ImageView pharmacyLogo;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    SharedPreferences prefs;
    public static final String MyPREFERENCES = "myprefs";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        progressDialog=new ProgressDialog(PharmacyProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //Define Views
        pharmacyName = findViewById(R.id.name);
        pharmacyEmail = findViewById(R.id.email);
        responsiblePharmacist = findViewById(R.id.pharmacist);
        commercialNumber = findViewById(R.id.commercial_number);
        openingDate = findViewById(R.id.opening_date_pha);
        pharmacyAddress = findViewById(R.id.address);
        pharmacyLogo = findViewById(R.id.pharmacy_logo);


        progressDialog.show();

        getPharmacyInfo(prefs.getString("email",""));


    }

    //get User

    private void getPharmacyInfo(String emails){

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        if (documentSnapshot.getString("email").equalsIgnoreCase(emails)) {
                            if (documentSnapshot.getString("Type").equalsIgnoreCase("Pharmacy")){
                                pharmacyName.setText(documentSnapshot.getString("pharmacyName").toString());
                                pharmacyEmail.setText(documentSnapshot.getString("email").toString());
                                responsiblePharmacist.setText(documentSnapshot.getString("responsiblePharmacy").toString());
                                commercialNumber.setText(documentSnapshot.getString("commercialNumber").toString());
                                pharmacyAddress.setText(documentSnapshot.getString("Address").toString());
                                openingDate.setText(documentSnapshot.getString("OpeningDate").toString());
                                try {
                                    Glide.with(PharmacyProfileActivity.this).load(documentSnapshot.getString("imagelink").toString()).centerCrop().placeholder(R.drawable.logo).into(pharmacyLogo);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}