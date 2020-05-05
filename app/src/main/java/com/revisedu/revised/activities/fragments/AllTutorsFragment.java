package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
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

public class AllTutorsFragment extends BaseFragment {

    private RecyclerView subjectsRecyclerView;
    private SubjectsAdapter mSubjectsAdapter;
    private String mTutorType = "";

    AllTutorsFragment(String tutorType) {
        mTutorType = tutorType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_subjects, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mTutorType);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        ToolBarManager.getInstance().onBackPressed(AllTutorsFragment.this);
        subjectsRecyclerView = mContentView.findViewById(R.id.subjectsRecyclerView);
        mContentView.findViewById(R.id.subjectTextView).setVisibility(View.GONE);
        subjectsRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mSubjectsAdapter = new SubjectsAdapter(mActivity);
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
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgress();
            }
        }, 1000);
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
    }
}
