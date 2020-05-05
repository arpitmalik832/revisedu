package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
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

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private Context mContext;

    public SubjectsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.subjectImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
        if (position % 4 == 0) {
            holder.subjectImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.discountoffer));
        } else {
            holder.subjectImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.coaching3));
        }
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private ImageView subjectImageView;
        private TextView subjectTextView;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImageView = itemView.findViewById(R.id.subjectImageView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
        }
    }
}
