package com.revisedu.revised.activities.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.CoachingVideoAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.request.BookTutorRequest;
import com.revisedu.revised.request.CoachingDetailRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.response.CoachingDetailResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class CoachingDetailFragment extends BaseFragment implements ICustomClickListener {

    private static final String TAG = "CoachingDetailFragment";

    private String mCoachingType;
    private String mCoachingId;

    private String mCoachingPrice;

    private ImageView firstImage;
    private TextView aboutInstituteTV;
    private ImageView instituteIV;
    private TextView aboutTeachersTV;
    private ImageView teacherIV;
    private TextView subjectsTV;
    private TextView addressTV;

    private CoachingVideoAdapter mCoachingVideoAdapter;
    private LinearLayout mCoachingVideoContainer;

    private Drawable mDefaultImage;

    public CoachingDetailFragment(String tutorType, String itemId) {
        mCoachingType = tutorType;
        mCoachingId = itemId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_coaching_detail, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mCoachingType);
        ToolBarManager.getInstance().onBackPressed(CoachingDetailFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        setHasOptionsMenu(false);

        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);

        firstImage = mContentView.findViewById(R.id.firstImage);
        aboutInstituteTV = mContentView.findViewById(R.id.aboutInstitute);
        instituteIV = mContentView.findViewById(R.id.instituteImage);
        aboutTeachersTV = mContentView.findViewById(R.id.aboutTeachers);
        teacherIV = mContentView.findViewById(R.id.teacherImage);
        subjectsTV = mContentView.findViewById(R.id.subjects);
        addressTV = mContentView.findViewById(R.id.address);

        mCoachingVideoContainer = mContentView.findViewById(R.id.youtubeLinksContainer);

        getCoachingDetailServerCall();

        mDefaultImage = ContextCompat.getDrawable(mActivity, R.drawable.default_image);

        return mContentView;
    }

   // private void showReadMoreDialog(String experience) {
     //   AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
       // builder.setTitle("My Experience");
       // builder.setMessage(experience);
        //builder.setCancelable(true);
        //AlertDialog alertDialog = builder.create();
       // alertDialog.show();
   // }

    private void getCoachingDetailServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CoachingDetailResponse> call = RetrofitApi.getServicesObject().getCoachingDetailServerCall(new CoachingDetailRequest(mCoachingId));
                    final Response<CoachingDetailResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CoachingDetailResponse> response) {
                if (response.isSuccessful()) {
                    final CoachingDetailResponse responseBody = response.body();
                    if (responseBody != null) {
                        CoachingDetailResponse.CoachingDetail coachingDetail = responseBody.getCoachingDetail();

                        mCoachingPrice = coachingDetail.getPrice();

                        if (coachingDetail.getBanner() != null && !coachingDetail.getBanner().isEmpty()) {
                            Picasso.get().load(coachingDetail.getBanner()).placeholder(mDefaultImage).into(firstImage);
                        }

                        aboutInstituteTV.setText(coachingDetail.getAboutInstitute());
                        if (coachingDetail.getInstituteImage() != null && !coachingDetail.getInstituteImage().isEmpty()) {
                            Picasso.get().load(coachingDetail.getInstituteImage()).placeholder(mDefaultImage).into(instituteIV);
                        }

                        aboutTeachersTV.setText(coachingDetail.getAboutTeacher());
                        if (coachingDetail.getTeacherImage() != null && !coachingDetail.getTeacherImage().isEmpty()) {
                            Picasso.get().load(coachingDetail.getTeacherImage()).placeholder(mDefaultImage).into(teacherIV);
                        }

                        List<CoachingDetailResponse.CoachingDetail.CoachingSubject> subjects = coachingDetail.getSubjects();
                        if (subjects != null && !subjects.isEmpty()) {
                            StringBuilder subjectStr = new StringBuilder();
                            for (CoachingDetailResponse.CoachingDetail.CoachingSubject subject : subjects) {
                                subjectStr.append("->  ").append(subject.getName()).append("\n");
                            }
                            subjectsTV.setText(subjectStr.toString());
                        }

                        addressTV.setText(coachingDetail.getAddress());

                        if(!coachingDetail.getVideo().isEmpty()) {
                            //Youtube Videos Adapter Setup
                            RecyclerView coachingVideoRecyclerView = mContentView.findViewById(R.id.youtubeLinksRecyclerView);
                            coachingVideoRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                            mCoachingVideoAdapter = new CoachingVideoAdapter(mActivity, coachingDetail.getVideo(), CoachingDetailFragment.this);
                            coachingVideoRecyclerView.setAdapter(mCoachingVideoAdapter);
                        } else {
                            mCoachingVideoContainer.setVisibility(View.GONE);
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
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().doBookingServerCall(new BookTutorRequest(getStringDataFromSharedPref(USER_ID), mCoachingId));
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
                mActivity.startPayment(mCoachingPrice);
                break;
            case R.id.addARating:
                //api not given
                showToast("Api Not Given");
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

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String coachingType) {
        mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(itemValue)));
    }
}
