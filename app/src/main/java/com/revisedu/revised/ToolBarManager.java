package com.revisedu.revised;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

    public void setHeaderTextGravity(int gravityCenter) {
        TextView v = mToolbar.findViewById(R.id.AppTitle);
        v.setGravity(gravityCenter);
    }

    public void onBackPressed(final Fragment fragment) {
        ImageView backImage = mToolbar.findViewById(R.id.backButtonToolbar);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment.getActivity() != null) {
                    fragment.getActivity().onBackPressed();
                }
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
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(mActivity, R.drawable.ic_nav_toggle_icon));
            mActivity.findViewById(R.id.toolbarLayout).setVisibility(View.GONE);
        } else {
            mToolbar.setNavigationIcon(null);
            mActivity.findViewById(R.id.toolbarLayout).setVisibility(View.VISIBLE);
        }
    }
}
