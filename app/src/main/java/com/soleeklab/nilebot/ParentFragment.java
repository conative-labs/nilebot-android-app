package com.soleeklab.nilebot;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.ProgressDialogFragment;

import dagger.android.support.DaggerFragment;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class  ParentFragment extends DaggerFragment implements ParentView {

    private ProgressDialog progressDialog;
    private int menuId;
    BottomSheetDialog bottomSheetRate;

    ProgressDialogFragment editNameDialogFragment;

    @Override
    public void showProgress() {
        try {
            FragmentManager fm = getChildFragmentManager();
            editNameDialogFragment = new ProgressDialogFragment();
            editNameDialogFragment.show(fm, "fragment_edit_name");
        }catch (Exception e){
            e.printStackTrace();
        }

//        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        try {
            editNameDialogFragment.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

//        hideProgressDialog();
    }

    @Override
    public void showError(String error) {
        showSnackbar(getView(), error);
    }

    @Override
    public void showAlert(String message) {
        DialogUtil.showAlertDialog(getActivity(), message);
    }

    @Override
    public void showAlert(int message) {
        DialogUtil.showAlertDialog(getActivity(), message);
    }

    @Override
    public void showNoConnectionAlert() {
        DialogUtil.showNoConnectionAlertDialog(getActivity());
    }

    @Override
    public void showError() {
        showSnackbar(getView(), R.string.error_occured);
    }

    @Override
    public void showNoConnection() {
        Toast.makeText(getContext(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }

    protected void showProgressDialog(int message) {
        progressDialog = DialogUtil.showProgressDialog(getActivity(), message, false);
    }

    protected void showProgressDialog() {
        progressDialog = DialogUtil.showProgressDialog(getActivity(), R.string.text_loading, false);
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void setFullScreen() {
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void hideInputType() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }

    protected void ShowInputType() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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


    protected void createOptionsMenu(int menuId) {
        this.menuId = menuId;
        getActivity().invalidateOptionsMenu();
    }

    protected void hero(String msg) {
        Log.e("HERO", msg);
    }

    /**
     * function is used to create a menu
     */
    protected void removeOptionsMenu() {
        menuId = 0;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menuId != 0) {
            getActivity().getMenuInflater().inflate(menuId, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected boolean isInternetConnection() {
        boolean isInternet;
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            isInternet = false;
        } else {
            isInternet = true;
        }
        return isInternet;
    }


}
