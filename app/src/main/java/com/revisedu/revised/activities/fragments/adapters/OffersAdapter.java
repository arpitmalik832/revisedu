package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.response.OffersResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.DiscountViewHolder> {

    private Context mContext;
    private List<OffersResponse.ListItem> offerList = new ArrayList<>();
    private Drawable mDefaultDrawable;

    public OffersAdapter(Context context) {
        mContext = context;
    }

    public void setOfferList(List<OffersResponse.ListItem> offerList) {
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDefaultDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_image);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, final int position) {
        OffersResponse.ListItem item = offerList.get(position);
        Picasso.get().load(item.getOfferImage()).placeholder(mDefaultDrawable).into(holder.discountItemImageView);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class DiscountViewHolder extends RecyclerView.ViewHolder {

        private ImageView discountItemImageView;

        DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discountItemImageView = itemView.findViewById(R.id.discountItemImageView);
        }
    }
}
