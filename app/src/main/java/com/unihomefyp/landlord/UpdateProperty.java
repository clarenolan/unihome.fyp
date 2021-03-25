package com.unihomefyp.landlord;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateProperty extends AppCompatActivity {

//    ImageView propertyImage;
//    Uri uri;
//    EditText txt_name, txt_description, txt_address, txt_price;
//    String imageUrl;
//    String key, oldImageUrl;
//    DatabaseReference databaseReference;
//    StorageReference storageReference;
//    String propertyname, propertyDescription, propertyAddress, propertyPrice;
//    String d1, d2, d3, d4, d5, d6, d7, d8, d9, d10;
//    CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
//    Spinner spTotalBeds, spAvBeds, spBathrooms, spPeriod;

    TextView textView1;
    ImageView propertyImage;
    Uri uri;
    EditText txt_name, txt_address, txt_price;
    String imageUrl;
    String d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11;
    CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11; //c11 = wifiIncluded
    Spinner spTotalBeds, spAvBeds, spBathrooms, spPeriod, spActive, spCounties;

    StorageReference storageReference;


    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private String property_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_property);

        property_key = getIntent().getExtras().getString("PropertyID");

        propertyImage = (ImageView) findViewById(R.id.udImage);
        txt_name = (EditText) findViewById(R.id.udPropertyName);
        txt_address = (EditText) findViewById(R.id.udAddress);
        txt_price = (EditText) findViewById(R.id.udPrice);


        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child("Properties");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        storageReference = FirebaseStorage.getInstance().getReference();



        c1 = findViewById(R.id.wifi2);
        c2 = findViewById(R.id.bins2);
        c3 = findViewById(R.id.electricity2);
        c4 = findViewById(R.id.heating2);
        c5 = findViewById(R.id.washing2);
        c6 = findViewById(R.id.dryer2);
        c7 = findViewById(R.id.parking2);
        c8 = findViewById(R.id.dish2);
        c9 = findViewById(R.id.tv2);
        c10 = findViewById(R.id.kitchen2);
        c11 = findViewById(R.id.wifiBill2);

        spTotalBeds = findViewById(R.id.udTotalBeds);
        spAvBeds = findViewById(R.id.udBeds);
        spBathrooms = findViewById(R.id.udBathrooms);
        spPeriod = findViewById(R.id.udPeriod);
        spActive = findViewById(R.id.udActive);
        spCounties = findViewById(R.id.udCounty);

        //Setting options for counties spinner
        List<String> counties = new ArrayList<>();

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

        ArrayAdapter<String> countiesAdapter;
        countiesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, counties);
        countiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCounties.setAdapter(countiesAdapter);


        //Setting options 1-12, total number of beds spinner
        List<String> categories = new ArrayList<>();

        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");

        ArrayAdapter<String> dataAdapter1;
        dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTotalBeds.setAdapter(dataAdapter1);


        //Setting options 1-12, number of available beds spinner
        List<String> categories2 = new ArrayList<>();

        categories2.add("1");
        categories2.add("2");
        categories2.add("3");
        categories2.add("4");
        categories2.add("5");
        categories2.add("6");
        categories2.add("7");
        categories2.add("8");
        categories2.add("9");
        categories2.add("10");
        categories2.add("11");
        categories2.add("12");

        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAvBeds.setAdapter(dataAdapter2);


        //Setting options 1-12, number of bathrooms spinner
        List<String> categories3 = new ArrayList<>();

        categories3.add("1");
        categories3.add("2");
        categories3.add("3");
        categories3.add("4");
        categories3.add("5");
        categories3.add("6");
        categories3.add("7");
        categories3.add("8");
        categories3.add("9");
        categories3.add("10");
        categories3.add("11");
        categories3.add("12");

        ArrayAdapter<String> dataAdapter3;
        dataAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories3);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBathrooms.setAdapter(dataAdapter3);

        //Setting options 1-12, rental period spinner
        List<String> categories4 = new ArrayList<>();

        categories4.add("Academic Year");
        categories4.add("Semester 1");
        categories4.add("Semester 2");
        categories4.add("Summer Period");
        categories4.add("Full Year");

        ArrayAdapter<String> dataAdapter4;
        dataAdapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories4);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(dataAdapter4);

        //Setting options yes/no
        List<String> activelist = new ArrayList<>();

        activelist.add("Yes");
        activelist.add("No");

        ArrayAdapter<String> dataAdapter5;
        dataAdapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, activelist);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActive.setAdapter(dataAdapter5);


        databaseRef.child(property_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PropertyData propertyData = snapshot.getValue(PropertyData.class);

                String property_name = (String) snapshot.child("name").getValue();
                String property_county = (String) snapshot.child("county").getValue();
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
                String active = (String) snapshot.child("active").getValue();

                txt_address.setText(property_address);
                txt_price.setText(property_price);
                txt_name.setText(property_name);
                Glide.with(UpdateProperty.this)
                        .load(property_image).into(propertyImage);

                if(propertyData.getRatingValue() == null)
                    propertyData.setRatingValue(0d);

                if(propertyData.getRatingCount() == null)
                    propertyData.setRatingCount(0l);

                Float sum =  Float.parseFloat(String.valueOf(propertyData.getRatingValue()));

                long ratingCount = propertyData.getRatingCount();
                double sumRating = propertyData.getRatingValue();

                String string = "Included";


                if (property_wifi.equals(string)) {
                    c1.setChecked(true);
                }

                if (property_bins.equals(string)) {
                    c2.setChecked(true);
                }

                if (property_heat.equals(string)) {
                    c4.setChecked(true);
                }

                if (property_wash.equals(string)) {
                    c5.setChecked(true);
                }

                if (property_dryer.equals(string)) {
                    c6.setChecked(true);
                }

                if (property_dish.equals(string)) {
                    c8.setChecked(true);
                }

                if (property_parking.equals(string)) {
                    c7.setChecked(true);
                }

                if (property_elec.equals(string)) {
                    c3.setChecked(true);
                }

                if (property_tv.equals(string)) {
                    c9.setChecked(true);
                }

                if (property_kitchen.equals(string)) {
                    c10.setChecked(true);
                }

                if (wifi_bill.equals(string)) {
                    c11.setChecked(true);
                }


                spTotalBeds.setSelection(dataAdapter1.getPosition(property_beds));
                spAvBeds.setSelection(dataAdapter2.getPosition(property_availableBeds));
                spBathrooms.setSelection(dataAdapter3.getPosition(property_bathrooms));
                spPeriod.setSelection(dataAdapter4.getPosition(property_period));
                spActive.setSelection(dataAdapter5.getPosition(active));
                spCounties.setSelection(countiesAdapter.getPosition(property_county));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Select Image from emulator gallery
    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);
    }


    //Upload property button calls upload image method
    public void btnUpdateProperty(View view) {
        uploadImage();
    }

    //Upload Image to firebase
    public void uploadImage() {
        if( uri != null ){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageUrl").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                //Call upload property method
                uploadProperty();
            });
        } else{
            uploadProperty();
        }
    }

    private void uploadProperty() {

        //Blank values checking
        if (txt_name.getText().toString().isEmpty() || txt_address.getText().toString().isEmpty() || txt_price.getText().toString().isEmpty()) {
            Toast.makeText(UpdateProperty.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Property Uploading...");
        progressDialog.show();

        //Setting variables from amenity checkboxes
        if (c1.isChecked()) {
            d1 = "Included";
        } else {
            d1 = "Not Included";
        }

        if (c2.isChecked()) {
            d2 = "Included";
        } else {
            d2 = "Not Included";
        }

        if (c3.isChecked()) {
            d3 = "Included";
        } else {
            d3 = "Not Included";
        }

        if (c4.isChecked()) {
            d4 = "Included";
        } else {
            d4 = "Not Included";
        }

        if (c5.isChecked()) {
            d5 = "Included";
        } else {
            d5 = "Not Included";
        }

        if (c6.isChecked()) {
            d6 = "Included";
        } else {
            d6 = "Not Included";
        }

        if (c7.isChecked()) {
            d7 = "Included";
        } else {
            d7 = "Not Included";
        }

        if (c8.isChecked()) {
            d8 = "Included";
        } else {
            d8 = "Not Included";
        }

        if (c9.isChecked()) {
            d9 = "Included";
        } else {
            d9 = "Not Included";
        }

        if (c10.isChecked()) {
            d10 = "Included";
        } else {
            d10 = "Not Included";
        }

        if (c11.isChecked()) {
            d11 = "Included";
        } else {
            d11 = "Not Included";
        }

        //Setting variables from selected spinner items
        final String name = txt_name.getText().toString().trim();
        final String county = spCounties.getSelectedItem().toString().trim();
        final String address = txt_address.getText().toString().trim();
        final String price = txt_price.getText().toString().trim();
        final String beds = spTotalBeds.getSelectedItem().toString().trim();
        final String availableBeds = spAvBeds.getSelectedItem().toString().trim();
        final String bathrooms = spBathrooms.getSelectedItem().toString().trim();
        final String period = spPeriod.getSelectedItem().toString().trim();
        final String active = spActive.getSelectedItem().toString().trim();
        final String uid = mCurrentUser.getUid();


        //New user model
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name );
        userData.put("county", county);
        userData.put("address", address);
        userData.put("price",price);
//        userData.put("imageUrl",imageUrl);
        userData.put("wifi",d1);
        userData.put("bins",d2);
        userData.put("electricity", d3);
        userData.put("heating",d4);
        userData.put("washing",d5);
        userData.put("dryer",d6);
        userData.put("parking", d7);
        userData.put("dish",d8);
        userData.put("tv",d9);
        userData.put("kitchen",d10);
        userData.put("wifiBill", d11);
        userData.put("beds",beds);
        userData.put("availableBeds",availableBeds);
        userData.put("bathrooms",bathrooms);
        userData.put("period", period);
        userData.put("active", active);
        userData.put("uid", uid);

        databaseRef.child(property_key).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Notify user of success
                Toast.makeText(UpdateProperty.this, "Property Updated!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProperty.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProperty.this,"Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Properties/"+property_key+"imageURL");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(propertyImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void btnDeleteProp(View view) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Property Deleting...");
        progressDialog.show();

        databaseRef.child(property_key).removeValue();

        Toast.makeText(getApplicationContext(), "Property Deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UpdateProperty.this, MainActivity.class));
//        finish();
    }
}


//        propertyImage = (ImageView) findViewById(R.id.ivPropImage);
//        txt_name = (EditText) findViewById(R.id.txt_PropName);
//        txt_description = (EditText) findViewById(R.id.txt_Description);
//        txt_address = (EditText) findViewById(R.id.txt_Address);
//        txt_price = (EditText) findViewById(R.id.text_price);
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//
//            Glide.with(UpdateProperty.this)
//                    .load(bundle.getString("oldimageUrl"))
//                    .into(propertyImage);
//            txt_name.setText(bundle.getString("propertyNameKey"));
//            txt_description.setText(bundle.getString("propertyDescriptionKey"));
//            txt_address.setText(bundle.getString("propertyAddressKey"));
//            txt_price.setText(bundle.getString("propertyPriceKey"));
//            key = bundle.getString("key");
//            oldImageUrl = bundle.getString("oldimageUrl");
//        }
//
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Property").child(key);

//        spCounties = findViewById(R.id.spCounties2);
//
//        //Setting options for counties spinner
//        List<String> counties = new ArrayList<>();
//
//        counties.add("Antrim");
//        counties.add("Armagh");
//        counties.add("Carlow");
//        counties.add("Cavan");
//        counties.add("Cork");
//        counties.add("Derry");
//        counties.add("Donegal");
//        counties.add("Down");
//        counties.add("Dublin");
//        counties.add("Fermanagh");
//        counties.add("Galway");
//        counties.add("Kerry");
//        counties.add("Kildare");
//        counties.add("Kilkenny");
//        counties.add("Laois");
//        counties.add("Leitrim");
//        counties.add("Longford");
//        counties.add("Louth");
//        counties.add("Mayo");
//        counties.add("Meath");
//        counties.add("Monaghan");
//        counties.add("Offaly");
//        counties.add("Roscommon");
//        counties.add("Sligo");
//        counties.add("Tipperary");
//        counties.add("Tyrone");
//        counties.add("Mayo");
//        counties.add("Waterford");
//        counties.add("Westmeath");
//        counties.add("Wexford");
//        counties.add("Wicklow");
//
//        ArrayAdapter<String> countiesAdapter;
//        countiesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, counties);
//        countiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCounties.setAdapter(countiesAdapter);

//

//
//    public void btnSelectImage(View view) {
//        Intent photoPicker = new Intent(Intent.ACTION_PICK);
//        photoPicker.setType("image/*");
//        startActivityForResult(photoPicker, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//
//            uri = data.getData();
//            propertyImage.setImageURI(uri);
//
//        } else Toast.makeText(this, "You haven't picked image", Toast.LENGTH_SHORT).show();
//
//    }
//
//
//    public void btnUpdateProperty(View view) {
//
//        propertyname = txt_name.getText().toString().trim();
//        propertyDescription = txt_description.getText().toString().trim();
//        propertyAddress = txt_address.getText().toString().trim();
//        propertyPrice = txt_price.getText().toString();

//
//        if (c1.isChecked()) {
//            d1 = "Wifi";
//
//        } else {
//
//        }
//        if (c2.isChecked()) {
//
//            d2 = "Bins";
//
//
//        } else {
//
//        }
//        if (c3.isChecked()) {
//            d3 = "Electricity";
//
//
//        } else {
//
//        }
//        if (c4.isChecked()) {
//
//            d4 = "Heating";
//
//
//        } else {
//
//        }
//        if (c5.isChecked()) {
//
//            d5 = "Washing Machine";
//
//
//        } else {
//
//        }
//        if (c6.isChecked()) {
//            d6 = "Dryer";
//
//
//        } else {
//
//        }
//
//            if (c7.isChecked()) {
//                d7 = "Parking";
//
//
//            } else {
//
//            }
//            if (c8.isChecked()) {
//
//                d8 = "Dish Washer";
//
//            } else {
//
//            }
//            if (c9.isChecked()) {
//
//                d9 = "TV Room";
//
//            } else {
//
//            }
//            if (c10.isChecked()) {
//
//                d10 = "Communal Kitchen";
//
//            } else {
//
//            }


//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Property Uploading....");
//            progressDialog.show();
//            storageReference = FirebaseStorage.getInstance()
//                    .getReference().child("PropertyImage").child(uri.getLastPathSegment());
//            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!uriTask.isComplete()) ;
//                    Uri urlImage = uriTask.getResult();
//                    imageUrl = urlImage.toString();
//                    uploadProperty();
//                    progressDialog.dismiss();
//                }
//
//
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
//                }
//            });
//
//
//        }
//
//
//        public void uploadProperty(){
//
//            PropertyData propertyData = new PropertyData(
//                    propertyname,
//                    propertyDescription,
//                    propertyAddress,
//                    propertyPrice,
//                    imageUrl,
//                    d1,
//                    d2,
//                    d3,
//                    d4,
//                    d5,
//                    d6,
//                    d7,
//                    d8,
//                    d9,
//                    d10,
//                    spTotalBeds.getSelectedItem().toString(),
//                    spAvBeds.getSelectedItem().toString(),
//                    spBathrooms.getSelectedItem().toString(),
//                    spPeriod.getSelectedItem().toString(),
//                    username);
//
//
//            databaseReference.setValue(propertyData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
//                    storageReferenceNew.delete();
//                    Toast.makeText(UpdateProperty.this, "Data Updated", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        }


