package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.AllTutorsAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.CoachingRequest;
import com.revisedu.revised.response.CoachingResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class AllTutorsFragment extends BaseFragment implements ICustomClickListener {

    private RecyclerView mAllTutorsRecyclerView;
    private AllTutorsAdapter mAllTutorsAdapter;
    private boolean isScrolling = false;
    private int currentItems;
    private int totalItems;
    private int scrollOutItems;
    private String mTutorType = "";
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsDataAvailable;
    private static final String TAG = "AllTutorsFragment";
    private List<CoachingResponse.CoachingResponseItem> mTutorsList = new ArrayList<>();

    AllTutorsFragment(String tutorType) {
        mTutorType = tutorType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_subjects, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mTutorType);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        ToolBarManager.getInstance().onBackPressed(AllTutorsFragment.this);
        mAllTutorsRecyclerView = mContentView.findViewById(R.id.subjectsRecyclerView);
        mContentView.findViewById(R.id.subjectTextView).setVisibility(View.GONE);
        mGridLayoutManager = new GridLayoutManager(mActivity, 2);
        mAllTutorsRecyclerView.setLayoutManager(mGridLayoutManager);
        mAllTutorsAdapter = new AllTutorsAdapter(mActivity, this);
        mAllTutorsRecyclerView.setAdapter(mAllTutorsAdapter);
        mAllTutorsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mGridLayoutManager.getChildCount();
                totalItems = mGridLayoutManager.getItemCount();
                scrollOutItems = mGridLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems == totalItems) && mIsDataAvailable) {
                    isScrolling = false;
                    getTutorsServerCall();
                }
            }
        });
        getTutorsServerCall();
        return mContentView;
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
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
    }

    private void getTutorsServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(new CoachingRequest(mTutorType));
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
                        mIsDataAvailable = coachingResponse.isDataAvailable();
                        List<CoachingResponse.CoachingResponseItem> tutorsList = coachingResponse.getArrayList();
                        mTutorsList.addAll(tutorsList);
                        mAllTutorsAdapter.setTutorsList(mTutorsList);
                        mAllTutorsAdapter.notifyDataSetChanged();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String tutorType) {
        launchFragment(new TutorDetailFragment(tutorType, itemId), true);
    }
}
