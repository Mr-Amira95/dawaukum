package com.dawaukum.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.dawaukum.Adapters.OrdersAdapter;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserOrdersActivity extends AppCompatActivity {

    RecyclerView ordersRecyclerview;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    ArrayList<Map<String, Object>> data;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressDialog=new ProgressDialog(UserOrdersActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        data=new ArrayList<>();
        //Define Views
        ordersRecyclerview = findViewById(R.id.orders_recyclerview);
        ordersRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        progressDialog.show();

        getOrder();


    }

    //get Order

    private void getOrder(){

        db.collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressDialog.dismiss();
                for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    if(documentSnapshot.getString("UserEmail").equalsIgnoreCase(sharedpreferences.getString("email",""))){
                        data.add(documentSnapshot.getData());
                    }
                }

                //set Recyclerview
                OrdersAdapter ordersAdapter = new OrdersAdapter(UserOrdersActivity.this,data);
                ordersRecyclerview.setAdapter(ordersAdapter);
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