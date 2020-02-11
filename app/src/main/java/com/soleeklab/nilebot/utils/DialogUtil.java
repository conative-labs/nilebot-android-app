package com.soleeklab.nilebot.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.NilebotApplication;


public final class DialogUtil {

    ProgressBar progressBar;

    private DialogUtil() {
    }

    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog showProgressDialog(Context context, int message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        dialog.setMessage(context.getString(message));
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();
        return dialog;
    }


    public static void showAlertDialog(Context context, int message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(context);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.dialog_error, null);
                        noNetworkDialog.setView(view);


                        final AlertDialog dialog = noNetworkDialog.create();
                        dialog.show();
                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_error);

                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                        TextView text = dialog.findViewById(R.id.txt_error);
                        text.setText(context.getString(message));

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }
        }).start();

    }

    public static void showAlertDialog(Context context, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(context);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.dialog_error, null);
                        noNetworkDialog.setView(view);


                        final AlertDialog dialog = noNetworkDialog.create();
                        dialog.show();
                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_error);

                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                        TextView text = dialog.findViewById(R.id.txt_error);
                        text.setText(message);

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });
            }
        }).start();
//
    }

    public static void showAlertDialog(Context context, String message, DialogClick dialogClick) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(context);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.dialog_error, null);
                        noNetworkDialog.setView(view);


                        final AlertDialog dialog = noNetworkDialog.create();
                        dialog.show();
                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_error);

                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                        TextView text = dialog.findViewById(R.id.txt_error);
                        text.setText(message);

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogClick.onDeleteClick();
                            }
                        });

                        dialog.show();

                    }
                });
            }
        }).start();
//
    }

    public static void showDialog(Context context, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(context);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.dialog_error, null);
                        noNetworkDialog.setView(view);


                        final AlertDialog dialog = noNetworkDialog.create();
                        dialog.show();
                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_error);

                        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                        TextView text = dialog.findViewById(R.id.txt_error);
                        TextView tv_info_message = dialog.findViewById(R.id.tv_info_message);
                        text.setText(message);
                        tv_info_message.setVisibility(View.GONE);

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });
            }
        }).start();
//
    }

    public static void showNoConnectionAlertDialog(Context context) {


        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.no_internet_dialog, null);
        noNetworkDialog.setView(view);

        final AlertDialog dialog = noNetworkDialog.create();
        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        Button btnDone = view.findViewById(R.id.btn_done);
        TextView textViewMessage = view.findViewById(R.id.tv_info_message);
        TextView textViewExtra = view.findViewById(R.id.tv_extra_info_message);

        textViewExtra.setText(context.getString(R.string.server_error));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        android.app.AlertDialog ad = new android.app.AlertDialog.Builder(context)
//                .create();
//        ad.setCancelable(false);
//        ad.setTitle(R.string.error_connection);
//        ad.setButton(context.getString(R.string.text_ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        ad.show();
    }


    public static Dialog showSureToDialog(Context context, String question, DialogClick dialogClick) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sure_to);

        TextView text = dialog.findViewById(R.id.txt_question);
        text.setText(question);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onDeleteClick();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showSureToDialog(Context context, String header, String question, String btnActionName, String cancelName, DialogClick dialogClick) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sure_to);

        TextView text = dialog.findViewById(R.id.txt_question);
        TextView txt_header_title = dialog.findViewById(R.id.txt_header_title);
        text.setText(question);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        txt_header_title.setText(header);
        btnDelete.setText(btnActionName);
        btnCancel.setText(cancelName);
        btnDelete.setBackgroundResource(R.drawable.round_corner_button_blue);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onCancelClick();
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onDeleteClick();
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showSureToExitDialog(Context context, String question, DialogClick dialogClick) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sure_to_exit);

        TextView text = dialog.findViewById(R.id.txt_question);
        text.setText(question);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onDeleteClick();
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showInformationDialog(Context context, String header, String question, String btnName, DialogClick dialogClick) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sure_to_exit);

        TextView text = dialog.findViewById(R.id.txt_question);
        TextView txt_header_title = dialog.findViewById(R.id.txt_header_title);
        text.setText(question);
        txt_header_title.setText(header);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);
        btnDelete.setText(btnName);
        btnCancel.setVisibility(View.GONE);
        btnDelete.setBackgroundResource(R.drawable.round_corner_button_blue);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onDeleteClick();
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public interface DialogClick {
        void onDeleteClick();

        void onCancelClick();
    }

}
