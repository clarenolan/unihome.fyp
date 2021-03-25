package com.unihomefyp.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.unihomefyp.R;
import com.unihomefyp.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class StudentSignUp extends AppCompatActivity {

    private Button btnRegister;
    private FirebaseAuth mAuth;
    private TextInputLayout etName, etEmail, etNumber, etPhone, etPassword;
    private ProgressBar progressBar;
    private Spinner spUniversity;
    private TextView txtUniversity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }


    private void registerUser() {
        String name = etName.getEditText().getText().toString();
        String number = etNumber.getEditText().getText().toString();
        String email = etEmail.getEditText().getText().toString();
        String phone = etPhone.getEditText().getText().toString();
        String password = etPassword.getEditText().getText().toString();
        String university = spUniversity.getSelectedItem().toString();



//        String[] separated = email.split("@");
//        separated[0];
//        separated[1];




//        if ( university == "University College Cork" &! email.endsWith("@umail.ucc.ie,")){
//            etEmail.requestFocus();
//            etEmail.setError("Please enter valid student email");
//            return;
//        }


        if (name.isEmpty()) {
            etName.setError("Please fill in all details");
            etName.requestFocus();
            return;
        }

        if (number.isEmpty()) {
            etNumber.setError("Please fill in all details");
            etNumber.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Please fill in all details");
            etPhone.requestFocus();
            return;
        }



        if (email.isEmpty()) {
            etEmail.setError("Please enter email address");
            etEmail.requestFocus();
            return;
        }

         if (password.isEmpty()) {
             etPassword.setError("Please enter your password");
             etPassword.requestFocus();
             return;
         }

//        if (spUniversity.getSelectedItem().toString() == "University College Cork"){
////            String str = "117344143@umail.ucc.ie";
//            if(email.endsWith("@umail.ucc.ie")){
//
//
//            } else {
////                Toast.makeText(StudentSignUp.this, "Please enter a valid student email.", Toast.LENGTH_SHORT).show();
//                etEmail.setError("Please enter valid student email");
//                etEmail.requestFocus();
//                return;
//            }
//        }
//




         if (password.length() < 6) {
             Toast.makeText(StudentSignUp.this, "Password too short", Toast.LENGTH_SHORT).show();
         }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(StudentSignUp.this, StudentLogin.class);
                            startActivity(intent);

                            createUser();
                        }
                        else {
//                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



    }

    private void createUser() {
        UserData user = new UserData(etName.getEditText().getText().toString(),
                etNumber.getEditText().getText().toString(),
                etEmail.getEditText().getText().toString(),
                etPhone.getEditText().getText().toString(),
                etPassword.getEditText().getText().toString());

        //String number = etNumber.getEditText().getText().toString();
        String user_id = mAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("Student").child(user_id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    finish();
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentSignUp.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initializeUI() {

        etName = findViewById(R.id.txtName);
        etEmail = findViewById(R.id.txtEmail);
        etNumber = findViewById(R.id.txtStudentNo);
        etPhone = findViewById(R.id.txtPhone);
        etPassword = findViewById(R.id.txtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        spUniversity = findViewById(R.id.spUniversitySU);
        txtUniversity = findViewById(R.id.txtUniversity);

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
        dataAdapter = new ArrayAdapter(StudentSignUp.this, android.R.layout.simple_spinner_item, universities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUniversity.setAdapter(dataAdapter);
    }

    public void startStuLogin(View view) {
        Intent intent = new Intent(StudentSignUp.this, StudentLogin.class);
        startActivity(intent);

    }
}