package com.revisedu.revised.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.activities.interfaces.IFavouriteClickListener;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.request.FavouriteRequest;
import com.revisedu.revised.response.TutorsResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.revisedu.revised.TerminalConstant.SHARED_PREF_NAME;
import static com.revisedu.revised.TerminalConstant.USER_ID;

public class FavouriteFragment extends BaseFragment {

    private static final String TAG = "Favourites";
    private FavouriteAdapter mFavouriteAdapter;
    private View noDataFoundGroup;
    private IFavouriteClickListener mFavouriteClickListener;
    private ICustomClickListener listener;

    public FavouriteFragment(IFavouriteClickListener favouriteClickListener, ICustomClickListener listener) {
        mFavouriteClickListener = favouriteClickListener;
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
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getFavouriteServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<TutorsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        noDataFoundGroup.setVisibility(View.VISIBLE);
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TutorsResponse> response) {
                if (response.isSuccessful()) {
                    final TutorsResponse bookingsResponse = response.body();
                    if (bookingsResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> list = bookingsResponse.getArrayList();
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
    }

    private class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

        private List<TutorsResponse.TutorsResponseItem> favouriteList = new ArrayList<>();

        private Context mContext;
        private Drawable mDefaultImage;

        FavouriteAdapter(Context context) {
            mContext = context;
        }

        void setFavouriteList(List<TutorsResponse.TutorsResponseItem> favouriteList) {
            this.favouriteList = favouriteList;
        }

        @NonNull
        @Override
        public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_near_you_item, parent, false);
            mDefaultImage = ContextCompat.getDrawable(mContext, R.drawable.default_image);
            return new FavouriteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteViewHolder holder, final int position) {
            TutorsResponse.TutorsResponseItem item = favouriteList.get(position);
            holder.subjectImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_left_to_right_transition));
            if (item.getImage() != null && !item.getImage().isEmpty()) {
                Picasso.get().load(item.getImage()).placeholder(mDefaultImage).into(holder.subjectImageView);
            }
            holder.rating.setText(item.getRating());
            holder.name.setText(item.getName());
            holder.location.setText(item.getLocation());
            holder.subjectImageView.setOnClickListener(view -> listener.onAdapterItemClick(item.getId(), "", "Favourite Tutor"));
            holder.discount.setText(item.getDiscount());
            if (item.isFavourite()) {
                Picasso.get().load(R.drawable.ic_favorite).into(holder.favouriteImageView);
            } else {
                Picasso.get().load(R.drawable.ic_un_favorite).into(holder.favouriteImageView);
            }
        }

        @Override
        public int getItemCount() {
            return favouriteList.size();
        }

        class FavouriteViewHolder extends RecyclerView.ViewHolder {

            private ImageView subjectImageView;
            private ImageView favouriteImageView;
            private TextView rating;
            private TextView name;
            private TextView location;
            private TextView discount;

            FavouriteViewHolder(@NonNull View itemView) {
                super(itemView);
                favouriteImageView = itemView.findViewById(R.id.favourite_tn);
                subjectImageView = itemView.findViewById(R.id.tutor_near_img);
                rating = itemView.findViewById(R.id.rating);
                name = itemView.findViewById(R.id.name);
                location = itemView.findViewById(R.id.name_place);
                discount = itemView.findViewById(R.id.discount_img);
                favouriteImageView.setOnClickListener(view -> {
                    SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                    String userId = preferences.getString(USER_ID, "");
                    int position = getAdapterPosition();
                    TutorsResponse.TutorsResponseItem item = favouriteList.get(position);
                    Picasso.get().load(R.drawable.ic_favorite).into(favouriteImageView);
                    mFavouriteClickListener.onFavouriteItemClick(new FavouriteRequest(userId, item.getId(), !item.isFavourite()));
                    favouriteList.remove(position);
                    notifyDataSetChanged();
                });
            }
        }
    }
}
