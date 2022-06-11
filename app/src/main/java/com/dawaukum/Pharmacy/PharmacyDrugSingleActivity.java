package com.dawaukum.Pharmacy;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dawaukum.R;
import com.dawaukum.User.CheckoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PharmacyDrugSingleActivity extends AppCompatActivity {

    ImageView drugImg;
    TextView AddEditImage;
    Button addEditBtn, deleteBtn;
    EditText title, price, quantity, description;
    RadioGroup needPrescription;
    RadioButton yesPrescription, noPrescription;

    String flag;

    ProgressDialog progressDialog;

    String profil_img;
    String filePath = "";
    File file;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    public static final int GALLERY_REQUEST = 100;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_drug_single);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        progressDialog=new ProgressDialog(PharmacyDrugSingleActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        //Define Views
        drugImg = findViewById(R.id.drug_img);
        AddEditImage = findViewById(R.id.edit_img);
        addEditBtn = findViewById(R.id.add_edit_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        title = findViewById(R.id.drug_title);
        price = findViewById(R.id.drug_price);
        quantity = findViewById(R.id.drug_quantity);
        description = findViewById(R.id.drug_description);
        needPrescription = findViewById(R.id.drug_need_prescription);
        yesPrescription = findViewById(R.id.yes);
        noPrescription = findViewById(R.id.no);


        addEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                try {
                    // Create a reference to "mountains.jpg"
                    StorageReference mountainsRef = storageRef.child(filePath);

// Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child(filePath);

// While the file names are the same, the references point to different files
                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());

                    if (yesPrescription.isChecked()) {
                        addDrug(description.getText().toString(), quantity.getText().toString(), title.getText().toString(), price.getText().toString(), "yes", String.valueOf(mountainsRef.getPath().equals(mountainImagesRef.getPath())));
                    } else if (noPrescription.isChecked()) {
                        addDrug(description.getText().toString(), quantity.getText().toString(), title.getText().toString(), price.getText().toString(), "no", String.valueOf(mountainsRef.getPath().equals(mountainImagesRef.getPath())));
                    }
                }catch (Exception e){
                    if (yesPrescription.isChecked()) {
                        addDrug(description.getText().toString(), quantity.getText().toString(), title.getText().toString(), price.getText().toString(), "yes", "");
                    } else if (noPrescription.isChecked()) {
                        addDrug(description.getText().toString(), quantity.getText().toString(), title.getText().toString(), price.getText().toString(), "no", "");
                    }
                }



            }
        });

        drugImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST);

                   // false

            }
        });


    }


    private void addDrug(String desc,String Quantity,String drugname,String price,String Prescription,String imagelink){
        // Create a new Drug
        Map<String, Object> Drug = new HashMap<>();
        Drug.put("Drugname",drugname);
        Drug.put("Price", price+"JD");
        Drug.put("Prescription", Prescription);
        Drug.put("Quantity",Quantity);
        Drug.put("imagelink",imagelink);
        Drug.put("DrugDesc",desc);
        Drug.put("Pharmacyname",sharedpreferences.getString("name",""));

        db.collection("Drugs").document(drugname).set(Drug).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                startActivity(new Intent(PharmacyDrugSingleActivity.this,PharmacyDrugsActivity.class));
                finish();
                Toast.makeText(PharmacyDrugSingleActivity.this,"Drug Added Successfully ",Toast.LENGTH_LONG).show();
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
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

            filePath = getPath(PharmacyDrugSingleActivity.this, data.getData());
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



}