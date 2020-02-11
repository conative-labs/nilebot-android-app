package com.soleeklab.nilebot.features.auth.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


public class ForgotPasswordFragment extends ParentFragment implements ForgotPasswordContract.View {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.txt_sing_in)
    TextView txtSingIn;
    @BindView(R.id.txtphone_hint)
    TextView txtphoneHint;
    @BindView(R.id.spinner_country_code)
    Spinner spinnerCountryCode;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.lay_rel)
    RelativeLayout layRel;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.txt_error_phone)
    TextView txtErrorPhone;
    @BindView(R.id.lay_number)
    LinearLayout layNumber;
    @BindView(R.id.btn_forgot_password)
    Button btnForgotPassword;
    Unbinder unbinder;

    CountryCodeAdapter countryCodeAdapter;
    CountryCode countryCode;


    @Inject
    ForgotPasswordContract.Presenter mPresenter;

    @Inject
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
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
    public void showInvalidPhone() {
        txtErrorPhone.setVisibility(View.VISIBLE);
        txtErrorPhone.setText(getString(R.string.error_required));
    }

    @Override
    public void handleWatcher() {

    }

    @Override
    public void removeErrors() {
        txtErrorPhone.setVisibility(View.GONE);
    }

    @Override
    public void navigateToConfirmcode() {
        startActivity(new Intent(getActivity(), ConfirmCodeActivity.class)
                .putExtra("screenType",1)
                .putExtra("prefix", countryCode.getCallingCodes().get(0))
                .putExtra("credential",edtPhoneNumber.getText().toString().trim()));
    }

    @Override
    public void setPresenter(ForgotPasswordContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_forgot_password:
                if (isInternetConnection()) {
                    removeErrors();
                    mPresenter.forgotPassword(countryCode.getCallingCodes().get(0), edtPhoneNumber.getText().toString().trim());
                } else {
                    showNoConnectionAlert();
                }
                break;
        }
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
