package com.unihomefyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.landlord.MainActivity;
import com.unihomefyp.landlord.UpdateProperty;
import com.unihomefyp.models.PropertyData;
import com.unihomefyp.models.ReviewModel;
import com.unihomefyp.student.MyDialogFragment;
import com.unihomefyp.student.PropertyAdapter;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class PropertyDetail extends AppCompatActivity {


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

//        private void showDialogRating() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Property Rating");
//        builder.setMessage("Please fill in");
//
//        View itemView = LayoutInflater.from(PropertyDetail.this).inflate(R.layout.layout_rating, null);
//
//        RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
//        EditText etReview = itemView.findViewById(R.id.etReview);
//
//        builder.setView(itemView);
//
//        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
//            dialogInterface.dismiss();
//        });
//
//        builder.setPositiveButton("OK", (dialogInterface, i) -> {
//            ReviewModel reviewModel = new ReviewModel();
//            reviewModel.setPropertyID(property_key);
//            reviewModel.setName(studentName.getText().toString());
//            reviewModel.setUid(mAuth.getCurrentUser().getUid());
//            reviewModel.setReview(etReview.getText().toString());
//            reviewModel.setRatingValue(ratingBar.getRating());
//            Map<String,Object>serverTimeStamp = new HashMap<>();
//            serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
//            reviewModel.setReviewTimestamp(serverTimeStamp);
//
//            propertyAdapter.setReviewModel(reviewModel);
//
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        initViews();
//        propertyAdapter.getMyReviewList().observe(this, reviewModel -> {
//            submitRatingtoFirebase(reviewModel);
//                });

        propertyName = (TextView) findViewById(R.id.txtPropertyName);
        propertyDescription = (TextView) findViewById(R.id.txtDescription);
        propertyAddress = (TextView) findViewById(R.id.txtAddress);
        bathrooms = (TextView) findViewById(R.id.txtBathrooms);
        rentPrice = (TextView) findViewById(R.id.txtPrice);
        propertyImage = (ImageView) findViewById(R.id.ivImage);
        btnEnquire = (Button) findViewById(R.id.btnEnquire);
        btnDeleteProperty = (Button) findViewById(R.id.btnDelete);
        btnUpdateProperty = (Button) findViewById(R.id.btnUpdate);
        rentalPeriod = (TextView) findViewById(R.id.txtRentalPeriod);
        ivWifi = (ImageView) findViewById(R.id.imgWifi);
        imgWifiBill = (ImageView) findViewById(R.id.imgWifiBill);
        ivBins = (ImageView) findViewById(R.id.imgBins);
        ivElectricity = (ImageView) findViewById(R.id.imgElectricity);
        ivHeating = (ImageView) findViewById(R.id.imgHeating);
        ivTV = (ImageView) findViewById(R.id.imgTV);
        ivKitchen = (ImageView) findViewById(R.id.imgKitcen);
        ivWashing = (ImageView) findViewById(R.id.imgWashingMachine);
        ivDryer = (ImageView) findViewById(R.id.imgDryer);
        ivDish = (ImageView) findViewById(R.id.imgDish);
        ivParking = (ImageView) findViewById(R.id.imgParking);
        ivWifi.setBackgroundResource(R.drawable.tick);
        imgWifiBill.setBackgroundResource(R.drawable.tick);
        btnRating = findViewById(R.id.btnRating);
        btnShowReview = findViewById(R.id.btnShowReview);
        studentName = findViewById(R.id.studentName);
        avgRating = findViewById(R.id.avgRating);


        tvEmail = (TextView) findViewById(R.id.tvlEmail);


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
                Toast.makeText(PropertyDetail.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRating();
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
                Intent updateIntent = new Intent(PropertyDetail.this, UpdateProperty.class);
                finish();
                updateIntent.putExtra("PropertyID",property_key);
                startActivity(updateIntent);
            }
        });



        btnDeleteProperty.setVisibility(View.INVISIBLE);
        //Delete Property
        btnDeleteProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(property_key).removeValue();
                Toast.makeText(getApplicationContext(), "Property Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


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

                if (mAuth.getCurrentUser().getUid().equals(property_uid)) {

                    propertyName.setText(property_name);
                    propertyDescription.setText(property_beds + " bedroom house with " + property_availableBeds + " bedrooms available to rent.");
                    rentalPeriod.setText("Rental Period: " + property_period);
                    bathrooms.setText("Bathrooms: " + property_bathrooms);
                    propertyAddress.setText(property_address);
                    rentPrice.setText(property_price);
                    Glide.with(getApplicationContext())
                            .load(property_image).into(propertyImage);

                } else {

                    if(propertyData.getRatingValue() == null)
                        propertyData.setRatingValue(0d);
                    if(propertyData.getRatingCount() == null)
                        propertyData.setRatingCount(0l);

                    Float sum =  Float.parseFloat(String.valueOf(propertyData.getRatingValue()));

                    avgRating.setRating(sum);

                    propertyName.setText(property_name);
                    propertyDescription.setText(property_beds + " bedroom house with " + property_availableBeds + " bedrooms available to rent.");
                    rentalPeriod.setText("Rental Period: " + property_period);
                    bathrooms.setText("Bathrooms: " + property_bathrooms);
                    propertyAddress.setText(property_address);
                    rentPrice.setText(property_price);
                    Glide.with(getApplicationContext())
                            .load(property_image).into(propertyImage);
                }


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


                btnEnquire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager manager=getSupportFragmentManager();
                        MyDialogFragment myDialogFragment = new MyDialogFragment();
                        manager.beginTransaction().replace(R.id.replace, myDialogFragment).commit();

                        Bundle bundle = new Bundle();
                        bundle.putString("propId", property_key);
                        bundle.putString("key", property_uid);
                        bundle.putString("string", property_name);
                        myDialogFragment.setArguments(bundle);
                    }
                });
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


    //Rating AlertDialog
    private void showDialogRating() {
        //Set alert dialog properties
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Property Rating");
        View itemView = LayoutInflater.from(PropertyDetail.this).inflate(R.layout.layout_rating, null);
        RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
        EditText etReview = itemView.findViewById(R.id.etReview);
        builder.setView(itemView);
        //Close dialog when cancelled
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        //Create  new review model with values entered
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setPropertyID(property_key);
            reviewModel.setName(studentName.getText().toString());
            reviewModel.setUid(mAuth.getCurrentUser().getUid());
            reviewModel.setReview(etReview.getText().toString());
            reviewModel.setRatingValue2(ratingBar.getRating());
            Map<String,Object> serverTimeStamp = new HashMap<>();
            serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
            reviewModel.setReviewTimestamp(serverTimeStamp);
            //Upload to database
            submitRatingToFirebase(reviewModel);

        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey_2));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.teal_700));
    }

    //Upload to reviews table
    private void submitRatingToFirebase(ReviewModel reviewModel) {
        waitingDialog.show();
        //Submit to Reviews table
        FirebaseDatabase.getInstance().getReference("Reviews").child(property_key).push().setValue(reviewModel).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                //add to Property table
                addRatingToProperty(reviewModel.getRatingValue2());
            }
            waitingDialog.dismiss();
        });
    }

    //Add rating to properties table
    private void addRatingToProperty(float ratingValue) {
        FirebaseDatabase.getInstance().getReference("Properties").child(property_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Get selected property
                    PropertyData propertyData = snapshot.getValue(PropertyData.class);
                    propertyData.setKey(property_key);
                    //Apply rating
                    if(propertyData.getRatingValue() == null)
                        propertyData.setRatingValue(0d);
                    if(propertyData.getRatingCount() == null)
                        propertyData.setRatingCount(0l);

                    //Calculate average rating
                    double sumRating = (propertyData.getRatingValue() * propertyData.getRatingCount())+ratingValue;
                    long ratingCount = propertyData.getRatingCount()+1;
                    double result = sumRating/ratingCount;

                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("ratingValue",result);
                    updateData.put("ratingCount",ratingCount);

                    //Set values
                    propertyData.setRatingValue(result);
                    propertyData.setRatingCount(ratingCount);
                    //Notify user once uploaded
                    snapshot.getRef().updateChildren(updateData).addOnCompleteListener(task -> {
                        waitingDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(PropertyDetail.this, "Review posted!",Toast.LENGTH_SHORT).show();
                            //propertyDetailViewModel.setReviewModel(propertyData);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                waitingDialog.dismiss();
                Toast.makeText(PropertyDetail.this,"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}


//working method
//        Bundle mBundle = getIntent().getExtras();
//
//        if (mBundle != null) {
//
//            propertyName.setText(mBundle.getString("PropertyName"));
//            propertyDescription.setText(mBundle.getString("Description"));
//            propertyAddress.setText(mBundle.getString("PropertyAddress"));
//            rentPrice.setText(mBundle.getString("Price"));
//            key = mBundle.getString("keyValue");

//
//


//    public void btnDeleteProperty(View view) {
//
//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Property");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
//        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                reference.child(key).removeValue();
//                Toast.makeText(PropertyDetail.this, "Property Deleted", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        });
//
//    }
//
