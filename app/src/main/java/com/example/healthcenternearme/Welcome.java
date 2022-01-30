package com.example.healthcenternearme;

import static com.example.healthcenternearme.R.drawable.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.healthcenternearme.databinding.ActivityWelcomeBinding;

public class Welcome extends AppCompatActivity implements OnMapReadyCallback {

    TextView firstName;
    ImageButton medBtn;
    Button adminBtn;


    userRegister userregister;

    //Google Map
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    DrawerLayout drawerLayout;
    //NavigationView navigationView;
    //Google Map

    boolean slidestate = false;
    boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;

    private String permissionUser = "User";

    private int PROXIMITY_RADIUS = 5000;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityWelcomeBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        name();
        viewInitializations();
        //adminSetting();

        firstName = findViewById(R.id.username);

        //Nav Draw
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Nav Draw


        //Google Map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);
        //Google Map

        //Hosp
        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = false;
        //Hop
    }//end oncreate

    public void profilePage(View view){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);

    //DRAWER
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }




    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.nav_profile:
                Intent intent = new Intent(Welcome.this,Profile.class);
                startActivity(intent);
                break;

            case R.id.nav_aboutus:
                Intent intent1 = new Intent(Welcome.this,AboutUs.class);
                startActivity(intent1);
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //DRAWER

    //Google Map
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMinZoomPreference(13);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(600000);
        mLocationRequest.setSmallestDisplacement(10);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);

        }

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);



                //TEST MAP
                String Hospital = "hospital";
                String url = getUrl(location.getLatitude(),location.getLongitude(),Hospital);
                Object[] Data = new Object[2];
                Data[0] = mGoogleMap;
                Data[1] = url;
                GetNearbyPlacesData get = new GetNearbyPlacesData();
                get.execute(Data);
                //TEST MAP

                //Get Full name
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://ict602-group-project-default-rtdb.asia-southeast1.firebasedatabase.app");
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String userID = currentFirebaseUser.getUid();
                DatabaseReference ref = database.getReference("userinfo").child(userID) ; //nama table
                DatabaseReference refer = database.getReference("register").child(userID) ;
                userregister = new userRegister();
                String name;

                refer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userRegister ur = snapshot.getValue(userRegister.class);
                        if (!check1) {
                            userregister.setFullName(ur.getFullName());
                            check1 = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TEST","Error",error.toException());
                    }
                });


                //send data to database
                Date time = Calendar.getInstance().getTime();
                String user_agent = System.getProperty("http.agent");


                userregister = new userRegister();
                userregister.setDate(String.valueOf(time));
                userregister.setUserCoordinate(String.valueOf(latLng));
                userregister.setUserAgent(user_agent);



                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ref.setValue(userregister);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //simpan
                        Log.d("cadcem1","Firebase failed");
                    }
                });

                //move map camera
                if(!check2)
                {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    check2 = true;
                }

            }
        };
    };

    void adminSetting()
    {
        //find admin Button
        adminBtn = findViewById(R.id.Admin);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ict602-group-project-default-rtdb.asia-southeast1.firebasedatabase.app");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userID = currentFirebaseUser.getUid();
        DatabaseReference ref = database.getReference("register").child(userID) ; //nama table

        userregister = new userRegister();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRegister ur = snapshot.getValue(userRegister.class);
                permissionUser =  ur.getPermission();
                Log.d("TESTJADI",permissionUser);
                adminBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                permissionUser = "User";
                Log.d("TESTFAILED",permissionUser);
                adminBtn.setVisibility(View.GONE);
            }
        });
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAy4hWUGyRLVpSFdTYxAewQWyqte_Helrc");
        //Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Welcome.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //Google Map

    @Override
    public void onPause()
    {
        super.onPause();

        //stop location update if activity not active
        if(mFusedLocationClient != null)
        {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    void viewInitializations() {

        //ImageButton
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

    public void name(){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ict602-group-project-default-rtdb.asia-southeast1.firebasedatabase.app");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userID = currentFirebaseUser.getUid();
        DatabaseReference ref = database.getReference("register").child(userID) ; //nama table

        userregister = new userRegister();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRegister ur = snapshot.getValue(userRegister.class);
                if(!check3)
                {
                    firstName.setText(ur.getFullName());
                    check3 = true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w("TEST","Error",error.toException());
            }
        });



    }

}