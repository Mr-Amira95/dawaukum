package com.dawaukum.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.dawaukum.User.CartActivity;
import com.dawaukum.User.DrugActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemHolder>{

    private Context context;

    Map<String, Object> data;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    boolean parescription;

    TextView price;

    public CartAdapter(Context context, Map<String, Object> data, TextView price, boolean parescription){
        this.context = context;
        this.data=data;
        this.price=price;
        this.parescription=parescription;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_cart, parent, false);

        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        Object[] keys = data.keySet().toArray();

        HashMap current= (HashMap<String , Object>) data.get(keys[i]);


        try {

            price.setText(String.valueOf(Double.parseDouble(current.get("Quantity").toString()) * Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))) + "JD");


            itemHolder.drugName.setText(current.get("Drugname").toString());
            itemHolder.drugPrice.setText(String.valueOf(Double.parseDouble(current.get("Quantity").toString()) * Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))));
            itemHolder.quantity.setText(current.get("Quantity").toString());

            if (current.get("Prescription").toString().equalsIgnoreCase("yes")) {
                itemHolder.needPrescription.setVisibility(View.VISIBLE);
                parescription=true;
            }

            try {

                Glide
                        .with(context)
                        .load(current.get("imagelink").toString())
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .into(itemHolder.drugImg);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //itemHolder.needPrescription

            itemHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DrugActivity.class);
                    i.putExtra("drugname", current.get("Drugname").toString());
                    context.startActivity(i);
                }
            });

            itemHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteFromCart(current.get("Drugname").toString());

                    Intent intent = new Intent(context, CartActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                    Toast.makeText(context, "Delete Item", Toast.LENGTH_LONG).show();
                }
            });

            itemHolder.increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.parseInt(itemHolder.quantity.getText().toString()) + 1;
                    itemHolder.quantity.setText(String.valueOf(qty));

                    progressDialog.show();

                    itemHolder.drugPrice.setText(String.valueOf(Double.parseDouble(itemHolder.drugPrice.getText().toString()) + Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))));

                    price.setText(String.valueOf(Double.parseDouble(price.getText().toString().replace("JD", "")) + Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))) + "JD");

                    quantityCart(itemHolder.quantity.getText().toString(), itemHolder.drugName.getText().toString(), String.valueOf(Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))), current.get("Prescription").toString(), current.get("imagelink").toString());

                }
            });

            itemHolder.decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.parseInt(itemHolder.quantity.getText().toString()) - 1;

                    if (qty > 0)
                        itemHolder.quantity.setText(String.valueOf(qty));


                    progressDialog.show();

                    itemHolder.drugPrice.setText(String.valueOf(Double.parseDouble(itemHolder.drugPrice.getText().toString()) - Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))));

                    price.setText(String.valueOf(Double.parseDouble(price.getText().toString().replace("JD", "")) - Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))) + "JD");

                    quantityCart(itemHolder.quantity.getText().toString(), itemHolder.drugName.getText().toString(), String.valueOf(Double.parseDouble(current.get("DrugPrice").toString().replace("JD", ""))), current.get("Prescription").toString(), current.get("imagelink").toString());

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        Object[] keys = data.keySet().toArray();

        HashMap current= (HashMap<String , Object>) data.get(keys[0]);

        if (current.size()==0){
            return 0;
        }else {
            return data.size();
        }
    }

    private void quantityCart(String Quantity,String drugname,String price,String Prescription,String imagelink){


        // Create a new user
        Map<String, Object> Drug = new HashMap<>();

        Drug.put("Drugname",drugname);
        Drug.put("DrugPrice", price+"JD");
        Drug.put("Prescription", Prescription);
        Drug.put("Quantity",Quantity);
        Drug.put("imagelink",imagelink);


        db.collection("Cart").document(sharedpreferences.getString("email",""))
                .update(drugname,Drug).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();

                        Toast.makeText(context,"Quantity Updated",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                });

    }

    private void deleteFromCart(String drugname){


        // Create a new user
        Map<String, Object> Drug = new HashMap<>();
        Drug.put(drugname, FieldValue.delete());


        db.collection("Cart").document(sharedpreferences.getString("email",""))
                .update(Drug).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();

                        Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                });

    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView drugName, drugPrice, needPrescription;
        ImageView drugImg, deleteIcon, increase, decrease;
        ConstraintLayout layout;
        EditText quantity;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            drugName=itemView.findViewById(R.id.drug_name);
            drugPrice=itemView.findViewById(R.id.drug_price);
            needPrescription=itemView.findViewById(R.id.need_prescription);
            drugImg=itemView.findViewById(R.id.drug_img);
            deleteIcon=itemView.findViewById(R.id.delete_icon);
            layout=itemView.findViewById(R.id.layout);
            increase = itemView.findViewById(R.id.increase_icon);
            decrease = itemView.findViewById(R.id.decrease_icon);
            quantity = itemView.findViewById(R.id.quantity);

        }
    }
}


