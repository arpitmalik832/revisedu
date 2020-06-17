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

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private Context mContext;
    private ICustomClickListener mCustomClickListener;

    public DiscountAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        mCustomClickListener = listener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, final int position) {
        holder.discountItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.discount));
        holder.discountItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomClickListener.onAdapterItemClick(String.valueOf(position), "Discount position " + (position + 1), "Discount");
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
