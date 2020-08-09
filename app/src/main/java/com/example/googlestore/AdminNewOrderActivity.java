package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.AdminOrders;
import Model.UserOrders;
import Model.Users;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef, orderReff;
    private TextView Temp;
    private String phonePrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.order_list);
        Temp = findViewById(R.id.temporary);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        holder.userName.setText(model.getName());
                        holder.userPhoneNumber.setText("Ph No:  "+model.getPhone());

                        holder.ShowOdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID = getRef(position).getKey();
                                Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        phonePrimary = model.getPhone();
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Temp.setVisibility(View.INVISIBLE);
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeStatus() {
        ordersRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        AdminOrders a = dataSnapshot.getValue(AdminOrders.class);
        String ph = a.getId();

        Toast.makeText(AdminNewOrderActivity.this, ph, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }


    //    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber;
        public Button shipped, delivered;
        public FloatingActionButton ShowOdersBtn;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            ShowOdersBtn = itemView.findViewById(R.id.show);
        }
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

}
