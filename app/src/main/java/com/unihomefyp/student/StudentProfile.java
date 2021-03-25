package com.unihomefyp.student;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.unihomefyp.BottomNavigationViewHelper;
import com.unihomefyp.R;
import com.unihomefyp.models.UserData;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {

    private static final String TAG = "StudentProfile";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = StudentProfile.this;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private CircleImageView studentImage;
    private TextView studentName;
    StorageReference storageReference;

    private Button btnSaved, btnDetails, btnEnquiries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        setupBottomNavigationView();

        mAuth = FirebaseAuth.getInstance();

        btnDetails = findViewById(R.id.btnDetails);
        btnSaved = findViewById(R.id.btnSaved);
        btnEnquiries = findViewById(R.id.btnEnquiries);
        studentImage = findViewById(R.id.img_student_profile);
        studentName = findViewById(R.id.txt_student_name);

        storageReference = FirebaseStorage.getInstance().getReference();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Student");


        //Get user data from firebase
        mDatabaseUsers.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    UserData user = snapshot.getValue(UserData.class);

                    String name = (String) snapshot.child("name").getValue();

                    studentName.setText(name);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });


        StorageReference profileRef = storageReference.child("Student/" + mAuth.getCurrentUser().getUid() + "imageURL");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(studentImage);
            }
        });


        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager1=getSupportFragmentManager();
                StudentDetails studentDetails = new StudentDetails();
                manager1.beginTransaction().setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left).replace(R.id.replace2, studentDetails).commit();


            }
        });

        btnEnquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager3=getSupportFragmentManager();
                StudentEnquiries studentEnquiries = new StudentEnquiries();
                manager3.beginTransaction().setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left).replace(R.id.replace2, studentEnquiries).commit();
            }
        });


    }
    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void startSaved(View view) {
        FragmentManager manager =getSupportFragmentManager();
        SavedProperties savedProperties = new SavedProperties();
        manager .beginTransaction().setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left).replace(R.id.replace2, savedProperties).commit();

    }
}