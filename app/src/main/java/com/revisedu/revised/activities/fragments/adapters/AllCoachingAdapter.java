package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.response.CoachingResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCoachingAdapter extends RecyclerView.Adapter<AllCoachingAdapter.ViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;
    private List<CoachingResponse.CoachingResponseItem> coachingList = new ArrayList<>();
    private Drawable mDrawable;

    public AllCoachingAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    public void setTutorsList(List<CoachingResponse.CoachingResponseItem> coachingList) {
        this.coachingList = coachingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_coaching_item, parent, false);
        mDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_image);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoachingResponse.CoachingResponseItem item = coachingList.get(position);

        holder.mParentLayout.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", TerminalConstant.COACHING_CENTER));
        holder.mImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage()).placeholder(mDrawable).into(holder.mImageView);
        }
        holder.mNameView.setText(item.getName());
        holder.mDiscountView.setText(TerminalConstant.SAMPLE_DISCOUNT);
    }

    @Override
    public int getItemCount() { return coachingList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mParentLayout;
        private AppCompatImageView mImageView;
        private AppCompatTextView mNameView;
        private AppCompatTextView mDiscountView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.cardLayout);
            mImageView = itemView.findViewById(R.id.image);
            mNameView = itemView.findViewById(R.id.name);
            mDiscountView = itemView.findViewById(R.id.discount);
        }
    }
}
