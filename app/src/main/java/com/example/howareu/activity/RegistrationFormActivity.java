package com.example.howareu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.howareu.R;
import com.example.howareu.databases.repository.UserRepository;
import com.example.howareu.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationFormActivity extends AppCompatActivity {


    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mCPasswordEditText;

    private FirebaseAuth mAuth;
    private Button mSignUpButton;
    private UserRepository userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.et_email);

        mUsernameEditText= findViewById(R.id.et_username);

        mPasswordEditText = findViewById(R.id.et_password);


        mCPasswordEditText = findViewById(R.id.et_c_password);

        mSignUpButton = findViewById(R.id.btn_sign_up);


        userDb = new UserRepository(getApplication());


    }

    public void register(View view){
        String email = mEmailEditText.getText().toString();
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String cPassword = mCPasswordEditText.getText().toString();




        if(!checkError(email,password,cPassword)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    User userModel = new User(null,email,username);
                                    userDb.addUser(userModel,isNetworkConnected());
                                    // Update UI with results on the main thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateUI(user);
                                        }
                                    });
                                    return null;
                                }
                            }.execute();




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationFormActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }});
        }

    }

    public boolean checkError(String email, String password, String cPassword) {
        Boolean hasError =false;
        if (email.isEmpty()) {
            mEmailEditText.setError("Email is required");
            mEmailEditText.requestFocus();
            hasError = true;
        }

        if (password.isEmpty()) {
            mPasswordEditText.setError("Password is required");
            mPasswordEditText.requestFocus();
            hasError = true;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Enter a valid email");
            mEmailEditText.requestFocus();
            hasError = true;
        }

        if (password.length() < 6) {
            mPasswordEditText.setError("Password should be at least 6 characters long");
            mPasswordEditText.requestFocus();
            hasError = true;
        }

        if (!cPassword.equals(password)) {
            mCPasswordEditText.setError("Passwords do not match");
            mCPasswordEditText.requestFocus();
            hasError = true;
        }

        return hasError;

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegistrationFormActivity.this, OnlineLoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
