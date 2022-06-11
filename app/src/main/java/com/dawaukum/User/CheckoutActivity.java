package com.dawaukum.User;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    RadioGroup paymentMethod;
    RadioButton cash, card;
    EditText note, address, holderName, cardNumber, code, expireDate;
    Button addPrescription, checkout;
    TextView total;
    ImageView prescriptionImg;
    LinearLayout cardLayout;

    String profil_img;
    String filePath = "";
    File file;

    public static final int GALLERY_REQUEST = 100;

    boolean needPrescription = false;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    String keys;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ArrayList<Map<String, Object>> data;

    ProgressDialog progressDialog;

    boolean checker=false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Intent intent = getIntent();

        data = (ArrayList<Map<String, Object>>) intent.getSerializableExtra("data");
        needPrescription=intent.getBooleanExtra("prescription",false);




        //Define Views
        paymentMethod = findViewById(R.id.payment_method);
        cash = findViewById(R.id.cash);
        card = findViewById(R.id.card);
        note = findViewById(R.id.note);
        address = findViewById(R.id.address);
        holderName = findViewById(R.id.holder_name);
        cardNumber = findViewById(R.id.card_number);
        code = findViewById(R.id.code);
        expireDate = findViewById(R.id.expire_date);
        addPrescription = findViewById(R.id.add_prescription);
        checkout = findViewById(R.id.checkout_btn);
        total = findViewById(R.id.total);
        prescriptionImg = findViewById(R.id.prescription_img);
        cardLayout = findViewById(R.id.card_layout);

        progressDialog=new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        total.setText(intent.getStringExtra("total"));

        address.setText(sharedpreferences.getString("address",""));

        if (true) {
            addPrescription.setVisibility(View.VISIBLE);
            prescriptionImg.setVisibility(View.VISIBLE);
        } else {
            addPrescription.setVisibility(View.GONE);
            prescriptionImg.setVisibility(View.GONE);
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardLayout.setVisibility(View.VISIBLE);
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardLayout.setVisibility(View.GONE);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // Create a reference to "mountains.jpg"
                    StorageReference mountainsRef = storageRef.child(filePath);

// Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child(filePath);

// While the file names are the same, the references point to different files
                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());

                    addorder(String.valueOf(mountainsRef.getPath().equals(mountainImagesRef.getPath())));
                }catch (Exception e){
                    e.printStackTrace();
                    addorder("");
                }


            }
        });

        addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST);


            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("OnActivityResult", "" + requestCode + ",Result " + resultCode);
        if (resultCode != RESULT_OK) {
            return;
        }
//        Uri uri = data.getData();

        if (requestCode == 100) {
            final Uri extras = Uri.parse(data.getData().buildUpon().toString());
            if (extras != null) {
                profil_img = data.getData().buildUpon().toString();
            }
        }

        try {

            filePath = getPath(CheckoutActivity.this, data.getData());
        }catch (Exception e){
            e.printStackTrace();
        }


        file = new File(filePath);




        super.onActivityResult(requestCode, resultCode, data);

    }

    public static String getPath(Activity activity, Uri selectedImaeUri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.managedQuery(selectedImaeUri, projection, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
    }


    private void addorder(String s){

        Map<String, Object> Order = new HashMap<>();
        Order.put("OrderStatus","Pending");
        Order.put("UserName",sharedpreferences.getString("name",""));
        Order.put("UserEmail",sharedpreferences.getString("email",""));
        Order.put("UserAddress",sharedpreferences.getString("address",""));
        Order.put("UserGender",sharedpreferences.getString("gender",""));
        Order.put("imagelink",s);
        Order.put("Total",total.getText().toString());
        Order.put("Drugs",data);


        ArrayList<Map<String, Object>> documents=new ArrayList<>();


        db.collection("Drugs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                for (int i=0; (data.size()-1)>= i;i++){

                                    try {

                                        Object[] keys = data.get(i).keySet().toArray();

                                        HashMap current = (HashMap<String, Object>) data.get(i).get(keys[i]);

                                        if (Integer.parseInt(document.get("Quantity").toString()) >= Integer.parseInt(current.get("Quantity").toString())) {

                                            documents.add(document.getData());

                                            checker = true;

                                        } else {

                                            Toast.makeText(CheckoutActivity.this, "Some Drugs out of stock", Toast.LENGTH_LONG).show();
                                            return;

                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }

                            }

                            if(checker){
                                db.collection("Order")
                                        .add(Order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                db.collection("Cart").document(sharedpreferences.getString("email","")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        for (int f=0; (data.size()-1)>=f;f++) {

                                                            Object[] keys = data.get(f).keySet().toArray();

                                                            HashMap current = (HashMap<String, Object>) data.get(f).get(keys[f]);

                                                            for (int j=0; documents.size()>j;j++) {

                                                                if (Integer.parseInt(documents.get(j).get("Quantity").toString()) >= Integer.parseInt(current.get("Quantity").toString())) {

                                                                    try {
                                                                        quantityCart(String.valueOf(Integer.parseInt(documents.get(j).get("Quantity").toString()) - Integer.parseInt(current.get("Quantity").toString()))
                                                                                , current.get("Drugname").toString(), current.get("DrugPrice").toString(), current.get("Prescription").toString(),
                                                                                current.get("imagelink").toString());
                                                                    }catch (Exception e){
                                                                        quantityCart(String.valueOf(Integer.parseInt(documents.get(j).get("Quantity").toString()) - Integer.parseInt(current.get("Quantity").toString()))
                                                                                , current.get("Drugname").toString(), current.get("DrugPrice").toString(), current.get("Prescription").toString(),
                                                                               "");
                                                                    }
                                                                }
                                                            }
                                                        }


                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(),"Order Submit successfully ",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(CheckoutActivity.this,UserOrdersActivity.class));
                                                        finish();

                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }else {

                                progressDialog.dismiss();

                                Toast.makeText(CheckoutActivity.this, "Some Drugs out of stock", Toast.LENGTH_LONG).show();
                                return;

                            }


                        } else {

                        }
                    }
                });




    }

    private void quantityCart(String Quantity,String drugname,String price,String Prescription,String imagelink){

        Map<String, Object> quant = new HashMap<>();

        quant.put("Quantity",Quantity);

        db.collection("Drugs").whereEqualTo("Drugname",drugname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot documentID : task.getResult() ) {

                            keys = documentID.getId();

                        }

                        db.collection("Drugs")
                                .document(keys)
                                .update(quant);

                        progressDialog.dismiss();
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



}