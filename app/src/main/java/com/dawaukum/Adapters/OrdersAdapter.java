package com.dawaukum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dawaukum.R;

import java.util.ArrayList;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ItemHolder>{

    private Context context;
    ArrayList<Map<String, Object>> data;

    ArrayList<Object> drug=new ArrayList<Object>();

    public OrdersAdapter(Context context, ArrayList<Map<String, Object>> data){
        this.context = context;
        this.data=data;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_order, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        Map<String, Object> current=data.get(i);

        itemHolder.orderNumber.setText(i+"");
        try {
            itemHolder.pharmacyName.setText(current.get("").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        itemHolder.totalPrice.setText(current.get("Total").toString());
        itemHolder.status.setText(current.get("OrderStatus").toString());


        drug.add(current.get("Drugs"));


        //set Recyclerview
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(context,  drug);
        itemHolder.itemRecyclerview.setAdapter(orderItemAdapter);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView orderNumber, pharmacyName, totalPrice, status;
        RecyclerView itemRecyclerview;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.order_number_value);
            pharmacyName = itemView.findViewById(R.id.pharmacy_value);
            status = itemView.findViewById(R.id.status_value);
            totalPrice = itemView.findViewById(R.id.total_value);
            itemRecyclerview = itemView.findViewById(R.id.order_items_recyclerview);
            itemRecyclerview.setLayoutManager(new LinearLayoutManager(context));

        }
    }
}


