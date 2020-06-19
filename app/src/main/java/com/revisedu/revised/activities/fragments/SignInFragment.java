package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.LoginRequest;
import com.revisedu.revised.response.LoginResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

public class SignInFragment extends BaseFragment {

    private static final String TAG = "SignInFragment";
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private boolean doubleBackToExitPressedOnce = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        mActivity.hideBottomNavigationView();
        userNameEditText = mContentView.findViewById(R.id.userNameEditText);
        userEmailEditText = mContentView.findViewById(R.id.userEmail);
        userPasswordEditText = mContentView.findViewById(R.id.userPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadySignInText:
                launchFragment(new LocationFragment(), false);
                break;
            case R.id.forgotPasswordTextView:
                launchFragment(new ForgotPasswordFragment(), true);
                break;
            case R.id.signInButton:
                String userEmail = userEmailEditText.getText().toString();
                String userPassword = userPasswordEditText.getText().toString();
                if (userEmail.isEmpty()) {
                    userEmailEditText.setError(getString(R.string.msgMandatory));
                    userEmailEditText.requestFocus();
                    return;
                }
                if (userPassword.isEmpty()) {
                    userPasswordEditText.setError(getString(R.string.msgMandatory));
                    userPasswordEditText.requestFocus();
                    return;
                }
                getLoginServerCall(userEmail, userPassword);
                break;
            default:
                break;
        }
    }

    private void getLoginServerCall(String userEmail, String userPassword) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<LoginResponse> call = RetrofitApi.getServicesObject().getLoginServerCall(new LoginRequest(userEmail, userPassword));
                    final Response<LoginResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    final LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        showToast(loginResponse.getErrorMessage());
                        if (loginResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            storeStringDataInSharedPref(TerminalConstant.USER_ID, loginResponse.getUserId());
                            storeStringDataInSharedPref(TerminalConstant.USER_LOGIN_DONE, TerminalConstant.YES);
                            launchFragment(new HomeScreenFragment(), true);
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressedToExit();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showToast(mActivity.getString(R.string.please_double_click_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, TerminalConstant.BACK_PRESS_TIME_INTERVAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
