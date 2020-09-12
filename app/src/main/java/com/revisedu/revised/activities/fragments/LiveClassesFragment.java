package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.adapters.LiveClassesAdapter;
import com.revisedu.revised.response.LiveClassesResponse;

import java.util.ArrayList;
import java.util.List;

public class LiveClassesFragment extends BaseFragment {

    private static final String TAG = "LiveClassesFragment";

    private LiveClassesAdapter mLiveClassesAdapter;
    private List<LiveClassesResponse.LiveClassesResponseItem> mLiveClassesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_live_classes, container, false);

        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        setHasOptionsMenu(false);

        // Live Classes Recycler View
        RecyclerView liveClassesRecyclerView = mContentView.findViewById(R.id.liveClassesRecyclerView);
        liveClassesRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false));
        mLiveClassesAdapter = new LiveClassesAdapter(mActivity, mLiveClassesList);
        liveClassesRecyclerView.setAdapter(mLiveClassesAdapter);

        getLiveClassesListServerCall();

        return mContentView;
    }

    private void getLiveClassesListServerCall() {
        final List<LiveClassesResponse.LiveClassesResponseItem> liveClassesList = new ArrayList<>();
        liveClassesList.clear();
        liveClassesList.add(new LiveClassesResponse.LiveClassesResponseItem(
                "Thumbnail 1",
                "Interesting Live Class in Mathematics"
        ));
        liveClassesList.add(new LiveClassesResponse.LiveClassesResponseItem(
                "Thumbnail 1",
                "Interesting Live Class in Mathematics"
        ));
        liveClassesList.add(new LiveClassesResponse.LiveClassesResponseItem(
                "Thumbnail 1",
                "Interesting Live Class in Mathematics"
        ));
        liveClassesList.add(new LiveClassesResponse.LiveClassesResponseItem(
                "Thumbnail 1",
                "Interesting Live Class in Mathematics"
        ));
        mLiveClassesList.addAll(liveClassesList);
        mLiveClassesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() { launchFragment(new HomeScreenFragment(), false); }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(this::stopProgress, 1000);
        mActivity.hideSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(0);
    }


}
