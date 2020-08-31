package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class TutorDetailFragment extends BaseFragment {

    private static final String TAG = "TutorDetailFragment";
    private String mTutorType = "";
    private String mTutorId;
    private String mExperience;
    private ImageView homeImageViewTop;
    private TextView aboutInstituteTV;
    private TextView aboutTeachersTV;
    private TextView subjectTV;
    private TextView addressTV;
    private Drawable mDefaultImage;

    public TutorDetailFragment(String tutorType, String itemId) {
        mTutorType = tutorType;
        mTutorId = itemId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_tutor_detail, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mTutorType);
        ToolBarManager.getInstance().onBackPressed(TutorDetailFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);

        homeImageViewTop = mContentView.findViewById(R.id.homeImageViewTop);
        aboutInstituteTV = mContentView.findViewById(R.id.aboutInstitute);
        aboutTeachersTV = mContentView.findViewById(R.id.aboutTeachers);
        subjectTV = mContentView.findViewById(R.id.subject);
        addressTV = mContentView.findViewById(R.id.address);
        mDefaultImage = ContextCompat.getDrawable(mActivity, R.drawable.default_image);

        getTutorDetailServerCall();
        return mContentView;
    }

    private void showReadMoreDialog(String experience) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("My Experience");
        builder.setMessage(experience);
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
                        addressTV.setText(detailResponse.getAddress());
                        mExperience = detailResponse.getExperience();
                        aboutInstituteTV.setText(mExperience);
                        List<TutorDetailResponse.TutorDetailSubjects> subjects = detailResponse.getSubjectsList();
                        if (subjects != null && !subjects.isEmpty()) {
                            StringBuilder subjectStr = new StringBuilder();
                            for (TutorDetailResponse.TutorDetailSubjects subject : subjects) {
                                subjectStr.append("->  ").append(subject.getSubjects()).append("\n");
                            }
                            subjectTV.setText(subjectStr.toString());
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void doBookingServerCall() {
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
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doBookingButton:
                showProgress();
                mActivity.startPayment("100");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPaymentSuccess(String txn) {
        showToast("Payment Done successfully");
        doBookingServerCall();
    }

    @Override
    public void onPaymentError(int i, String s) {
        stopProgress();
        showToast("Payment failed :: " + s);
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
