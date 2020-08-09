package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlestore.R;

import Interface.ItmeClickListner;

public class UserOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName, userPhoneNumber , userTotalPrice, ordered, shipped, ofd, delivered, qty;
    public ItmeClickListner listner;
    public ImageView proImg, step2, step3, step4;
    public RatingBar rating;

    public UserOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.nameOrder);
        userTotalPrice = itemView.findViewById(R.id.priceOrder);
        qty = itemView.findViewById(R.id.qty);
//        status = itemView.findViewById(R.id.statusOrder);
        proImg = itemView.findViewById(R.id.imageView6);
        step2 = itemView.findViewById(R.id.step2);
        step3 = itemView.findViewById(R.id.step3);
        step4 = itemView.findViewById(R.id.step4);
        ordered = itemView.findViewById(R.id.ordered);
        shipped = itemView.findViewById(R.id.shipped);
        ofd = itemView.findViewById(R.id.ofd);
        delivered = itemView.findViewById(R.id.delivered);

    }
    public void setItemClickListner(ItmeClickListner listner){
        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false );
    }
}
