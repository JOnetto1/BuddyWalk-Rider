package com.grimlin31.buddywalkowner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grimlin31.buddywalkowner.SaveSharedPreferences.SavedSharedPreference;
import com.grimlin31.buddywalkowner.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(LoginActivity.this);
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
        if(SavedSharedPreference.getUserName(LoginActivity.this).length() != 0){
            Intent gotoHome = new Intent(LoginActivity.this, WalkRiderHomeActivity.class);
            startActivity(gotoHome);
        }

    }

    public void toRegister(View view) {
        Intent gotoRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(gotoRegister);


    }

    public void toLogin(View view) {
        EditText email = (EditText) findViewById(R.id.et_loginEmail);
        EditText password = (EditText) findViewById(R.id.et_loginPassword);

        //Verificar campos son validos

        //Verificar valores de campos

        if(databaseHelper.checkUser(email.getText().toString().trim(), password.getText().toString().trim()))
        {
            //Login success
            Intent gotoHome = new Intent(LoginActivity.this, WalkRiderHomeActivity.class);
            SavedSharedPreference.setUserName(LoginActivity.this, email.getText().toString());
            startActivity(gotoHome);

            emptyInputEditText();
        } else {
            //Login failure
            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
        }
    }

    private void emptyInputEditText() {
        EditText email = (EditText) findViewById(R.id.et_loginEmail);
        EditText password = (EditText) findViewById(R.id.et_loginPassword);
        email.setText(null);
        password.setText(null);
    }

    public void toForgotPassword(View view) {
    }
}