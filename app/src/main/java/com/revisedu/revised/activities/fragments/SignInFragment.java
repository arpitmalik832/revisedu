package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;

public class SignInFragment extends BaseFragment {

    private static final String TAG = "SignInFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
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
            case R.id.alreadySignInText:
                launchFragment(new SignUpFragment(), false);
                break;
            case R.id.forgotPasswordTextView:
                launchFragment(new ForgotPasswordFragment(), true);
                break;
            case R.id.signInButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopProgress();
                        launchFragment(new HomeScreenFragment(), true);
                    }
                },300);
                showProgress();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        launchFragment(new LocationFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
