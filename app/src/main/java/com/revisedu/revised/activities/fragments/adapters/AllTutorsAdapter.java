package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.response.TutorsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllTutorsAdapter extends RecyclerView.Adapter<AllTutorsAdapter.SubjectViewHolder> {

    private Context mContext;
    private List<TutorsResponse.TutorsResponseItem> tutorsList = new ArrayList<>();
    private Drawable mDrawable;

    public AllTutorsAdapter(Context context) {
        mContext = context;
    }

    public void setTutorsList(List<TutorsResponse.TutorsResponseItem> tutorsList) {
        this.tutorsList = tutorsList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_near_you_item, parent, false);
        mDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_image);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        TutorsResponse.TutorsResponseItem item = tutorsList.get(position);
        holder.subjectImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage()).placeholder(mDrawable).into(holder.subjectImageView);
        }
        holder.rating.setText(item.getRating());
        holder.name.setText(item.getName());
        holder.location.setText(item.getLocation());
        holder.discount.setText(item.getDiscount());
        if (item.isFavourite()) {
            holder.favouriteImageView.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.favouriteImageView.setImageResource(R.drawable.ic_un_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return tutorsList.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private ImageView subjectImageView;
        private ImageView favouriteImageView;
        private TextView rating;
        private TextView name;
        private TextView location;
        private TextView discount;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            favouriteImageView = itemView.findViewById(R.id.favourite_tn);
            subjectImageView = itemView.findViewById(R.id.tutor_near_img);
            rating = itemView.findViewById(R.id.rating);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.name_place);
            discount = itemView.findViewById(R.id.discount_img);
        }
    }
}
