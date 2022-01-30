package com.example.healthcenternearme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    TextView firstName, username, email, userAgent, coordinate;

    userRegister userregister;

    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile();

        firstName = findViewById(R.id.fullnamedata);
        username = findViewById(R.id.usernamedata);
        email = findViewById(R.id.emaildata);
        userAgent = findViewById(R.id.useragentdata);
        coordinate = findViewById(R.id.usercoordinatedata);

        boolean check = false;
    }//onCreate

    public void profile(){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ict602-group-project-default-rtdb.asia-southeast1.firebasedatabase.app");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userID = currentFirebaseUser.getUid();
        DatabaseReference ref = database.getReference("register").child(userID) ; //nama table

        userregister = new userRegister();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRegister ur = snapshot.getValue(userRegister.class);
                if(!check)
                {
                    firstName.setText(ur.getFullName());
                    username.setText(ur.getUserName());
                    email.setText(ur.getEmail());
                    userAgent.setText(ur.getUserAgent());
                    coordinate.setText(ur.getUserCoordinate());
                    check = true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w("TEST","Error",error.toException());
            }
        });
    }//profile()
}//appCompact