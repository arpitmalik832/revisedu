package com.revisedu.revised.activities;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.activities.fragments.BaseFragment;
import com.revisedu.revised.activities.fragments.HomeScreenFragment;
import com.revisedu.revised.activities.fragments.LocationFragment;
import com.revisedu.revised.activities.fragments.ProfileFragment;
import com.revisedu.revised.activities.fragments.SignInFragment;
import com.revisedu.revised.activities.fragments.StudyMaterialFragment;
import com.revisedu.revised.activities.fragments.SubjectsFragment;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mBottomNavigationView;
    private DrawerLayout mSideNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbarLayout);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationView navigationView = findViewById(R.id.nav_viewa);
        setSupportActionBar(toolbar);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        ToolBarManager.getInstance().setupToolbar(toolbar);
        findViewById(R.id.backButtonToolbar).setVisibility(View.GONE);
        mSideNavigationDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mSideNavigationDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mSideNavigationDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        launchFragment(new LocationFragment(), false);
    }

    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    public void hideSideNavigationView() {
        mSideNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void showSideNavigationView() {
        mSideNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void showBottomNavigationItem(int count) {
        switch (count) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.favourite);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.nav_courses);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
            case 3:
                mBottomNavigationView.setSelectedItemId(R.id.nav_material);
                break;
            case 4:
                mBottomNavigationView.setSelectedItemId(R.id.nav_profile);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourite:
                showToast("Favourite");
                break;
            case R.id.nav_courses:
                if (!(getCurrentFragment() instanceof SubjectsFragment)) {
                    launchFragment(new SubjectsFragment(), true);
                }
                break;
            case R.id.nav_home:
                if (!(getCurrentFragment() instanceof HomeScreenFragment)) {
                    launchFragment(new HomeScreenFragment(), true);
                }
                break;
            case R.id.nav_material:
                if (!(getCurrentFragment() instanceof StudyMaterialFragment)) {
                    launchFragment(new StudyMaterialFragment(), true);
                }
                break;
            case R.id.nav_profile:
                if (!(getCurrentFragment() instanceof ProfileFragment)) {
                    launchFragment(new ProfileFragment(), true);
                }
                break;
            case R.id.booking:
                break;
            case R.id.privacy:
                break;
            case R.id.term:
                break;
            case R.id.about:
                break;
            case R.id.contact:
                break;
            case R.id.resources:
                break;
            case R.id.share:
                break;
            case R.id.logout:
                showLogoutDialog();
                break;
        }
        mSideNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void showLogoutDialog() {
        AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(this);
        logoutBuilder.setMessage("Are you sure to logout");
        logoutBuilder.setCancelable(true);
        logoutBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                launchFragment(new SignInFragment(), false);
            }
        });
        AlertDialog alertDialog = logoutBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mSideNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mSideNavigationDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (getCurrentFragment() != null) {
            getCurrentFragment().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        if (null != getCurrentFragment()) {
            getCurrentFragment().onClick(view);
        }
    }

    public void showBackButton() {
        findViewById(R.id.backButtonToolbar).setVisibility(View.VISIBLE);
    }

    public void setBackButtonDrawable(Drawable icon) {
        ((ImageView) findViewById(R.id.backButtonToolbar)).setImageDrawable(icon);
    }

    public void hideBackButton() {
        findViewById(R.id.backButtonToolbar).setVisibility(View.GONE);
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager mgr = getSupportFragmentManager();
        List<Fragment> list = mgr.getFragments();
        int count = mgr.getBackStackEntryCount();
        if (0 == count) {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof BaseFragment) {
                        return (BaseFragment) list.get(i);
                    }
                }
            }
            return null;
        }
        FragmentManager.BackStackEntry entry = mgr.getBackStackEntryAt(count - 1);
        return (BaseFragment) mgr.findFragmentByTag(entry.getName());
    }

    public void launchFragment(final Fragment fragment, final boolean addBackStack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doSwitchToScreen(fragment, addBackStack);
            }
        });
    }

    public void launchFragmentWithoutAnimation(final Fragment fragment, final boolean addBackStack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doSwitchToScreenWithoutAnimation(fragment, addBackStack);
            }
        });
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSwitchToScreen(Fragment fragment, boolean addToBackStack) {
        if (null == fragment) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        BaseFragment current = getCurrentFragment();
        if (null != current) {
            manager.popBackStackImmediate();
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        String fragmentTag = fragment.getClass().getCanonicalName();
        try {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("doSwitchToScreen ", e.getMessage(), e);
            try {
                fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
                if (addToBackStack) {
                    fragmentTransaction.addToBackStack(fragmentTag);
                }
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e2) {
                Log.e("doSwitchToScreen", e.getMessage(), e);
            }
        }
    }

    private void doSwitchToScreenWithoutAnimation(Fragment fragment, boolean addToBackStack) {
        if (null == fragment) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        BaseFragment current = getCurrentFragment();
        if (null != current) {
            manager.popBackStackImmediate();
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        String fragmentTag = fragment.getClass().getCanonicalName();
        try {
            fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("doSwitchToScreen ", e.getMessage(), e);
            try {
                fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
                if (addToBackStack) {
                    fragmentTransaction.addToBackStack(fragmentTag);
                }
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e2) {
                Log.e("doSwitchToScreen", e.getMessage(), e);
            }
        }
    }
}
