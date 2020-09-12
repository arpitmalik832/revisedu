package com.revisedu.revised.activities.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.response.BookingsResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class BookingFragment extends BaseFragment {

    private static final String TAG = "My Bookings";
    private BookingAdapter mBookingAdapter;
    private View noDataFoundGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().onBackPressed(BookingFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        setHasOptionsMenu(false);

        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        mBookingAdapter = new BookingAdapter(mActivity);
        noDataFoundGroup = mContentView.findViewById(R.id.noDataFoundGroup);
        RecyclerView bookingRecyclerView = mContentView.findViewById(R.id.bookingRecyclerView);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        bookingRecyclerView.setAdapter(mBookingAdapter);
        getBookingsServerCall();
    }

    private void getBookingsServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<BookingsResponse> call = RetrofitApi.getServicesObject().getBookingsServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<BookingsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        noDataFoundGroup.setVisibility(View.VISIBLE);
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<BookingsResponse> response) {
                if (response.isSuccessful()) {
                    final BookingsResponse bookingsResponse = response.body();
                    if (bookingsResponse != null) {
                        List<BookingsResponse.BookingItem> bookingList = bookingsResponse.getBookingList();
                        if (!bookingList.isEmpty()) {
                            mBookingAdapter.setBookingList(bookingList);
                            mBookingAdapter.notifyDataSetChanged();
                        } else {
                            noDataFoundGroup.setVisibility(View.VISIBLE);
                        }
                    } else {
                        noDataFoundGroup.setVisibility(View.VISIBLE);
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        launchFragment(new HomeScreenFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }

    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

        private List<BookingsResponse.BookingItem> bookingList = new ArrayList<>();

        private Context mContext;
        private Drawable mDefaultImage;

        BookingAdapter(Context context) {
            mContext = context;
        }

        void setBookingList(List<BookingsResponse.BookingItem> bookingList) {
            this.bookingList = bookingList;
        }

        @NonNull
        @Override
        public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
            mDefaultImage = ContextCompat.getDrawable(mContext, R.drawable.default_image);
            return new BookingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingViewHolder holder, final int position) {
            BookingsResponse.BookingItem item = bookingList.get(position);
            holder.imageViewBooking.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_left_to_right_transition));
            if (item.getPhotoUrl() != null && !item.getPhotoUrl().isEmpty()) {
                Picasso.get().load(item.getPhotoUrl()).placeholder(mDefaultImage).into(holder.imageViewBooking);
            }
            holder.tutorName.setText(item.getTitle());
            holder.tutorBookDate.setText(item.getDateOfBooking());
            holder.tutorBookingStatus.setText(item.getStatus());
        }

        @Override
        public int getItemCount() {
            return bookingList.size();
        }

        class BookingViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageViewBooking;
            private TextView tutorName;
            private TextView tutorBookingStatus;
            private TextView tutorBookDate;

            BookingViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewBooking = itemView.findViewById(R.id.imageViewBooking);
                tutorName = itemView.findViewById(R.id.tutorName);
                tutorBookingStatus = itemView.findViewById(R.id.tutorBookingStatus);
                tutorBookDate = itemView.findViewById(R.id.tutorBookDate);
            }
        }
    }
}
