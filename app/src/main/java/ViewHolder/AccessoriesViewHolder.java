package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlestore.R;

import Interface.ItmeClickListner;

public class AccessoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItmeClickListner listner;
    public TextView txtProductName , txtProductPrice;
    public ImageView imageView;

    public AccessoriesViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView =  itemView.findViewById(R.id.g_image);
        txtProductName = (TextView) itemView.findViewById(R.id.g_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.g_price);
    }
    public void setItemClickListner(ItmeClickListner listner){
        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false );
    }
}
