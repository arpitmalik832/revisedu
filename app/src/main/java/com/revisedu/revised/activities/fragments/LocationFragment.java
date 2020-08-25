package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;
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
    private List<ListResponse.ListItem> mAreaList = new ArrayList<>();
    private List<ListResponse.ListItem> mLandmarkList = new ArrayList<>();
    private List<ListResponse.ListItem> mCityList = new ArrayList<>();
    private TextView areaTextView;
    private TextView landmarkTextView;
    private String mSelectedAreaItemId = "";
    private String mSelectedLandMarkItemId = "";
    private Button continueLocationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_location, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        getAreaServerCall();
        areaTextView = mContentView.findViewById(R.id.areaTextView);
        landmarkTextView = mContentView.findViewById(R.id.landmarkTextView);
        continueLocationButton = mContentView.findViewById(R.id.continueLocationButton);
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
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getLandmarkServerCall(new LandmarkRequest(mSelectedAreaItemId));
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
                mSelectedAreaItemId = mAreaList.get(position).getId();
                getLandmarkServerCall();
                landmarkTextView.setVisibility(View.VISIBLE);
                break;
            case R.id.landmarkTextView:
                mSelectedLandMarkItemId = mLandmarkList.get(position).getId();
                landmarkTextView.setText(selectedItemStr);
                continueLocationButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueLocationButton:
                launchFragment(SignUpFragment.newInstance(mSelectedAreaItemId, mSelectedLandMarkItemId), true);
                break;
            case R.id.areaTextView:
                String[] areaArray = new String[mAreaList.size()];
                for (int position = 0; position < mAreaList.size(); position++) {
                    areaArray[position] = mAreaList.get(position).getName();
                }
                showListAlertDialog(areaArray, R.id.areaTextView, "Select Area");
                break;
            case R.id.landmarkTextView:
                String[] landmarkArray = new String[mLandmarkList.size()];
                for (int position = 0; position < mLandmarkList.size(); position++) {
                    landmarkArray[position] = mLandmarkList.get(position).getName();
                }
                showListAlertDialog(landmarkArray, R.id.landmarkTextView, "Select Landmark");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        launchFragment(new SignInFragment(), false);
    }
}
