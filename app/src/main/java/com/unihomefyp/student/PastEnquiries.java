package com.unihomefyp.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.R;
import com.unihomefyp.models.EnquiryModel;

import java.util.ArrayList;
import java.util.List;

public class PastEnquiries extends AppCompatActivity {

    TextView textView, sEmail;
    private FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    List<EnquiryModel> myEnquiryList2;
    private DatabaseReference databaseReference;
    PastEnquiriesAdapter myAdapter3;
    private ValueEventListener eventListener;
    Button btnBack;

    private DatabaseReference mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_enquiries);


        btnBack = findViewById(R.id.toProfile);

        mAuth = FirebaseAuth.getInstance();

        sEmail = findViewById(R.id.sEmail2);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerEnquiry2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PastEnquiries.this,1 );
        mRecyclerView.setLayoutManager(gridLayoutManager);

        myEnquiryList2 = new ArrayList<>();

        myAdapter3 = new PastEnquiriesAdapter(PastEnquiries.this,myEnquiryList2);
        mRecyclerView.setAdapter(myAdapter3);
        databaseReference = FirebaseDatabase.getInstance().getReference("Enquiries");

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseDatabase.getInstance().getReference().child("Student");
        mUser.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String email2 = (String) snapshot.child("email").getValue();
                sEmail.setText(email2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PastEnquiries.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent ToProfile = new Intent(PastEnquiries.this, StudentProfile.class);
                startActivity(ToProfile);
            }
        });

        loadUI();
    }

    private void loadUI() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myEnquiryList2.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    EnquiryModel enquiryData = itemSnapshot.getValue(EnquiryModel.class);

                    String strEmail = (String) itemSnapshot.child("sEmail").getValue();

                    //(propertyData.getActive().equals("yes"))&&
                    if ((strEmail.equals(sEmail.getText().toString())) ) {
                        myEnquiryList2.add(enquiryData);
                    }
                }

                myAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}