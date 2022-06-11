package com.dawaukum.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dawaukum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    TextView name,email,birthdate,gender,Identifier,number,address;

    ImageView profile_img;

    SharedPreferences prefs;
    public static final String MyPREFERENCES = "myprefs";

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        progressDialog=new ProgressDialog(UserProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        birthdate=findViewById(R.id.birthdate);
        gender=findViewById(R.id.commercial_number);
        Identifier=findViewById(R.id.opening_date_pha);
        number=findViewById(R.id.number);
        address=findViewById(R.id.address);
        profile_img=findViewById(R.id.profile_img);

        progressDialog.show();

        getUserInfo(prefs.getString("email",""));

    }


    //get User

    private void getUserInfo(String emails){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        if (documentSnapshot.getString("email").equalsIgnoreCase(emails)) {
                            if (documentSnapshot.getString("Type").equalsIgnoreCase("user")){
                                name.setText(documentSnapshot.getString("name").toString());
                                email.setText(documentSnapshot.getString("email").toString());
                                birthdate.setText(documentSnapshot.getString("birthday").toString());
                                gender.setText(documentSnapshot.getString("gender").toString());
                                Identifier.setText(documentSnapshot.getString("identifierType").toString());
                                number.setText(documentSnapshot.getString("identifierNumber").toString());
                                address.setText(documentSnapshot.getString("Address").toString());

                                SharedPreferences.Editor editor = prefs.edit();
                                try {
                                    editor.putString("imagelink", documentSnapshot.getString("imagelink").toString());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                editor.commit();

                                try {

                                    Glide.with(UserProfileActivity.this).load(documentSnapshot.getString("imagelink").toString()).centerCrop().placeholder(R.drawable.logo).into(profile_img);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }

                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

}