package com.grimlin31.buddywalkowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grimlin31.buddywalkowner.SaveSharedPreferences.SavedSharedPreference;
import com.grimlin31.buddywalkowner.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(LoginActivity.this);

        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /*if(SavedSharedPreference.getUserName(LoginActivity.this).length() != 0){
            Intent gotoHome = new Intent(LoginActivity.this, WalkRiderHomeActivity.class);
            startActivity(gotoHome);
        }*/

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        if(currentUser!= null)
            updateUI(currentUser);
    }

    public void toRegister(View view) {
        Intent gotoRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(gotoRegister);
    }

    public void toLogin(View view) {
        EditText email = (EditText) findViewById(R.id.et_loginEmail);
        EditText password = (EditText) findViewById(R.id.et_loginPassword);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser walker = mAuth.getCurrentUser();
                            SavedSharedPreference.setUserName(LoginActivity.this, email.getText().toString());
                            updateUI(walker);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            //Toast.makeText(CustomAuthActivity.this, "Authentication failed.",
                            //      Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

        // Aun la vuelve loca
        /*if(databaseHelper.checkWalker(email.getText().toString().trim(), password.getText().toString().trim()))
        {
            //Login success
            Intent gotoHome = new Intent(LoginActivity.this, WalkRiderHomeActivity.class);
            SavedSharedPreference.setUserName(LoginActivity.this, email.getText().toString());
            startActivity(gotoHome);

            emptyInputEditText();
        } else {
            //Login failure
            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void emptyInputEditText() {
        EditText email = (EditText) findViewById(R.id.et_loginEmail);
        EditText password = (EditText) findViewById(R.id.et_loginPassword);
        email.setText(null);
        password.setText(null);
    }

    public void toForgotPassword(View view) {
    }

    public void updateUI(FirebaseUser currentUser){
        Intent gotoHome = new Intent(this, WalkRiderHomeActivity.class);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("walker").orderByKey();
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if(currentUser.getEmail().equals(child.child("email").getValue().toString())){
                        String walkerIndex = child.getKey();
                        //Log.i("Hola", walkerIndex);
                        gotoHome.putExtra("walkerIndex", walkerIndex);
                        startActivity(gotoHome);
                        emptyInputEditText();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}