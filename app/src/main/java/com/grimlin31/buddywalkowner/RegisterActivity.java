package com.grimlin31.buddywalkowner;

import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.grimlin31.buddywalkowner.model.User;
import com.grimlin31.buddywalkowner.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(RegisterActivity.this);
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

    public void onRegister(View view) {
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
           User user = new User();
           user.setUsername(username_str);
           user.setEmail(mail);
           user.setPassword(pass);
           databaseHelper.addUser(user);
           Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_SHORT).show();
           finish();
        }
    }
}
