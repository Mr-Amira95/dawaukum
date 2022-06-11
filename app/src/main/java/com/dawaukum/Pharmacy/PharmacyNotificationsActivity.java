package com.dawaukum.Pharmacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.dawaukum.Adapters.DrugsAdapter;
import com.dawaukum.Adapters.NotificationsAdapter;
import com.dawaukum.R;
import com.dawaukum.User.UserPharmacyActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class PharmacyNotificationsActivity extends AppCompatActivity {

    RecyclerView notificationsRecyclerview;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_notifications);

        //Define Views
        notificationsRecyclerview = findViewById(R.id.notification_recyclerview);
        notificationsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        notf();


    }

    private void notf(){

       db.collection("Order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //set Recyclerview
                NotificationsAdapter notificationsAdapter = new NotificationsAdapter(PharmacyNotificationsActivity.this,++counter);
                notificationsRecyclerview.setAdapter(notificationsAdapter);
            }
        });


    }


}