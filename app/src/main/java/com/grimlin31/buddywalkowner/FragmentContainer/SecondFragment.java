package com.grimlin31.buddywalkowner.FragmentContainer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grimlin31.buddywalkowner.R;
import com.grimlin31.buddywalkowner.WalkRiderHomeActivity;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    GoogleMap mMap;
    MapView mMapView;
    View mView;
    LatLng latLng;
    List<Marker> markers;
    private DatabaseReference ref1;
    private DatabaseReference ref2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userIndex;
    private String walkerIndex;
    private String notification;
    private double latitude;
    private double longitude;
    private FloatingActionButton cancel;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        latLng = new LatLng(-33.034705, -71.596523);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle data = getArguments();
        if(data != null) {
            walkerIndex = data.getString("walkerIndex");
            if(!Objects.equals(data.getString("userIndex"), "")) {
                userIndex = data.getString("userIndex");
                latitude = Double.parseDouble(data.getString("latitude"));
                longitude = Double.parseDouble(data.getString("longitude"));
            }
        }
        mView = inflater.inflate(R.layout.fragment_second, container, false);
        cancel = (FloatingActionButton) mView.findViewById(R.id.cancel);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(walkerIndex);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMapStyle(new MapStyleOptions(getResources().getString(R.string.dia_json)));
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            ref1 = FirebaseDatabase.getInstance().getReference();
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    ref1.child("walker").child(walkerIndex).child("latitude").setValue(location.getLatitude());
                    ref1.child("walker").child(walkerIndex).child("longitude").setValue(location.getLongitude());
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("walker");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(walkerIndex).child("current").getValue() != null) {
                        notification = String.valueOf(dataSnapshot.child(walkerIndex).child("current").getValue());
                        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng((Double) dataSnapshot.child(walkerIndex).child("notifications").
                                        child(notification).child("latitude").getValue(), (Double) dataSnapshot.child(walkerIndex).child("notifications").
                                        child(notification).child("longitude").getValue()))
                                .title("Meetup location"));
                        marker.showInfoWindow();
                        cancel.setVisibility(View.VISIBLE);
                    }
                    else{
                        cancel.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            map.setOnMarkerClickListener(this);
            map.getUiSettings().setMapToolbarEnabled(false);

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
        MapsInitializer.initialize(getContext());

        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        /*
        if(checkPermission()) {
            //mGoogleMap.setMyLocationEnabled(true);
            // Showing the current location in Google Map
        }
        */
        CameraPosition USM = CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(USM));

    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        Uri myUri = Uri.parse("geo:0,0?q=" + marker.getPosition().latitude +"," + marker.getPosition().longitude);

        Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
        startActivity(intent);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void cancel(String walkerIndex){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sureCancel);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ref2 = FirebaseDatabase.getInstance().getReference().child("walker").child(walkerIndex);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ref2.child("busy").setValue(0);
                        ref2.child("current").removeValue();
                        ref2.child("notifications").child(notification).child("pending").setValue(1);
                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("user").child(userIndex);
                        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref3.child("notifications").child(notification).child("pending").setValue(1);
                                ref3.child("current").removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });

                Intent refresh = new Intent(getActivity(), WalkRiderHomeActivity.class);
                refresh.putExtra("userIndex", "");
                refresh.putExtra("walkerIndex", walkerIndex);
                refresh.putExtra("latitude", "");
                refresh.putExtra("longitude", "");
                startActivity(refresh);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}