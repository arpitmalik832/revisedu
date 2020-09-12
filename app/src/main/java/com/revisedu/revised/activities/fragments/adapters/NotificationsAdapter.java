package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.revisedu.revised.R;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.response.NotificationsResponse;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final List<NotificationsResponse.ListItem> mNotificationsList;
    private Context mContext;
    private ICustomClickListener mListener;

    public NotificationsAdapter(List<NotificationsResponse.ListItem> mNotificationsList, Context mContext, ICustomClickListener mListener) {
        this.mNotificationsList = mNotificationsList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.parentLayout.setOnClickListener(v -> mListener.onAdapterItemClick(
                mNotificationsList.get(holder.getAdapterPosition()).getId(),
                new Gson().toJson(mNotificationsList.get(holder.getAdapterPosition())),
                ""
        ));
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout parentLayout;
        private TextView time;
        private TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            time = itemView.findViewById(R.id.time);
            msg = itemView.findViewById(R.id.msg);
        }
    }
}
