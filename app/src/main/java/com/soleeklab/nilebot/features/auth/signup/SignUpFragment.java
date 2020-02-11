package com.soleeklab.nilebot.features.auth.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.CountryCode;
import com.soleeklab.nilebot.features.auth.CountryCodeAdapter;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SignUpFragment extends ParentFragment implements SignUpContract.View {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.txt_sing_in)
    TextView txtSingIn;
    @BindView(R.id.txt_sing_in_hello)
    TextView txtSingInHello;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_error_name)
    TextView txtErrorName;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.txt_error_number)
    TextView txtErrorNumber;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.txt_error_email)
    TextView txtErrorEmail;
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
    @BindView(R.id.checkbox_accept_terms)
    CheckBox checkboxAcceptTerms;
    @BindView(R.id.txt_error_terms)
    TextView txtErrorTerms;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    Unbinder unbinder;

    @Inject
    SignUpContract.Presenter mPresenter;
    @BindView(R.id.spinner_countries)
    Spinner spinnerCountries;

    @BindView(R.id.spinner_country_code)
    Spinner spinnerCountryCode;
    @BindView(R.id.edt_code)
    EditText edtCode;
    CountryCodeAdapter countryCodeAdapter;
    CountryCode countryCode;
    private String country = "";
    private boolean isTermAccepted = false;

    @Inject
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        isTermAccepted = checkboxAcceptTerms.isChecked();
        checkboxAcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTermAccepted)
                    isTermAccepted = false;
                else
                    isTermAccepted = true;
            }
        });

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

    @OnClick({R.id.btn_back, R.id.btn_sign_up, R.id.edt_code, R.id.txt_accept_terms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_sign_up:
                if (isInternetConnection()) {
                    mPresenter.signup(edtName.getText().toString().trim()
                            , edtPhoneNumber.getText().toString().trim()
                            , edtEmail.getText().toString().trim()
                            , countryCode.getCallingCodes().get(0)
                            , edtPassword.getText().toString().trim()
                            , edtConfirmPassword.getText().toString().trim(), isTermAccepted);
                } else
                    showNoConnectionAlert();
                break;
            case R.id.edt_code:
                spinnerCountryCode.performClick();
                break;
            case R.id.txt_accept_terms:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dev.soleekhub.com/nilebot_ar/component/register/terms-conditions.html")));
                break;
        }
    }

    @Override
    public void showInvalidName() {
        txtErrorName.setText(getString(R.string.error_name_invalid));
        txtErrorName.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidPhone() {
        txtErrorNumber.setText(getString(R.string.error_phone_invalid));
        txtErrorNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidEmail() {
        txtErrorEmail.setText(getString(R.string.error_email_invalid));
        txtErrorEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidPassword() {
        txtErrorPassword.setText(getString(R.string.error_password_short));
        txtErrorPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidCinfirmPassword() {
        txtErrorConfirmPassword.setText(getString(R.string.error_confirm_password));
        txtErrorConfirmPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAcceptTerms() {
        txtErrorTerms.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeErrors() {
        txtErrorName.setVisibility(View.GONE);
        txtErrorNumber.setVisibility(View.GONE);
        txtErrorEmail.setVisibility(View.GONE);
        txtErrorPassword.setVisibility(View.GONE);
        txtErrorConfirmPassword.setVisibility(View.GONE);
        txtErrorTerms.setVisibility(View.GONE);

    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(getActivity(), ConfirmCodeActivity.class)
                .putExtra("screenType",2)
                .putExtra("prefix", countryCode.getCallingCodes().get(0))
                .putExtra("isRegister", true)
                .putExtra("credential", edtPhoneNumber.getText().toString().trim())
                .putExtra("name", edtName.getText().toString().trim())
                .putExtra("password", edtPassword.getText().toString().trim())
                .putExtra("email", edtEmail.getText().toString().trim())
        );
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {

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
