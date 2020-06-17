package com.revisedu.revised.activities.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.DetailRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

public class ViewDetailsFragment extends BaseFragment {

    private String mMode = "";
    private TextView textView;

    public static ViewDetailsFragment newInstance(String mode) {
        ViewDetailsFragment fragment = new ViewDetailsFragment();
        fragment.mMode = mode;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_view_details, container, false);
        setupToolbar();
        getDetailsResponse();
        textView = mContentView.findViewById(R.id.textView);
        return mContentView;
    }

    private void getDetailsResponse() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().getDetailsResponse(new DetailRequest(mMode));
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(mMode, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    final CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textView.setText(Html.fromHtml(commonResponse.getErrorMessage(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            textView.setText(Html.fromHtml(commonResponse.getErrorMessage()));
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void setupToolbar() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(mMode);
        ToolBarManager.getInstance().onBackPressed(ViewDetailsFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        launchFragment(new HomeScreenFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.isToggleButtonEnabled(false);
        mActivity.showBackButton();
        mActivity.hideBottomNavigationView();
    }
}
