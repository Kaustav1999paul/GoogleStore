package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlestore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Interface.ItmeClickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView productImage;
    private ItmeClickListner itmeClickListner;
    public FloatingActionButton removeFromCart;
    public ImageView img;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

//        productImage = itemView.findViewById(R.id.cart_product_image);
        txtProductName =  itemView.findViewById(R.id.product_title);
        txtProductPrice =  itemView.findViewById(R.id.product_price2);
        txtProductQuantity =  itemView.findViewById(R.id.qty);
        removeFromCart = itemView.findViewById(R.id.button);
        img = itemView.findViewById(R.id.imageView7);
    }

    @Override
    public void onClick(View v) {itmeClickListner.onClick(v, getAdapterPosition(),false);}

    public void setItmeClickListner(ItmeClickListner itmeClickListner) {
        this.itmeClickListner = itmeClickListner;
    }
}
