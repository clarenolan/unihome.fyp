package com.unihomefyp.student;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unihomefyp.PropertyDetail;
import com.unihomefyp.R;
import com.unihomefyp.models.EnquiryModel;

import java.text.DateFormat;
import java.util.Calendar;

public class MyDialogFragment extends Fragment {

    private EditText etMessage;
    private TextView etEmail, etSubject, sName, sEmail, sNumber;
    private Button btnSend, btnCancel;
    private DatabaseReference mDatabase;
    View view;
    private DatabaseReference mUser;
    private FirebaseAuth mAuth;


    public MyDialogFragment(){
        //empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.dialog_fragment,container,false);

        etEmail = view.findViewById(R.id.etEmail);
        etSubject = view.findViewById(R.id.etSubject);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        btnCancel = view.findViewById(R.id.btnCancel);
        sName = view.findViewById(R.id.tvSName);
        sEmail = view.findViewById(R.id.tvSEmail);
        sNumber = view.findViewById(R.id.tvSNumber);

        String string2 = getArguments().getString("string");
        String property_key = getArguments().getString("key");
        String property_id = getArguments().getString("propId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Landlord");

        mDatabase.child(property_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String email2 = (String) snapshot.child("email").getValue();

                etEmail.setText(email2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseDatabase.getInstance().getReference().child("Student");
        mUser.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String email2 = (String) snapshot.child("email").getValue();
                String number2 = (String) snapshot.child("phoneNo").getValue();


                sName.setText(name);
                sEmail.setText(email2);
                sNumber.setText(number2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Read Failed", Toast.LENGTH_SHORT).show();
            }

        });

        etSubject.setText(string2);
        //   etEmail.setText(property_email);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void loadEntries() {
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Landlord");
//
//        mDatabase.child(property_key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                String name = (String) snapshot.child("name").getValue();
//                String email2 = (String) snapshot.child("email").getValue();
//
//                etEmail.setText(email2);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Read Failed", Toast.LENGTH_SHORT).show();
//            }
//
//        });

    }


    private void sendEmail() {
        String mContact1 = sName.getText().toString();
        String mContact2 = sEmail.getText().toString();
        String mContact3 = sNumber.getText().toString();

        String mEmail = etEmail.getText().toString();
        String mSubject = etSubject.getText().toString();
        String mMessage = etMessage.getText().toString()+ System.getProperty("line.separator")+ System.getProperty("line.separator") + System.getProperty("line.separator") + "Student: " + mContact1 + System.getProperty("line.separator") +
                " Email: " + mContact2 + System.getProperty("line.separator") +
                " Number: " + mContact3;


        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        Toast.makeText(getContext(), "Message Sent!", Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(getActivity(), StudentNav.class);
//        startActivity(intent);

        logEnquiry();





    }

    private void logEnquiry() {
        String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String mContact1 = sName.getText().toString();
        String mContact2 = sEmail.getText().toString();
        String mContact3 = sNumber.getText().toString();
        String property_key = getArguments().getString("key");
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        String mEmail = etEmail.getText().toString();
        String mSubject = etSubject.getText().toString();
        String mMessage = etMessage.getText().toString();


        EnquiryModel user2 = new EnquiryModel(property_key,
                myCurrentDateTime,
                mSubject,
                mMessage,
                mContact1,
                mContact2,
                mContact3);



        FirebaseDatabase.getInstance().getReference("Enquiries").child(user_id).setValue(user2).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Enquiry logged", Toast.LENGTH_SHORT).show();
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(),R.anim.slide_in, R.anim.slide_out_right);
                    Intent intent = new Intent(getContext(), PropertyDetail.class);
                    getContext().startActivity(intent, options.toBundle());

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}