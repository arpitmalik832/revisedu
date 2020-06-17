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

public class FeaturedTutorAdapter extends RecyclerView.Adapter<FeaturedTutorAdapter.TutorNearYourViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;

    public FeaturedTutorAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorNearYourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_near_you_item, parent, false);
        return new TutorNearYourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorNearYourViewHolder holder, final int position) {
        holder.TutorNearItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.coaching3));
        holder.TutorNearItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAdapterItemClick(String.valueOf(position), "Featured Tutor near you item clicked :" + (position + 1), "Featured");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class TutorNearYourViewHolder extends RecyclerView.ViewHolder {

        private ImageView TutorNearItemImageView;

        TutorNearYourViewHolder(@NonNull View itemView) {
            super(itemView);
            TutorNearItemImageView = itemView.findViewById(R.id.tutor_near_img);
        }
    }
}
