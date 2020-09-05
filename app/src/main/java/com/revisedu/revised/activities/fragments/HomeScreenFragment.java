package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.FeaturedCoachingAdapter;
import com.revisedu.revised.activities.fragments.adapters.OffersAdapter;
import com.revisedu.revised.activities.fragments.adapters.SuperCoachingAdapter;
import com.revisedu.revised.activities.fragments.adapters.PopularCoachingAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.request.CoachingRequest;
import com.revisedu.revised.response.FetchBannersResponse;
import com.revisedu.revised.response.OffersResponse;
import com.revisedu.revised.response.ProfileResponse;
import com.revisedu.revised.response.CoachingResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class HomeScreenFragment extends BaseFragment implements ICustomClickListener {

    private static final String TAG = "HomeScreenFragment";

    private ImageView firstImage;
    private ImageView secondImage;

    private PopularCoachingAdapter mPopularCoachingAdapter;
    private FeaturedCoachingAdapter mFeaturedCoachingAdapter;
    private SuperCoachingAdapter mSuperCoachingAdapter;
    private OffersAdapter mOffersAdapter;

    private boolean doubleBackToExitPressedOnce = false;
    private Drawable mDefaultDrawable;

    //private DiscountAdapter mDiscountAdapter;
    //private RecyclerView discountRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));

        SpannableStringBuilder header = new SpannableStringBuilder();
        SpannableString str = new SpannableString("RevisED");
        str.setSpan(
            new ForegroundColorSpan(
                    ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.text_color_2)
            ),
            0,
            1,
            0
        );
        str.setSpan(
                new ForegroundColorSpan(
                        ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.text_color_1)
                ),
                1,
                5,
                0
        );
        str.setSpan(
                new ForegroundColorSpan(
                        ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.text_color_2)
                ),
                5,
                str.length(),
                0
        );
        header.append(str);
        ToolBarManager.getInstance().setHeaderTitle(header, TextView.BufferType.SPANNABLE);

        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.CENTER);

        //Banners Setup
        firstImage = mContentView.findViewById(R.id.firstImage);
        secondImage = mContentView.findViewById(R.id.secondImage);

        getBannersServerCall();

        //Popular Coaching Adapter Setup
        RecyclerView popularCoachingRecyclerView = mContentView.findViewById(R.id.popularCoachingRecyclerView);
        popularCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mPopularCoachingAdapter = new PopularCoachingAdapter(mActivity, this);
        popularCoachingRecyclerView.setAdapter(mPopularCoachingAdapter);

        getPopularCoachingServerCall();

        //Featured Coaching Adapter Setup
        RecyclerView featuredCoachingRecyclerView = mContentView.findViewById(R.id.featuredCoachingRecyclerView);
        featuredCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mFeaturedCoachingAdapter = new FeaturedCoachingAdapter(mActivity, this);
        featuredCoachingRecyclerView.setAdapter(mFeaturedCoachingAdapter);

        getFeaturedCoachingServerCall();

        //Super Coaching Adapter Setup
        RecyclerView superCoachingRecyclerView = mContentView.findViewById(R.id.superCoachingRecyclerView);
        superCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mSuperCoachingAdapter = new SuperCoachingAdapter(mActivity, this);
        superCoachingRecyclerView.setAdapter(mSuperCoachingAdapter);

        getSuperCoachingServerCall();

        //Offers Adapter Setup
        RecyclerView offersRecyclerView = mContentView.findViewById(R.id.offersRecyclerView);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mOffersAdapter = new OffersAdapter(mActivity);
        offersRecyclerView.setAdapter(mOffersAdapter);

        getOffersServerCall();

        getProfileResponse();

        //Discount Adapter Setup
        //discountRecyclerView = mContentView.findViewById(R.id.discountRecyclerView);
        //discountRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        //mDiscountAdapter = new DiscountAdapter(mActivity, this);
        //discountRecyclerView.setAdapter(mDiscountAdapter);

        return mContentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popularCoachingText:
                launchFragment(new AllCoachingFragment(TerminalConstant.MODE_POPULAR_COACHING), true);
                break;
            case R.id.featuredCoachingText:
                launchFragment(new AllCoachingFragment(TerminalConstant.MODE_FEATURED_COACHING), true);
                break;
            case R.id.superCoachingText:
                launchFragment(new AllCoachingFragment(TerminalConstant.MODE_SUPER_COACHING), true);
                break;
            default:
                break;
        }
    }

    private void getBannersServerCall() {
        mDefaultDrawable = ContextCompat.getDrawable(mActivity, R.drawable.default_image);
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<FetchBannersResponse> call = RetrofitApi.getServicesObject().getBannersServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<FetchBannersResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<FetchBannersResponse> response) {
                if (response.isSuccessful()) {
                    final FetchBannersResponse bannersResponse = response.body();
                    if (bannersResponse != null) {
                        if (bannersResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            if (bannersResponse.getBannerOne() != null && !bannersResponse.getBannerOne().isEmpty()) {
                                Picasso.get().load(bannersResponse.getBannerOne()).placeholder(mDefaultDrawable).into(firstImage);
                            }
                            if (bannersResponse.getBannerTwo() != null && !bannersResponse.getBannerTwo().isEmpty()) {
                                Picasso.get().load(bannersResponse.getBannerTwo()).placeholder(mDefaultDrawable).into(secondImage);
                            }
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void getPopularCoachingServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_POPULAR_COACHING));
                    final Response<CoachingResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CoachingResponse> response) {
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mPopularCoachingAdapter.setCoachingList(newCoachingList);
                            mPopularCoachingAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void getFeaturedCoachingServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_FEATURED_COACHING));
                    final Response<CoachingResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CoachingResponse> response) {
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mFeaturedCoachingAdapter.setCoachingList(newCoachingList);
                            mFeaturedCoachingAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void getSuperCoachingServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_SUPER_COACHING));
                    final Response<CoachingResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CoachingResponse> response) {
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mSuperCoachingAdapter.setCoachingList(newCoachingList);
                            mSuperCoachingAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void getOffersServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<OffersResponse> call = RetrofitApi.getServicesObject().getOffersServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<OffersResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<OffersResponse> response) {
                if (response.isSuccessful()) {
                    final OffersResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        if (offersResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            List<OffersResponse.ListItem> offerList = offersResponse.getArrayList();
                            if (!offerList.isEmpty()) {
                                mOffersAdapter.setOfferList(offerList);
                                mOffersAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void getProfileResponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ProfileResponse> call = RetrofitApi.getServicesObject().getProfileResponse(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<ProfileResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    final ProfileResponse profileResponse = response.body();
                    if (profileResponse != null) {
                        showToast(profileResponse.getErrorMessage());
                        if (profileResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            setupNavigationHeader(profileResponse.getName(),profileResponse.getEmail(),profileResponse.getUserImageUrl());
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressedToExit();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showToast(mActivity.getString(R.string.please_double_click_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, TerminalConstant.BACK_PRESS_TIME_INTERVAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.showSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(2);
        mActivity.hideBackButton();
        mActivity.isToggleButtonEnabled(true);
        mActivity.invalidateOptionsMenu();

    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String coachingType) {
        launchFragment(new CoachingDetailFragment(coachingType, itemId), true);
    }

}
