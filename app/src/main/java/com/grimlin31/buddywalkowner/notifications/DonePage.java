package com.grimlin31.buddywalkowner.notifications;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grimlin31.buddywalkowner.R;
import com.grimlin31.buddywalkowner.WalkRiderHomeActivity;

public class DonePage extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private String userIndex;
    private String walkerIndex;
    private String notification;
    private String userPhone;
    private Button callbutton;
    private String message;
    private Button confirmButton;
    private LatLng lugar;
    private DatabaseReference ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_page);
        callbutton = (Button) findViewById(R.id.callButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPhone = String.valueOf(dataSnapshot.child(userIndex).child("phone").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        callbutton.setOnClickListener(view -> {
            Intent phoneCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",userPhone, null));
            startActivity(phoneCall);
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
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
    public void onMapReady(@NonNull GoogleMap map) {
        Intent intent = getIntent();
        userIndex = intent.getStringExtra("userIndex");
        walkerIndex = intent.getStringExtra("walkerIndex");
        notification = intent.getStringExtra("notification");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMapStyle(new MapStyleOptions(getResources().getString(R.string.dia_json)));
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            ref = FirebaseDatabase.getInstance().getReference().child("walker").child(walkerIndex).
                    child("notifications").child(notification);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lugar = new LatLng((Double) dataSnapshot.child("latitude").getValue(),
                            (Double) dataSnapshot.child("longitude").getValue());
                    message = String.valueOf(dataSnapshot.child("message").getValue());
                    Marker marker = map.addMarker(new MarkerOptions().position(lugar)
                            .title("Your meetup location!"));
                    marker.showInfoWindow();
                    CameraPosition USM = CameraPosition.builder().target(lugar).zoom(16).build();
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(USM));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
    }
}
