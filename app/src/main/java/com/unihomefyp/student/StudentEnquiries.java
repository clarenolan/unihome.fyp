package com.unihomefyp.student;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class StudentEnquiries extends Fragment {

    TextView textView, sEmail;
    private FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    List<EnquiryModel> myEnquiryList2;
    private DatabaseReference databaseReference;
    PastEnquiriesAdapter myAdapter3;
    private ValueEventListener eventListener;
    private ImageView btnBack;
    private LinearLayout noEnquiries;
    private Button btnBrowse;

    private DatabaseReference mUser;


    public StudentEnquiries() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_past_enquiries, container, false);

        mAuth = FirebaseAuth.getInstance();

        sEmail = view.findViewById(R.id.sEmail2);
        btnBack = view.findViewById(R.id.toProfile);
        noEnquiries = view.findViewById(R.id.noEnquiries);
        btnBrowse = view.findViewById(R.id.browseProp);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerEnquiry2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        myEnquiryList2 = new ArrayList<>();

        myAdapter3 = new PastEnquiriesAdapter(getContext(), myEnquiryList2);
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
                Toast.makeText(getContext(), "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });

        loadUI();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(),R.anim.slide_in, R.anim.slide_out_right);
                Intent intent = new Intent(getContext(), StudentProfile.class);
                getContext().startActivity(intent, options.toBundle());
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(),R.anim.slide_in, R.anim.slide_out_right);
                Intent intent = new Intent(getContext(), StudentNav.class);
                getContext().startActivity(intent, options.toBundle());
            }
        });

        return view;
    }

    private void loadUI() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myEnquiryList2.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    EnquiryModel enquiryData = itemSnapshot.getValue(EnquiryModel.class);

                    String strEmail = (String) itemSnapshot.child("sEmail").getValue();

                    //(propertyData.getActive().equals("yes"))&&
                    if ((strEmail.equals(sEmail.getText().toString()))) {
                        myEnquiryList2.add(enquiryData);
                    }
                }

                myAdapter3.notifyDataSetChanged();
                int no = myAdapter3.getItemCount();

                if(no == 0){
                    noEnquiries.setVisibility(View.VISIBLE);
                }else{
                    noEnquiries.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
