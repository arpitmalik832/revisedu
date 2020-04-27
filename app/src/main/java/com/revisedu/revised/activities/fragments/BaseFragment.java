package com.revisedu.revised.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.revisedu.revised.activities.HomeActivity;

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
        updateOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
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

    void storeStringDataInSharedPref(String keyName, String value) {
        if (getActivity() != null) {
            SharedPreferences.Editor editor = mActivity.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putString(keyName, value);
            editor.apply();
        }
    }

    public String getStringDataFromSharedPref(String keyName) {
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

    public void onBackPressedToExit() {
        mActivity.finish();
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
}
