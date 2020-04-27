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

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private Context mContext;

    public DiscountAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        holder.discountItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.discount));
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
