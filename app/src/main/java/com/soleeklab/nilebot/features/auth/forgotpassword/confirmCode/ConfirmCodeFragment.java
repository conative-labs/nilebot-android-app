package com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword.ResetPasswordActivity;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.PinEntryView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ConfirmCodeFragment extends ParentFragment implements ConfirmCodeContract.View {

    CountDownTimer timer = null;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.txt_sing_in)
    TextView txtSingIn;
    @BindView(R.id.btn_verify)
    Button btnVerify;
    Unbinder unbinder;

    @BindView(R.id.ib_reload)
    ImageView ibReload;
    @BindView(R.id.tv_resend)
    TextView tvResend;

    @BindView(R.id.edt_code)
    PinEntryView pinView;

    private String verificationCode = null;

    private String credential = "", prefix = "", password = "", name = "", email = "";


    private int comeFromScreenType = 1;

    public static int resetPasswordScreen = 1;
    public static int signUpScreen = 2;
    public static int loginScreen = 3;
    public static int editPhoneScreen = 4;
    public static int addPhoneScreen = 5;

    private String language;

    @Inject
    ConfirmCodeContract.Presenter mPresenter;

    @Inject
    public ConfirmCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_code, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(ConfirmCodeContract.Presenter presenter) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);

        comeFromScreenType = getActivity().getIntent().getIntExtra("screenType", 1);


        credential = getActivity().getIntent().getStringExtra("credential");
        prefix = getActivity().getIntent().getStringExtra("prefix");

        if (comeFromScreenType == signUpScreen) {
            password = getActivity().getIntent().getStringExtra("password");
            name = getActivity().getIntent().getStringExtra("name");
            email = getActivity().getIntent().getStringExtra("email");
        }

        if(comeFromScreenType == addPhoneScreen){
            language = getActivity().getIntent().getStringExtra("language");
        }


        startTimer();
        handleTextWatcher();

    }

    @Override
    public void startTimer() {
        disableViews();

        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timer != null)
                    tvResend.setText(getString(R.string.still) + " " + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                if (timer != null && millisUntilFinished < 1000) {
                    enableViews();
                }
            }

            @Override
            public void onFinish() {
                enableViews();
            }
        }.start();
    }

    @Override
    public void handleTextWatcher() {
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();

                if (length == 4) {
                    verificationCode = s.toString();
                    btnVerify.setEnabled(true);
                    btnVerify.setBackgroundResource(R.drawable.round_corner_button_blue);
                    btnVerify.setTextColor(getResources().getColor(R.color.white));
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                } else {
                    btnVerify.setEnabled(false);
                    btnVerify.setBackgroundResource(R.drawable.round_view_white_stroke_gray);
                    btnVerify.setTextColor(getResources().getColor(R.color.colorGray));
                }
            }
        });
    }

    @Override
    public void disableViews() {
        tvResend.setEnabled(false);
        ibReload.setVisibility(View.GONE);
        tvResend.setTextColor(getResources().getColor(R.color.colorGray));

    }

    @Override
    public void enableViews() {

        if (getView() != null) {
            ibReload.setVisibility(View.VISIBLE);
            ibReload.setEnabled(true);
            ibReload.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
            tvResend.setEnabled(true);
            tvResend.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvResend.setText(getResources().getString(R.string.resend));
        }


    }

    @Override
    public void navigateToLogin() {

    }

    @Override
    public void showInvalidCredtials(String error) {

    }

    @Override
    public void resendApiSuccess() {
        startTimer();
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvResend.setText("" + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                if (timer != null && millisUntilFinished < 1000) {
                    enableViews();
                }
            }

            @Override
            public void onFinish() {
                enableViews();
            }
        }.start();
    }

    @Override
    public void verificationSuccess(String message) {


        if (comeFromScreenType == resetPasswordScreen) {
            startActivity(new Intent(getActivity(), ResetPasswordActivity.class)
                    .putExtra("code", verificationCode)
                    .putExtra("prefix", prefix)
                    .putExtra("credential", credential));
            return;
        }

        if (comeFromScreenType == editPhoneScreen) {
            EventBus.getDefault().post(new MessageEvent("refresh_user"));
            getActivity().finish();
            return;
        }
        if (comeFromScreenType == addPhoneScreen) {
            EventBus.getDefault().post(new MessageEvent("refresh_add_phone"));
            getActivity().finish();
            return;
        }


        DialogUtil.showAlertDialog(getActivity(), message, new DialogUtil.DialogClick() {
            @Override
            public void onDeleteClick() {
                if (comeFromScreenType == signUpScreen || comeFromScreenType == loginScreen) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finishAffinity();
                }
            }

            @Override
            public void onCancelClick() {

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        mPresenter.unregisterView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_verify, R.id.tv_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_verify:
                mPresenter.verifyCode(comeFromScreenType, prefix, credential, verificationCode);
                break;
            case R.id.tv_resend:
                if (isInternetConnection()) {
                    mPresenter.resendCode(comeFromScreenType, prefix, credential, name, password, email,language);
                } else {
                    showNoConnectionAlert();
                }
                break;
        }
    }
}
