package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SuperCoachingAdapter extends RecyclerView.Adapter<SuperCoachingAdapter.ViewHolder> {

    private Context mContext;
    private ICustomClickListener listener;
    private List<CoachingResponse.CoachingResponseItem> coachingList = new ArrayList<>();
    private Drawable mDrawable;

    public SuperCoachingAdapter(Context context, ICustomClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    public void setCoachingList(List<CoachingResponse.CoachingResponseItem> coachingList) { this.coachingList = coachingList; }

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

        holder.mParentLayout.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", TerminalConstant.MODE_SUPER_COACHING));
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