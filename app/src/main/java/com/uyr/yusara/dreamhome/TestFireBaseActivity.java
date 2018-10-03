package com.uyr.yusara.dreamhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uyr.yusara.dreamhome.Modal.Product;
import com.uyr.yusara.dreamhome.RecyclerViewTest.RecycleViewTest;

public class TestFireBaseActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText editTextName;
    private EditText editTextBrand;
    private EditText editTextDesc;
    private EditText editTextPrice;
    private EditText editTextQty;

    private FirebaseFirestore db;

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fire_base);

        db = FirebaseFirestore.getInstance();

        editTextName    = findViewById(R.id.edittext_name);
        editTextBrand   = findViewById(R.id.edittext_brand);
        editTextDesc    = findViewById(R.id.edittext_desc);
        editTextPrice   = findViewById(R.id.edittext_price);
        editTextQty     = findViewById(R.id.edittext_qty);
        Toolbar toolbar = findViewById(R.id.toolbar);


        findViewById(R.id.button_save).setOnClickListener(this);
        findViewById(R.id.textView_view_products).setOnClickListener(this);
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
    public void onClick(View v) {

        String name = editTextName.getText().toString().trim();
        String brand = editTextBrand.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String qty = editTextQty.getText().toString().trim();

        if(!validateInputs(name, brand, desc, price, qty))
        {
            CollectionReference dbProducts = db.collection("property");

            Product product = new Product(
                    name,
                    brand,
                    desc,
                    Double.parseDouble(price),
                    Integer.parseInt(qty)
            );

            dbProducts.add(product)
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
                    });
        }

    }

    public void next(View next)
    {
        Intent homeIntent = new Intent(TestFireBaseActivity.this, RecycleViewTest.class);
        startActivity(homeIntent);
    }

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
