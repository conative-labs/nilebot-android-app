package com.soleeklab.nilebot.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.NilebotApplication;


public class CustomProgressDialog {

    public static ProgressDialog progressDialog;
    public CustomProgressDialog(Context context){
        progressDialog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.text_loading));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(NilebotApplication.get(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void hideProgress() {
        progressDialog.dismiss();
    }
    public void setProgressDialog(Context context){

    }

}
