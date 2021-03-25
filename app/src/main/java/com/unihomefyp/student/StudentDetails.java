package com.unihomefyp.student;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDetails extends Fragment {


    private TextView txtFullName, txtEmail;
    private TextInputLayout fullName, studEmail, phoneNo, studPassword, studentNumber, studentBio;
    private Spinner spUniversity, spYear;
    private Button btnUpdate2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageRef;
    private CircleImageView studentImage;
    private ImageView btnBack;
    //Uri uri = null;
    //String imageUrl;

    public static final String TAG = "TAG";

    View view;
    private static StudentDetails instance;

    StorageReference storageReference;
    FirebaseFirestore fStore;
    FirebaseUser user;

    public StudentDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_details, container, false);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Student");

        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        studentImage = view.findViewById(R.id.img_student_profile);
        StorageReference profileRef = storageReference.child("Student/" + mAuth.getCurrentUser().getUid() + "imageURL");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(studentImage);
            }
        });

        btnUpdate2 = view.findViewById(R.id.btnUpdateProfile);
        fullName = (TextInputLayout) view.findViewById(R.id.full_name_profile);
        studEmail =(TextInputLayout) view.findViewById(R.id.etEmailProfile);
        phoneNo = (TextInputLayout)view.findViewById(R.id.etPhoneProfile);
        studPassword = (TextInputLayout)view.findViewById(R.id.etPasswordProfile);
        studentNumber = (TextInputLayout)view.findViewById(R.id.etStudentNumber);
        spUniversity = view.findViewById(R.id.spUniversity);
        spYear = view.findViewById(R.id.spYear);
        studentBio = (TextInputLayout) view.findViewById(R.id.etBio);
        btnBack = view.findViewById(R.id.back);



        //Setting options education institutions of ireland, university spinner
        List<String> universities = new ArrayList<>();

        universities.add("");
        universities.add("Athlone Institute of Technology");
        universities.add("Carlow College");
        universities.add("Dublin City University");
        universities.add("Dún Laoghaire Institute of Art");
        universities.add("Dundalk Institute of Technology");
        universities.add("Galway-Mayo Institute of Technology");
        universities.add("Institute of Public Administration");
        universities.add("Institute of Technology Carlow");
        universities.add("Institute of Technology Sligo");
        universities.add("Institute of Technology Tralee");
        universities.add("Letterkenny Institute of Technology");
        universities.add("Limerick Institute of Technology");
        universities.add("Marino Institute of Education");
        universities.add("Maynooth University");
        universities.add("National College of Art and Design");
        universities.add("National College of Ireland, Dublin");
        universities.add("National University of Ireland, Galway");
        universities.add("National University of Ireland, System");
        universities.add("Royal College of Surgeons in Ireland");
        universities.add("Royal Irish Academy of Music");
        universities.add("Saint Patrick's College, Maynooth");
        universities.add("Technological University Dublin");
        universities.add("Trinity College Dublin");
        universities.add("University College Cork");
        universities.add("University College Dublin");
        universities.add("University of Limerick");
        universities.add("Waterford Institute of Technology");
        universities.add("Munster University");
        universities.add("Other");


        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, universities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUniversity.setAdapter(dataAdapter);

        //Setting options for year of study spinner
        List<String> uniYear = new ArrayList<>();

        uniYear.add("");
        uniYear.add("First Year");
        uniYear.add("Second Year");
        uniYear.add("Third Year");
        uniYear.add("Fourth Year");
        uniYear.add("Fifth Year");
        uniYear.add("Post Graduate");
        uniYear.add("Masters");



        ArrayAdapter<String> yearAdapter;
        yearAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, uniYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);



        //Get user data from firebase
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

                    if(snapshot.child("university").getValue() != null) {
                        String university = (String) snapshot.child("university").getValue();
                        spUniversity.setSelection(dataAdapter.getPosition(university));
                    }
                    if (snapshot.child("courseYear").getValue() != null) {
                        String courseYear = (String) snapshot.child("courseYear").getValue();
                        spYear.setSelection(yearAdapter.getPosition(courseYear));
                    }
                    if (snapshot.child("bio").getValue() != null) {
                        String bio = (String) snapshot.child("bio").getValue();
                        studentBio.getEditText().setText(bio);
                    }

                    fullName.getEditText().setText(name);
                    studentNumber.getEditText().setText(username);
                    studEmail.getEditText().setText(email2);
                    phoneNo.getEditText().setText(phoneNo2);
                    studPassword.getEditText().setText(password2);

                    // String property_image = (String) snapshot.child("imageURL").getValue();
                    // Glide.with(getContext())
                    //        .load(property_image).into(studentImage);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });

        String studentName = fullName.getEditText().getText().toString();
        String studentEmail = studEmail.getEditText().getText().toString();
        String studentPhone = phoneNo.getEditText().getText().toString();
        String studentPassword = studPassword.getEditText().getText().toString();
        String strStudentNumber = studentNumber.getEditText().getText().toString();



        studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });


        //Upload to Firebase
        btnUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Blank values checking
                if (fullName.getEditText().toString().isEmpty() || studEmail.getEditText().toString().isEmpty() || phoneNo.getEditText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Get values entered
                String studentName = fullName.getEditText().getText().toString();
                String studentEmail = studEmail.getEditText().getText().toString();
                String studentPhone = phoneNo.getEditText().getText().toString();
                String studentPassword = studPassword.getEditText().getText().toString();
                String strStudentNumber = studentNumber.getEditText().getText().toString();

                mAuth = FirebaseAuth.getInstance();
                String user_id = mAuth.getCurrentUser().getUid();
                //New user model
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", studEmail.getEditText().getText().toString().trim() );
                userData.put("name", studentName);
                userData.put("phoneNo", phoneNo.getEditText().getText().toString().trim());
                userData.put("password",studPassword.getEditText().getText().toString().trim());
                userData.put("username",studentNumber.getEditText().getText().toString().trim());
                userData.put("university",spUniversity.getSelectedItem().toString());
                userData.put("courseYear",spYear.getSelectedItem().toString());
                userData.put("bio", studentBio.getEditText().getText().toString().trim());

                mDatabaseUsers.child(user_id).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Notify user of success
                        Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), StudentProfile.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        studEmail.getEditText().setText(studentEmail);
        fullName.getEditText().setText(studentName);
        phoneNo.getEditText().setText(studentPhone);
        studPassword.getEditText().setText(studentPassword);
        studentNumber.getEditText().setText(strStudentNumber);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(),R.anim.slide_in, R.anim.slide_out_right);
                Intent intent = new Intent(getContext(), StudentProfile.class);
                getContext().startActivity(intent, options.toBundle());
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        final StorageReference fileRef = storageReference.child("Student/"+mAuth.getCurrentUser().getUid()+"imageURL");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(studentImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

