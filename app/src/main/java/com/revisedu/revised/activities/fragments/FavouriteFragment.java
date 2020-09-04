package com.revisedu.revised.activities.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.activities.interfaces.IFavouriteClickListener;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.response.CoachingResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class FavouriteFragment extends BaseFragment {

    private static final String TAG = "Favourites";
    private FavouriteAdapter mFavouriteAdapter;
    private View noDataFoundGroup;
    private ICustomClickListener listener;

    public FavouriteFragment(ICustomClickListener listener) {
        this.listener = listener;
    }

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
        ToolBarManager.getInstance().onBackPressed(FavouriteFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        mFavouriteAdapter = new FavouriteAdapter(mActivity);
        noDataFoundGroup = mContentView.findViewById(R.id.noDataFoundGroup);
        RecyclerView recyclerView = mContentView.findViewById(R.id.bookingRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        recyclerView.setAdapter(mFavouriteAdapter);
        getFavouriteServerCall();
    }

    private void getFavouriteServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getFavouriteServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<CoachingResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        noDataFoundGroup.setVisibility(View.VISIBLE);
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CoachingResponse> response) {
                if (response.isSuccessful()) {
                    final CoachingResponse bookingsResponse = response.body();
                    if (bookingsResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> list = bookingsResponse.getArrayList();
                        if (!list.isEmpty()) {
                            mFavouriteAdapter.setFavouriteList(list);
                            mFavouriteAdapter.notifyDataSetChanged();
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

        private List<CoachingResponse.CoachingResponseItem> favouriteList = new ArrayList<>();

        private Context mContext;
        private Drawable mDefaultImage;

        FavouriteAdapter(Context context) {
            mContext = context;
        }

        void setFavouriteList(List<CoachingResponse.CoachingResponseItem> favouriteList) {
            this.favouriteList = favouriteList;
        }

        @NonNull
        @Override
        public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_coaching_item, parent, false);
            mDefaultImage = ContextCompat.getDrawable(mContext, R.drawable.default_image);
            return new FavouriteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteViewHolder holder, final int position) {
            CoachingResponse.CoachingResponseItem item = favouriteList.get(position);

            holder.mParentLayout.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", "Favourite Tutor"));
            holder.mImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
            if (item.getImage() != null && !item.getImage().isEmpty()) {
                Picasso.get().load(item.getImage()).placeholder(mDefaultImage).into(holder.mImageView);
            }
            holder.mNameView.setText(item.getName());
            holder.mDiscountView.setText(TerminalConstant.SAMPLE_DISCOUNT);
        }

        @Override
        public int getItemCount() {
            return favouriteList.size();
        }

        class FavouriteViewHolder extends RecyclerView.ViewHolder {

            private ConstraintLayout mParentLayout;
            private AppCompatImageView mImageView;
            private AppCompatTextView mNameView;
            private AppCompatTextView mDiscountView;

            FavouriteViewHolder(@NonNull View itemView) {
                super(itemView);
                mParentLayout = itemView.findViewById(R.id.cardLayout);
                mImageView = itemView.findViewById(R.id.image);
                mNameView = itemView.findViewById(R.id.name);
                mDiscountView = itemView.findViewById(R.id.discount);
            }
        }
    }
}
