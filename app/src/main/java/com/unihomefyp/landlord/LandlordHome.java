package com.unihomefyp.landlord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.unihomefyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.UserWelcome;

public class LandlordHome extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_home);

        tvName = findViewById(R.id.txtName);
        tvEmail = findViewById(R.id.txtEmail);


        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Landlord");

        mDatabaseUsers.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String email2 = (String) snapshot.child("email").getValue();

                tvName.setText(name);
                tvEmail.setText(email2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandlordHome.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });






//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intToSignUp = new Intent(LandlordHome.this, LandlordLogin.class);
//                startActivity(intToSignUp);
//            }
//        });
////
////
//        btnMyProperties.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intToMyProp = new Intent(LandlordHome.this, MainActivity.class);
//                startActivity(intToMyProp);
//            }
//        });
//
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToSignUp = new Intent(LandlordHome.this, UserWelcome.class);
        startActivity(intToSignUp);
    }

    public void startProfile(View view) {
        Intent intToProfile = new Intent(LandlordHome.this, UserProfile.class);
        startActivity(intToProfile);

    }

    public void startEnq(View view) {
        Intent intToEnq = new Intent(LandlordHome.this, Enquiries.class);
        startActivity(intToEnq);

    }
//
//
    public void startProperties(View view) {
        Intent intToMyProp = new Intent(LandlordHome.this, MainActivity.class);
        startActivity(intToMyProp);
    }
}