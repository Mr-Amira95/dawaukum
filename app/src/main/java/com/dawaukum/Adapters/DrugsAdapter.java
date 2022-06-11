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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.dawaukum.User.DrugActivity;
import com.dawaukum.User.UserPharmacyActivity;

import java.util.ArrayList;
import java.util.Map;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.ItemHolder>{

    private Context context;

    ArrayList<Map<String, Object>> data;

    public DrugsAdapter(Context context, ArrayList<Map<String, Object>> data){
        this.context = context;

        this.data=data;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_drug, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        Map<String, Object> currntdata = data.get(i);

        itemHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DrugActivity.class);
                i.putExtra("drugname", currntdata.get("Drugname").toString());
                context.startActivity(i);
            }
        });

        itemHolder.drugName.setText(currntdata.get("Drugname").toString());
        itemHolder.drugPrice.setText(currntdata.get("Price").toString());


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

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView drugName, drugPrice;
        ImageView drugImg;
        Button addToCartBtn;
        ConstraintLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            drugName=itemView.findViewById(R.id.drug_name);
            drugPrice=itemView.findViewById(R.id.drug_price);
            drugImg=itemView.findViewById(R.id.drug_img);
            addToCartBtn=itemView.findViewById(R.id.add_to_cart_btn);
            layout=itemView.findViewById(R.id.layout);

        }
    }
}


