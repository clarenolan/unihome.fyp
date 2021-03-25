package com.unihomefyp.landlord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unihomefyp.R;
import com.unihomefyp.models.PropertyData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    public class UploadProperty extends AppCompatActivity {

        TextView textView1;
        ImageView propertyImage;
        Uri uri;
        EditText txt_name, txt_address, txt_price;
        String imageUrl = null;
        String d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11;
        CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11; //c11 = wifiIncluded
        Spinner spTotalBeds, spAvBeds, spBathrooms, spPeriod, spActive, spCounties;
        Button btnUpload;

        StorageReference storageReference;

        // request code
        private final int PICK_IMAGE_REQUEST = 22;

        boolean pictureSelected = false;

        private StorageReference storage;
        private FirebaseDatabase database;
        private DatabaseReference databaseRef;
        private FirebaseAuth mAuth;
        private DatabaseReference mDatabaseUsers;
        private FirebaseUser mCurrentUser;

//    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference ref = database.getReference("Landlords");


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_property);

            textView1 = findViewById(R.id.text);
//        getSupportActionBar().setTitle("                NEW PROPERTY     ");

            propertyImage = (ImageView) findViewById(R.id.ivPropImage);
            txt_name = (EditText) findViewById(R.id.txt_PropName);
            txt_address = (EditText) findViewById(R.id.txt_Address);
            txt_price = (EditText) findViewById(R.id.text_price);
            btnUpload = (Button) findViewById(R.id.btnUploadProperty);


            storage = FirebaseStorage.getInstance().getReference();
            databaseRef = database.getInstance().getReference("Properties");
            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

            storageReference = FirebaseStorage.getInstance().getReference();

            c1 = findViewById(R.id.wifi);
            c2 = findViewById(R.id.bins);
            c3 = findViewById(R.id.electricity);
            c4 = findViewById(R.id.heating);
            c5 = findViewById(R.id.washing);
            c6 = findViewById(R.id.dryer);
            c7 = findViewById(R.id.parking);
            c8 = findViewById(R.id.dish);
            c9 = findViewById(R.id.tv);
            c10 = findViewById(R.id.kitchen);
            c11 = findViewById(R.id.wifiBill);


            spTotalBeds = findViewById(R.id.spTotalBeds);
            spAvBeds = findViewById(R.id.spAvBeds);
            spBathrooms = findViewById(R.id.spBathrooms);
            spPeriod = findViewById(R.id.spPeriod);
            spActive = findViewById(R.id.spActive);
            spCounties = findViewById(R.id.spCounties2);

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


            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage();
                }
            });

        }


        //
//Select Image from emulator gallery
        public void btnSelectImage(View view) {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            startActivityForResult(photoPicker, 1);
        }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //Checking codes
//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//            //Get uri
//            uri = data.getData();
//
//            //Setting image view
//            propertyImage.setImageURI(uri);
//
//        } else {
//            Toast.makeText(this, "Pick an image", Toast.LENGTH_SHORT).show();
//        }
//
//    }


//    // Select Image method
//    public void btnSelectImage(View view)
//    {
//
////        // Defining Implicit Intent to mobile gallery
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(
////                Intent.createChooser(
////                        intent,
////                        "Select Image from here..."),
////                PICK_IMAGE_REQUEST);
//
//        Intent photoPicker = new Intent(Intent.ACTION_PICK);
//        photoPicker.setType("image/*");
//        startActivityForResult(photoPicker, 1);
//    }

//    // Override onActivityResult method
//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode,
//                                    Intent data)
//    {
//
//        super.onActivityResult(requestCode,
//                resultCode,
//                data);
//
//        // checking request code and result code
//        // if request code is PICK_IMAGE_REQUEST and
//        // resultCode is RESULT_OK
//        // then set image in the image view
//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            // Get the Uri of data
//            uri = data.getData();
//            try {
//
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                uri);
//                propertyImage.setImageBitmap(bitmap);
//            }
//
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
//    }


//    //Upload Image to firebase
//    public void uploadImage() {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageUrl").child(uri.getLastPathSegment());
//        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
//
//            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//            while (!uriTask.isComplete()) ;
//            Uri urlImage = uriTask.getResult();
//            imageUrl = urlImage.toString();
//            //Call upload property method
//            uploadProperty();
//        });
//
//    }
//

        //Upload Image to firebase
        public void uploadImage() {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageUrl").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                //Call upload property method
                uploadProperty();
            });

        }



//    // UploadImage method
//    private void uploadImage()
//    {
//        if (uri != null) {
//
//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            // Defining the child of storageReference
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageUrl").child(uri.getLastPathSegment());
//            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
//
//                    // Image uploaded successfully
//                    // Dismiss dialog
//                    progressDialog.dismiss();
//                    Toast
//                            .makeText(UploadProperty.this,
//                                    "Image Uploaded!!",
//                                    Toast.LENGTH_SHORT)
//                            .show();
//
//                    uploadProperty();
//
//                })
//
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e)
//                        {
//
//                            // Error, Image not uploaded
//                            progressDialog.dismiss();
//                            Toast
//                                    .makeText(UploadProperty.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    })
//                    .addOnProgressListener(
//                            new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                // Progress Listener for loading
//                                // percentage on the dialog box
//                                @Override
//                                public void onProgress(
//                                        UploadTask.TaskSnapshot taskSnapshot)
//                                {
//                                    double progress
//                                            = (100.0
//                                            * taskSnapshot.getBytesTransferred()
//                                            / taskSnapshot.getTotalByteCount());
//                                    progressDialog.setMessage(
//                                            "Uploaded "
//                                                    + (int)progress + "%");
//                                }
//                            });
//        }
//    }



//    //Upload Image to firebase
//    public void uploadImage() {
//
//        if (uri != null) {
//
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageUrl").child(uri.getLastPathSegment());
//            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
//
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isComplete()) ;
//
//                Uri urlImage = uriTask.getResult();
//
//                imageUrl = urlImage.toString();
//
////                Toast.makeText(UploadProperty.this,
////                        "Image Uploaded!!",
////                        Toast.LENGTH_SHORT).show();
//
//                //Call upload property method
//                uploadProperty();
//            });
//
//        } else {
//            Toast.makeText(this, "Pick an image", Toast.LENGTH_SHORT).show();
//        }
//    }

        //Upload property to firebase
        public void uploadProperty() {
            // Showing Progress Dialog while uploading
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



//        if (name.isEmpty()) {
//            txt_name.setError("Please fill in all details");
//            txt_name.requestFocus();
//            return;
//        }
//
//        if (county.isEmpty()) {
//            spCounties.requestFocus();
//            return;
//        }
//
//        if (address.isEmpty()) {
//            txt_address.setError("Please fill in all details");
//            txt_address.requestFocus();
//            return;
//        }
//
//        if (price.isEmpty()) {
//            txt_price.setError("Please fill in all details");
//            txt_price.requestFocus();
//            return;
//        }

//        PropertyData propertyData = new PropertyData(
//                name,
//                county,
//                address,
//                price,
//                imageUrl,
//                d1,
//                d2,
//                d3,
//                d4,
//                d5,
//                d6,
//                d7,
//                d8,
//                d9,
//                d10,
//                d11,
//                beds,
//                availableBeds,
//                bathrooms,
//                period,
//                active,
//                uid
//        );

            //New user model
            Map<String, Object> propertyData = new HashMap<>();
            propertyData.put("name", name );
            propertyData.put("county", county);
            propertyData.put("address", address);
            propertyData.put("price",price);
            propertyData.put("imageUrl",imageUrl);
            propertyData.put("wifi",d1);
            propertyData.put("bins",d2);
            propertyData.put("electricity", d3);
            propertyData.put("heating",d4);
            propertyData.put("washing",d5);
            propertyData.put("dryer",d6);
            propertyData.put("parking", d7);
            propertyData.put("dish",d8);
            propertyData.put("tv",d9);
            propertyData.put("kitchen",d10);
            propertyData.put("wifiBill", d11);
            propertyData.put("beds",beds);
            propertyData.put("availableBeds",availableBeds);
            propertyData.put("bathrooms",bathrooms);
            propertyData.put("period", period);
            propertyData.put("active", active);
            propertyData.put("uid", uid);

            String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());



            DatabaseReference newReference = databaseRef.push();

            newReference.setValue(propertyData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(UploadProperty.this, "Property Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProperty.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode,Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                uri = data.getData();
                propertyImage.setImageURI(uri);

            } else Toast.makeText(this, "Pick an image", Toast.LENGTH_SHORT).show();

        }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1000){
//            if(resultCode == Activity.RESULT_OK){
//                Uri imageUri = data.getData();
//                //profileImage.setImageURI(imageUri);
//                uploadImageToFirebase(imageUri);
//            }
//        }
//    }
//
//    private void uploadImageToFirebase(Uri imageUri) {
//        // upload image to firebase storage
//        final StorageReference fileRef = storageReference.child("Properties/"+property_key+"imageURL");
//        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into(propertyImage);
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    }

//
//
//public class UploadProperty extends AppCompatActivity {
//
//    TextView textView1;
//    ImageView propertyImage;
//    Uri uri;
//    EditText txt_name, txt_address, txt_price;
//    String imageUrl;
//    String d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11;
//    CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11; //c11 = wifiIncluded
//    Spinner spTotalBeds, spAvBeds, spBathrooms, spPeriod, spActive, spCounties;
//    Button btnUpload;
//
//    StorageReference storageReference;
//
//    // request code
//    private final int PICK_IMAGE_REQUEST = 22;
//
//    boolean pictureSelected = false;
//
//    private StorageReference storage;
//    private FirebaseDatabase database;
//    private DatabaseReference databaseRef;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabaseUsers;
//    private FirebaseUser mCurrentUser;
//
////    final FirebaseDatabase database = FirebaseDatabase.getInstance();
////    DatabaseReference ref = database.getReference("Landlords");
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_property);
//
//        textView1 = findViewById(R.id.text);
////        getSupportActionBar().setTitle("                NEW PROPERTY     ");
//
//        propertyImage = (ImageView) findViewById(R.id.ivPropImage);
//        txt_name = (EditText) findViewById(R.id.txt_PropName);
//        txt_address = (EditText) findViewById(R.id.txt_Address);
//        txt_price = (EditText) findViewById(R.id.text_price);
//        btnUpload = (Button) findViewById(R.id.btnUploadProperty);
//
//
//        storage = FirebaseStorage.getInstance().getReference();
//        databaseRef = database.getInstance().getReference().child("Properties");
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
//
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        c1 = findViewById(R.id.wifi);
//        c2 = findViewById(R.id.bins);
//        c3 = findViewById(R.id.electricity);
//        c4 = findViewById(R.id.heating);
//        c5 = findViewById(R.id.washing);
//        c6 = findViewById(R.id.dryer);
//        c7 = findViewById(R.id.parking);
//        c8 = findViewById(R.id.dish);
//        c9 = findViewById(R.id.tv);
//        c10 = findViewById(R.id.kitchen);
//        c11 = findViewById(R.id.wifiBill);
//
//
//        spTotalBeds = findViewById(R.id.spTotalBeds);
//        spAvBeds = findViewById(R.id.spAvBeds);
//        spBathrooms = findViewById(R.id.spBathrooms);
//        spPeriod = findViewById(R.id.spPeriod);
//        spActive = findViewById(R.id.spActive);
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
//        //Setting options 1-12, total number of beds spinner
//        List<String> categories = new ArrayList<>();
//
//        categories.add("1");
//        categories.add("2");
//        categories.add("3");
//        categories.add("4");
//        categories.add("5");
//        categories.add("6");
//        categories.add("7");
//        categories.add("8");
//        categories.add("9");
//        categories.add("10");
//        categories.add("11");
//        categories.add("12");
//
//        ArrayAdapter<String> dataAdapter1;
//        dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spTotalBeds.setAdapter(dataAdapter1);
//
//
//        //Setting options 1-12, number of available beds spinner
//        List<String> categories2 = new ArrayList<>();
//
//        categories2.add("1");
//        categories2.add("2");
//        categories2.add("3");
//        categories2.add("4");
//        categories2.add("5");
//        categories2.add("6");
//        categories2.add("7");
//        categories2.add("8");
//        categories2.add("9");
//        categories2.add("10");
//        categories2.add("11");
//        categories2.add("12");
//
//        ArrayAdapter<String> dataAdapter2;
//        dataAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories2);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spAvBeds.setAdapter(dataAdapter2);
//
//
//        //Setting options 1-12, number of bathrooms spinner
//        List<String> categories3 = new ArrayList<>();
//
//        categories3.add("1");
//        categories3.add("2");
//        categories3.add("3");
//        categories3.add("4");
//        categories3.add("5");
//        categories3.add("6");
//        categories3.add("7");
//        categories3.add("8");
//        categories3.add("9");
//        categories3.add("10");
//        categories3.add("11");
//        categories3.add("12");
//