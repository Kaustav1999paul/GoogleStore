package com.example.googlestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView Phones , Accessories, Fabrics, Home;
    private FloatingActionButton logout;
    private Button checkOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Phones = (ImageView) findViewById(R.id.AddPhones);
        Accessories = (ImageView) findViewById(R.id.AddAccessories);
        logout = findViewById(R.id.logout);
        checkOrders = findViewById(R.id.checkOrders);
        Fabrics = findViewById(R.id.fabrics);
        Home = findViewById(R.id.home);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminCategoryActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        Phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewProductActivity.class);
                intent.putExtra("category" , "Phones");
                startActivity(intent);
            }
        });

        Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddnewAccessoriesActivity.class);
                intent.putExtra("category" , "Accessories");
                startActivity(intent);
            }
        });

        Fabrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddnewFabricsActivity.class);
                intent.putExtra("category" , "Fabrics");
                startActivity(intent);
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddnewHomeActivity.class);
                intent.putExtra("category" , "Home");
                startActivity(intent);
            }
        });

    }
}
