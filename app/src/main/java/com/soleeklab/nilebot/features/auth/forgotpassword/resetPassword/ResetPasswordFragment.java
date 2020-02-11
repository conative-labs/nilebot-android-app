package com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.utils.DialogUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends ParentFragment implements ResetPasswordContract.View {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.txt_sing_in)
    TextView txtSingIn;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;
    @BindView(R.id.txt_error_password)
    TextView txtErrorPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @BindView(R.id.etConfirmPasswordLayout)
    TextInputLayout etConfirmPasswordLayout;
    @BindView(R.id.txt_error_confirm_password)
    TextView txtErrorConfirmPassword;
    @BindView(R.id.btn_reset_password)
    Button btnResetPassword;
    Unbinder unbinder;

    private String credential, prefix,verificationCode;

    @Inject
    ResetPasswordContract.Presenter mPresenter;

    @Inject
    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(ResetPasswordContract.Presenter presenter) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);

        credential = getActivity().getIntent().getStringExtra("credential");
        prefix = getActivity().getIntent().getStringExtra("prefix");
        verificationCode = getActivity().getIntent().getStringExtra("code");

    }

    @Override
    public void navigateToLogin(String message) {

        DialogUtil.showAlertDialog(getActivity(), message, new DialogUtil.DialogClick() {
            @Override
            public void onDeleteClick() {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }

            @Override
            public void onCancelClick() {

            }
        });


    }

    @Override
    public void removeErrors() {
        txtErrorPassword.setVisibility(View.GONE);
        txtErrorConfirmPassword.setVisibility(View.GONE);
    }

    @Override
    public void showInvalidPassword() {
        txtErrorPassword.setText(getString(R.string.error_password_short));
        txtErrorPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidConfirmPassword() {
        txtErrorConfirmPassword.setText(getString(R.string.error_confirm_password));
        txtErrorConfirmPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidCredtials(String error) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_reset_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_reset_password:
                mPresenter.resetPassword(prefix,credential,verificationCode,edtPassword.getText().toString(),edtConfirmPassword.getText().toString());
                break;
        }
    }
}
