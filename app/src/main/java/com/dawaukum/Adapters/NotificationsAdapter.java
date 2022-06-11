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

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ItemHolder>{

    private Context context;
    int i;

    public NotificationsAdapter(Context context, int i){
        this.context = context;
        this.i=i;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_notification, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

    }


    @Override
    public int getItemCount() {
        return i;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView title, desc;
        ImageView icon;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            icon=itemView.findViewById(R.id.icon);

        }
    }
}


