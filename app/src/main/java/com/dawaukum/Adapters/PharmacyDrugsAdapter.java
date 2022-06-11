package com.dawaukum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dawaukum.Pharmacy.PharmacyDrugSingleActivity;
import com.dawaukum.Pharmacy.PharmacyDrugsActivity;
import com.dawaukum.Pharmacy.PharmacyNotificationsActivity;
import com.dawaukum.R;
import com.dawaukum.User.DrugActivity;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PharmacyDrugsAdapter extends RecyclerView.Adapter<PharmacyDrugsAdapter.ItemHolder>{

    private Context context;
    ArrayList<Map<String, Object>> data;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public PharmacyDrugsAdapter(Context context, ArrayList<Map<String, Object>> data){
        this.context = context;
        this.data=data;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_drug_pharmacy, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        Map<String, Object> currntdata = data.get(i);

        itemHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PharmacyDrugSingleActivity.class);
                i.putExtra("flag", "edit");
                context.startActivity(i);
            }
        });


        itemHolder.drugName.setText(currntdata.get("Drugname").toString());
        itemHolder.drugPrice.setText(currntdata.get("Quantity").toString());

        try {

            Glide
                    .with(context)
                    .load(currntdata.get("imagelink").toString())
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(itemHolder.drugImg);

        }catch (Exception e){
            e.printStackTrace();
        }

        notf(itemHolder.drugPrice);

    }

    private void notf(TextView drugPrice){

        db.collection("Drugs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                drugPrice.setText(value.getDocumentChanges().get(0).getDocument().get("Quantity").toString());
            }
        });


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView drugName, drugPrice;
        ImageView drugImg;
        ConstraintLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            drugName=itemView.findViewById(R.id.drug_name);
            drugPrice=itemView.findViewById(R.id.drug_price);
            drugImg=itemView.findViewById(R.id.drug_img);
            layout=itemView.findViewById(R.id.layout);

        }
    }
}


