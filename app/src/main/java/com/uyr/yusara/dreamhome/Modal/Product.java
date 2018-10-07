package com.uyr.yusara.dreamhome.Modal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uyr.yusara.dreamhome.R;
import com.uyr.yusara.dreamhome.TestFireBaseActivity;

import java.io.Serializable;

public class Product implements Serializable {

    @Exclude private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name, brand, description;
    private double price;
    private int qty;

    public Product()
    {

    }

    public Product(String name, String brand, String description, double price, int qty) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

}
