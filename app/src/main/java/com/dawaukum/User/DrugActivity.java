package com.dawaukum.User;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DrugActivity extends AppCompatActivity {

    TextView drugName, drugDescription, drugPrice;
    EditText quantity;
    ImageView drugImg, increase, decrease;
    Button addToCart;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    String drugname,drugPriscrip,imagelink,pharmacynam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_drug);

        Intent intent = getIntent();

        drugname = intent.getStringExtra("drugname");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressDialog=new ProgressDialog(DrugActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //Define Views
        drugName = findViewById(R.id.drug_name);
        drugDescription = findViewById(R.id.drug_description);
        drugPrice = findViewById(R.id.drug_price);
        quantity = findViewById(R.id.quantity);
        drugImg = findViewById(R.id.drug_img);
        increase = findViewById(R.id.increase_icon);
        decrease = findViewById(R.id.decrease_icon);
        addToCart = findViewById(R.id.add_to_cart_btn);

        progressDialog.show();

        getDrug(drugname);

        //Clicks Action
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(quantity.getText().toString()) + 1;
                quantity.setText(String.valueOf(qty));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(quantity.getText().toString()) - 1;

                if (qty>0)
                    quantity.setText(String.valueOf(qty));
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                storeInCart(sharedpreferences.getString("email",""),drugPrice.getText().toString(),drugName.getText().toString(),
                        drugPriscrip,quantity.getText().toString(),imagelink,pharmacynam);

            }
        });


    }

    //get Drug

    private void getDrug(String Drugname){
        db.collection("Drugs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        if (documentSnapshot.getString("Drugname").equalsIgnoreCase(Drugname)) {
                            drugName.setText(documentSnapshot.getString("Drugname").toString());
                            drugDescription.setText(documentSnapshot.getString("DrugDesc").toString());
                            drugPrice.setText(documentSnapshot.getString("Price").toString());
                            drugPriscrip=documentSnapshot.getString("Prescription");
                            try {
                                imagelink = documentSnapshot.getString("imagelink").toString();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            pharmacynam=documentSnapshot.getString("Pharmacyname").toString();
                            try {
                                Glide.with(DrugActivity.this).load(documentSnapshot.getString("imagelink").toString()).centerCrop().placeholder(R.drawable.logo).into(drugImg);
                            }catch (Exception e){
                                e.printStackTrace();
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


    private void storeInCart(String email,String price,String drugname,String Prescription,String Quantity,String imagelink,String pharmacyname){


        // Create a new user
        Map<String, Object> Drug = new HashMap<>();

        Drug.put("Drugname",drugname);
        Drug.put("DrugPrice", price);
        Drug.put("Prescription", Prescription);
        Drug.put("Quantity",Quantity);
        Drug.put("imagelink",imagelink);
        Drug.put("PharmacyName",pharmacyname);



        db.collection("Cart").document(email)
                .update(drugname,Drug).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent in=new Intent(DrugActivity.this,CartActivity.class);
                        startActivity(in);
                        finish();

                    }
                })
               .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Map<String, Object> Drug = new HashMap<>();

                        db.collection("Cart").document(email).set(Drug);


                        storeInCart(sharedpreferences.getString("email",""),drugPrice.getText().toString(),drugName.getText().toString(),
                                drugPriscrip,quantity.getText().toString(),imagelink,pharmacynam);

                    }
                });

    }


}