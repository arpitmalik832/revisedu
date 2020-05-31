package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;

public class SignUpFragment extends BaseFragment {

    private static final String TAG = "SignUpFragment";
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private EditText userConfirmPasswordEditText;
    private EditText userMobileNumberEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_signup, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        LinearLayout loginParentContainer = mContentView.findViewById(R.id.signUpParentContainer);
        AnimationDrawable animationDrawable = (AnimationDrawable) loginParentContainer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        mActivity.hideBottomNavigationView();
        userNameEditText = mContentView.findViewById(R.id.userNameEditText);
        userEmailEditText = mContentView.findViewById(R.id.userEmailEditText);
        userPasswordEditText = mContentView.findViewById(R.id.userPasswordEditText);
        userConfirmPasswordEditText = mContentView.findViewById(R.id.userConfirmPasswordEditText);
        userMobileNumberEditText = mContentView.findViewById(R.id.userMobileNumberEditText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadySignInText:
                launchFragment(new SignInFragment(), true);
                break;
            case R.id.signupButton:
                String userName = userNameEditText.getText().toString();
                String userEmail = userEmailEditText.getText().toString();
                String password = userPasswordEditText.getText().toString();
                String confirmPassword = userConfirmPasswordEditText.getText().toString();
                String mobile = userMobileNumberEditText.getText().toString();

                if (userName.isEmpty()) {
                    userNameEditText.setError(getString(R.string.msgMandatory));
                    userNameEditText.requestFocus();
                    return;
                }
                if (userEmail.isEmpty()) {
                    userEmailEditText.setError(getString(R.string.msgMandatory));
                    userEmailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    userPasswordEditText.setError(getString(R.string.msgMandatory));
                    userPasswordEditText.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    userConfirmPasswordEditText.setError(getString(R.string.msgMandatory));
                    userConfirmPasswordEditText.requestFocus();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    userConfirmPasswordEditText.setError(getString(R.string.confirm_password_msg));
                    userConfirmPasswordEditText.requestFocus();
                    return;
                }
                if (mobile.isEmpty()) {
                    userMobileNumberEditText.setError(getString(R.string.msgMandatory));
                    userMobileNumberEditText.requestFocus();
                    return;
                }

                showProgress();
                new Handler().postDelayed(() -> {
                    stopProgress();
                    launchFragment(new HomeScreenFragment(), true);
                }, 300);
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
