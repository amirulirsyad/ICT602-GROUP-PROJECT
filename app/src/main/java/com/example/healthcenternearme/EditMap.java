package com.example.healthcenternearme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class EditMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_map);

        final LatLng latlng = (LatLng) getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        Button btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(latlng);
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                String text ;
                if (title.getText() != null)
                {
                    text = title.getText().toString();
                    marker.title(text);
                }
                else
                {
                    text = "Custom Hospital";
                    marker.title(text);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("marker", marker);
                setResult(Activity.RESULT_OK, resultIntent);

                String data = String.valueOf(latlng.latitude) + "," + String.valueOf(latlng.longitude) + ";" +  text + "\n";

                File directory = new File(Environment.getExternalStorageDirectory() + File.separator , "map.txt");


                //Insert to file
                try
                {
                    /*FileWriter file = new FileWriter("map.txt",true);
                    file.write(data);
                    file.close();
                    Log.d("EDITMAP","SUCCESS = "+data);*/
                    if(!directory.exists())
                    {
                        directory.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(directory,true);
                    fos.write(data.getBytes(StandardCharsets.UTF_8));
                    fos.close();
                    Log.d("EDITMAP","SUCCESS = "+data);
                }
                catch(Exception e)
                {
                    Log.d("EDITMAP","ERROR "+e);
                }

                //Insert to file

                finish();
            }
        });
    }
}