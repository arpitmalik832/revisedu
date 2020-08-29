package com.revisedu.revised.activities.fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.revisedu.revised.R;
import com.revisedu.revised.TerminalConstant;
import com.revisedu.revised.ToolBarManager;
import com.revisedu.revised.request.CommonRequest;
import com.revisedu.revised.response.CommonResponse;
import com.revisedu.revised.response.PrefSubjectsResponse;
import com.revisedu.revised.response.ProfileResponse;
import com.revisedu.revised.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.revisedu.revised.TerminalConstant.USER_ID;

public class ProfileFragment extends BaseFragment {

    private static final String TAG = "Profile";
    private EditText profilePhoneNumber;
    private EditText profileNameEditText;
    private TextView profileEmailAddress;
    private TextView profilePreferredSubject;
    private ImageView profileImageView;
    private PickResult mPickResult = new PickResult();
    private List<PrefSubjectsResponse.ListItem> mPrefSubjectList = new ArrayList<>();
    private Drawable mDefaultDrawable;
    private String mProfilePhotoUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_profile, container, false);
        mDefaultDrawable = ContextCompat.getDrawable(mActivity, R.drawable.default_image);
        setupUI();
        getPrefSubjectsServerCall();
        getProfileResponse();
        return mContentView;
    }

    private void setupUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
        ToolBarManager.getInstance().hideSearchBar(mActivity,true);
        ToolBarManager.getInstance().changeToolBarColor(ContextCompat.getColor(mActivity, R.color.dark_background));
        ToolBarManager.getInstance().setHeaderTitle(TAG);
        ToolBarManager.getInstance().setHeaderTitleColor(ContextCompat.getColor(mActivity, R.color.white));
        ToolBarManager.getInstance().setHeaderTextGravity(Gravity.START);
        profileImageView = mContentView.findViewById(R.id.profileImageView);
        profilePhoneNumber = mContentView.findViewById(R.id.profilePhoneNumber);
        profileNameEditText = mContentView.findViewById(R.id.profileNameEditText);
        profileEmailAddress = mContentView.findViewById(R.id.profileEmailAddress);
        profilePreferredSubject = mContentView.findViewById(R.id.profilePreferredSubject);
    }

    private void getProfileResponse() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ProfileResponse> call = RetrofitApi.getServicesObject().getProfileResponse(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                    final Response<ProfileResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (final Exception e) {
                    updateOnUiThread(() -> {
                        showToast(e.toString());
                        stopProgress();
                    });
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    final ProfileResponse profileResponse = response.body();
                    if (profileResponse != null) {
                        showToast(profileResponse.getErrorMessage());
                        if (profileResponse.getErrorCode() == TerminalConstant.SUCCESS) {
                            profileNameEditText.setText(profileResponse.getName());
                            profileEmailAddress.setText(profileResponse.getEmail());
                            profilePhoneNumber.setText(profileResponse.getMobile());
                            profilePreferredSubject.setText(profileResponse.getPrefSubjects());
                            mProfilePhotoUrl = profileResponse.getUserImageUrl();
                            if (mProfilePhotoUrl != null && !mProfilePhotoUrl.isEmpty()) {
                                Picasso.get().load(mProfilePhotoUrl).placeholder(mDefaultDrawable).into(profileImageView);
                            }
                        }
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
                    final PrefSubjectsResponse prefSubjectsResponse = response.body();
                    if (prefSubjectsResponse != null) {
                        if (!mPrefSubjectList.isEmpty()) {
                            mPrefSubjectList.clear();
                        }
                        if (prefSubjectsResponse.getArrayList() != null && !prefSubjectsResponse.getArrayList().isEmpty()) {
                            mPrefSubjectList.addAll(prefSubjectsResponse.getArrayList());
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateProfileButton:
                saveUserProfileServerCall();
                break;
            case R.id.editTextView:
                pickImage();
                break;
            case R.id.profilePreferredSubject:
                String[] subjectList = new String[mPrefSubjectList.size()];
                for (int position = 0; position < mPrefSubjectList.size(); position++) {
                    subjectList[position] = mPrefSubjectList.get(position).getSubject();
                }
                showProductsAlertDialog(subjectList);
                break;
            case R.id.profileImageView:
                showSelectedImage();
                break;
            default:
                break;
        }
    }

    private void saveUserProfileServerCall() {
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
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody userIdRequestBody = RequestBody.create(getStringDataFromSharedPref(USER_ID), MultipartBody.FORM);
                    RequestBody nameBody = RequestBody.create(name, MultipartBody.FORM);
                    RequestBody emailRequestBody = RequestBody.create(email, MultipartBody.FORM);
                    RequestBody subjectsRequestBody = RequestBody.create(subject, MultipartBody.FORM);
                    RequestBody phoneRequestBody = RequestBody.create(phone, MultipartBody.FORM);
                    File originalFile = new File(mPickResult.getPath());
                    File compressedFile = new Compressor(mActivity).compressToFile(originalFile);
                    RequestBody fileRequestBody = RequestBody.create(compressedFile, MediaType.parse(Objects.requireNonNull(mActivity.getContentResolver().getType(mPickResult.getUri()))));
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", compressedFile.getName(), fileRequestBody);
                    Call<CommonResponse> call = RetrofitApi.getServicesObject().uploadUserProfileServerCall(userIdRequestBody,
                                                                                                            nameBody,
                                                                                                            phoneRequestBody,
                                                                                                            subjectsRequestBody,
                                                                                                            emailRequestBody,
                                                                                                            filePart);
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
                    final CommonResponse profileResponse = response.body();
                    if (profileResponse != null) {
                        showToast(profileResponse.getErrorMessage());
                    }
                }
                stopProgress();
            }
        }).start();
    }

    private List<String> selectedProducts = new ArrayList<>();

    private void showProductsAlertDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Subject(s)");
        builder.setCancelable(false);
        boolean[] checkBooleanArray = new boolean[items.length];
        for (int position = 0; position < items.length; position++) {
            for (int selectedItemPosition = 0; selectedItemPosition < selectedProducts.size(); selectedItemPosition++) {
                if (selectedProducts.get(selectedItemPosition).equalsIgnoreCase(items[position])) {
                    checkBooleanArray[position] = true;
                }
            }
        }
        builder.setMultiChoiceItems(items, checkBooleanArray, (dialogInterface, position, b) -> {
            if (b) {
                selectedProducts.add(items[position]);
            } else {
                String itemStr = items[position];
                for (int itemPosition = 0; itemPosition < selectedProducts.size(); itemPosition++) {
                    if (itemStr.equalsIgnoreCase(selectedProducts.get(itemPosition))) {
                        selectedProducts.remove(itemPosition);
                        break;
                    }
                }
            }
        });
        builder.setPositiveButton(
            "Submit",
            (dialog, id) -> {
                dialog.cancel();
                String selectItemsStr = "";
                if (!selectedProducts.isEmpty()) {
                    for (String item : selectedProducts) {
                        selectItemsStr = selectItemsStr.concat(" ").concat(item);
                    }
                }
                profilePreferredSubject.setText(selectItemsStr);
            });
        builder.setNegativeButton(
            "Cancel",
            (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        ImageView imageView = dialog.findViewById(R.id.imgUserIcon);
        if (mProfilePhotoUrl.isEmpty()) {
            imageView.setImageBitmap(mPickResult.getBitmap());
        } else {
            Picasso.get().load(mProfilePhotoUrl).placeholder(mDefaultDrawable).into(imageView);
        }
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        launchFragment(new SignInFragment(), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.hideSideNavigationView();
        mActivity.showBottomNavigationView();
        mActivity.showBottomNavigationItem(4);
    }
}
