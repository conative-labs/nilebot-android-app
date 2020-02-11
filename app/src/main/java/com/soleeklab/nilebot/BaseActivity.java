package com.soleeklab.nilebot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.LocalHelper;
import com.soleeklab.nilebot.utils.ProgressDialogFragment;

import dagger.android.support.DaggerAppCompatActivity;


public abstract class BaseActivity extends DaggerAppCompatActivity implements ParentView {
    private ProgressDialog progressDialog;
    DialogInterface.OnClickListener ClickListener;
    ProgressDialogFragment editNameDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        Set Application local
        LocalHelper localeHelper = LocalHelper.getInstance(this);
        localeHelper.setLanguage(localeHelper.getLanguage());
        if (localeHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC))
            LocalHelper.ChangeDesignToRTL(this);
        else
            LocalHelper.ChangeDesignToLTR(this);
        super.onCreate(savedInstanceState);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void showProgressDialog(int message) {
        progressDialog = DialogUtil.showProgressDialog(this, message, false);
    }

    protected void showProgressDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        editNameDialogFragment = new ProgressDialogFragment();
//        editNameDialogFragment.show(fm, "fragment_edit_name");
        progressDialog = DialogUtil.showProgressDialog(this, R.string.text_loading, false);
    }


    protected void hideProgressDialog() {
//        editNameDialogFragment.dismiss();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void hideInputType() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void showSnackbar(View view, String text) {
        Snackbar snack = Snackbar.make(
                view,
                text,
                Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        snack.show();
    }

    public void showSnackbar(View view, int text) {
        Snackbar snack = Snackbar.make(
                view,
                text,
                Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        snack.show();
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showError(String error) {
        showSnackbar(getView(), error);
    }

    @Override
    public void showError() {
        showSnackbar(getView(), R.string.error_occured);
    }

    @Override
    public void showNoConnection() {
        showSnackbar(getView(), R.string.error_connection);
    }

    @Override
    public void showAlert(String message) {
        DialogUtil.showAlertDialog(this, message);
    }

    @Override
    public void showAlert(int message) {
        DialogUtil.showAlertDialog(this, message);

    }

    @Override
    public void showNoConnectionAlert() {
        DialogUtil.showNoConnectionAlertDialog(this);

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected abstract View getView();


    protected boolean isInternetConnection() {
        boolean isInternet;
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            isInternet = false;
        } else {
            isInternet = true;
        }
        return isInternet;
    }
}
