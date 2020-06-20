package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.SubjectsAdapter;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.response.PrefSubjectsResponse;
import com.revisedu.revised.retrofit.RetrofitApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Response;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class SubjectsFragment extends BaseFragment {

    private static final String TAG = "SubjectsFragment";
    private RecyclerView subjectsRecyclerView;
    private SubjectsAdapter mSubjectsAdapter;
    private List<PrefSubjectsResponse.ListItem> mPrefSubjectList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_subjects, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        getPrefSubjectsServerCall();
        subjectsRecyclerView = mContentView.findViewById(R.id.subjectsRecyclerView);
        subjectsRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        mSubjectsAdapter = new SubjectsAdapter(mActivity, mPrefSubjectList);
        subjectsRecyclerView.setAdapter(mSubjectsAdapter);
        return mContentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        new Handler().postDelayed(this::stopProgress, 1000);
        mActivity.hideSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(1);
    }


    private void getPrefSubjectsServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<PrefSubjectsResponse> call = RetrofitApi.getServicesObject().getPrefSubjectsServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<PrefSubjectsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<PrefSubjectsResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    final PrefSubjectsResponse prefSubjectsResponse = response.body();
                    if (prefSubjectsResponse != null) {
                        if (!mPrefSubjectList.isEmpty()) {
                            mPrefSubjectList.clear();
                        }
                        if (prefSubjectsResponse.getArrayList() != null && !prefSubjectsResponse.getArrayList().isEmpty()) {
                            mPrefSubjectList.addAll(prefSubjectsResponse.getArrayList());
                            mSubjectsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }
}
