package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.Accessories;
import Model.Fabrics;
import Model.Home;
import Model.Products;
import ViewHolder.AccessoriesViewHolder;
import ViewHolder.ProductViewHolder;
import maes.tech.intentanim.CustomIntent;
import pl.droidsonroids.gif.GifImageView;

public class SearchActivity extends AppCompatActivity {

    private Button searchBtn, searchAcc, searchOutfit, searchHome;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private GifImageView abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputText = findViewById(R.id.editText);
        searchBtn = findViewById(R.id.searchPhones);
        searchAcc = findViewById(R.id.searchAcc);
        searchList = findViewById(R.id.searchList);
        searchOutfit = findViewById(R.id.searchOutfits);
        searchHome = findViewById(R.id.searchHome);
        abc = findViewById(R.id.anim);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchList.setVisibility(View.INVISIBLE);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                abc.setVisibility(View.INVISIBLE);
                SearchPhone();
                searchList.setVisibility(View.VISIBLE);
            }
        });

        searchAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                abc.setVisibility(View.INVISIBLE);
               SearchAccessories();
                searchList.setVisibility(View.VISIBLE);
            }
        });

        searchOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                abc.setVisibility(View.INVISIBLE);
                SearchOutfits();
                searchList.setVisibility(View.VISIBLE);
            }
        });

        searchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                abc.setVisibility(View.INVISIBLE);
                SearchHome();
                searchList.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "bottom-to-up");
    }

    private void SearchHome() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products").child("Home");
        FirebaseRecyclerOptions<Home> opt = new FirebaseRecyclerOptions.Builder<Home>()
                .setQuery(ref.orderByChild("Product_Name").startAt(SearchInput), Home.class).build();
        FirebaseRecyclerAdapter<Home, ProductViewHolder> ada  = new FirebaseRecyclerAdapter<Home, ProductViewHolder>(opt) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Home model) {
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder hold = new ProductViewHolder(view);
                return hold;
            }
        };
        searchList.setAdapter(ada);
        ada.startListening();
    }

    private void SearchOutfits() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products").child("Fabrics");
        FirebaseRecyclerOptions<Fabrics> opt = new FirebaseRecyclerOptions.Builder<Fabrics>()
                .setQuery(ref.orderByChild("Product_Name").startAt(SearchInput), Fabrics.class).build();
        FirebaseRecyclerAdapter<Fabrics, ProductViewHolder> ada  = new FirebaseRecyclerAdapter<Fabrics, ProductViewHolder>(opt) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Fabrics model) {
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder hold = new ProductViewHolder(view);
                return hold;
            }
        };
        searchList.setAdapter(ada);
        ada.startListening();
    }

    private void SearchAccessories() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products").child("Accessories");
        FirebaseRecyclerOptions<Accessories> opt = new FirebaseRecyclerOptions.Builder<Accessories>()
                .setQuery(ref.orderByChild("Product_Name").startAt(SearchInput), Accessories.class).build();
        FirebaseRecyclerAdapter<Accessories, ProductViewHolder> ada  = new FirebaseRecyclerAdapter<Accessories, ProductViewHolder>(opt) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Accessories model) {
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder hold = new ProductViewHolder(view);
                return hold;
            }
        };
        searchList.setAdapter(ada);
        ada.startListening();
    }

    private void SearchPhone() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products").child("Phones");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("Product_Name").startAt(SearchInput), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
