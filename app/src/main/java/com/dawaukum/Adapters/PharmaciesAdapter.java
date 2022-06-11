package com.dawaukum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.dawaukum.User.UserPharmacyActivity;

import java.util.ArrayList;
import java.util.Map;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.ItemHolder>{

    private Context context;
    ArrayList<Map<String, Object>> data;

    public PharmaciesAdapter(Context context, ArrayList<Map<String, Object>> data){
        this.context = context;
        this.data=data;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_pharmacy, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        Map<String, Object> currntdata = data.get(i);


        itemHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserPharmacyActivity.class);
                i.putExtra("pharmacyName", currntdata.get("pharmacyName").toString());
                context.startActivity(i);
            }
        });

        try {
            itemHolder.pharmacyName.setText(currntdata.get("pharmacyName").toString());

            itemHolder.pharmacyAddress.setText(currntdata.get("Address").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Glide
                    .with(context)
                    .load(currntdata.get("imagelink").toString())
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(itemHolder.pharmacyImg);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView pharmacyName, pharmacyAddress;
        ImageView pharmacyImg;
        ConstraintLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            pharmacyName=itemView.findViewById(R.id.pharmacy_name);
            pharmacyAddress=itemView.findViewById(R.id.pharmacy_address);
            pharmacyImg=itemView.findViewById(R.id.pharmacy_img);
            layout=itemView.findViewById(R.id.layout);

        }
    }
}


