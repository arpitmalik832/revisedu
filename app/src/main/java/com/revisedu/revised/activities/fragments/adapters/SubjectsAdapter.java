package com.revisedu.revised.activities.fragments.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.activities.interfaces.IGetPositionClickListener;
import com.revisedu.revised.response.PrefSubjectsResponse;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private final List<PrefSubjectsResponse.ListItem> mPrefSubjectList;
    private Context mContext;
    private IGetPositionClickListener mListener;

    public SubjectsAdapter(Context context, List<PrefSubjectsResponse.ListItem> prefSubjectList, IGetPositionClickListener listener) {
        this.mPrefSubjectList = prefSubjectList;
        this.mListener = listener;
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
        PrefSubjectsResponse.ListItem currentItem = mPrefSubjectList.get(position);
        holder.thumbnail.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
        holder.subjectTextView.setText(currentItem.getSubject());
        Random random = new Random();
        int red = random.nextInt(255 + 1);
        int green = random.nextInt(255 + 1);
        int blue = random.nextInt(255 + 1);
        String initials = currentItem.getSubject();
        holder.nameInitials.setText(initials.substring(0, 1));
        holder.nameInitials.setTextColor(Color.rgb(red, green, blue));
        holder.nameInitials.setVisibility(View.VISIBLE);
        holder.contactsDefaultImage.setVisibility(View.GONE);
        holder.clSubjects.setOnClickListener(v -> mListener.onAdapterItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mPrefSubjectList.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private TextView subjectTextView;
        private RelativeLayout thumbnail;
        private TextView nameInitials;
        private ConstraintLayout clSubjects;
        private CircleImageView contactsDefaultImage;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            contactsDefaultImage = itemView.findViewById(R.id.contactsDefaulImage);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            nameInitials = itemView.findViewById(R.id.nameInitials);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            clSubjects = itemView.findViewById(R.id.clSubjects);
        }
    }
}
