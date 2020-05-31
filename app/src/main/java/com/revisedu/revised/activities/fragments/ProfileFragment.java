package com.revisedu.revised.activities.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.ToolBarManager;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

public class ProfileFragment extends BaseFragment {

    private static final String TAG = "Profile";
    private EditText profilePhoneNumber;
    private EditText profileNameEditText;
    private EditText profileEmailAddress;
    private EditText profilePreferredSubject;
    private TextView editTextView;
    private ImageView profileImageView;
    private PickResult mPickResult = new PickResult();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_profile, container, false);
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        editTextView = mContentView.findViewById(R.id.editTextView);
        profileImageView = mContentView.findViewById(R.id.profileImageView);
        profilePhoneNumber = mContentView.findViewById(R.id.profilePhoneNumber);
        profileNameEditText = mContentView.findViewById(R.id.profileNameEditText);
        profileEmailAddress = mContentView.findViewById(R.id.profileEmailAddress);
        profilePreferredSubject = mContentView.findViewById(R.id.profilePreferredSubject);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateProfileButton:
                String name = profileNameEditText.getText().toString();
                String email = profileEmailAddress.getText().toString();
                String subject = profilePreferredSubject.getText().toString();
                String phone = profilePhoneNumber.getText().toString();
                if (name.isEmpty()) {
                    profileNameEditText.setError(getString(R.string.msgMandatory));
                    profileNameEditText.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    profileEmailAddress.setError(getString(R.string.msgMandatory));
                    profileEmailAddress.requestFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    profilePhoneNumber.setError(getString(R.string.msgMandatory));
                    profilePhoneNumber.requestFocus();
                    return;
                }
                break;
            case R.id.editTextView:
                pickImage();
                break;
            case R.id.profileImageView:
                showSelectedImage();
                break;
            default:
                break;
        }
    }

    private void pickImage() {
        PickImageDialog.build(new PickSetup())
                       .setOnPickResult(r -> {
                           mPickResult = r;
                           profileImageView.setImageBitmap(r.getBitmap());
                       })
                       .setOnPickCancel(() -> {
                           // do nothing
                       }).show(mActivity);
    }

    private void showSelectedImage() {
        if (null == mPickResult) {
            return;
        }
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.show_image);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        imageView.setImageBitmap(mPickResult.getBitmap());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        launchFragment(new SignInFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgress();
            }
        }, 1000);
        mActivity.hideSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(4);
    }
}
