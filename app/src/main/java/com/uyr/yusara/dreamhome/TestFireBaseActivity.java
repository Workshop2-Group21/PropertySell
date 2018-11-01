package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.uyr.yusara.dreamhome.Modal.Product;
import com.uyr.yusara.dreamhome.Modal.User;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Context;

public class TestFireBaseActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText editTextName;
    private EditText editTextBrand;
    private EditText editTextDesc;
    private EditText editTextPrice;
    private EditText editTextQty;

    private TextView userEmail;

    FirebaseUser firebaseUser;

    private CircleImageView ProfileImage;
    final static int gallerypick = 1;
    private StorageReference UserProfileImageRef;

    private FirebaseAuth mAuth;
    private String currentUserid;

    private DocumentReference UsersRef;
    //private DatabaseReference UserRef2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection("Users");

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fire_base);

        db = FirebaseFirestore.getInstance();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        editTextName    = findViewById(R.id.edittext_name);
        editTextBrand   = findViewById(R.id.edittext_brand);
        editTextDesc    = findViewById(R.id.edittext_desc);
        editTextPrice   = findViewById(R.id.edittext_price);
        editTextQty     = findViewById(R.id.edittext_qty);
        ProfileImage    = findViewById(R.id.profile_image);

        Toolbar toolbar = findViewById(R.id.toolbar);

        userEmail = findViewById(R.id.tvUserEmail);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail.setText(firebaseUser.getEmail());

        //get current user
        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserid);
        //UserRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);


        findViewById(R.id.button_save).setOnClickListener(this);
        findViewById(R.id.textView_view_products).setOnClickListener(this);
        findViewById(R.id.profile_image).setOnClickListener(this);

        //DIsplay back Image
        UsersRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    String image = documentSnapshot.getString("profileimage2");

                    //Picasso.get().load(image).placeholder(R.drawable.cc).into(ProfileImage);
                    Glide.with(TestFireBaseActivity.this).load(image).into(ProfileImage);
                }

            }
        });
    }

    private boolean validateInputs(String name, String brand, String desc, String price, String qty)
    {
        if(name.isEmpty())
        {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return true;
        }
        if(brand.isEmpty())
        {
            editTextBrand.setError("Brand required");
            editTextBrand.requestFocus();
            return true;
        }
        if(desc.isEmpty())
        {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return true;
        }
        if(price.isEmpty())
        {
            editTextPrice.setError("Price required");
            editTextPrice.requestFocus();
            return true;
        }
        if(qty.isEmpty())
        {
            editTextQty.setError("Quantity required");
            editTextQty.requestFocus();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
/*        cr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
*//*                if (e != null) {
                    Toast.makeText(TestFireBaseActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();

                    return;
                }*//*

                if (documentSnapshot.exists()) {

                    Intent homeIntent = new Intent(TestFireBaseActivity.this, RecycleViewTest.class)
                    startActivity(new Intent(this, Register2.class));
                }
            }
        });*/

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_save:
                saveProduct();
                break;
            case R.id.textView_view_products:
                startActivity(new Intent(this, ProductsActitvity.class));
                break;
            case R.id.profile_image:
                toGallery();
                break;

        }
    }

    private void saveProduct() {

        String name = editTextName.getText().toString().trim();
        String brand = editTextBrand.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String qty = editTextQty.getText().toString().trim();

        if(!validateInputs(name, brand, desc, price, qty))
        {
            //CollectionReference dbProducts = db.collection("property");

            Product product = new Product(
                    name,
                    brand,
                    desc,
                    Double.parseDouble(price),
                    Integer.parseInt(qty)
            );

            FirebaseFirestore.getInstance().collection("property")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(TestFireBaseActivity.this, "Product Added", Toast.LENGTH_LONG).show();

                    } else {
                        //display a failure message

                        Toast.makeText(TestFireBaseActivity.this, "Product fail to add", Toast.LENGTH_LONG).show();
                    }

                }
            });


/*            dbProducts.add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(TestFireBaseActivity.this, "Product Added", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(TestFireBaseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });*/
        }

    }

    public void toGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypick && resultCode == RESULT_OK && data!= null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Toast.makeText(TestFireBaseActivity.this, "msuk requestcode", Toast.LENGTH_LONG).show();

            if(resultCode == RESULT_OK)
            {
                Toast.makeText(TestFireBaseActivity.this, "result masuk", Toast.LENGTH_LONG).show();
                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserid + ".jpg");

                //save the crop inside firebase storage
                //save the link inside firebase
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {


                        if(task.isSuccessful())
                        {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String downloadUrl = String.valueOf(filePath.getDownloadUrl());
                                    Log.i("downloadUrl",downloadUrl);

                                    //Important msukkan url ke dlam profileimage2
                                    String uriurl = uri.toString();
                                    Log.i("uri",uri.toString());

                                    Toast.makeText(TestFireBaseActivity.this, "Your Image Successfully Uploaded ", Toast.LENGTH_SHORT).show();

                                    HashMap userMap = new HashMap();
                                    userMap.put("profileimage2", uriurl);
                                    //Pilihan untuk update
                                    //UsersRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {

                                    UsersRef.set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(TestFireBaseActivity.this, "Link update successfully ", Toast.LENGTH_SHORT).show();
                                                Intent selfIntent = new Intent(TestFireBaseActivity.this, TestFireBaseActivity.class);
                                                startActivity(selfIntent);
                                                Toast.makeText(TestFireBaseActivity.this, "Profile Image Store to Firebase Success", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(TestFireBaseActivity.this, "update image link error ", Toast.LENGTH_SHORT).show();
                                                String message = task.getException().getMessage();
                                                Toast.makeText(TestFireBaseActivity.this, "Error Occured" + message, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            });


/*                            UsersRef.collection("profileimage2").document(downloadUrl).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e)
                                {

                                    if(task.isSuccessful())
                                    {
                                        Updateprofilelink(downloadUrl);
                                        Intent selfIntent = new Intent(TestFireBaseActivity.this, TestFireBaseActivity.class);
                                        startActivity(selfIntent);
                                        Toast.makeText(TestFireBaseActivity.this, "Profile Image Store to Firebase Success", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                        {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(TestFireBaseActivity.this, "Error Occured" + message, Toast.LENGTH_LONG).show();
                                        }
                                }
                            });*/
                        }

                    }
                });

            }
            else
                {
                    Toast.makeText(TestFireBaseActivity.this, "Error Occured: Image cant be crop, try again", Toast.LENGTH_LONG).show();
                }
        }
    }

    //Untuk update link image dlm db
    private void Updateprofilelink(String downloadUrl) {

/*        HashMap userMap = new HashMap();
        userMap.put("profileimage2", downloadUrl);
        UsersRef.update(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(TestFireBaseActivity.this, "Link update successfully ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(TestFireBaseActivity.this, "update image link error ", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    //
    // for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    //for toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuLogout:

            FirebaseAuth.getInstance().signOut();;
            finish();
            startActivity(new Intent(this, Login.class));

            break;
        }
        return true;
    }
}
