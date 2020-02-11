package com.soleeklab.nilebot.features.auth.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.CountryCode;
import com.soleeklab.nilebot.features.auth.CountryCodeAdapter;
import com.soleeklab.nilebot.features.auth.forgotpassword.ForgotPasswordActivity;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeActivity;
import com.soleeklab.nilebot.features.auth.signup.SignUpActivity;
import com.soleeklab.nilebot.features.home.HomeActivity;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends ParentFragment implements LoginContract.View {


    @Inject
    LoginContract.Presenter mPresenter;

    Unbinder unbinder;

    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_sign_up)
    TextView btnSignUp;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;

    @BindView(R.id.txt_error_phone)
    TextView txtErrorPhone;
    @BindView(R.id.txt_error_password)
    TextView txtErrorPassword;

    CountryCodeAdapter countryCodeAdapter;
    CountryCode countryCode;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.spinner_country_code)
    Spinner spinnerCountryCode;


    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        setCountryCodes();
    }


    private void setCountryCodes() {

        edtCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    spinnerCountryCode.performClick();
                }
            }
        });
        countryCodeAdapter = new CountryCodeAdapter(getActivity(), R.layout.item_spinner_img_row, getCountriesList());

        spinnerCountryCode.setAdapter(countryCodeAdapter);
        spinnerCountryCode.setSelection(65);
        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode = getCountriesList().get(position);

                edtCode.setText(localeToEmoji(countryCode.getAlpha2Code()) + " +" + countryCode.getCallingCodes().get(0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_login, R.id.btn_sign_up, R.id.txt_forgot_password, R.id.edt_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                if (isInternetConnection()) {
                    removeErrors();
                    mPresenter.login(countryCode.getCallingCodes().get(0), edtPhoneNumber.getText().toString().trim(), edtPassword.getText().toString().trim());
                } else {
                    showNoConnectionAlert();
                }
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.txt_forgot_password:
                navigateToForgotPassword();
                break;
            case R.id.edt_code:
                spinnerCountryCode.performClick();
                break;
        }
    }

    @Override
    public void showInvalidPhone() {
        txtErrorPhone.setVisibility(View.VISIBLE);
        txtErrorPhone.setText(getString(R.string.error_required));
    }

    @Override
    public void showInvalidPassword() {
        txtErrorPassword.setVisibility(View.VISIBLE);
        txtErrorPassword.setText(getString(R.string.error_password_short));
    }


    @Override
    public void handleWatcher() {

    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @Override
    public void navigateToVerifyPhone() {
        startActivity(new Intent(getActivity(), ConfirmCodeActivity.class)
                .putExtra("screenType",3)
                .putExtra("prefix", countryCode.getCallingCodes().get(0))
                .putExtra("isRegister", true)
                .putExtra("credential",edtPhoneNumber.getText().toString().trim()));
    }

    @Override
    public void navigateToForgotPassword() {
        startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));

    }

    @Override
    public void showInvalidCredentials(String error) {

    }

    @Override
    public void removeErrors() {
        txtErrorPhone.setVisibility(View.GONE);
        txtErrorPassword.setVisibility(View.GONE);


    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    private ArrayList<CountryCode> getCountriesList() {
        Gson gson = new Gson();
        ArrayList<CountryCode> countryCodeArrayList = new ArrayList<>();

        countryCodeArrayList = gson.fromJson(Constants.jsonCountries, new TypeToken<List<CountryCode>>() {
        }.getType());
        return countryCodeArrayList;
    }

    private String localeToEmoji(String alpha) {
        int firstLetter = Character.codePointAt(alpha, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(alpha, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));


    }

}
