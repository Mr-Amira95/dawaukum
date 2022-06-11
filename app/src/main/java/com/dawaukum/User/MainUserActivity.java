package com.dawaukum.User;

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
import com.dawaukum.Adapters.PharmaciesAdapter;
import com.dawaukum.General.LoginActivity;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainUserActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    ImageView userImg, menuIcon, cartIcon;
    TextView username;
    RecyclerView pharmaciesRecyclerview;

    ArrayList<Map<String, Object>> data;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public static final String MyPREFERENCES = "myprefs";

    ProgressDialog progressDialog;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        //Define Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        cartIcon = findViewById(R.id.cart_icon);
        pharmaciesRecyclerview = findViewById(R.id.pharmacies_recyclerview);
        pharmaciesRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.name);
        userImg = headerView.findViewById(R.id.image);

        data=new ArrayList<>();

        progressDialog=new ProgressDialog(MainUserActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        username.setText(prefs.getString("name",""));

        try {

            Glide
                    .with(MainUserActivity.this)
                    .load(prefs.getString("imagelink",""))
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(userImg);

        }catch (Exception e){
            e.printStackTrace();
        }

        NavigationTransaction();

        progressDialog.show();

       getPharmacyData();

        //Clicks Action
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CartActivity.class);
            }
        });
    }

    private void NavigationTransaction() {
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(Gravity.LEFT);
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_home:
                    startActivity(MainUserActivity.class);
                    break;
                case R.id.nav_cart:
                    startActivity(CartActivity.class);
                    break;
                case R.id.nav_orders:
                    startActivity(UserOrdersActivity.class);
                    break;
                case R.id.nav_profile:
                    startActivity(UserProfileActivity.class);
                    break;
                case R.id.nav_logout:
                    SharedPreferences settings = MainUserActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                    Intent i = new Intent(MainUserActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }

            return true;
        });
    }

    private void startActivity(Class classs) {
        Intent i = new Intent(MainUserActivity.this, classs);
        startActivity(i);
    }

    private void getPharmacyData(){

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                progressDialog.dismiss();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){

                        if(documentSnapshot.getString("Type").equalsIgnoreCase("Pharmacy")){
                            data.add(documentSnapshot.getData());
                        }
                    }
                    //set Recyclerview
                    PharmaciesAdapter pharmaciesAdapter = new PharmaciesAdapter(MainUserActivity.this,data);
                    pharmaciesRecyclerview.setAdapter(pharmaciesAdapter);

                }else {
                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}