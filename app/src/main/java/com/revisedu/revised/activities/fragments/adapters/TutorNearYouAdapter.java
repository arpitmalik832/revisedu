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
import com.revisedu.revised.response.TutorsResponse;

import java.util.ArrayList;
import java.util.List;

public class TutorNearYouAdapter extends RecyclerView.Adapter<TutorNearYouAdapter.TutorNearYourViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;
    private List<TutorsResponse.TutorsResponseItem> tutorsList = new ArrayList<>();

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
        return new TutorNearYourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorNearYourViewHolder holder, final int position) {
        TutorsResponse.TutorsResponseItem item = tutorsList.get(position);
        holder.mTutorNearItemImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.coaching2));
        holder.mTutorNearItemImageView.setOnClickListener(view -> listener.onAdapterItemClick(String.valueOf(position), "Tutor near you item clicked :" + (position + 1), "Near Tutor"));
    }

    @Override
    public int getItemCount() {
        return tutorsList.size();
    }

    static class TutorNearYourViewHolder extends RecyclerView.ViewHolder {

        private ImageView mTutorNearItemImageView;

        TutorNearYourViewHolder(@NonNull View itemView) {
            super(itemView);
            mTutorNearItemImageView = itemView.findViewById(R.id.tutor_near_img);
        }
    }
}
