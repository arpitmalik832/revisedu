package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.DiscountViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;

    public OffersAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, final int position) {
        holder.discountItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.discountoffer));
        holder.discountItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAdapterItemClick(String.valueOf(position), "Offers Tutor near you item clicked :" + (position + 1), "Offer");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class DiscountViewHolder extends RecyclerView.ViewHolder {

        private ImageView discountItemImageView;

        DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discountItemImageView = itemView.findViewById(R.id.discountItemImageView);
        }
    }
}
