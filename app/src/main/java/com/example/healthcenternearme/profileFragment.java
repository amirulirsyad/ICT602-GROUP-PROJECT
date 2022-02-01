package com.example.healthcenternearme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profileFragment extends Fragment {

    TextView firstName, username, email, userAgent, coordinate;

    userRegister userregister;

    boolean check = false;

    public profileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profile();

        firstName = v.findViewById(R.id.fullnamedata);
        username = v.findViewById(R.id.usernamedata);
        email = v.findViewById(R.id.emaildata);
        userAgent = v.findViewById(R.id.useragentdata);
        coordinate = v.findViewById(R.id.usercoordinatedata);
        //last
        return v;
    }

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
    }
}