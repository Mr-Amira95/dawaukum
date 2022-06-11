package com.dawaukum.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dawaukum.Adapters.OrdersAdapter;
import com.dawaukum.Adapters.PharmacyOrdersAdapter;
import com.dawaukum.General.LoginActivity;
import com.dawaukum.R;
import com.dawaukum.User.MainUserActivity;
import com.dawaukum.User.UserOrdersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPharmacyActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    ImageView pharmacyImg, menuIcon;
    TextView pharmacyName;
    RecyclerView ordersRecyclerview;


    ProgressDialog progressDialog;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public static final String MyPREFERENCES = "myprefs";

    ArrayList<Map<String, Object>> data;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_main);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        //Define Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        ordersRecyclerview = findViewById(R.id.orders_recyclerview);
        ordersRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        View headerView = navigationView.getHeaderView(0);
        pharmacyImg = headerView.findViewById(R.id.image);
        pharmacyName = headerView.findViewById(R.id.name);

        progressDialog=new ProgressDialog(MainPharmacyActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        data=new ArrayList<>();


        pharmacyName.setText(prefs.getString("name",""));

        try {

            Glide
                    .with(MainPharmacyActivity.this)
                    .load(prefs.getString("imagelink",""))
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(pharmacyImg);

        }catch (Exception e){
            e.printStackTrace();
        }

        NavigationTransaction();

        progressDialog.show();

        getOrder();

        //Clicks Action
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }
    private void NavigationTransaction() {
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(Gravity.LEFT);

            int id = item.getItemId();
            switch (id) {
                case R.id.nav_orders:
                    startActivity(MainPharmacyActivity.class);
                    break;
                case R.id.nav_drugs:
                    startActivity(PharmacyDrugsActivity.class);
                    break;
                case R.id.nav_notifications:
                    startActivity(PharmacyNotificationsActivity.class);
                    break;
                case R.id.nav_profile:
                    startActivity(PharmacyProfileActivity.class);
                    break;
                case R.id.nav_logout:
                    SharedPreferences settings = MainPharmacyActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    Intent i = new Intent(MainPharmacyActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }

            return true;
        });
    }

    private void startActivity(Class classs) {
        Intent i = new Intent(MainPharmacyActivity.this, classs);
        startActivity(i);
    }

    //get Cart

    private void getOrder(){
        db.collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                          //  if(documentSnapshot.get("PharmacyName").toString().equalsIgnoreCase(prefs.getString("name",""))){
                                data.add(documentSnapshot.getData());
                        }

                        //set Recyclerview
                        PharmacyOrdersAdapter pharmacyOrdersAdapter = new PharmacyOrdersAdapter(MainPharmacyActivity.this,data);
                        ordersRecyclerview.setAdapter(pharmacyOrdersAdapter);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }


}