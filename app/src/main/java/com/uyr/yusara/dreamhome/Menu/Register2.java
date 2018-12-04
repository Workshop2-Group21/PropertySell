package com.uyr.yusara.dreamhome.Menu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.uyr.yusara.dreamhome.Modal.User;
import com.uyr.yusara.dreamhome.R;

public class Register2 extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //test push
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhone = findViewById(R.id.edit_text_phone);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String role = "agent";
        final String profileimages2 = "https://firebasestorage.googleapis.com/v0/b/dreamhome-a806d.appspot.com/o/profile%20Images%2Fnophoto.png?alt=media&token=724e2036-5419-4eef-bbeb-d8b0083ea123";
        final String profiledescription = "";
        final String devicetoken = FirebaseInstanceId.getInstance().getToken();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    role,
                                    profileimages2,
                                    profiledescription,
                                    devicetoken

                            );


                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register2.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }

                                }
                            })


/*                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            })*/;

                        } else {
                            Toast.makeText(Register2.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

/*    public void next(View view)
    {
        Intent homeIntent = new Intent(Register2.this, TestFireBaseActivity.class);
        startActivity(homeIntent);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }
}
