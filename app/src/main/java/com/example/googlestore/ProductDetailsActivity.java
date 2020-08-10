package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.Products;
import Prevalent.Prevalent;
import maes.tech.intentanim.CustomIntent;
import pl.droidsonroids.gif.GifImageView;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView addToCartBtn , wishlist , productImage;
    private TextView productPrice, productName, productDescription, qtyvalue;
    private String productID = "";
    private String RandomKey;
    private Spinner sp;
    String[] quantity;
    private String a, b;
    private GifImageView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        qtyvalue = findViewById(R.id.qtyvalue);
        productID = getIntent().getStringExtra("pid");
        addToCartBtn = (ImageView) findViewById(R.id.Product_cart);
        productPrice = (TextView) findViewById(R.id.ProductPrice);
        productImage = (ImageView) findViewById(R.id.productImage);
        productName = (TextView) findViewById(R.id.ProductName);
        productDescription = (TextView) findViewById(R.id.product_details);
        sp = findViewById(R.id.spinner);
        quantity = getResources().getStringArray(R.array.quantity);
        ok = findViewById(R.id.ok);



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quantity);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = quantity[position];
                qtyvalue.setText(a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getProductDetails(productID);
        getAccessoriesDetails(productID);
        getFabricsDetails(productID);
        getHomeDetails(productID);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingCartList();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "up-to-bottom");
    }

    private void getHomeDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Home");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getProduct_Name());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getProduct_Image()).into(productImage);

                    b = products.getProduct_Image();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFabricsDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Fabrics");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getProduct_Name());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getProduct_Image()).into(productImage);
                    b = products.getProduct_Image();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAccessoriesDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Accessories");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getProduct_Name());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getProduct_Image()).into(productImage);

                    b = products.getProduct_Image();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addingCartList() {

        String saveCurrentTime , saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
           saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        RandomKey = saveCurrentDate+ saveCurrentTime+Prevalent.CurrentOnlineUser.getPhone();
           final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("pid", productID);
            cartMap.put("Product_Name", productName.getText().toString());
            cartMap.put("Date", saveCurrentDate);
            cartMap.put("Time", saveCurrentTime);
            cartMap.put("quantity", a);
            cartMap.put("price", productPrice.getText().toString());
            cartMap.put("state", "Ordered");
            cartMap.put("productimg", b);

            cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                    .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        // ++++++++++++++++++ To show the alert dialog +++++++++++++++
                        Alerter.create(ProductDetailsActivity.this)
                                .setTitle("Item added to cart")
                                .setText("Please check your cart to proceed your payments")
                                .setIcon(R.drawable.ic_round_check_circle)
                                .setDuration(3000)
                                .enableProgress(true)
                                .setProgressColorRes(R.color.pro)
                                .setBackgroundColorRes(R.color.proBack)
                                .show();

                        cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
                                  addToCartBtn.setVisibility(View.INVISIBLE);
                                  ok.setVisibility(View.VISIBLE);
                              }
                            }
                        });
                    }
                }
            });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Phones");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getProduct_Name());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());

                    b = products.getProduct_Image();
                    Glide.with(productImage).load(products.getProduct_Image()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
