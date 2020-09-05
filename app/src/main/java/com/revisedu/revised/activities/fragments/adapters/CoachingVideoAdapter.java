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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String videoId = getYoutubeVideoIdFromUrl(mVideoList.get(position).getVideo());
            String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg";
            Picasso.get().load(img_url).placeholder(mDrawable).into(holder.thumbnail);
        }
        holder.thumbnail.setOnClickListener(view -> listener.onAdapterItemClick(mVideoList.get(position).getTopic(), mVideoList.get(position).getVideo(), ""));
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed/)[^#&?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
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
