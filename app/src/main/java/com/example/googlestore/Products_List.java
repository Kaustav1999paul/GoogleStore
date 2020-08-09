package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.ProductViewHolder;
import maes.tech.intentanim.CustomIntent;

public class Products_List extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Phones");
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model.Products> options = new FirebaseRecyclerOptions.Builder<Model.Products>()
                .setQuery(ProductsRef, Model.Products.class)
                .build();

        FirebaseRecyclerAdapter<Model.Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Model.Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Model.Products model) {
                        holder.txtProductName.setText(model.getProduct_Name());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Products_List.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                                CustomIntent.customType(Products_List.this, "bottom-to-up");
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
