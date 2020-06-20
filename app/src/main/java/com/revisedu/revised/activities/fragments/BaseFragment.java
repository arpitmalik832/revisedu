package com.revisedu.revised.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.revisedu.revised.R;
import com.revisedu.revised.activities.HomeActivity;
import com.revisedu.revised.request.FavouriteRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.revisedu.revised.TerminalConstant.SHARED_PREF_NAME;

public class BaseFragment extends Fragment implements View.OnClickListener {

    protected View mContentView;
    protected HomeActivity mActivity;
    private ProgressDialog mProgressDialog;
    private String selectedItemStr = "";
    private static int sHomeScreenLastTabPosition = 0;

    static int getHomeScreenLastTabPosition() {
        return sHomeScreenLastTabPosition;
    }

    static void setHomeScreenLastTabPosition(int homeScreenLastTabPosition) {
        sHomeScreenLastTabPosition = homeScreenLastTabPosition;
    }

    void showProgress() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Processing");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    protected void stopProgress() {
        updateOnUiThread(() -> {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        mActivity = (HomeActivity) activity;
    }

    protected void updateOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void storeStringDataInSharedPref(String keyName, String value) {
        if (getActivity() != null) {
            SharedPreferences.Editor editor = mActivity.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putString(keyName, value);
            editor.apply();
        }
    }

    void favouriteServerCall(FavouriteRequest request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().favouriteServerCall(request);
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e("BaseFragment", e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    final CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        showToast(commonResponse.getErrorMessage());
                    }
                }
            }
        }).start();
    }

    String getStringDataFromSharedPref(String keyName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(keyName, "");
    }

    protected void showToast(String msg) {
        mActivity.showToast(msg);
    }

    @Override
    public void onClick(View view) {
        /*
         * Just a override method to invoke the back pressed of the fragments
         * */
    }

    public void onBackPressed(BaseFragment fragment) {
        if (fragment != null && fragment.getActivity() != null) {
            fragment.getActivity().onBackPressed();
        }
    }

    void onBackPressedToExit() {
        mActivity.finish();
    }

    void showListAlertDialog(final String[] list, final int id, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setItems(list, (dialogInterface, i) -> {
            selectedItemStr = list[i];
            onAlertDialogItemClicked(selectedItemStr, id, i);
            dialogInterface.dismiss();
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void onAlertDialogItemClicked(String selectedItemStr, int id, int position) {
    }

    public void onBackPressed() {
        /*
         * Empty
         * */
    }

    protected void launchFragment(Fragment fragment, boolean addBackStack) {
        mActivity.launchFragment(fragment, addBackStack);
    }

    protected void launchFragmentWithoutAnimation(Fragment fragment, boolean addBackStack) {
        mActivity.launchFragmentWithoutAnimation(fragment, addBackStack);
    }

    protected void setupNavigationHeader(String name, String email, String imgUrl){
        Drawable mDefaultDrawable = ContextCompat.getDrawable(mActivity, R.drawable.ic_person_black_24dp);
        ConstraintLayout nav_view_header_layout = mActivity.findViewById(R.id.nav_view_header_layout);
        ImageView imgUserIcon = nav_view_header_layout.findViewById(R.id.imgUserIcon);
        TextView txtEmailId = nav_view_header_layout.findViewById(R.id.txtEmailId);
        TextView txtName = nav_view_header_layout.findViewById(R.id.txtName);
        txtName.setText(name);
        txtEmailId.setText(email);
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Picasso.get().load(imgUrl).resize(100, 100).placeholder(mDefaultDrawable).into(imgUserIcon);
        }


    }
}
