package com.soleeklab.nilebot.features.home.settings.changePassword;


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
import com.soleeklab.nilebot.utils.DialogUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends ParentFragment implements ChangePasswordContract.View {


    @Inject
    ChangePasswordContract.Presenter mPresenter;
    @BindView(R.id.edt_old_password)
    EditText edtOldPassword;
    @BindView(R.id.etOldPasswordLayout)
    TextInputLayout etOldPasswordLayout;
    @BindView(R.id.txt_error_old_password)
    TextView txtErrorOldPassword;
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
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    Unbinder unbinder;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    @Inject
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        txtToolbarTitle.setText(getString(R.string.change_password));

    }

    @Override
    public void showInvalidOldPassword() {
        txtErrorOldPassword.setText(getString(R.string.error_password_short));
        txtErrorOldPassword.setVisibility(View.VISIBLE);

    }

    @Override
    public void showInvalidNewPassword() {
        txtErrorPassword.setText(getString(R.string.error_password_short));
        txtErrorPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidConfirmPassword() {
        txtErrorConfirmPassword.setText(getString(R.string.error_confirm_password));
        txtErrorConfirmPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeErrors() {

        txtErrorOldPassword.setVisibility(View.GONE);
        txtErrorPassword.setVisibility(View.GONE);
        txtErrorConfirmPassword.setVisibility(View.GONE);

    }

    @Override
    public void successUpdates(String msg) {
        DialogUtil.showInformationDialog(getActivity(), getString(R.string.change_password), getString(R.string.password_chagne_success), getString(R.string.text_ok), new DialogUtil.DialogClick() {
            @Override
            public void onDeleteClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onCancelClick() {

            }
        });

    }

    @Override
    public void setPresenter(ChangePasswordContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_sign_up:
                if (isInternetConnection()) {
                    mPresenter.updatePassword(edtOldPassword.getText().toString()
                            , edtPassword.getText().toString().trim()
                            , edtConfirmPassword.getText().toString());
                } else
                    showNoConnectionAlert();
                break;
        }
    }
}
