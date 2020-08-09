package com.example.googlestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddnewFabricsActivity extends AppCompatActivity {
    private String CategoryName, Description , Price, Pname,ImageURL, saveCurrentDate, saveCurrentTime, ProductRandomKey , downloadImageUrl;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName , InputProductKeyFeature, InputProductPrice, AddImageURL;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_fabrics);
        CategoryName = getIntent().getExtras().get("category").toString();

        AddNewProductButton = (Button) findViewById(R.id.add_product);
        InputProductName = (EditText) findViewById(R.id.add_product_title);
        InputProductKeyFeature = (EditText) findViewById(R.id.add_product_key_feature);
        InputProductPrice = (EditText) findViewById(R.id.add_price);
        InputProductImage = (ImageView) findViewById(R.id.add_product_image);
        loadingBar = new ProgressDialog(this);
        AddImageURL = (EditText) findViewById(R.id.add_image_url);

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Please Wait");
                loadingBar.setMessage("We are adding the google accessory");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {
        ImageURL = AddImageURL.getText().toString();
        Description = InputProductKeyFeature.getText().toString();
        Pname = InputProductName.getText().toString();
        Price = InputProductPrice.getText().toString();

        if (ImageURL == null){
            loadingBar.dismiss();
            Toast.makeText(this, "Add product image URL", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Pname)){
            loadingBar.dismiss();
            Toast.makeText(this, "Add product name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Description)){
            loadingBar.dismiss();
            Toast.makeText(this, "Add product description", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Price)){
            loadingBar.dismiss();
            Toast.makeText(this, "Add product price", Toast.LENGTH_SHORT).show();
        }else {StoreProductInformation(ImageURL , Description , Pname , Price);}
    }

    private void StoreProductInformation(final String imageURL, final String description, final String pname, final String price) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        ProductRandomKey = saveCurrentDate + saveCurrentTime;

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Products");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Products").child("Fabrics").child(ProductRandomKey).exists())){
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("Product_Name" ,pname);
                    productMap.put("pid" ,ProductRandomKey);
                    productMap.put("Date" , saveCurrentDate);
                    productMap.put("Time" ,saveCurrentTime);
                    productMap.put("Description" ,description);
                    productMap.put("Product_Image" , imageURL);
                    productMap.put("category" , CategoryName);
                    productMap.put("price" ,price);

                    RootRef.child("Fabrics").child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Intent intent = new Intent(AddnewFabricsActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                                Toast.makeText(AddnewFabricsActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                loadingBar.dismiss();
                                String mess = task.getException().toString();
                                Toast.makeText(AddnewFabricsActivity.this, "Error: " + mess, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
