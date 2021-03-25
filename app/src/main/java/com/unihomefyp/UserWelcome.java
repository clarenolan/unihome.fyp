package com.unihomefyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.unihomefyp.landlord.LandlordLogin;
import com.unihomefyp.student.StudentLogin;

public class UserWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);



    }

    public void startLandlord(View view) {
        Intent intToProfile = new Intent(UserWelcome.this, LandlordLogin.class);
        startActivity(intToProfile);
    }

    public void startStudent(View view) {
        Intent intToProfile = new Intent(UserWelcome.this, StudentLogin.class);
        startActivity(intToProfile);
    }
}