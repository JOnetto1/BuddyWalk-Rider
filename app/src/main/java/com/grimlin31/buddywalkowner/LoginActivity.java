package com.grimlin31.buddywalkowner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    }

    public void toRegister(View view) {
    }

    public void toLogin(View view) {
        EditText email = (EditText) findViewById(R.id.et_loginEmail);
        EditText password = (EditText) findViewById(R.id.et_loginPassword);

        //Verificar campos son validos

        //Verificar valores de campos

        if(email.getText().toString().equals("admin") && password.getText().toString().equals("123"))
        //if(databaseHelper.checkUser(email.getText().toString().trim(), password.getText().toString().trim()))
        {
            //Login success
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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