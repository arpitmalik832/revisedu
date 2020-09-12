package com.revisedu.revised.activities.fragments;

import android.os.Bundle;
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
import com.revisedu.revised.activities.fragments.adapters.NotificationsAdapter;
import com.revisedu.revised.activities.interfaces.ICustomClickListener;
import com.revisedu.revised.response.NotificationsResponse;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends BaseFragment implements ICustomClickListener {

    private NotificationsAdapter mNotificationsAdapter;
    private List<NotificationsResponse.ListItem> mNotificationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_notifications,container,false);

        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle("Notifications");
        ToolBarManager.getInstance().onBackPressed(this);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);

        mActivity.showBackButton();
        mActivity.isToggleButtonEnabled(false);

        //setup notifications recycler view
        RecyclerView notificationsRecyclerView = mContentView.findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mNotificationsAdapter = new NotificationsAdapter(mNotificationsList,getContext(),this);
        notificationsRecyclerView.setAdapter(mNotificationsAdapter);

        getNotificationsServerCall();

        return mContentView;
    }

    private void getNotificationsServerCall() {
        final List<NotificationsResponse.ListItem> notificationsList = new ArrayList<>();
        notificationsList.clear();
        notificationsList.add(new NotificationsResponse.ListItem(
                "1",
                "2020-08-25012:20:25",
                "Notification 1"
        ));
        notificationsList.add(new NotificationsResponse.ListItem(
                "2",
                "2020-08-25012:20:25",
                "Notification 1"
        ));
        notificationsList.add(new NotificationsResponse.ListItem(
                "3",
                "2020-08-25012:20:25",
                "Notification 1"
        ));
        notificationsList.add(new NotificationsResponse.ListItem(
                "4",
                "2020-08-25012:20:25",
                "Notification 1"
        ));
        mNotificationsList.addAll(notificationsList);
        mNotificationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        launchFragment(new HomeScreenFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.hideBottomNavigationView();
    }

    @Override
    public void onAdapterItemClick(String itemId, String itemValue, String coachingType) {
        launchFragment(new NotificationDetailFragment(),true);
    }
}
