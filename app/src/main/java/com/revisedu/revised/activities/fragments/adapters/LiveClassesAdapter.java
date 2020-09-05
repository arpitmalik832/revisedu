package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revisedu.revised.R;
import com.revisedu.revised.response.LiveClassesResponse;

import java.util.List;

public class LiveClassesAdapter extends RecyclerView.Adapter<LiveClassesAdapter.ViewHolder> {

    private final List<LiveClassesResponse.LiveClassesResponseItem> mLiveClassesList;
    private Context mContext;

    public LiveClassesAdapter(Context mContext, List<LiveClassesResponse.LiveClassesResponseItem> mLiveClassesList) {
        this.mContext = mContext;
        this.mLiveClassesList = mLiveClassesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LiveClassesResponse.LiveClassesResponseItem currentItem = mLiveClassesList.get(position);
        holder.thumbnail.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
        holder.name.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mLiveClassesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
        }
    }
}
