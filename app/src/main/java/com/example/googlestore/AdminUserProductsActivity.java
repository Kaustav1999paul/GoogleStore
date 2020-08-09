package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.mCart;
import Prevalent.Prevalent;
import ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID = "";
    private String proID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
        .child("Admin View").child(userID).child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<mCart> options =
                new FirebaseRecyclerOptions.Builder<mCart>().setQuery(cartListRef, mCart.class).build();

        FirebaseRecyclerAdapter<mCart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<mCart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, final int position, @NonNull final mCart model) {
                holder.txtProductName.setText(model.getProduct_Name());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductQuantity.setText("Qty: "+model.getQuantity());
                holder.removeFromCart.setVisibility(View.INVISIBLE);
                Picasso.get().load(model.getProductimg()).into(holder.img);

                proID = model.getPid();



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]  = new CharSequence[]{
                                "Shipped", "Out for Delivery" ,"Delivered"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserProductsActivity.this);
                        builder.setTitle("What's the order status?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0){
                                   String uID = getRef(position).getKey();
                                    ChangeStatus(uID);

                                }else if (i==1){
                                    String usID = getRef(position).getKey();
                                    OutforDelivery(usID);
                                }
                                else if (i==2){
                                    String usID = getRef(position).getKey();
                                    Delivered(usID);
                                }else {}
                            }
                        });

                        builder.show();
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
        productsList.setAdapter(adapter);
        adapter.startListening();
    }

    private void OutforDelivery(String usID) {
        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("state", "Out for Delivery");
        cartListRef.child(usID).updateChildren(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Delivered(String usID) {
        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("state", "Delivered");
        cartListRef.child(usID).updateChildren(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ChangeStatus(String uID) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "Shipped");
        cartListRef.child(uID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void RemoveOrder(String uID) {
        cartListRef.child(uID).removeValue();
    }
}
