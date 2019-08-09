package com.example.sciencevision;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LaunchActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;
    LinearLayout llLogin;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        llLogin = findViewById(R.id.llLogin);
        tvTitle = findViewById(R.id.tvTitle);

        YoYo.with(Techniques.FadeIn)
                .duration(2100)
                .repeat(0)
                .playOn(llLogin);
        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .repeat(0)
                .playOn(tvTitle);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "Welcome, " + currentUser.getUsername(), Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // show the login screen
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), "Please enter a username!", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(etUsername);
                }
                else if (etPassword.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(etPassword);
                } else{
                    ParseUser.logInInBackground(String.valueOf(etUsername.getText()), String.valueOf(etPassword.getText()), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                Toast.makeText(getApplicationContext(), "Username or password is incorrect!", Toast.LENGTH_LONG).show();
                                YoYo.with(Techniques.Shake)
                                        .duration(500)
                                        .playOn(etUsername);
                                YoYo.with(Techniques.Shake)
                                        .duration(500)
                                        .playOn(etPassword);
                            }
                        }
                    });
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseUser user = new ParseUser();
                // Set core properties
                user.setUsername(String.valueOf(etUsername.getText()));
                user.setPassword(String.valueOf(etPassword.getText()));

                AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                LinearLayout parentEmailLayout = new LinearLayout(getApplicationContext());
                parentEmailLayout.setOrientation(LinearLayout.VERTICAL);

                TextView tvEmailPrompt = new TextView(getApplicationContext());
                tvEmailPrompt.setText("Enter your parent's email address.");
                EditText etEmailInput = new EditText(getApplicationContext());
                etEmailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                Button btSubmit = new Button(getApplicationContext());
                btSubmit.setText("SUBMIT");


                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidEmail(etEmailInput.getText().toString())) { //email text validated
                            // Invoke signUpInBackground
                            user.setEmail(etEmailInput.getText().toString());
                            user.put("NumberOfFindings", 0);
                            user.put("EarnBadge",Boolean.parseBoolean("True"));
                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Welcome, new user!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), ChangeProfilePicture.class);
                                        startActivity(i);
                                    } else {
                                        // Sign up didn't succeed. Look at the ParseException
                                        // to figure out what went wrong
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter a valid email address!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                parentEmailLayout.addView(tvEmailPrompt, 0);
                parentEmailLayout.addView(etEmailInput, 1);
                parentEmailLayout.addView(btSubmit, 2);


                builder.setView(parentEmailLayout);
                AlertDialog dialog = builder.create();
                dialog.show();


            }


        });

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            return btnLogin.callOnClick();
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
