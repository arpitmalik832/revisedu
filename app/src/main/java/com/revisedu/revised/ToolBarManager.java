package com.revisedu.revised;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.revisedu.revised.activities.HomeActivity;

public class ToolBarManager {

    private static final ToolBarManager ourInstance = new ToolBarManager();
    private Toolbar mToolbar;

    public static ToolBarManager getInstance() {
        return ourInstance;
    }

    private ToolBarManager() {
    }

    public void setupToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    public void changeToolBarColor(int color) {
        this.mToolbar.setBackgroundColor(color);
    }

    public void setHeaderTitle(String title) {
        TextView v = mToolbar.findViewById(R.id.AppTitle);
        v.setText(title);
    }

    public void setHeaderTitle(SpannableStringBuilder title, TextView.BufferType type) {
        TextView v = mToolbar.findViewById(R.id.AppTitle);
        v.setText(title, type);
    }

    public void setHeaderTextGravity(int gravityCenter) {
        TextView v = mToolbar.findViewById(R.id.AppTitle);
        v.setGravity(gravityCenter);
    }

    public void onBackPressed(final Fragment fragment) {
        ImageView backImage = mToolbar.findViewById(R.id.backButtonToolbar);
        backImage.setOnClickListener(view -> {
            if (fragment.getActivity() != null) {
                fragment.getActivity().onBackPressed();
            }
        });
    }

    public void setHeaderTitleColor(int color) {
        TextView appTitle = mToolbar.findViewById(R.id.AppTitle);
        appTitle.setTextColor(color);
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }

    public void hideToolBar(HomeActivity mActivity, boolean toHide) {
        if (toHide) {
            mActivity.findViewById(R.id.appBarLayout).setVisibility(View.GONE);
        } else {
            mActivity.findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
        }
    }

    public void hideSearchBar(HomeActivity mActivity, boolean toHide) {
        if (toHide) {
            mActivity.findViewById(R.id.searchBar).setVisibility(View.GONE);
        } else {
            mActivity.findViewById(R.id.searchBar).setVisibility(View.VISIBLE);
        }
    }
}
