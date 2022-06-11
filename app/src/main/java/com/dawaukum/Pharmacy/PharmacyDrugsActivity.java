package com.dawaukum.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dawaukum.Adapters.DrugsAdapter;
import com.dawaukum.Adapters.PharmacyDrugsAdapter;
import com.dawaukum.R;
import com.dawaukum.User.UserPharmacyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PharmacyDrugsActivity extends AppCompatActivity {

    RecyclerView drugsRecyclerview;
    Button addDrug;

    ProgressDialog progressDialog;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ArrayList<Map<String, Object>> data;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_drugs);

        //Define Views
        addDrug = findViewById(R.id.add_drug_btn);
        drugsRecyclerview = findViewById(R.id.drugs_recyclerview);
        drugsRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));

        progressDialog=new ProgressDialog(PharmacyDrugsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        data=new ArrayList<>();


        //clicks Action
        addDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PharmacyDrugsActivity.this, PharmacyDrugSingleActivity.class);
                i.putExtra("flag", "add");
                startActivity(i);
            }
        });


        progressDialog.show();

        getDrugs(sharedpreferences.getString("name",""));


    }



    //get Drug

    private void getDrugs(String Parmacyname){
        db.collection("Drugs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        if (documentSnapshot.getString("Pharmacyname").equalsIgnoreCase(Parmacyname)) {
                            data.add(documentSnapshot.getData());
                        }
                    }

                    //set Recyclerview
                    PharmacyDrugsAdapter drugsAdapter = new PharmacyDrugsAdapter(PharmacyDrugsActivity.this,data);
                    drugsRecyclerview.setAdapter(drugsAdapter);

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}