package com.example.googlestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.AdminOrders;
import Model.UserOrders;
import Prevalent.Prevalent;
import ViewHolder.ProductViewHolder;
import ViewHolder.UserOrderViewHolder;

public class OrdersFragment extends Fragment {

    private RecyclerView list;
    LinearLayoutManager layoutManager;
    private DatabaseReference ordersRef;
    private TextView tempOrders;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        list = view.findViewById(R.id.list);
        tempOrders = view.findViewById(R.id.tempOrders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products");
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        list.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserOrders> options = new FirebaseRecyclerOptions.Builder<UserOrders>()
                .setQuery(ordersRef, UserOrders.class)
                .build();

        FirebaseRecyclerAdapter<UserOrders, UserOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<UserOrders, UserOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserOrderViewHolder holder, final int position, @NonNull final UserOrders model) {
                        String qty = model.getQuantity();
                        String price = model.getPrice();

                        tempOrders.setVisibility(View.GONE);
                        int total = Integer.parseInt(qty) * Integer.parseInt(price);
                        String a = String.valueOf(total);
                        holder.userTotalPrice.setText("₹"+ a);
//                        holder.status.setText(model.getState());
                        holder.qty.setText("Qty: "+qty);
                        holder.userName.setText(model.getProduct_Name());
                        Picasso.get().load(model.getProductimg()).into(holder.proImg);

                        String state = model.getState();
                        String s1= "Shipped";
                        String s2 = "Out for Delivery";
                        String s3 = "Delivered";

                        if (state.equals(s1)){
                            holder.ordered.setVisibility(View.INVISIBLE);
                            holder.shipped.setVisibility(View.VISIBLE);
                            holder.ofd.setVisibility(View.INVISIBLE);
                            holder.delivered.setVisibility(View.INVISIBLE);

                            holder.step2.setVisibility(View.VISIBLE);
                            holder.step3.setVisibility(View.INVISIBLE);
                            holder.step4.setVisibility(View.INVISIBLE);
                        }else if (state.equals(s2)){
                            holder.ordered.setVisibility(View.INVISIBLE);
                            holder.ofd.setVisibility(View.VISIBLE);
                            holder.shipped.setVisibility(View.INVISIBLE);
                            holder.delivered.setVisibility(View.INVISIBLE);

                            holder.step2.setVisibility(View.VISIBLE);
                            holder.step3.setVisibility(View.VISIBLE);
                            holder.step4.setVisibility(View.INVISIBLE);
                        }else if (state.equals(s3)){
                            holder.ordered.setVisibility(View.INVISIBLE);
                            holder.delivered.setVisibility(View.VISIBLE);
                            holder.shipped.setVisibility(View.INVISIBLE);
                            holder.ofd.setVisibility(View.INVISIBLE);

                            holder.step2.setVisibility(View.VISIBLE);
                            holder.step3.setVisibility(View.VISIBLE);
                            holder.step4.setVisibility(View.VISIBLE);
                        }else {
                            holder.ordered.setVisibility(View.VISIBLE);
                            holder.delivered.setVisibility(View.INVISIBLE);
                            holder.shipped.setVisibility(View.INVISIBLE);
                            holder.ofd.setVisibility(View.INVISIBLE);

                            holder.step2.setVisibility(View.INVISIBLE);
                            holder.step3.setVisibility(View.INVISIBLE);
                            holder.step4.setVisibility(View.INVISIBLE);
                        }


                    }

                    @NonNull
                    @Override
                    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_layout, parent, false);
                        return new UserOrderViewHolder(view);
                    }
                };
        list.setAdapter(adapter);
        adapter.startListening();
    }
}
