package com.grimlin31.buddywalkowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.grimlin31.buddywalkowner.FragmentContainer.FirstFragment;
import com.grimlin31.buddywalkowner.FragmentContainer.SecondFragment;
import com.grimlin31.buddywalkowner.FragmentContainer.ThirdFragment;
import com.grimlin31.buddywalkowner.SaveSharedPreferences.SavedSharedPreference;

import java.util.Objects;

public class WalkRiderHomeActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    FirstFragment  firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment  thirdFragment = new ThirdFragment();
    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private String walkerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_rider_home);

        Intent intent = getIntent();
        walkerIndex = intent.getStringExtra("walkerIndex");

        Bundle bundle = new Bundle();
        bundle.putString("walkerIndex", walkerIndex);
        if(!Objects.equals(intent.getStringExtra("userIndex"), "")) {
            bundle.putString("userIndex", intent.getStringExtra("userIndex"));
            LatLng latLng = new LatLng(intent.getDoubleExtra("latitude", 0),
                    intent.getDoubleExtra("longitude", 0));
            bundle.putString("latitude", String.valueOf(latLng.latitude));
            bundle.putString("longitude", String.valueOf(latLng.longitude));
            bundle.putString("notification", intent.getStringExtra("notification"));
        }
        else {
            bundle.putString("userIndex", "");
            bundle.putString("notification", "");
            bundle.putString("latitude", "");
            bundle.putString("longitude", "");
        }

        firstFragment.setArguments(bundle);
        secondFragment.setArguments(bundle);
        thirdFragment.setArguments(bundle);

        BottomNavigationView navigation = findViewById(R.id.home_navigator);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(!checkPermission())
        {
            askPermission();
        }
        loadFragment(secondFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.firstFragment:
                    loadFragment(firstFragment);
                    return true;
                case R.id.secondFragment:
                    loadFragment(secondFragment);
                    return true;
                case R.id.thirdFragment:
                    loadFragment(thirdFragment);
                    return true;
            }
            return false;
        }
    };


    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_home, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if(SavedSharedPreference.getUserName(WalkRiderHomeActivity.this).length() == 0){
            finish();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                200);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                // Si el request es cancelado entonces el arreglo tiene largo 0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //se aceptaron los permisos, hacer algo
                } else {
                    //se rechazaron los permisos, hacer otra cosa
                    Toast.makeText(getApplicationContext(), "Localization access is needed to use this app.", Toast.LENGTH_SHORT);
                    SavedSharedPreference.clearUserName(WalkRiderHomeActivity.this);
                    finish();
                }

                return;
            }
        }
    }

    public void toLogout(View view) {
        SavedSharedPreference.clearUserName(WalkRiderHomeActivity.this);
        FirebaseAuth.getInstance().signOut();
        finish();
        System.exit(0);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
    }
}