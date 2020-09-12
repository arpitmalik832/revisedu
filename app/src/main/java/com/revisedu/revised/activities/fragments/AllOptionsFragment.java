package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.revisedu.revised.activities.fragments.adapters.SuperCoachingAdapter;
import com.revisedu.revised.activities.fragments.adapters.PopularCoachingAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.SearchRequestModel;
import com.revisedu.revised.request.CoachingRequest;
import com.revisedu.revised.response.CoachingResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.revisedu.revised.TerminalConstant.MODE_NORMAL;
import static com.revisedu.revised.TerminalConstant.MODE_SEARCH;
import static com.revisedu.revised.TerminalConstant.MODE_SUBJECT;

public class AllOptionsFragment extends BaseFragment implements ICustomClickListener {

    private static final String TAG = "All Options";

    private String mSubject;
    private SearchRequestModel mSearchModel;
    private String mMode;

    private PopularCoachingAdapter mPopularCoachingAdapter;
    private RecyclerView popularCoachingRecyclerView;

    private FeaturedCoachingAdapter mFeaturedCoachingAdapter;
    private RecyclerView featuredCoachingRecyclerView;

    private SuperCoachingAdapter mSuperCoachingAdapter;
    private RecyclerView superCoachingRecyclerView;

    public AllOptionsFragment(String subject, String mode) {
        this.mSubject = subject;
        this.mMode = mode;
    }

    public AllOptionsFragment(SearchRequestModel searchModel, String mode) {
        this.mSearchModel = searchModel;
        this.mMode = mode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_all_options, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity,false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().onBackPressed(AllOptionsFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        setHasOptionsMenu(false);

        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        showProgress();

        getPopularCoachingServerCall();
        getFeaturedCoachingServerCall();
        getSuperCoachingServerCall();

        setupHeader();
        return mContentView;
    }

    private void setupHeader() {
        TextView txt_all_options_heading = mContentView.findViewById(R.id.txt_all_options_heading);
        switch (mMode){
            case MODE_SUBJECT:
                txt_all_options_heading.setVisibility(View.VISIBLE);
                txt_all_options_heading.setText(mSubject);
                break;
            case MODE_SEARCH:
                txt_all_options_heading.setVisibility(View.VISIBLE);
                txt_all_options_heading.setText(mSearchModel.getShowText());
                break;
            case MODE_NORMAL:
                txt_all_options_heading.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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

    private void getPopularCoachingServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CoachingRequest request = new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_POPULAR_COACHING);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            //Popular Coaching Adapter Setup
                            popularCoachingRecyclerView = mContentView.findViewById(R.id.popularCoachingRecyclerView);
                            mContentView.findViewById(R.id.popularCoachingText).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            popularCoachingRecyclerView.setVisibility(View.VISIBLE);
                            popularCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mPopularCoachingAdapter = new PopularCoachingAdapter(mActivity, AllOptionsFragment.this);

                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mPopularCoachingAdapter.setCoachingList(newCoachingList);
                            popularCoachingRecyclerView.setAdapter(mPopularCoachingAdapter);
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
                    CoachingRequest request = new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_FEATURED_COACHING);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            //Featured Coaching Adapter Setup
                            featuredCoachingRecyclerView = mContentView.findViewById(R.id.featuredCoachingRecyclerView);
                            featuredCoachingRecyclerView.setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.featuredCoachingText).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            featuredCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mFeaturedCoachingAdapter = new FeaturedCoachingAdapter(mActivity, AllOptionsFragment.this);

                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mFeaturedCoachingAdapter.setCoachingList(newCoachingList);
                            featuredCoachingRecyclerView.setAdapter(mFeaturedCoachingAdapter);
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
                    CoachingRequest request = new CoachingRequest(TerminalConstant.PACKAGE_ID_FOR_SUPER_COACHING);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<CoachingResponse> call = RetrofitApi.getServicesObject().getCoachingServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final CoachingResponse coachingResponse = response.body();
                    if (coachingResponse != null) {
                        List<CoachingResponse.CoachingResponseItem> coachingList = coachingResponse.getArrayList();
                        if (!coachingList.isEmpty()) {
                            //Super Coaching Adapter Setup
                            superCoachingRecyclerView = mContentView.findViewById(R.id.superCoachingRecyclerView);
                            superCoachingRecyclerView.setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.superCoachingText).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            superCoachingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mSuperCoachingAdapter = new SuperCoachingAdapter(mActivity, AllOptionsFragment.this);

                            ArrayList<CoachingResponse.CoachingResponseItem> newCoachingList = new ArrayList<>();
                            if(coachingList.size()<=10) {
                                newCoachingList.addAll(coachingList);
                            } else {
                                for(int i = 0; i < 10; i++) {
                                    newCoachingList.add(coachingList.get(i));
                                }
                            }
                            mSuperCoachingAdapter.setCoachingList(newCoachingList);
                            superCoachingRecyclerView.setAdapter(mSuperCoachingAdapter);
                        }
                    }
                }
            }
        }).start();
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

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String tutorType) {
        launchFragment(new CoachingDetailFragment(tutorType, itemId), true);
    }

    @Override
    public void onBackPressed() {
        if (mMode.equalsIgnoreCase(MODE_SEARCH)) {
        launchFragment(new SearchFragment(), false);
        }
        else if (mMode.equalsIgnoreCase(MODE_SUBJECT)) {
            launchFragment(new SubjectsFragment(), false);
        }
    }

}
