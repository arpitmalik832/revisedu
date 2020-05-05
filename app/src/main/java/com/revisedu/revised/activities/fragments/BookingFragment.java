package com.revisedu.revised.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;

public class BookingFragment extends BaseFragment {

    private static final String TAG = "My Bookings";
    private RecyclerView bookingRecyclerView;
    private BookingAdapter mBookingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().onBackPressed(BookingFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        mBookingAdapter = new BookingAdapter(mActivity, null);
        bookingRecyclerView = mContentView.findViewById(R.id.bookingRecyclerView);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        bookingRecyclerView.setAdapter(mBookingAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.readMoreTextView:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        launchFragment(new HomeScreenFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgress();
            }
        }, 1000);
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }

    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

        private Context mContext;
        private ICustomClickListener mCustomClickListener;

        public BookingAdapter(Context context, ICustomClickListener listener) {
            mContext = context;
            mCustomClickListener = listener;
        }

        @NonNull
        @Override
        public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
            return new BookingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingViewHolder holder, final int position) {
            holder.imageViewBooking.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_left_to_right_transition));
            holder.imageViewBooking.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.discount));
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class BookingViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageViewBooking;

            BookingViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewBooking = itemView.findViewById(R.id.imageViewBooking);
            }
        }
    }
}
