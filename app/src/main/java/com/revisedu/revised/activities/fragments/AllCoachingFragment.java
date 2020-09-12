package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.AllCoachingAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.CoachingRequest;
import com.revisedu.revised.response.CoachingResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class AllCoachingFragment extends BaseFragment implements ICustomClickListener {

    private static final String TAG = "AllCoachingFragment";

    private AllCoachingAdapter mAllCoachingAdapter;

    private String mCoachingType;
    private String packageId;

    private List<CoachingResponse.CoachingResponseItem> mCoachingList = new ArrayList<>();

    AllCoachingFragment(String tutorType) {
        mCoachingType = tutorType;
        if(tutorType.equals(TerminalConstant.MODE_POPULAR_COACHING))
            packageId = TerminalConstant.PACKAGE_ID_FOR_POPULAR_COACHING;
        if(tutorType.equals(TerminalConstant.MODE_FEATURED_COACHING))
            packageId = TerminalConstant.PACKAGE_ID_FOR_FEATURED_COACHING;
        if(tutorType.equals(TerminalConstant.MODE_SUPER_COACHING))
            packageId = TerminalConstant.PACKAGE_ID_FOR_SUPER_COACHING;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_subjects, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mCoachingType);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        ToolBarManager.getInstance().onBackPressed(AllCoachingFragment.this);
        setHasOptionsMenu(false);

        mContentView.findViewById(R.id.subjectTextView).setVisibility(View.GONE);

        //All Coaching Recycler View Setup
        RecyclerView mAllCoachingRecyclerView = mContentView.findViewById(R.id.subjectsRecyclerView);
        mAllCoachingRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAllCoachingAdapter = new AllCoachingAdapter(mActivity, this);
        mAllCoachingRecyclerView.setAdapter(mAllCoachingAdapter);

        getCoachingServerCall();
        return mContentView;
    }

    @Override
    public void onBackPressed() { launchFragment(new HomeScreenFragment(), false); }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
    }

    private void getCoachingServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(new CoachingRequest(packageId));
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
                        List<CoachingResponse.CoachingResponseItem> tutorsList = coachingResponse.getArrayList();
                        mCoachingList.addAll(tutorsList);
                        mAllCoachingAdapter.setTutorsList(mCoachingList);
                        mAllCoachingAdapter.notifyDataSetChanged();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String tutorType) {
        launchFragment(new CoachingDetailFragment(tutorType, itemId), true);
    }
}