package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.response.TutorsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TutorNearYouAdapter extends RecyclerView.Adapter<TutorNearYouAdapter.TutorNearYourViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;
    private List<TutorsResponse.TutorsResponseItem> tutorsList = new ArrayList<>();
    private Drawable mDrawable;

    public TutorNearYouAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    public void setTutorsList(List<TutorsResponse.TutorsResponseItem> tutorsList) {
        this.tutorsList = tutorsList;
    }

    @NonNull
    @Override
    public TutorNearYourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_near_you_item, parent, false);
        mDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_image);
        return new TutorNearYourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorNearYourViewHolder holder, final int position) {
        TutorsResponse.TutorsResponseItem item = tutorsList.get(position);
        holder.mTutorNearItemImageView.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", "Near Tutor"));
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage()).placeholder(mDrawable).into(holder.mTutorNearItemImageView);
        }
        holder.rating.setText(item.getRating());
        holder.name.setText(item.getName());
        holder.location.setText(item.getLocation());
        holder.discount.setText(item.getDiscount());
        if (item.isFavourite()) {
            Picasso.get().load(R.drawable.ic_favorite).into(holder.favouriteImageView);
        } else {
            Picasso.get().load(R.drawable.ic_un_favorite).into(holder.favouriteImageView);
        }
    }

    @Override
    public int getItemCount() {
        return tutorsList.size();
    }

    static class TutorNearYourViewHolder extends RecyclerView.ViewHolder {

        private ImageView mTutorNearItemImageView;
        private ImageView favouriteImageView;
        private TextView rating;
        private TextView name;
        private TextView location;
        private TextView discount;

        TutorNearYourViewHolder(@NonNull View itemView) {
            super(itemView);
            mTutorNearItemImageView = itemView.findViewById(R.id.tutor_near_img);
            favouriteImageView = itemView.findViewById(R.id.favourite_tn);
            rating = itemView.findViewById(R.id.rating);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.name_place);
            discount = itemView.findViewById(R.id.discount_img);
        }
    }
}
