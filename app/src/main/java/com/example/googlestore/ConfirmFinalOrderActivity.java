package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Prevalent.Prevalent;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements PaymentResultListener{

    private static final String TAG = ConfirmFinalOrderActivity.class.getSimpleName();
    private String totalAmount = "", saveCurrentTime , saveCurrentDate, RandomKey, method, inPaise;
    private TextView price;
    private FloatingActionButton confirmOrderBtn;
    private Dialog conf;
    private CircleImageView aaav;
    private ImageView editAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        editAdd = findViewById(R.id.edit_Add);
        aaav = findViewById(R.id.aaav);
        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, addressEdit.class);
                startActivity(intent);
                CustomIntent.customType(ConfirmFinalOrderActivity.this, "bottom-to-up");
            }
        });

        conf = new Dialog(this);
        totalAmount = getIntent().getStringExtra("Total price: ");
        price = findViewById(R.id.price);
        price.setText(totalAmount);
        confirmOrderBtn = findViewById(R.id.ConfirmOrderButton);

        double k = Double.parseDouble(totalAmount);
        double p = k * 100;
        inPaise = String.valueOf(p);


        TextView fullNameEditText = (TextView) findViewById(R.id.setting_full_name);
        TextView userPhoneEditText = (TextView) findViewById(R.id.setting_phone_no);
        TextView addressEditText =  (TextView) findViewById(R.id.setting_address);

        fullNameEditText.setText(Prevalent.CurrentOnlineUser.getName());
        userPhoneEditText.setText(Prevalent.CurrentOnlineUser.getPhone());
        addressEditText.setText(Prevalent.CurrentOnlineUser.getAddress());
        Picasso.get().load(Prevalent.CurrentOnlineUser.getPhoto()).into(aaav);

//        +++++++++++++++++++++++++++++ Date, time and randomKey +++++++++++++++++++++++++++++++++++
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        RandomKey = saveCurrentDate + saveCurrentTime + Prevalent.CurrentOnlineUser.getPhone();
//        +++++++++++++++++++++++++++++ Date, time and randomKey +++++++++++++++++++++++++++++++++++


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePayment();
            }
        });
    }

    private void choosePayment() {

        conf.setContentView(R.layout.choose_payment_method_layout);
        conf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        conf.setCanceledOnTouchOutside(true);
        conf.show();

        LinearLayout cod = conf.findViewById(R.id.cod);
        LinearLayout Rezpay = conf.findViewById(R.id.razpay);

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = "COD";
                ConfirmOrder(method);
            }
        });

        Rezpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conf.dismiss();
                startPayment();
            }
        });
    }

    public void startPayment(){
        Activity activity = this;
        Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Google Store");
            options.put("description", "Pay via Razorpay");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", inPaise);

            JSONObject preFill = new JSONObject();
            preFill.put("contact", Prevalent.CurrentOnlineUser.getPhone());
            options.put("prefill", preFill);

            co.open(activity, options);
        }catch (Exception e){
            Toast.makeText(activity, "Error: "+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void ConfirmOrder(String method) {

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        HashMap<String, Object> ordersMAp = new HashMap<>();
        ordersMAp.put("totalamount", totalAmount);
        ordersMAp.put("Name", Prevalent.CurrentOnlineUser.getName() );
        ordersMAp.put("phone", Prevalent.CurrentOnlineUser.getPhone());
        ordersMAp.put("address", Prevalent.CurrentOnlineUser.getAddress());
        ordersMAp.put("Date", saveCurrentDate);
        ordersMAp.put("Time", saveCurrentTime);
        ordersMAp.put("state", "not shipped");
        ordersMAp.put("id", RandomKey);
        ordersMAp.put("method", method);

        orderRef.child(RandomKey).updateChildren(ordersMAp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.CurrentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        HashMap<String, Object> om = new HashMap<>();
                                        om.put("Name",Prevalent.CurrentOnlineUser.getName() );
                                        om.put("phone", Prevalent.CurrentOnlineUser.getPhone());
                                        om.put("state", RandomKey);
                                        orderRef.updateChildren(om);

                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order confirmed", Toast.LENGTH_SHORT).show();
                                        confirmDialogShow();
                                    }
                                }
                            });
                }
            }
        });
    }



    private void confirmDialogShow() {
        conf.setContentView(R.layout.confirm_layout);
        conf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        conf.setCanceledOnTouchOutside(false);
        conf.show();

        Button b = conf.findViewById(R.id.ok);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onPaymentSuccess(String s) {
        method = "Online Razorpay";
        ConfirmOrder(method);
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
