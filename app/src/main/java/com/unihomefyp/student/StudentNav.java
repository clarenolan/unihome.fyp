package com.unihomefyp.student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.core.OrderBy;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.unihomefyp.BottomNavigationViewHelper;
import com.unihomefyp.R;
import com.unihomefyp.UserWelcome;
import com.unihomefyp.models.PropertyData;
import com.unihomefyp.models.ReviewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentNav extends AppCompatActivity {

    private static final String TAG = "StudentHome";
    private static final int ACTIVITY_NUM = 0;

    CheckBox chbWifi, chbParking, chbSem1, chbSem2, chbAcademic, chbSummer;
    TextView textView;
    private FirebaseAuth mAuth;
    List<PropertyData> myPropertyList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText txtSearch;
    Button btnApply, btnClear, btnGo;
    String ch1, ch2, ch3, ch4, ch5, ch6;
    Spinner spRental, spCounties, spBeds, spRent;
    private PropertyAdapter adapter;
    private RecyclerView recyclerView;
    private Context mContext = StudentNav.this;
    NestedScrollView filters;
    private LinearLayout noMatches;

    FirebaseFirestore db;

    LinearLayoutManager linearLayoutManager;
    SharedPreferences mSharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_nav);


        mAuth = FirebaseAuth.getInstance();
        chbWifi = findViewById(R.id.cbWifi2);
        chbParking = findViewById(R.id.cbParking2);
        spRental = findViewById(R.id.spRental);
        spCounties = findViewById(R.id.spCounty);
        textView = findViewById(R.id.text);
//        txtSearch = findViewById(R.id.txtSearch2);
        btnGo = findViewById(R.id.btnGo);
        btnApply = findViewById(R.id.btnApply2);
        btnClear = findViewById(R.id.btnClear2);
        filters = findViewById(R.id.collapsing);
        spBeds = findViewById(R.id.spMinBeds);
        spRent = findViewById(R.id.maxPrice);
        noMatches = findViewById(R.id.noMatches);


//        mSharedPref = getSharedPreferences("Sort Settings", MODE_PRIVATE);
        //default value
//        String mSorting = mSharedPref.getString("Sort", "lowest");

//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(StudentNav.this, 1);


        recyclerView = findViewById(R.id.recyclerView2);




        myPropertyList = new ArrayList<>();

        adapter = new PropertyAdapter(StudentNav.this, myPropertyList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Properties");
        db = FirebaseFirestore.getInstance();

            linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setReverseLayout(true);
//            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.setLayoutManager(gridLayoutManager2);

        loadUI();



////
//        if (mSorting.equals("lowest")){
//            linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setReverseLayout(true);
//            linearLayoutManager.setStackFromEnd(true);
//            recyclerView.setLayoutManager(linearLayoutManager);
//        } else if(mSorting.equals("highest")){
//            linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setReverseLayout(false);
//            linearLayoutManager.setStackFromEnd(false);
//            recyclerView.setLayoutManager(linearLayoutManager);
////            recyclerView.setLayoutManager(linearLayoutManager);
//        }

//        recyclerView.setLayoutManager(linearLayoutManager);



//
//
//        Collections.sort(myPropertyList, new Comparator<PropertyData>() {
//        @Override
//        public int compare(PropertyData lhs, PropertyData rhs) {
//            return lhs.title(rhs.title);
//        }
//    });







        //Setting options for counties spinner
        List<String> counties = new ArrayList<>();

        counties.add("Select your county...");
        counties.add("Antrim");
        counties.add("Armagh");
        counties.add("Carlow");
        counties.add("Cavan");
        counties.add("Cork");
        counties.add("Derry");
        counties.add("Donegal");
        counties.add("Down");
        counties.add("Dublin");
        counties.add("Fermanagh");
        counties.add("Galway");
        counties.add("Kerry");
        counties.add("Kildare");
        counties.add("Kilkenny");
        counties.add("Laois");
        counties.add("Leitrim");
        counties.add("Longford");
        counties.add("Louth");
        counties.add("Mayo");
        counties.add("Meath");
        counties.add("Monaghan");
        counties.add("Offaly");
        counties.add("Roscommon");
        counties.add("Sligo");
        counties.add("Tipperary");
        counties.add("Tyrone");
        counties.add("Mayo");
        counties.add("Waterford");
        counties.add("Westmeath");
        counties.add("Wexford");
        counties.add("Wicklow");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(StudentNav.this, android.R.layout.simple_spinner_item, counties);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spCounties.setAdapter(dataAdapter);

        //Setting options for academic year
        List<String> categories4 = new ArrayList<>();


        categories4.add("Academic Year");
        categories4.add("Semester 1");
        categories4.add("Semester 2");
        categories4.add("Summer Period");

        ArrayAdapter<String> dataAdapter4;
        dataAdapter4 = new ArrayAdapter(StudentNav.this, R.layout.spinner_item, categories4);
        dataAdapter4.setDropDownViewResource(R.layout.spinner_item);
        spRental.setAdapter(dataAdapter4);

        //Setting options for academic year
        List<String> minBeds = new ArrayList<>();


        minBeds.add("1");
        minBeds.add("2");
        minBeds.add("3");
        minBeds.add("4");
        minBeds.add("5");
        minBeds.add("6");

        ArrayAdapter<String> bedsAdapter;
        bedsAdapter = new ArrayAdapter(StudentNav.this, R.layout.spinner_item, minBeds);
        bedsAdapter.setDropDownViewResource(R.layout.spinner_item);
        spBeds.setAdapter(bedsAdapter);

        //Setting options for academic year
        List<String> prices = new ArrayList<>();


        prices.add("80");
        prices.add("90");
        prices.add("100");
        prices.add("150");
        prices.add("200");
        prices.add("250");

        ArrayAdapter<String> priceAdapter;
        priceAdapter = new ArrayAdapter(StudentNav.this, R.layout.spinner_item, prices);
        priceAdapter.setDropDownViewResource(R.layout.spinner_item);
        spRent.setAdapter(priceAdapter);



        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spCounties.getSelectedItem().toString() == "Select your county...") {
                    spCounties.requestFocus();
                    Toast.makeText(getApplicationContext(),"Please select your county", Toast.LENGTH_SHORT).show();
                } else {
                    myPropertyList.clear();
                    loadByCounty();
                }

            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spCounties.getSelectedItem().toString() == "Select your county...") {
                    spCounties.requestFocus();
                    Toast.makeText(getApplicationContext(),"Please selct your county", Toast.LENGTH_SHORT).show();
                } else {
                    myPropertyList.clear();
                    fetch();
                }


            }
        });

//        txtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//            }
//        });

        //Load method to reset UI
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUI();
                spRental.setSelection(0);
                spCounties.setSelection(0);
                spRent.setSelection(0);
                spBeds.setSelection(0);
                filters.setVisibility(View.GONE);
            }
        });

        setupBottomNavigationView();

    }

    private void loadByCounty() {

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Load new propertyData model
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
                    propertyData.setKey(itemSnapshot.getKey());
                    propertyData.setName((String) itemSnapshot.child("name").getValue());
                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());
                    propertyData.setCounty((String) itemSnapshot.child("county").getValue());

                    String countySelected = (String) itemSnapshot.child("county").getValue();
                    //Load only active properties
                    if (propertyData.getActive().equals("Yes") && spCounties.getSelectedItem().toString().equals(countySelected)) {
                        myPropertyList.add(propertyData);
                    }
                }
                adapter.notifyDataSetChanged();
                int no = adapter.getItemCount();

                if(no == 0){
                    noMatches.setVisibility(View.VISIBLE);
                }else{
                    noMatches.setVisibility(View.GONE);
                }
                // progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  progressDialog.dismiss();
            }
        });
    }

    //Reset UI
    private void loadUI() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clear selected values
                myPropertyList.clear();
                if (chbWifi.isChecked()) {
                    chbWifi.setChecked(false);
                }
                if (chbParking.isChecked()) {
                    chbParking.setChecked(false);
                }
                spRental.setSelection(0);
                spCounties.setSelection(0);
                spRent.setSelection(0);
                spBeds.setSelection(0);

                //Load new propertyData model
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
                    propertyData.setKey(itemSnapshot.getKey());
                    propertyData.setName((String) itemSnapshot.child("name").getValue());
                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());
                    //Load only active properties
                    if (propertyData.getActive().equals("Yes")) {
                        myPropertyList.add(propertyData);
                    }
                }
                adapter.notifyDataSetChanged();
                int no = adapter.getItemCount();

                if(no == 0){
                    noMatches.setVisibility(View.VISIBLE);
                }else{
                    noMatches.setVisibility(View.GONE);
                }
                // progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  progressDialog.dismiss();
            }
        });
    }

    public void signOutStudent(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToWelcome = new Intent(StudentNav.this, UserWelcome.class);
        startActivity(intToWelcome);
    }


//    //Sort from lowest to highest
//    private void loadLowToHigh() {
//        eventListener = databaseReference.orderByChild("price").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //Clear selected values
//                myPropertyList.clear();
//                if (chbWifi.isChecked()) {
//                    chbWifi.setChecked(false);
//                }
//                if (chbParking.isChecked()) {
//                    chbParking.setChecked(false);
//                }
//                spRental.setSelection(0);
//                spCounties.setSelection(0);
////                spRent.setSelection(0);
//                spBeds.setSelection(0);
//
//                //Load new propertyData model
//                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
//                    propertyData.setKey(itemSnapshot.getKey());
//                    propertyData.setName((String) itemSnapshot.child("name").getValue());
//                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
//                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
//                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());
//                    //Load only active properties
//                    if (propertyData.getActive().equals("Yes")) {
//                        myPropertyList.add(propertyData);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                // progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //  progressDialog.dismiss();
//            }
//        });
//    }
//
    //Sort from highest to lowest
    private void loadHighToLow() {
        eventListener = databaseReference.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clear selected values
                myPropertyList.clear();
//                if (chbWifi.isChecked()) {
//                    chbWifi.setChecked(false);
//                }
//                if (chbParking.isChecked()) {
//                    chbParking.setChecked(false);
//                }
//                spRental.setSelection(0);
//                spCounties.setSelection(0);
////                spRent.setSelection(0);
//                spBeds.setSelection(0);

                //Load new propertyData model
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
                    propertyData.setKey(itemSnapshot.getKey());
                    propertyData.setName((String) itemSnapshot.child("name").getValue());
                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());
                    //Load only active properties
                    if (propertyData.getActive().equals("Yes")) {
                        myPropertyList.add(propertyData);
                    }
                }
//                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//            linearLayoutManager.setReverseLayout(true);
//            linearLayoutManager.setStackFromEnd(true);
//            recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
                // progressDialog.dismiss();
                int no = adapter.getItemCount();

                if(no == 0){
                    noMatches.setVisibility(View.VISIBLE);
                }else{
                    noMatches.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  progressDialog.dismiss();
            }
        });
    }


    private void fetch() {

        eventListener = databaseReference.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    PropertyData propertyData = itemSnapshot.getValue(PropertyData.class);
                    propertyData.setKey(itemSnapshot.getKey());
                    propertyData.setName((String) itemSnapshot.child("name").getValue());
                    propertyData.setAddress((String) itemSnapshot.child("address").getValue());
                    propertyData.setPrice((String) itemSnapshot.child("price").getValue());
                    propertyData.setImageUrl((String) itemSnapshot.child("imageUrl").getValue());



                    String countySelected = (String) itemSnapshot.child("county").getValue();
                    String rentalPeriod = (String) itemSnapshot.child("period").getValue();
                    String strWifi = (String) itemSnapshot.child("wifi").getValue();
                    String strParking = (String) itemSnapshot.child("parking").getValue();
                    String minBeds = (String) itemSnapshot.child("availableBeds").getValue();
                    String rentPrice = (String) itemSnapshot.child("price").getValue();

                    int numberBeds = Integer.parseInt(minBeds);
                    String selectedBeds=spBeds.getSelectedItem().toString();
                    int intSelected = Integer.parseInt(selectedBeds);

                    int propPrice = Integer.parseInt(rentPrice);
                    String maxPrice = spRent.getSelectedItem().toString();
                    int priceSelected = Integer.parseInt(maxPrice);


                    String property_uid = (String) itemSnapshot.child("uid").getValue();
                    String string = "Included";
                    String sem1 = "Semester 1";
                    String sem2 = "Semester 2";
                    String sem3 = "Academic Year";
                    String sem4 = "Summer Period";


                    //Setting variables from amenity checkboxes
                    if (chbWifi.isChecked()) {
                        ch1 = "Included";
                    } else {
                        ch1 = "Not Included";
                    }
                    if (chbParking.isChecked()) {
                        ch2 = "Included";
                    } else {
                        ch2 = "Not Included";
                    }

                    //Wifi = yes, Parking = no, County = Cork, Period = Sem1
                    if ((ch1.equals(strWifi))&& (propPrice < priceSelected) && (numberBeds > intSelected ) && (ch2.equals(strParking))&& (spRental.getSelectedItem().toString().equals(rentalPeriod))  && (itemSnapshot.child("wifi").getValue()).equals(string)  && spCounties.getSelectedItem().toString().equals(countySelected)  ) {
                        myPropertyList.add(propertyData);

                        //Wifi = yes, Parking = no, County = Cork, Period = Sem2
//                    } else if ((ch1.equals("yes")) && (ch2.equals("no")) && (spRental.getSelectedItem().toString().equals(sem2)) && (itemSnapshot.child("wifi").getValue()).equals(string) && (itemSnapshot.child("period").getValue()).equals(sem2) && spCounties.getSelectedItem().toString().equals(countySelected) ) {
//                        myPropertyList.add(propertyData);
//
//                        //Wifi = yes, Parking = no, County = Cork, Period = Academic year
//                    } else if ((ch1.equals("yes")) && (ch2.equals("no")) && (spRental.getSelectedItem().toString().equals(sem3)) && (itemSnapshot.child("wifi").getValue()).equals(string) && (itemSnapshot.child("period").getValue()).equals(sem3) && spCounties.getSelectedItem().toString().equals(countySelected) ) {
//                        myPropertyList.add(propertyData);
//
//                        //Wifi = yes, Parking = no, County = Cork, Period = Summer Period
//                    } else if ((ch1.equals("yes")) && (ch2.equals("no")) && (spRental.getSelectedItem().toString().equals(sem4)) && (itemSnapshot.child("wifi").getValue()).equals(string) && (itemSnapshot.child("period").getValue()).equals(sem4) && spCounties.getSelectedItem().toString().equals(countySelected) ) {
//                        myPropertyList.add(propertyData);
//
//
//                    } else if ((ch1.equals("no")) && (ch2.equals("no")) && (spRental.getSelectedItem().toString().equals(sem1)) && (itemSnapshot.child("period").getValue()).equals(sem1) && spCounties.getSelectedItem().toString().equals(countySelected) ) {
//                        myPropertyList.add(propertyData);


                    }

                }
                adapter.notifyDataSetChanged();
                //  progressDialog.dismiss();
                int no = adapter.getItemCount();

                if(no == 0){
                    noMatches.setVisibility(View.VISIBLE);
                }else{
                    noMatches.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  progressDialog.dismiss();
            }
        });


    }

    //Search Bar
//    private void filter(String text) {
//        ArrayList<PropertyData> filterList = new ArrayList<>();
//
//        for (PropertyData item : myPropertyList) {
//            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
//                filterList.add(item);
//            }
//        }
//        adapter.filteredList(filterList);
//
//    }


    //BottomNavigationView setup

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void showFilters(View view) {
        if (filters.getVisibility() == View.VISIBLE) {
            filters.setVisibility(View.GONE);
        } else {
            filters.setVisibility(View.VISIBLE);
        }
    }

    public void sortList(View view) {
        showSortDialog();
        
    }

    private void showSortDialog() {
        String[] sortOptions = {"Lowest", "Highest"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_baseline_sort_24)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
//                            sort by lowest

//                            SharedPreferences.Editor editor = mSharedPref.edit();
//                            editor.putString("Sort" , "lowest");
//                            editor.apply();
//                            recreate();
//                            dialogInterface.dismiss();

                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setReverseLayout(false);
//                            linearLayoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(linearLayoutManager);

                            loadHighToLow();

                           // ArrayList<PropertyData> filterList = new ArrayList<>();

//                            for(PropertyData item: myPropertyList){
//                                String x = item.getPrice();
////
////                                Collections.sort(myPropertyList, new Comparator<PropertyData>() {
////                                    @Override
////                                    public int compare(PropertyData lhs, PropertyData rhs) {
////                                        return lhs.compareTo(rhs.mPrice);
////                                    }
////                                });
//
//
////                         if(item.getPrice().toLowerCase().contains(text.toLowerCase())){
////
////                                    filterList.add(item);
////                                }
//
//                            }
//                            adapter.filteredList(filterList);


                        }
                        else if(i == 1){
                            //sort by highest

//                            SharedPreferences.Editor editor = mSharedPref.edit();
//                            editor.putString("Sort" , "highest");
//                            editor.apply();
//
                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(linearLayoutManager);

                            loadHighToLow();
//                            recreate();
//
//                            ArrayList<PropertyData> filterList = new ArrayList<PropertyData>();
//
//                                Collections.sort(filterList, new Comparator<PropertyData>() {
//                                    @Override
//                                    public int compare(PropertyData lhs, PropertyData rhs) {
//                                        return lhs.getPrice().compareTo(rhs.getPrice());
//                                    }
//                                });
//
//
//
//                           adapter.notifyDataSetChanged();

                            dialogInterface.dismiss();


                        }
                    }
                });

        builder.show();
    }

}