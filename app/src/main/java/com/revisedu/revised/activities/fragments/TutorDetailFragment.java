package com.revisedu.revised.activities.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;

public class TutorDetailFragment extends BaseFragment {

    private static final String TAG = "About Tutor";
    private String mTutorType = "";

    TutorDetailFragment(String tutorType) {
        mTutorType = tutorType;
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
        return mContentView;
    }

    void showReadMoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("My Experience");
        builder.setMessage(mActivity.getString(R.string.sample_tuition_intro));
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.readMoreTextView:
                showReadMoreDialog();
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
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgress();
            }
        }, 1000);
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
