package com.dawaukum.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dawaukum.Adapters.DrugsAdapter;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserPharmacyActivity extends AppCompatActivity {

    TextView all, prescription, nonePrescription;
    RecyclerView drugsRecyclerview;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    ArrayList<Map<String, Object>> data;
    String Pharmname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pharmacy);

        Intent intent = getIntent();

        Pharmname = intent.getStringExtra("pharmacyName");

        progressDialog=new ProgressDialog(UserPharmacyActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //Define Views
        all = findViewById(R.id.all_drug);
        prescription = findViewById(R.id.prescription_drug);
        nonePrescription = findViewById(R.id.none_prescription_drug);
        drugsRecyclerview = findViewById(R.id.drugs_recyclerview);
        drugsRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));

        data=new ArrayList<>();

        progressDialog.show();

        getDrugs(Pharmname,"All");

        //Clicks Action
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all.setBackgroundResource(R.drawable.background_button);
                all.setTextColor(getResources().getColor(R.color.white));
                prescription.setBackgroundResource(R.drawable.background_edittext);
                prescription.setTextColor(getResources().getColor(R.color.primary_color));
                nonePrescription.setBackgroundResource(R.drawable.background_edittext);
                nonePrescription.setTextColor(getResources().getColor(R.color.primary_color));

                data=new ArrayList<>();

                progressDialog.show();

                getDrugs(Pharmname,"All");

            }
        });

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prescription.setBackgroundResource(R.drawable.background_button);
                prescription.setTextColor(getResources().getColor(R.color.white));
                all.setBackgroundResource(R.drawable.background_edittext);
                all.setTextColor(getResources().getColor(R.color.primary_color));
                nonePrescription.setBackgroundResource(R.drawable.background_edittext);
                nonePrescription.setTextColor(getResources().getColor(R.color.primary_color));

                data=new ArrayList<>();

                progressDialog.show();

                getDrugs(Pharmname,"yes");

            }
        });

        nonePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nonePrescription.setBackgroundResource(R.drawable.background_button);
                nonePrescription.setTextColor(getResources().getColor(R.color.white));
                all.setBackgroundResource(R.drawable.background_edittext);
                all.setTextColor(getResources().getColor(R.color.primary_color));
                prescription.setBackgroundResource(R.drawable.background_edittext);
                prescription.setTextColor(getResources().getColor(R.color.primary_color));

                data=new ArrayList<>();

                progressDialog.show();

                getDrugs(Pharmname,"no");

            }
        });

    }


    //get Drug

    private void getDrugs(String Parmacyname,String prescription){
        db.collection("Drugs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        if (documentSnapshot.getString("Pharmacyname").equalsIgnoreCase(Parmacyname)) {
                            if (documentSnapshot.getString("Prescription").equalsIgnoreCase(prescription)) {
                                data.add(documentSnapshot.getData());
                            } else if (documentSnapshot.getString("Prescription").equalsIgnoreCase("no")) {
                                data.add(documentSnapshot.getData());
                            } else if (prescription.equalsIgnoreCase("All")) {
                                data.add(documentSnapshot.getData());
                            }
                        }
                    }

                    //set Recyclerview
                    DrugsAdapter drugsAdapter = new DrugsAdapter(UserPharmacyActivity.this,data);
                    drugsRecyclerview.setAdapter(drugsAdapter);

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}