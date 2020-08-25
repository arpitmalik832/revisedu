package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;

public class ForgotPasswordFragment extends BaseFragment {

    private static final String TAG = "ForgotPasswordFragment";
    private EditText userEmailEditText;
    private EditText otpEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        mActivity.hideBottomNavigationView();
        userEmailEditText = mContentView.findViewById(R.id.userEmail);
        otpEditText = mContentView.findViewById(R.id.userOtpEditText);
        otpEditText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgotPasswordButton:
                String emailAddress = userEmailEditText.getText().toString();
                if (emailAddress.isEmpty()) {
                    userEmailEditText.setError(getString(R.string.msgMandatory));
                    userEmailEditText.requestFocus();
                    return;
                }
                //launchFragment(new HomeScreenFragment(), true);
                break;
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
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
