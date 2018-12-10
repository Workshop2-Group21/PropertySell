package com.uyr.yusara.dreamhome.Menu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uyr.yusara.dreamhome.Admin.AdminMainMenu;
import com.uyr.yusara.dreamhome.Customer.MainActivityCustomer;
import com.uyr.yusara.dreamhome.MainActivity;
import com.uyr.yusara.dreamhome.R;


public class Login extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    private FirebaseFirestore db;

    private ImageView loginlogo;
    private static int SPLASH_TIME_OUT = 3000;

    private Toolbar mToolbar;

    private RelativeLayout R1,R2;
    private Animation uptodown,downtoup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null)
        {
            DetermineRole();
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.textResetPassword).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        loginlogo = findViewById(R.id.loginlogo);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        loginlogo.startAnimation(anim);

        mToolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" Sign In To Our System");

        R1 = (RelativeLayout) findViewById(R.id.R1);
        R2 = (RelativeLayout) findViewById(R.id.R2);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        R1.setAnimation(uptodown);
        R2.setAnimation(downtoup);

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String roles = "0";

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful())
                {
                    if (password.length() < 6)
                    {
                        editTextPassword.setError(getString(R.string.minimum_password));
                    }
                    else
                    {
                        Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    DetermineRole();
                    Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void DetermineRole(){
        final FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<DocumentSnapshot> xx = database.collection("Users").document(uid).get();

        xx.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String role;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    role = doc.get("role").toString();

                    if(role.equals("Agent")){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Customer")) {
                        Intent intent = new Intent(Login.this, MainActivityCustomer.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (role.equals("Admin")) {
                        Intent intent = new Intent(Login.this, AdminMainMenu.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Login.this, "Unable to find roles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



/*
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, TestFireBaseActivity.class));
        }
    }
*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, Register2.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
            case R.id.textResetPassword:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }
}
