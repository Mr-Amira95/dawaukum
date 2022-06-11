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
import com.dawaukum.User.DrugActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemHolder>{

    private Context context;
    ArrayList<Object> drugs;

    public OrderItemAdapter(Context context, ArrayList<Object> drugs){
        this.context = context;
        this.drugs=drugs;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_order_item, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {


        Object current= ((HashMap) ((ArrayList) ((ArrayList) drugs).get(0)).get(0));

        Object[] keys = ((HashMap) current).keySet().toArray();

        Object currentd= ((HashMap) current).get(keys[i]);

        itemHolder.qty.append(" 1");

        itemHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DrugActivity.class);
                i.putExtra("drugname", ((HashMap) currentd).get("Drugname").toString());
                context.startActivity(i);
            }
        });

        itemHolder.drugName.setText(((HashMap) currentd).get("Drugname").toString());
        itemHolder.drubPrice.setText(((HashMap) currentd).get("DrugPrice").toString());
        itemHolder.qty.setText(((HashMap) currentd).get("Quantity").toString());


        try {

            Glide
                    .with(context)
                    .load(((HashMap) currentd).get("imagelink").toString())
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(itemHolder.drugImg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return ((HashMap) ((ArrayList) ((ArrayList) drugs).get(0)).get(0)).size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ImageView drugImg;
        TextView drugName, drubPrice, qty;
        ConstraintLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            drugImg = itemView.findViewById(R.id.drug_img);
            drugName = itemView.findViewById(R.id.drug_name);
            drubPrice = itemView.findViewById(R.id.drug_price);
            layout = itemView.findViewById(R.id.layout);
            qty = itemView.findViewById(R.id.drug_qty);

        }
    }
}


