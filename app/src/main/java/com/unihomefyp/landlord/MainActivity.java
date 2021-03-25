package com.unihomefyp.landlord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CheckBox chbWifi, chbParking;
    TextView textView;
    private FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    List<PropertyData> myPropertyList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText txtSearch;
    MyAdapter myAdapter;
    Button btnApply, btnClear;
    String ch1, ch2, ch3, ch4, ch5, ch6;
    private LinearLayout noSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.text);
        txtSearch = findViewById(R.id.txtSearch);
        noSaved = findViewById(R.id.noProperties);




        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1 );
        mRecyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items...");

        myPropertyList = new ArrayList<>();

        myAdapter = new MyAdapter(MainActivity.this,myPropertyList);
        mRecyclerView.setAdapter(myAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Properties");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(mRecyclerView);


        loadUI();

        //.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())
//        progressDialog.show();




        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });



    }

    private void loadUI() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPropertyList.clear();


                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
                    propertyData.setKey(itemSnapshot.getKey());
                    propertyData.setName((String) itemSnapshot.child("name").getValue());
                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());

                    String property_uid = (String) itemSnapshot.child("uid").getValue();

                    //(propertyData.getActive().equals("yes"))&&
                    if ((property_uid.equals(mAuth.getCurrentUser().getUid())) ) {
                        myPropertyList.add(propertyData);
                    }
                }

                myAdapter.notifyDataSetChanged();
                int no = myAdapter.getItemCount();

                if(no == 0){
                    noSaved.setVisibility(View.VISIBLE);
                }else{
                    noSaved.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }


    private void filter(String text) {

        ArrayList<PropertyData> filterList = new ArrayList<>();

        for(PropertyData item: myPropertyList){

            if(item.getName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(item);
            }

        }
        myAdapter.filteredList(filterList);
        //MainActivity.this.getCurrentFocus().clearFocus();

    }


    public void btn_UploadActivity(View view) {

        startActivity(new Intent(this, UploadProperty.class));

    }

    public void btnBack(View view) {
        Intent intBack = new Intent(MainActivity.this, LandlordHome.class);
        startActivity(intBack);
    }


}