package com.revisedu.revised.activities.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.request.RegisterRequest;
import com.revisedu.revised.response.LoginResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

public class SignUpFragment extends BaseFragment {

    private static final String TAG = "SignUpFragment";
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private EditText userConfirmPasswordEditText;
    private EditText userMobileNumberEditText;
    private String userName = "";
    private String userEmail = "";
    private String password = "";
    private String mobile = "";
    private String areaId;
    private String landmarkId;
    private String cityId;

    static SignUpFragment newInstance(String areaId, String landmarkId, String cityId) {
        SignUpFragment fragment = new SignUpFragment();
        fragment.areaId = areaId;
        fragment.landmarkId = landmarkId;
        fragment.cityId = cityId;
        return fragment;
    }

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
                userName = userNameEditText.getText().toString();
                userEmail = userEmailEditText.getText().toString();
                password = userPasswordEditText.getText().toString();
                String confirmPassword = userConfirmPasswordEditText.getText().toString();
                mobile = userMobileNumberEditText.getText().toString();
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
                getRegisterServerCall();
                break;
            default:
                break;
        }
    }

    private void getRegisterServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setEmail(userEmail);
                    registerRequest.setName(userName);
                    registerRequest.setPassword(password);
                    registerRequest.setMobile(mobile);
                    registerRequest.setCity(cityId);
                    registerRequest.setLandmark(landmarkId);
                    registerRequest.setArea(areaId);
                    Call<LoginResponse> call = RetrofitApi.getServicesObject().getRegisterServerCall(registerRequest);
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
        launchFragment(new LocationFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }
}
