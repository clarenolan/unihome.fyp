package com.unihomefyp.landlord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.CommentFragment;
import com.unihomefyp.PropertyDetailViewModel;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;
import com.unihomefyp.student.PropertyAdapter;

import dmax.dialog.SpotsDialog;

public class LandlordPropertyDetail extends AppCompatActivity {

    private PropertyDetailViewModel propertyDetailViewModel;

    TextView propertyDescription, rentalPeriod, propertyName, propertyAddress, rentPrice, bathrooms, tvEmail, studentName;
    ImageView propertyImage, ivWifi, ivBins, ivElectricity, ivHeating, ivTV, ivKitchen, ivWashing, ivDryer, ivParking, ivDish,imgWifiBill;
    String key = "";
    String imageUrl = "";
    Button btnDeleteProperty, btnUpdateProperty, btnEnquire, btnShowReview;
    String string = "yes";
    FloatingActionButton btnRating;
    RatingBar ratingBar, avgRating;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Context mContext;
    private SpotsDialog waitingDialog;
    private PropertyAdapter propertyAdapter;

    private String property_key = null;
    private DatabaseReference mDatabase, ratingTable, mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_property_detail);
        initViews();
//        propertyAdapter.getMyReviewList().observe(this, reviewModel -> {
//            submitRatingtoFirebase(reviewModel);
//                });

        propertyName = (TextView) findViewById(R.id.txtPropertyName2);
        propertyDescription = (TextView) findViewById(R.id.txtDescription2);
        propertyAddress = (TextView) findViewById(R.id.txtAddress2);
        bathrooms = (TextView) findViewById(R.id.txtBathrooms2);
        rentPrice = (TextView) findViewById(R.id.txtPrice2);
        propertyImage = (ImageView) findViewById(R.id.ivImage2);
        btnEnquire = (Button) findViewById(R.id.btnEnquire2);
        btnUpdateProperty = (Button) findViewById(R.id.btnUpdate2);
        rentalPeriod = (TextView) findViewById(R.id.txtRentalPeriod2);
        ivWifi = (ImageView) findViewById(R.id.imgWifi2);
        imgWifiBill = (ImageView) findViewById(R.id.imgWifiBill2);
        ivBins = (ImageView) findViewById(R.id.imgBins2);
        ivElectricity = (ImageView) findViewById(R.id.imgElectricity2);
        ivHeating = (ImageView) findViewById(R.id.imgHeating2);
        ivTV = (ImageView) findViewById(R.id.imgTV2);
        ivKitchen = (ImageView) findViewById(R.id.imgKitcen2);
        ivWashing = (ImageView) findViewById(R.id.imgWashingMachine2);
        ivDryer = (ImageView) findViewById(R.id.imgDryer2);
        ivDish = (ImageView) findViewById(R.id.imgDish2);
        ivParking = (ImageView) findViewById(R.id.imgParking2);
        ivWifi.setBackgroundResource(R.drawable.tick);
        imgWifiBill.setBackgroundResource(R.drawable.tick);
        btnRating = findViewById(R.id.btnRating2);
        btnShowReview = findViewById(R.id.btnShowReview2);
        studentName = findViewById(R.id.studentName2);
        avgRating = findViewById(R.id.avgRating2);


        tvEmail = (TextView) findViewById(R.id.tvlEmail2);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Properties");
        ratingTable = FirebaseDatabase.getInstance().getReference().child("Reviews");
        imageUrl = getIntent().getExtras().getString("Image");
        property_key = getIntent().getExtras().getString("PropertyID");
        mAuth = FirebaseAuth.getInstance();



        String user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseDatabase.getInstance().getReference().child("Student");
        mUser.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String email2 = (String) snapshot.child("email").getValue();
                String number2 = (String) snapshot.child("phoneNo").getValue();

                studentName.setText(name);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandlordPropertyDetail.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }
        });



        btnShowReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentFragment commentFragment = CommentFragment.getInstance();
                commentFragment.show(getSupportFragmentManager(),"Comment Fragment");
                Bundle bundle = new Bundle();
                bundle.putString("propId", property_key);
                commentFragment.setArguments(bundle);
            }
        });

        btnEnquire.setVisibility(View.VISIBLE);

        //Update and Delete buttons hidden for students
        btnUpdateProperty.setVisibility(View.INVISIBLE);
        //Update Property Activity
        btnUpdateProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent(LandlordPropertyDetail.this, UpdateProperty.class);
                updateIntent.putExtra("PropertyID",property_key);
                startActivity(updateIntent);
                finish();
            }
        });



//        btnDeleteProperty.setVisibility(View.INVISIBLE);
//        //Delete Property
//        btnDeleteProperty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDatabase.child(property_key).removeValue();
//                Toast.makeText(getApplicationContext(), "Property Deleted", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        });


        mDatabase.child(property_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PropertyData propertyData = snapshot.getValue(PropertyData.class);

                String property_name = (String) snapshot.child("name").getValue();
                //String property_desc = (String) snapshot.child("county").getValue();
                String property_address = (String) snapshot.child("address").getValue();
                String property_price = (String) snapshot.child("price").getValue();
                String property_uid = (String) snapshot.child("uid").getValue();
                String property_image = (String) snapshot.child("imageUrl").getValue();
                String property_wifi = (String) snapshot.child("wifi").getValue();
                String property_bins = (String) snapshot.child("bins").getValue();
                String property_elec = (String) snapshot.child("electricity").getValue();
                String property_heat = (String) snapshot.child("heating").getValue();
                String property_wash = (String) snapshot.child("washing").getValue();
                String property_dryer = (String) snapshot.child("dryer").getValue();
                String property_parking = (String) snapshot.child("parking").getValue();
                String property_dish = (String) snapshot.child("dish").getValue();
                String property_tv = (String) snapshot.child("tv").getValue();
                String property_kitchen = (String) snapshot.child("kitchen").getValue();
                String property_beds = (String) snapshot.child("beds").getValue();
                String property_availableBeds = (String) snapshot.child("availableBeds").getValue();
                String property_bathrooms = (String) snapshot.child("bathrooms").getValue();
                String property_period = (String) snapshot.child("period").getValue();
                String wifi_bill = (String) snapshot.child("wifiBill").getValue();


                    propertyName.setText(property_name);
                    propertyDescription.setText(property_beds + " bedroom house with " + property_availableBeds + " bedrooms available to rent.");
                    rentalPeriod.setText("Rental Period: " + property_period);
                    bathrooms.setText("Bathrooms: " + property_bathrooms);
                    propertyAddress.setText(property_address);
                    rentPrice.setText(property_price);
                    Glide.with(getApplicationContext())
                            .load(property_image).into(propertyImage);


                String string = "Not Included";

                if (property_wifi.equals(string)) {
                    ivWifi.setBackgroundResource(R.drawable.cross);
                }

                if (property_bins.equals(string)) {
                    ivBins.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_heat.equals(string)) {
                    ivElectricity.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_wash.equals(string)) {
                    ivWashing.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_dryer.equals(string)) {
                    ivDryer.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_dish.equals(string)) {
                    ivDish.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_parking.equals(string)) {
                    ivParking.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_elec.equals(string)) {
                    ivElectricity.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_tv.equals(string)) {
                    ivTV.setBackground(getDrawable(R.drawable.cross));
                }

                if (property_kitchen.equals(string)) {
                    ivKitchen.setBackground(getDrawable(R.drawable.cross));
                }

                if (wifi_bill.equals(string)) {
                    imgWifiBill.setBackgroundResource(R.drawable.cross);
                }



                //Update and delete buttons only visible if the landlord logged in matches the landlord id in propertyData
                if (mAuth.getCurrentUser().getUid().equals(property_uid)) {
                    // btnDeleteProperty.setVisibility(View.VISIBLE);
                    btnUpdateProperty.setVisibility(View.VISIBLE);
                    btnEnquire.setVisibility(View.INVISIBLE);
                    btnRating.setVisibility(View.INVISIBLE);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        propertyAdapter.getMyReviewList().observe(this, reviewModel -> {
//            submitRatingToFirebase(reviewModel);
//        });



        //     getRatingFood(property_key);

    }

    private void initViews() {
        waitingDialog = (SpotsDialog) new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
    }

//    private void submitRatingtoFirebase(ReviewModel reviewModel) {
//        waitingDialog.show();
//        ratingTable.child(property_key).push().setValue(reviewModel).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                addRatingToProperty(reviewModel.getRatingValue());
//            }
//            waitingDialog.dismiss();
//        });
//    }

    //   private void addRatingToProperty(float ratingValue) {
//        ratingTable.child(property_key))
//    }

//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(mAuth.getCurrentUser().getUid()).exists())
//                {
//                    //Remove old value
//                    ratingTable.child(mAuth.getCurrentUser().getUid()).removeValue();
//                    //Update new value
//                    ratingTable.child(mAuth.getCurrentUser().getUid()).setValue(reviewModel);
//                }
//                else{
//                    //Upload new Value
//                    ratingTable.child(mAuth.getCurrentUser().getUid()).setValue(reviewModel);
//                }
//                Toast.makeText(PropertyDetail.this, "Review posted!", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }

//    private void getRatingFood(String property_key){
//        Query propertyRating = ratingTable.orderByChild(property_key);
//        propertyRating.addValueEventListener(new ValueEventListener() {
//            float sum;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot postSnapshot:snapshot.getChildren())
//                {
//                    ReviewModel item = postSnapshot.getValue(ReviewModel.class);
//                    sum =  Float.parseFloat(String.valueOf(item.getRatingValue2()));
//                }
//                avgRating.setRating(sum);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }



}
