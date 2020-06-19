package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.BookTutorRequest;
import com.revisedu.revised.request.TutorDetailRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.response.TutorDetailResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class TutorDetailFragment extends BaseFragment {

    private static final String TAG = "TutorDetailFragment";
    private String mTutorType = "";
    private String mTutorId;
    private String mExperience;
    private TextView addressTextView;
    private TextView experienceTextView;
    private ImageView homeImageViewTop;
    private Drawable mDefaultImage;

    TutorDetailFragment(String tutorType, String itemId) {
        mTutorType = tutorType;
        mTutorId = itemId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_tutor_detail, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mTutorType);
        ToolBarManager.getInstance().onBackPressed(TutorDetailFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        homeImageViewTop = mContentView.findViewById(R.id.homeImageViewTop);
        addressTextView = mContentView.findViewById(R.id.addressTextView);
        experienceTextView = mContentView.findViewById(R.id.experienceTextView);
        mDefaultImage = ContextCompat.getDrawable(mActivity, R.drawable.default_image);
        getTutorDetailServerCall();
        return mContentView;
    }

    private void showReadMoreDialog(String experience) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("My Experience");
        builder.setMessage(mActivity.getString(R.string.sample_tuition_intro));
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getTutorDetailServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<TutorDetailResponse> call = RetrofitApi.getServicesObject().getTutorDetailServerCall(new TutorDetailRequest(mTutorId));
                    final Response<TutorDetailResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TutorDetailResponse> response) {
                if (response.isSuccessful()) {
                    final TutorDetailResponse detailResponse = response.body();
                    if (detailResponse != null) {
                        if (detailResponse.getImage() != null && !detailResponse.getImage().isEmpty()) {
                            Picasso.get().load(detailResponse.getImage()).placeholder(mDefaultImage).into(homeImageViewTop);
                        }
                        addressTextView.setText(detailResponse.getAddress());
                        mExperience = detailResponse.getExperience();
                        experienceTextView.setText(mExperience);
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void doBookingServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().doBookingServerCall(new BookTutorRequest(getStringDataFromSharedPref(USER_ID), mTutorId));
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    final CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (commonResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            launchFragment(new HomeScreenFragment(), false);
                        }
                        showToast(commonResponse.getErrorMessage());
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.readMoreTextView:
                showReadMoreDialog(mExperience);
                break;
            case R.id.doBookingButton:
                doBookingServerCall();
                break;
            default:
                break;
        }
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
}
