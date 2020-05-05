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

public class TutorNearYouAdapter extends RecyclerView.Adapter<TutorNearYouAdapter.TutorNearYourViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;

    public TutorNearYouAdapter(Context context, ICustomClickListener listener) {
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
        holder.mTutorNearItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.coaching2));
        holder.mTutorNearItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAdapterItemClick(String.valueOf(position), "Tutor near you item clicked :" + (position + 1), "Near Tutor");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class TutorNearYourViewHolder extends RecyclerView.ViewHolder {

        private ImageView mTutorNearItemImageView;

        TutorNearYourViewHolder(@NonNull View itemView) {
            super(itemView);
            mTutorNearItemImageView = itemView.findViewById(R.id.tutor_near_img);
        }
    }
}
