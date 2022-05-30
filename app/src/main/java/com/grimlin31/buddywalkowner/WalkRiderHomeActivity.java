package com.grimlin31.buddywalkowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grimlin31.buddywalkowner.FragmentContainer.FirstFragment;
import com.grimlin31.buddywalkowner.FragmentContainer.SecondFragment;
import com.grimlin31.buddywalkowner.FragmentContainer.ThirdFragment;
import com.grimlin31.buddywalkowner.SaveSharedPreferences.SavedSharedPreference;

public class WalkRiderHomeActivity extends AppCompatActivity {

    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_rider_home);

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
        finish();
    }
}