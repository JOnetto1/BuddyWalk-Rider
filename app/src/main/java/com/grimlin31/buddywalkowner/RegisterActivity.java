package com.grimlin31.buddywalkowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.grimlin31.buddywalkowner.model.Walker;
import com.grimlin31.buddywalkowner.sql.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Button registerButton;
    private Walker walker;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register);

        mDatabase = FirebaseDatabase.getInstance().getReference("walker");
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("walker").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if(child.child("id").getValue() != null)
                        id = Integer.parseInt(child.child("id").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        registerButton.setOnClickListener(view -> {
            EditText username = (EditText) findViewById(R.id.et_username);
            EditText email = (EditText) findViewById(R.id.et_email);
            EditText password = (EditText) findViewById(R.id.et_password);
            EditText password2 = (EditText) findViewById(R.id.et_confirmpass);

            if(!password.getText().toString().equals(password2.getText().toString())){
                Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                password.setText(null);
                password2.setText(null);
            }
            else if(!isValidEmailAddress(email.getText().toString())){
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }
            else {
                String username_str = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                walker = new Walker(id + 1, mail, pass, username_str, -33.034705, -71.596523, 0, 0);
                databaseHelper.addUser(walker);
                onRegister(mail, pass);
            }
        });
    }

    private void onRegister(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_SHORT).show();
                    updateUI();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
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
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^.+@.+\\..+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void updateUI() {
        String keyID = mDatabase.push().getKey();
        mDatabase.child(keyID).setValue(walker);
    }

    /*public void onRegister(View view) {
        Toast.makeText(this, "onRegister", Toast.LENGTH_SHORT).show();
        EditText username = (EditText) findViewById(R.id.et_username);
        EditText email = (EditText) findViewById(R.id.et_email);
        EditText password = (EditText) findViewById(R.id.et_password);
        EditText password2 = (EditText) findViewById(R.id.et_confirmpass);

        if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            password.setText(null);
            password2.setText(null);
        }
        else if(!isValidEmailAddress(email.getText().toString())){
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        else{
           String username_str = username.getText().toString();
           String mail = email.getText().toString();
           String pass = password.getText().toString();
           Walker user = new Walker();
           user.setUsername(username_str);
           user.setEmail(mail);
           user.setPassword(pass);
           databaseHelper.addUser(user);
           Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_SHORT).show();
           finish();
        }
    }*/
}
