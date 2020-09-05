package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.revisedu.revised.R;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.response.CoachingDetailResponse;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CoachingVideoAdapter extends RecyclerView.Adapter<CoachingVideoAdapter.ViewHolder> {

    private Context mContext;
    private List<CoachingDetailResponse.CoachingDetail.Video> mVideoList;
    private ICustomClickListener listener;
    private Drawable mDrawable;

    public CoachingVideoAdapter(Context mContext, List<CoachingDetailResponse.CoachingDetail.Video> mVideoList, ICustomClickListener listener) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coaching_video_item, parent, false);
        mDrawable = ContextCompat.getDrawable(mContext, R.drawable.default_image);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mVideoList.get(position).getVideo() != null && !mVideoList.get(position).getVideo().isEmpty()) {
            String videoId= null;
            try {
                videoId = extractYoutubeId(mVideoList.get(position).getVideo());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg";
            Picasso.get().load(img_url).placeholder(mDrawable).into(holder.thumbnail);
        }
        holder.thumbnail.setOnClickListener(view -> listener.onAdapterItemClick(mVideoList.get(position).getTopic(), mVideoList.get(position).getVideo(), ""));
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
