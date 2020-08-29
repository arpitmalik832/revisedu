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
import com.revisedu.revised.activities.fragments.adapters.FeaturedTutorAdapter;
import com.revisedu.revised.activities.fragments.adapters.SuperTutorsAdapter;
import com.revisedu.revised.activities.fragments.adapters.TutorNearYouAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.activities.interfaces.IFavouriteClickListener;
import com.revisedu.revised.request.FavouriteRequest;
import com.revisedu.revised.request.SearchRequestModel;
import com.revisedu.revised.request.TutorRequest;
import com.revisedu.revised.response.TutorsResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

import static com.revisedu.revised.TerminalConstant.MODE_NORMAL;
import static com.revisedu.revised.TerminalConstant.MODE_SEARCH;
import static com.revisedu.revised.TerminalConstant.MODE_SUBJECT;
import static com.revisedu.revised.TerminalConstant.USER_ID;

public class AllOptionsFragment extends BaseFragment implements ICustomClickListener, IFavouriteClickListener {

    private static final String TAG = "All Tutors";
    private String mSubject="";
    private TutorNearYouAdapter mTutorNearYouAdapter;
    private RecyclerView tutorNearYouRecyclerView;
    private FeaturedTutorAdapter mFeaturedTutorAdapter;
    private RecyclerView featuredTutorialRecyclerView;
    private SuperTutorsAdapter mSuperTutorsAdapter;
    private RecyclerView superTutorsRecyclerView;
    private String mMode = MODE_NORMAL;
    private SearchRequestModel mSearchModel= null;

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
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        showProgress();
        getSuperTutorsServerCall();
        getFeaturedTutorsServerCall();
        getNearMeTutorsServerCall();

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
            case R.id.tutor_near_text:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_POPULAR_COACHING), false);
                break;
            case R.id.featuredTutorialText:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_FEATURED_COACHING), false);
                break;
            case R.id.superTutorsText:
                launchFragment(new AllTutorsFragment(TerminalConstant.MODE_SUPER_COACHING), false);
                break;
            default:
                break;
        }
    }

    private void getNearMeTutorsServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userId = getStringDataFromSharedPref(USER_ID);
                    TutorRequest request = new TutorRequest(TerminalConstant.MODE_POPULAR_COACHING, 0, userId);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            //Tutor Near me Adapter Setup
                            tutorNearYouRecyclerView = mContentView.findViewById(R.id.tutorNearYouRecyclerView);
                            mContentView.findViewById(R.id.tutor_near_text).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            tutorNearYouRecyclerView.setVisibility(View.VISIBLE);
                            tutorNearYouRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mTutorNearYouAdapter = new TutorNearYouAdapter(mActivity, AllOptionsFragment.this,AllOptionsFragment.this);
                            mTutorNearYouAdapter.setTutorsList(tutorsList);
                            tutorNearYouRecyclerView.setAdapter(mTutorNearYouAdapter);
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
                    String userId = getStringDataFromSharedPref(USER_ID);
                    TutorRequest request = new TutorRequest(TerminalConstant.MODE_FEATURED_COACHING, 0, userId);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            //Featured Tutor Adapter Setup
                            featuredTutorialRecyclerView = mContentView.findViewById(R.id.featuredTutorialRecyclerView);
                            featuredTutorialRecyclerView.setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.featuredTutorialText).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            featuredTutorialRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mFeaturedTutorAdapter = new FeaturedTutorAdapter(mActivity, AllOptionsFragment.this, AllOptionsFragment.this);
                            mFeaturedTutorAdapter.setTutorsList(tutorsList);
                            featuredTutorialRecyclerView.setAdapter(mFeaturedTutorAdapter);
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
                    String userId = getStringDataFromSharedPref(USER_ID);
                    TutorRequest request = new TutorRequest(TerminalConstant.MODE_SUPER_COACHING, 0, userId);
                    if(mMode.equalsIgnoreCase(MODE_SUBJECT) && !mSubject.isEmpty()){
                        request.setSubject(mSubject);
                    }
                    else if(mMode.equalsIgnoreCase(MODE_SEARCH)){
                        request.setSearchParams(mSearchModel);
                    }
                    Call<TutorsResponse> call = RetrofitApi.getServicesObject().getTutorsServerCall(request);
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
                stopProgress();
                if (response.isSuccessful()) {
                    final TutorsResponse offersResponse = response.body();
                    if (offersResponse != null) {
                        List<TutorsResponse.TutorsResponseItem> tutorsList = offersResponse.getArrayList();
                        if (!tutorsList.isEmpty()) {
                            //Super Tutor Adapter Setup
                            superTutorsRecyclerView = mContentView.findViewById(R.id.superTutorsRecyclerView);
                            superTutorsRecyclerView.setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.superTutorsText).setVisibility(View.VISIBLE);
                            mContentView.findViewById(R.id.noDataFoundGroup).setVisibility(View.GONE);
                            superTutorsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                            mSuperTutorsAdapter = new SuperTutorsAdapter(mActivity, AllOptionsFragment.this,AllOptionsFragment.this);
                            mSuperTutorsAdapter.setTutorsList(tutorsList);
                            superTutorsRecyclerView.setAdapter(mSuperTutorsAdapter);

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
        launchFragment(new TutorDetailFragment(tutorType, itemId), true);
    }

    @Override
    public void onFavouriteItemClick(FavouriteRequest request) {
        favouriteServerCall(request);
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
