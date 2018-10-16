package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextphone;

    //private CircleImageView userProfImage;

    private DocumentReference SettinguserRef;
    private FirebaseAuth mAuth;

    private String currentUserid;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        SettinguserRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);

        editTextEmail    = findViewById(R.id.edittext_email);
        editTextName   = findViewById(R.id.edittext_fullname);
        editTextphone    = findViewById(R.id.edittext_phone);

        findViewById(R.id.button_update).setOnClickListener(this);

        SettinguserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.i("LOGGER",document.getString("email"));

                        String myProfileemail = document.getString("email");
                        String myProfilename = document.getString("name");
                        String myProfilephone = document.getString("phone");

                        editTextEmail.setText(myProfileemail);
                        editTextName.setText(myProfilename);
                        editTextphone.setText(myProfilephone);

                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

/*        SettinguserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    String myProfileemail = documentSnapshot.getString("email");
                    String myProfilename = documentSnapshot.getDocumentReference("name").toString();
                    String myProfilephone = documentSnapshot.getDocumentReference("phone").toString();

                    editTextEmail.setText("Hi");
                    editTextName.setText(myProfilename);
                    editTextphone.setText(myProfilephone);
                }
            }
        });*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_update:
                //saveProduct();
                break;

        }

    }
}
