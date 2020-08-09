package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.mCart;
import Prevalent.Prevalent;
import ViewHolder.CartViewHolder;
import maes.tech.intentanim.CustomIntent;

public class FinalCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessButton;
    private TextView txttotalAmount, tempCartaa;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessButton = findViewById(R.id.cart_continue_button);
        txttotalAmount = findViewById(R.id.total_cart_amount);
        tempCartaa = findViewById(R.id.tempCartaa);

        final View parentLayout = findViewById(android.R.id.content);

        NextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (overTotalPrice == 0){
                    Snackbar snackbar = Snackbar.make(parentLayout, "Cart should not be empty to proceed further.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    Intent intent = new Intent(FinalCartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total price: ", String.valueOf(overTotalPrice));
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(FinalCartActivity.this , HomeActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "bottom-to-up");
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<mCart> options = new FirebaseRecyclerOptions.Builder<mCart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.CurrentOnlineUser.getPhone())
                        .child("Products"), mCart.class).build();

        FirebaseRecyclerAdapter<mCart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<mCart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final mCart model) {

                tempCartaa.setVisibility(View.GONE);
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductQuantity.setText("Qty: "+model.getQuantity());
                Picasso.get().load(model.getProductimg()).into(holder.img);

                int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = oneTypeProductTPrice + overTotalPrice;

                holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(model.getPid())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(model.getPid())
                                            .removeValue();
                                    Toast.makeText(FinalCartActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder =new CartViewHolder(view);
                return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
