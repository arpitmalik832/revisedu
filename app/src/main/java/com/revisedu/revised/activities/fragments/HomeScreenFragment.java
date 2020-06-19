package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.DiscountAdapter;
import com.revisedu.revised.activities.fragments.adapters.FeaturedTutorAdapter;
import com.revisedu.revised.activities.fragments.adapters.OffersAdapter;
import com.revisedu.revised.activities.fragments.adapters.SuperTutorsAdapter;
import com.revisedu.revised.activities.fragments.adapters.TutorNearYouAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.request.TutorRequest;
import com.revisedu.revised.response.FetchBannersResponse;
import com.revisedu.revised.response.OffersResponse;
import com.revisedu.revised.response.TutorsResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class HomeScreenFragment extends BaseFragment implements ICustomClickListener {

    private static final String TAG = "HomeScreenFragment";
    private DiscountAdapter mDiscountAdapter;
    private RecyclerView discountRecyclerView;
    private TutorNearYouAdapter mTutorNearYouAdapter;
    private RecyclerView tutorNearYouRecyclerView;
    private FeaturedTutorAdapter mFeaturedTutorAdapter;
    private RecyclerView featuredTutorialRecyclerView;
    private OffersAdapter mOffersAdapter;
    private RecyclerView offersRecyclerView;
    private SuperTutorsAdapter mSuperTutorsAdapter;
    private RecyclerView superTutorsRecyclerView;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView homeImageViewTop;
    private ImageView homeImageViewSecond;
    private Drawable mDefaultDrawable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mActivity.getString(R.string.app_name));
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        homeImageViewTop = mContentView.findViewById(R.id.homeImageViewTop);
        homeImageViewSecond = mContentView.findViewById(R.id.imageView2);
        //Discount Adapter Setup
        discountRecyclerView = mContentView.findViewById(R.id.discountRecyclerView);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mDiscountAdapter = new DiscountAdapter(mActivity, this);
        discountRecyclerView.setAdapter(mDiscountAdapter);
        //Tutor Near me Adapter Setup
        tutorNearYouRecyclerView = mContentView.findViewById(R.id.tutorNearYouRecyclerView);
        tutorNearYouRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mTutorNearYouAdapter = new TutorNearYouAdapter(mActivity, this);
        tutorNearYouRecyclerView.setAdapter(mTutorNearYouAdapter);
        //Featured Tutor Adapter Setup
        featuredTutorialRecyclerView = mContentView.findViewById(R.id.featuredTutorialRecyclerView);
        featuredTutorialRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mFeaturedTutorAdapter = new FeaturedTutorAdapter(mActivity, this);
        featuredTutorialRecyclerView.setAdapter(mFeaturedTutorAdapter);
        //Offers Adapter Setup
        offersRecyclerView = mContentView.findViewById(R.id.offersRecyclerView);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mOffersAdapter = new OffersAdapter(mActivity);
        offersRecyclerView.setAdapter(mOffersAdapter);
        //Super Tutor Adapter Setup
        superTutorsRecyclerView = mContentView.findViewById(R.id.superTutorsRecyclerView);
        superTutorsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mSuperTutorsAdapter = new SuperTutorsAdapter(mActivity, this);
        superTutorsRecyclerView.setAdapter(mSuperTutorsAdapter);
        getSuperTutorsServerCall();
        getFeaturedTutorsServerCall();
        getNearMeTutorsServerCall();
        getOffersServerCall();
        getBannersServerCall();
        return mContentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tutor_near_text:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_TUTOR_NEAR_ME), false);
                break;
            case R.id.featuredTutorialText:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_FEATURE_TUTOR), false);
                break;
            case R.id.superTutorsText:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_SUPER_TUTOR), false);
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
                                Picasso.get().load(bannersResponse.getBannerOne()).placeholder(mDefaultDrawable).into(homeImageViewTop);
                            }
                            if (bannersResponse.getBannerTwo() != null && !bannersResponse.getBannerTwo().isEmpty()) {
                                Picasso.get().load(bannersResponse.getBannerTwo()).placeholder(mDefaultDrawable).into(homeImageViewSecond);
                            }
                        }
                    }
                }
                stopProgress();
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

    private void getNearMeTutorsServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(new TutorRequest(TerminalConstant.MODE_TUTOR_NEAR_ME, 0));
                    final Response<TutorsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TutorsResponse> response) {
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            mTutorNearYouAdapter.setTutorsList(tutorsList);
                            mTutorNearYouAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void getFeaturedTutorsServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(new TutorRequest(TerminalConstant.MODE_FEATURE_TUTOR, 0));
                    final Response<TutorsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TutorsResponse> response) {
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            mFeaturedTutorAdapter.setTutorsList(tutorsList);
                            mFeaturedTutorAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void getSuperTutorsServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(new TutorRequest(TerminalConstant.MODE_SUPER_TUTOR, 0));
                    final Response<TutorsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TutorsResponse> response) {
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            mSuperTutorsAdapter.setTutorsList(tutorsList);
                            mSuperTutorsAdapter.notifyDataSetChanged();
                        }
                    }
                }
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
    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String tutorType) {
        launchFragment(new TutorDetailFragment(tutorType, itemId), true);
    }
}
