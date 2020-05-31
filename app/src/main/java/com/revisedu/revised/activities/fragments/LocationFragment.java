package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.LandmarkRequest;
import com.revisedu.revised.response.ListResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends BaseFragment {

    private static final String TAG = "LocationFragment";
    private boolean doubleBackToExitPressedOnce = false;
    private List<String> mAreaList = new ArrayList<>();
    private List<String> mLandmarkList = new ArrayList<>();
    private TextView areaTextView;
    private TextView landmarkTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_location, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        LinearLayout loginParentContainer = mContentView.findViewById(R.id.locationParentContainer);
        AnimationDrawable animationDrawable = (AnimationDrawable) loginParentContainer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        getAreaServerCall();
        areaTextView = mContentView.findViewById(R.id.areaTextView);
        landmarkTextView = mContentView.findViewById(R.id.landmarkTextView);
        return mContentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }

    private void getAreaServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getAreaServerCall();
                    final Response<ListResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ListResponse> response) {
                if (response.isSuccessful()) {
                    final ListResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mAreaList.isEmpty()) {
                            mAreaList.clear();
                        }
                        mAreaList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void getLandmarkServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getLandmarkServerCall(new LandmarkRequest(""));
                    final Response<ListResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ListResponse> response) {
                if (response.isSuccessful()) {
                    final ListResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mLandmarkList.isEmpty()) {
                            mLandmarkList.clear();
                        }
                        mLandmarkList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    protected void onAlertDialogItemClicked(String selectedItemStr, int id, int position) {
        switch (id) {
            case R.id.areaTextView:
                areaTextView.setText(selectedItemStr);
                getLandmarkServerCall();
                landmarkTextView.setVisibility(View.VISIBLE);
                break;
            case R.id.landmarkTextView:
                landmarkTextView.setText(selectedItemStr);
                //getFeesDetailServerCall();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueLocationButton:
                launchFragment(new SignUpFragment(), true);
                break;
            case R.id.areaTextView:
                String[] areaArray = new String[mAreaList.size()];
                for (int position = 0; position < mAreaList.size(); position++) {
                    areaArray[position] = mAreaList.get(position);
                }
                showListAlertDialog(areaArray, R.id.selectClassTextView, "Select Area");
                break;
            case R.id.landmarkTextView:
                String[] landmarkArray = new String[mLandmarkList.size()];
                for (int position = 0; position < mLandmarkList.size(); position++) {
                    landmarkArray[position] = mLandmarkList.get(position);
                }
                showListAlertDialog(landmarkArray, R.id.selectClassTextView, "Select Landmark");
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
}
