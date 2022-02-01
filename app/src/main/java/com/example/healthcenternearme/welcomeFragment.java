package com.example.healthcenternearme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;


public class welcomeFragment extends Fragment implements OnMapReadyCallback {

    TextView firstName;
    ImageButton medBtn;
    Button notifBtn;


    userRegister userregister;

    //Google Map
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    //NavigationView navigationView;
    //Google Map

    boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;

    private int PROXIMITY_RADIUS = 5000;

    Switch switchtheme;

    public welcomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //DO NOT CHANGE
        View v ;

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            // create ContextThemeWrapper from the original Activity Context with the custom theme
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.DarkTheme);
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

            //setTheme(R.style.DarkTheme);
            //DO NOT CHANGE
            v = localInflater.inflate(R.layout.fragment_welcome, container, false);
        }
        else {
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            //setTheme(R.style.AppTheme);
            //DO NOT CHANGE
            v = localInflater.inflate(R.layout.fragment_welcome, container, false);
        }



        super.onCreate(savedInstanceState);

        //findview
        firstName = v.findViewById(R.id.username);
        notifBtn = v.findViewById(R.id.notification);
        medBtn = v.findViewById(R.id.medicButton);
        //findview

        //button onClick
        medBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                startActivity(intent);
            }
        });
        //button onClick



        switchtheme=(Switch) v.findViewById(R.id.switch1);

        switchtheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        name();
        viewInitializations();

        //Google Map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapFrag = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapView);
        assert mapFrag != null;
        mapFrag.getMapAsync(this);
        //Google Map


        //last...DO NOT CHANGE
        return v;
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMinZoomPreference(13);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(600000);
        mLocationRequest.setSmallestDisplacement(10);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(),
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

                //Place User's marker
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "map.txt");
                try
                {
                    FileInputStream file = new FileInputStream(directory);
                    Scanner sc = new Scanner(file);

                    while (sc.hasNext())
                    {
                        String[] position ;
                        String line,text;

                        line = sc.nextLine();
                        StringTokenizer check = new StringTokenizer(line,";");
                        position = check.nextToken().split(",");

                        text = check.nextToken();

                        double lat = Double.parseDouble(position[0]);
                        double log = Double.parseDouble(position[1]);

                        LatLng loc = new LatLng(lat,log);
                        MarkerOptions MO = new MarkerOptions();
                        MO.position(loc);
                        MO.title(text);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mGoogleMap.addMarker(MO);
                        Log.d("MAP","MAPS SUCCESS = "+loc);
                    }
                }
                catch(Exception e)
                {
                    Log.d("MAP","MAPS MARK ERROR = "+e);
                }
                //Place User's marker


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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(requireActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
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
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

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