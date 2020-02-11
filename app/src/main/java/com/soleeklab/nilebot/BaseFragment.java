package com.soleeklab.nilebot;

import android.app.ProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.soleeklab.nilebot.utils.DialogUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class BaseFragment extends Fragment implements ParentView {
    private ProgressDialog progressDialog;

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
        Toast.makeText(getContext(),getString(R.string.no_connection),Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

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
}
