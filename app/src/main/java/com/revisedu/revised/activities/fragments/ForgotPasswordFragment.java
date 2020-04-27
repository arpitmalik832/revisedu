package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;

public class ForgotPasswordFragment extends BaseFragment {

    private static final String TAG = "ForgotPasswordFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        LinearLayout loginParentContainer = mContentView.findViewById(R.id.signUpParentContainer);
        AnimationDrawable animationDrawable = (AnimationDrawable) loginParentContainer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        mActivity.hideBottomNavigationView();
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
        launchFragment(new SignInFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.showSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
