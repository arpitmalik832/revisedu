package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
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
import com.revisedu.revised.request.CityRequest;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.request.SearchRequestModel;
import com.revisedu.revised.request.SubjectRequest;
import com.revisedu.revised.response.ClassResponse;
import com.revisedu.revised.response.ListResponse;
import com.revisedu.revised.response.PrefSubjectsResponse;
import com.revisedu.revised.response.SubjectResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.revisedu.revised.TerminalConstant.MODE_SEARCH;
import static com.revisedu.revised.TerminalConstant.USER_ID;

public class SearchFragment extends BaseFragment {

    private static final String TAG = "Search Tutor";
    private TextView selectCityTextView;
    private TextView selectLocationTextView;
    private TextView selectClassTextView;
    private TextView selectSubjectTextView;
    private List<ClassResponse.ListItem> mClassList = new ArrayList<>();
    private List<PrefSubjectsResponse.ListItem> mSubjectList = new ArrayList<>();
    private String mClassId = "";
    private List<ListResponse.ListItem> mCityList = new ArrayList<>();
    private List<ListResponse.ListItem> mLocationArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_search, container, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().onBackPressed(SearchFragment.this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        setHasOptionsMenu(false);

        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);
        selectCityTextView = mContentView.findViewById(R.id.selectCityTextView);
        selectLocationTextView = mContentView.findViewById(R.id.selectLocationTextView);
        selectClassTextView = mContentView.findViewById(R.id.selectClassTextView);
        selectSubjectTextView = mContentView.findViewById(R.id.selectSubjectTextView);
        getClassServerCall();
        getPrefSubjectsServerCall();
        getCityServerCall();
//        getLocationServerCall();

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

    private void getPrefSubjectsServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<PrefSubjectsResponse> call = RetrofitApi.getServicesObject().getPrefSubjectsServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<PrefSubjectsResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<PrefSubjectsResponse> response) {
                if (response.isSuccessful()) {
                    final PrefSubjectsResponse listResponse = response.body();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectCityTextView:
                String[] cityArray = new String[mCityList.size()];
                for (int position = 0; position < mCityList.size(); position++) {
                    cityArray[position] = mCityList.get(position).getName();
                }
                showListAlertDialog(cityArray, R.id.selectCityTextView, "Select your City");
                break;
            case R.id.selectLocationTextView:
                String[] locationArray = new String[mLocationArray.size()];
                for (int position = 0; position < mLocationArray.size(); position++) {
                    locationArray[position] = mLocationArray.get(position).getName();
                }
                showListAlertDialog(locationArray, R.id.selectLocationTextView, "Select your Location");
                break;
            case R.id.selectClassTextView:
                String[] classArray = new String[mClassList.size()];
                for (int position = 0; position < mClassList.size(); position++) {
                    classArray[position] = mClassList.get(position).getClassName();
                }
                showListAlertDialog(classArray, R.id.selectClassTextView, "Select your Class");
                break;
            case R.id.selectSubjectTextView:
                String[] subjectArray = new String[mSubjectList.size()];
                for (int position = 0; position < mSubjectList.size(); position++) {
                    subjectArray[position] = mSubjectList.get(position).getSubject();
                }
                showListAlertDialog(subjectArray, R.id.selectSubjectTextView, "Select your Subject");
                break;
            case R.id.doSearchButton:
                String city = selectCityTextView.getText().toString().contains("Select your")?"":selectCityTextView.getText().toString();
                String location = selectLocationTextView.getText().toString().contains("Select your")?"":selectLocationTextView.getText().toString();
                String classValue = selectClassTextView.getText().toString().contains("Select your")?"":selectClassTextView.getText().toString();
                String  subject = selectSubjectTextView.getText().toString().contains("Select your")?"":selectSubjectTextView.getText().toString();
                SearchRequestModel requestModel = new SearchRequestModel(city,location,classValue,subject);
                launchFragment(new AllOptionsFragment(requestModel,MODE_SEARCH),true);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onAlertDialogItemClicked(String selectedItemStr, int id, int position) {
        switch (id) {
            case R.id.selectCityTextView:
                selectCityTextView.setText(selectedItemStr);
                break;
            case R.id.selectLocationTextView:
                selectLocationTextView.setText(selectedItemStr);
                break;
            case R.id.selectClassTextView:
                selectClassTextView.setText(selectedItemStr);
                break;
                case R.id.selectSubjectTextView:
                selectSubjectTextView.setText(selectedItemStr);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }

    @Override
    public void onBackPressed() {
        launchFragment(new HomeScreenFragment(), false);
    }

    private void getCityServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ListResponse> call = RetrofitApi.getServicesObject().getCityServerCall(new CityRequest(""));
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
                        if (!mCityList.isEmpty()) {
                            mCityList.clear();
                        }
                        mCityList = listResponse.getArrayList();
                    }
                }
                stopProgress();
            }
        }).start();
    }
}
