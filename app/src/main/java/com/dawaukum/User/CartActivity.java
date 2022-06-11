package com.dawaukum.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dawaukum.Adapters.CartAdapter;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerview;
    Button checkoutBtn;

    TextView total;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ArrayList<Map<String, Object>> data;

    ProgressDialog progressDialog;

    static boolean parescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressDialog=new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        //Define Views
        total=findViewById(R.id.total);
        checkoutBtn = findViewById(R.id.checkout_btn);
        cartRecyclerview = findViewById(R.id.carts_recyclerview);
        cartRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        data=new ArrayList<>();

        progressDialog.show();
        getCart();


        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CartActivity.this, CheckoutActivity.class);
                i.putExtra("data",data);
                i.putExtra("prescription",parescription);
                i.putExtra("total",total.getText().toString());
                startActivity(i);


            }
        });

    }


    //get Cart

    private void getCart(){
        db.collection("Cart").document(sharedpreferences.getString("email","")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                progressDialog.dismiss();
                try {
                    if (!(task.getResult().getData().size() == 0)) {
                        //set Recyclerview
                        data.add(task.getResult().getData());
                        CartAdapter cartAdapter = new CartAdapter(CartActivity.this, task.getResult().getData(), total,parescription);
                        cartRecyclerview.setAdapter(cartAdapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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