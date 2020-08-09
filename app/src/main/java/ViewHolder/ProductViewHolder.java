package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlestore.R;

import Interface.ItmeClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName , txtProductPrice;
    public ImageView imageView;
    public ItmeClickListner listner;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView =  itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItmeClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false );
    }
}
