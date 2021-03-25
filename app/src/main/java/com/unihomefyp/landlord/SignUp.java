package com.unihomefyp.landlord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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


public class SignUp extends AppCompatActivity {

    private Button btnRegister;
    private FirebaseAuth mAuth;
    private TextView tvSignIn;
    private TextInputLayout etName2, etEmail2, etUser2, etPhone2, etPassword2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();


        //Redirect user to landlord sign in page
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (SignUp.this, LandlordLogin.class);
                startActivity(i);
            }
        });




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser2();
            }
        });


    }


    private void registerUser2() {
            String name2 = etName2.getEditText().getText().toString();
            String username2 = etUser2.getEditText().getText().toString().trim();
            String email2 = etEmail2.getEditText().getText().toString().trim();
            String phone2 = etPhone2.getEditText().getText().toString().trim();
            String password2 = etPassword2.getEditText().getText().toString().trim();



            if (username2.isEmpty()) {
                etUser2.setError("Please fill in all details");
                etUser2.requestFocus();
                return;
            }

            if (phone2.isEmpty()) {
                etPhone2.setError("Please fill in all details");
                etPhone2.requestFocus();
                return;
            }


            if (name2.isEmpty()) {
                etName2.setError("Please fill in all details");
                etName2.requestFocus();
                return;
            }

            if (email2.isEmpty()) {
                etEmail2.setError("Please enter email2 address");
                etEmail2.requestFocus();
                return;
            }

            if (password2.isEmpty()) {
                etPassword2.setError("Please enter your password2");
                etPassword2.requestFocus();
                return;
            }


            if (password2.length() < 6) {
                Toast.makeText(SignUp.this, "Password too short", Toast.LENGTH_SHORT).show();
            }



            mAuth.createUserWithEmailAndPassword(email2, password2)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);


                                Intent intent = new Intent(SignUp.this, LandlordLogin.class);
                                startActivity(intent);

                                createUser2();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });



        }

        private void createUser2() {
            UserData user2 = new UserData(etName2.getEditText().getText().toString(),
                    etUser2.getEditText().getText().toString(),
                    etEmail2.getEditText().getText().toString(),
                    etPhone2.getEditText().getText().toString(),
                    etPassword2.getEditText().getText().toString());
            String user_id = mAuth.getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference("Landlord").child(user_id).setValue(user2).addOnCompleteListener(new OnCompleteListener<Void>() {


                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(SignUp.this, "UserUploaded", Toast.LENGTH_SHORT).show();

                        finish();
                    }


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    private void initializeUI() {
        etName2 = findViewById(R.id.txtName2);
        etEmail2 = findViewById(R.id.txtEmail2);
        etUser2 = findViewById(R.id.txtUserName2);
        etPhone2 = findViewById(R.id.txtPhone2);
        etPassword2 = findViewById(R.id.txtPassword2);
        btnRegister = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar2);
        tvSignIn = findViewById(R.id.tvAlreadyHave);
    }

}