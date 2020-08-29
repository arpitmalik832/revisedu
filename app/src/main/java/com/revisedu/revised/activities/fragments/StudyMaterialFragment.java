package com.revisedu.revised.activities.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.NotesDownloadRequest;
import com.revisedu.revised.request.SubjectRequest;
import com.revisedu.revised.request.TopicRequest;
import com.revisedu.revised.response.ClassResponse;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.response.SubjectResponse;
import com.revisedu.revised.response.TopicResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class StudyMaterialFragment extends BaseFragment {

    private static final String TAG = "StudyMaterialFragment";
    private TextView selectClassTextView;
    private TextView selectSubjectTextView;
    private TextView selectTopicTextView;
    private Button fetchDetailButton;
    private List<ClassResponse.ListItem> mClassList = new ArrayList<>();
    private List<SubjectResponse.ListItem> mSubjectList = new ArrayList<>();
    private List<TopicResponse.ListItem> mTopicList = new ArrayList<>();
    private String mClassId = "";
    private String mSubjectId = "";
    private String mTopicId = "";
    private String mDownloadLink = "";
    private TextView downloadNotesTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_study_material, container, false);
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        fetchDetailButton = mContentView.findViewById(R.id.updateProfileButton);
        selectClassTextView = mContentView.findViewById(R.id.selectClassTextView);
        selectSubjectTextView = mContentView.findViewById(R.id.classFirstTextView);
        selectTopicTextView = mContentView.findViewById(R.id.topicFirstTextView);
        downloadNotesTextView = mContentView.findViewById(R.id.downloadNotesTextView);
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
                    Call<ClassResponse> call = RetrofitApi.getServicesObject().getClassServerCall();
                    final Response<ClassResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ClassResponse> response) {
                if (response.isSuccessful()) {
                    final ClassResponse listResponse = response.body();
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
                    Call<SubjectResponse> call = RetrofitApi.getServicesObject().getSubjectServerCall(new SubjectRequest(mClassId));
                    final Response<SubjectResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    final SubjectResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mSubjectList.isEmpty()) {
                            mSubjectList.clear();
                        }
                        mSubjectList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void getTopicServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<TopicResponse> call = RetrofitApi.getServicesObject().getTopicServerCall(new TopicRequest(mClassId, mSubjectId));
                    final Response<TopicResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    final TopicResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (!mTopicList.isEmpty()) {
                            mTopicList.clear();
                        }
                        mTopicList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void getNotesDownloadServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NotesDownloadRequest request = new NotesDownloadRequest();
                    request.setUserId(getStringDataFromSharedPref(USER_ID));
                    request.setClassStr(mClassId);
                    request.setSubjectStr(mSubjectId);
                    request.setTopicStr(mTopicId);
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().getNotesDownloadServerCall(request);
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    final CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (commonResponse.getErrorMessage() != null && !commonResponse.getErrorMessage().isEmpty()) {
                            downloadNotesTextView.setVisibility(View.VISIBLE);
                            mDownloadLink = commonResponse.getErrorMessage();
                        } else {
                            showToast("No link available for download");
                        }
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private void startDownloading() {
        showToast("Downloading started...");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mDownloadLink));
        request.setTitle(mTopicId);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mTopicId + ".pdf");
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager downloadManager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectClassTextView:
                selectSubjectTextView.setText(getString(R.string.select_subject));
                selectTopicTextView.setText(getString(R.string.select_topic));
                fetchDetailButton.setVisibility(View.INVISIBLE);
                selectSubjectTextView.setVisibility(View.INVISIBLE);
                selectTopicTextView.setVisibility(View.INVISIBLE);
                downloadNotesTextView.setVisibility(View.INVISIBLE);
                String[] classArray = new String[mClassList.size()];
                for (int position = 0; position < mClassList.size(); position++) {
                    classArray[position] = mClassList.get(position).getClassName();
                }
                showListAlertDialog(classArray, R.id.selectClassTextView, "Select Class");
                break;
            case R.id.classFirstTextView:
                selectTopicTextView.setText(getString(R.string.select_topic));
                fetchDetailButton.setVisibility(View.INVISIBLE);
                selectTopicTextView.setVisibility(View.INVISIBLE);
                downloadNotesTextView.setVisibility(View.INVISIBLE);
                String[] subjectArray = new String[mSubjectList.size()];
                for (int position = 0; position < mSubjectList.size(); position++) {
                    subjectArray[position] = mSubjectList.get(position).getSubjectName();
                }
                showListAlertDialog(subjectArray, R.id.classFirstTextView, "Select Subject");
                break;
            case R.id.topicFirstTextView:
                downloadNotesTextView.setVisibility(View.INVISIBLE);
                String[] topicArray = new String[mTopicList.size()];
                for (int position = 0; position < mTopicList.size(); position++) {
                    topicArray[position] = mTopicList.get(position).getTopicName();
                }
                showListAlertDialog(topicArray, R.id.topicFirstTextView, "Select Topic");
                break;
            case R.id.updateProfileButton:
                getNotesDownloadServerCall();
                break;
            case R.id.downloadNotesTextView:
                startDownloading();
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
                mClassId = mClassList.get(position).getClassName();
                selectSubjectTextView.setVisibility(View.VISIBLE);
                getSubjectServerCall();
                break;
            case R.id.classFirstTextView:
                selectSubjectTextView.setText(selectedItemStr);
                mSubjectId = mSubjectList.get(position).getSubjectName();
                selectTopicTextView.setVisibility(View.VISIBLE);
                getTopicServerCall();
                break;
            case R.id.topicFirstTextView:
                selectTopicTextView.setText(selectedItemStr);
                fetchDetailButton.setVisibility(View.VISIBLE);
                mTopicId = mTopicList.get(position).getTopicName();
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
        mActivity.hideSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(3);
    }
}
