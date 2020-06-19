package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.revisedu.revised.activities.interfaces.IFavouriteClickListener;
import com.revisedu.revised.request.FavouriteRequest;
import com.revisedu.revised.response.TutorsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.revisedu.revised.TerminalConstant.SHARED_PREF_NAME;
import static com.revisedu.revised.TerminalConstant.USER_ID;

public class FeaturedTutorAdapter extends RecyclerView.Adapter<FeaturedTutorAdapter.TutorNearYourViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;
    private IFavouriteClickListener mFavouriteClickListener;
    private List<TutorsResponse.TutorsResponseItem> tutorsList = new ArrayList<>();
    private Drawable mDrawable;

    public FeaturedTutorAdapter(Context context, ICustomClickListener listener, IFavouriteClickListener favouriteClickListener) {
        mContext = context;
        this.listener = listener;
        this.mFavouriteClickListener = favouriteClickListener;
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
        holder.TutorNearItemImageView.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", "Featured"));
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage()).placeholder(mDrawable).into(holder.TutorNearItemImageView);
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

    class TutorNearYourViewHolder extends RecyclerView.ViewHolder {

        private ImageView favouriteImageView;
        private ImageView TutorNearItemImageView;
        private TextView rating;
        private TextView name;
        private TextView location;
        private TextView discount;

        TutorNearYourViewHolder(@NonNull View itemView) {
            super(itemView);
            TutorNearItemImageView = itemView.findViewById(R.id.tutor_near_img);
            favouriteImageView = itemView.findViewById(R.id.favourite_tn);
            rating = itemView.findViewById(R.id.rating);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.name_place);
            discount = itemView.findViewById(R.id.discount_img);
            favouriteImageView.setOnClickListener(view -> {
                SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                String userId = preferences.getString(USER_ID, "");
                int position = getAdapterPosition();
                TutorsResponse.TutorsResponseItem item = tutorsList.get(position);
                Picasso.get().load(R.drawable.ic_favorite).into(favouriteImageView);
                mFavouriteClickListener.onFavouriteItemClick(new FavouriteRequest(userId, item.getId(), !item.isFavourite()));
            });
        }
    }
}
