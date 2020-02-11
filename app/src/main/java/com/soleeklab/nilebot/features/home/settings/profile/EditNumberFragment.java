package com.soleeklab.nilebot.features.home.settings.profile;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.ModifyUserPhoneMutation;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.CountryCode;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.features.auth.CountryCodeAdapter;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeActivity;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNumberFragment extends ParentFragment {

    Handler uiHandler = new Handler(Looper.getMainLooper());
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
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
    @BindView(R.id.txt_error_number)
    TextView txtErrorNumber;
    @BindView(R.id.btn_edit_number)
    Button btnEditNumber;
    Unbinder unbinder;
    @BindView(R.id.txt_phone)
    TextView txtPhone;

    private String phone;
    private String prefix;


    CountryCodeAdapter countryCodeAdapter;
    CountryCode countryCode;

    @Inject
    LocalRepo localRepo;

    @Inject
    public EditNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_number, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtToolbarTitle.setText(getString(R.string.edit_number));
        phone = getArguments().getString("phone");
        prefix = getArguments().getString("prefix");
        txtPhone.setText(prefix + phone);

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

    @OnClick({R.id.btn_back, R.id.btn_edit_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_edit_number:
                editNumber(countryCode.getCallingCodes().get(0), edtPhoneNumber.getText().toString().trim());
                break;
        }
    }

    private void editNumber(String prefix, String number) {

        txtErrorNumber.setVisibility(View.GONE);

        if (number.length() < 8 || number.length() > 15) {
            txtErrorNumber.setVisibility(View.VISIBLE);
            txtErrorNumber.setText(getString(R.string.error_phone_invalid));
        }

        showProgress();

        if (number.substring(0, 1).equals("0"))
            number = number.substring(1);

        ModifyUserPhoneMutation modifyUserPhoneMutation = ModifyUserPhoneMutation.builder()
                .phone(IPhoneNumber.builder().prefix("+"+prefix).number(number).build()).build();

        String finalNumber = number;
        ApolloClientBuilder.getApolloClient(true).mutate(modifyUserPhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyUserPhoneMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyUserPhoneMutation.Data> response) {
                hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyUserPhone().equals(Status.SUCCESS)) {
                        startActivity(new Intent(getActivity(), ConfirmCodeActivity.class)
                                .putExtra("screenType",4)
                                .putExtra("prefix", prefix)
                                .putExtra("credential", finalNumber));
                        getActivity().onBackPressed();
                    } else {
                        showAlert(localRepo.getTranslation(response.data().modifyUserPhone().rawValue()));
                    }
                } else
                    showAlert(R.string.server_error);

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                hideProgress();
                showAlert(R.string.error_occured);
            }
        }, uiHandler));

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
