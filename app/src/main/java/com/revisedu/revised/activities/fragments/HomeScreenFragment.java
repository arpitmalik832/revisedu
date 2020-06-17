package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mActivity.getString(R.string.app_name));
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
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
        mOffersAdapter = new OffersAdapter(mActivity, this);
        offersRecyclerView.setAdapter(mOffersAdapter);
        //Super Tutor Adapter Setup
        superTutorsRecyclerView = mContentView.findViewById(R.id.superTutorsRecyclerView);
        superTutorsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mSuperTutorsAdapter = new SuperTutorsAdapter(mActivity);
        superTutorsRecyclerView.setAdapter(mSuperTutorsAdapter);
        return mContentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tutor_near_text:
                launchFragment(new AllTutorsFragment("Tutor Near me"), false);
                break;
            case R.id.featuredTutorialText:
                showToast("featuredTutorialText");
                launchFragment(new AllTutorsFragment("Featured Tutor"), false);
                break;
            case R.id.offersText:
                showToast("offersText");
                launchFragment(new AllTutorsFragment("All Offers"), false);
                break;
            case R.id.superTutorsText:
                showToast("superTutorsText");
                launchFragment(new AllTutorsFragment("Super Tutor"), false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressedToExit();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showToast(mActivity.getString(R.string.please_double_click_to_exit));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, TerminalConstant.BACK_PRESS_TIME_INTERVAL);
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
        mActivity.showSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(2);
        mActivity.hideBackButton();
        mActivity.isToggleButtonEnabled(true);
    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String tutorType) {
        showToast(itemValue);
        launchFragment(new TutorDetailFragment(tutorType), true);
    }
}
