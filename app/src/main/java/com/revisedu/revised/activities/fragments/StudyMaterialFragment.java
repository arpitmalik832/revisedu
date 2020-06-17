package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
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
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.response.ListResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialFragment extends BaseFragment {

    private static final String TAG = "Profile";
    private TextView selectClassTextView;
    private TextView classFirstTextView;
    private TextView topicFirstTextView;
    private List<ListResponse.ListItem> mClassList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_study_material, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        selectClassTextView = mContentView.findViewById(R.id.selectClassTextView);
        classFirstTextView = mContentView.findViewById(R.id.classFirstTextView);
        topicFirstTextView = mContentView.findViewById(R.id.topicFirstTextView);
        TextView downloadNotesTextView = mContentView.findViewById(R.id.downloadNotesTextView);
        SpannableString downloadNotesString = new SpannableString(mActivity.getString(R.string.click_here_to_download_your_notes));
        UnderlineSpan underlineSpan = new UnderlineSpan();
        downloadNotesString.setSpan(underlineSpan, 14, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        downloadNotesTextView.setText(downloadNotesString);
        getClassServerCall();
        return mContentView;
    }

    private void getClassServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getClassServerCall();
                    final Response<ListResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ListResponse> response) {
                if (response.isSuccessful()) {
                    final ListResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mClassList.isEmpty()) {
                            mClassList.clear();
                        }
                        mClassList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void getSubjectServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getClassServerCall();
                    final Response<ListResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ListResponse> response) {
                if (response.isSuccessful()) {
                    final ListResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mClassList.isEmpty()) {
                            mClassList.clear();
                        }
                        mClassList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectClassTextView:
                String[] classArray = new String[mClassList.size()];
                for (int position = 0; position < mClassList.size(); position++) {
                    classArray[position] = mClassList.get(position).getName();
                }
                showListAlertDialog(classArray, R.id.selectClassTextView, "Select Class");
                break;
            case R.id.classFirstTextView:
                showListAlertDialog(TerminalConstant.CLASSES_ARRAY, R.id.classFirstTextView, "Select Sub Class");
                break;
            case R.id.topicFirstTextView:
                showListAlertDialog(TerminalConstant.TOPICS_ARRAY, R.id.topicFirstTextView, "Select Topic");
                break;
            case R.id.downloadNotesTextView:
                showToast("downloading start...");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onAlertDialogItemClicked(String selectedItemStr, int id, int position) {
        switch (id) {
            case R.id.selectClassTextView:
                selectClassTextView.setText(selectedItemStr);
                break;
            case R.id.classFirstTextView:
                classFirstTextView.setText(selectedItemStr);
                break;
            case R.id.topicFirstTextView:
                topicFirstTextView.setText(selectedItemStr);
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
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(3);
    }
}
