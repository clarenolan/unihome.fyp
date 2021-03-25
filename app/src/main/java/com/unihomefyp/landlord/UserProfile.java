package com.unihomefyp.landlord;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.unihomefyp.R;
import com.unihomefyp.models.UserData;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private TextView  txtEmail;
    private TextInputLayout fullName, email, phoneNo, password, etBio;
    private Button btnUpdate2;
    private CircleImageView landlordImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageRef;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Landlord");

        btnUpdate2 = findViewById(R.id.btnUpdate);
        txtEmail = findViewById(R.id.txtEmail);
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.etEmail);
        phoneNo = findViewById(R.id.etPhone);
        password = findViewById(R.id.etPassword);
        etBio = findViewById(R.id.etLandlordBio);

        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        landlordImage = findViewById(R.id.imgProfile);
        StorageReference profileRef = storageReference.child("Landlord/" + mAuth.getCurrentUser().getUid() + "imageURL");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(landlordImage);
            }
        });

        mDatabaseUsers.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    UserData user = snapshot.getValue(UserData.class);

                    String name = (String) snapshot.child("name").getValue();
                    String email2 = (String) snapshot.child("email").getValue();
                    String password2 = (String) snapshot.child("password").getValue();
                    String phoneNo2 = (String) snapshot.child("phoneNo").getValue();
                    String username = (String) snapshot.child("username").getValue();

                    fullName.getEditText().setText(name);
                    email.getEditText().setText(email2);
                    phoneNo.getEditText().setText(phoneNo2);
                    password.getEditText().setText(password2);

                    if(snapshot.child("bio").getValue() != null){
                        String bio = (String) snapshot.child("bio").getValue();
                        etBio.getEditText().setText(bio);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }
        });

        landlordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });


        btnUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = fullName.getEditText().getText().toString().trim();
                final String emailaddress = email.getEditText().getText().toString().trim();
                final String phone = phoneNo.getEditText().getText().toString().trim();
                final String pass = password.getEditText().getText().toString().trim();
                final String userID = mAuth.getCurrentUser().getUid();
                final String llBio = etBio.getEditText().getText().toString().trim();

                mAuth = FirebaseAuth.getInstance();
                String user_id = mAuth.getCurrentUser().getUid();
                //New user model
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", emailaddress );
                userData.put("name", name);
                userData.put("phoneNo", phone);
                userData.put("password",pass);
                userData.put("bio", llBio);

                mDatabaseUsers.child(user_id).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Notify user of success
                        //Return to homepage
                        Intent intToProfile = new Intent(UserProfile.this, LandlordHome.class);
                        startActivity(intToProfile);
                        Toast.makeText(UserProfile.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this,"Failed", Toast.LENGTH_SHORT).show();
                    }
                });
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
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("Landlord/"+mAuth.getCurrentUser().getUid()+"imageURL");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(landlordImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }


}

