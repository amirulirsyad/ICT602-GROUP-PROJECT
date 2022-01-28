package com.example.healthcenternearme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {

    ImageButton medBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewInitializations();
    }

    void viewInitializations() {
        medBtn = (ImageButton)findViewById(R.id.medicButton);
        medBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Welcome.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }


}