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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.util.ArrayList;
import java.util.List;

public class SavedProperties extends Fragment {

    private List<String> mySaved;

    FirebaseUser firebaseUser;

    private RecyclerView recyclerView_saved;
    private SavedAdapter savedAdapter;
    private List<PropertyData> savedPropertyList;
    private DatabaseReference databaseReference;
    private LinearLayout noSaved;
    private ImageView btnBack;
    private Button btnBrowse;

    public SavedProperties(){

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_properties, container, false);

        btnBack = view.findViewById(R.id.back2);
        btnBrowse = view.findViewById(R.id.browse);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView_saved = view.findViewById(R.id.recyclerViewSaved);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView_saved.setLayoutManager(gridLayoutManager2);
        savedPropertyList = new ArrayList<>();
        savedAdapter = new SavedAdapter(getContext(), savedPropertyList);
        recyclerView_saved.setAdapter(savedAdapter);


        noSaved = view.findViewById(R.id.noSaved);
        //noSaved.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Properties");

        mySaved();

//            int str = savedAdapter.getItemCount();
//
//            noSaved.setText(str);

//            if(savedAdapter.getItemCount() == 0)
//            {
//                noSaved.setVisibility(View.VISIBLE);
//            }

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


    private void mySaved() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saved").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mySaved = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mySaved.add(snapshot.getKey());
                }
                System.out.println(mySaved);

                readSaved();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readSaved() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                savedPropertyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PropertyData post = snapshot.getValue(PropertyData.class);
                    String postKey = snapshot.getKey();
                    post.setKey(snapshot.getKey());

                    for (String id : mySaved) {
                        if (postKey.equals(id)) {
                            savedPropertyList.add(post);
                        }
                    }
                }
                //            if(savedAdapter.getItemCount() == 0)
//            {
//                noSaved.setVisibility(View.VISIBLE);
//            }
                savedAdapter.notifyDataSetChanged();
                int no = savedAdapter.getItemCount();

                if(no == 0){
                    noSaved.setVisibility(View.VISIBLE);
                }else{
                    noSaved.setVisibility(View.GONE);
                }


//                String sved = String.valueOf(no);
//                noSaved.setText(sved);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

