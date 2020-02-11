package com.soleeklab.nilebot.features.home.settings.phones;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.GetPhonesQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.CountryCode;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.features.auth.CountryCodeAdapter;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeActivity;
import com.soleeklab.nilebot.type.LanguageType;
import com.soleeklab.nilebot.utils.LocalHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PhonesFragment extends ParentFragment implements PhonesContract.View, SwipeRefreshLayout.OnRefreshListener {


    @Inject
    PhonesContract.Presenter mPresenter;

    PhonesAdapter phonesAdapter;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_add_phone)
    Button btnAddPhone;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.rv_phones)
    RecyclerView rvPhones;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;
    private Dialog dialogAddPhone, dialogEditPhone;
    Unbinder unbinder;
    private LanguageType lang;
    LocalHelper localHelper;

    Dialog deleteDialog;
    CountryCodeAdapter countryCodeAdapter;
    CountryCode countryCode;

    @Inject
    public PhonesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phones, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localHelper = LocalHelper.getInstance(getActivity());
        txtToolbarTitle.setText(getString(R.string.phone_numbers));
        mPresenter.registerView(this);
        swipToRefreshLayout.setOnRefreshListener(this);
        if (isInternetConnection())
            mPresenter.getPhones(true);
        else
            showNoConnectionAlert();

    }

    @Override
    public void initPhones(List<GetPhonesQuery.Phone> phoneArrayList) {

        if (phoneArrayList.size() > 0) {
            rvPhones.setVisibility(View.VISIBLE);
            layNoData.setVisibility(View.GONE);
            phonesAdapter = new PhonesAdapter(getActivity(), phoneArrayList, new PhonesAdapter.PhonesClicks() {
                @Override
                public void onEditPhoneClick(GetPhonesQuery.Phone phone) {
                    editPhone(phone);
                }

                @Override
                public void onDeletePhone(GetPhonesQuery.Phone phone) {
                    deleteDialog = new Dialog(getActivity());
                    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deleteDialog.setCancelable(false);
                    deleteDialog.setContentView(R.layout.dialog_sure_to);

                    TextView text = deleteDialog.findViewById(R.id.txt_question);
                    text.setText(getString(R.string.sure_to_delete_phone));
                    TextView txtHeaderTitle = deleteDialog.findViewById(R.id.txt_header_title);
                    txtHeaderTitle.setText(getString(R.string.delete));

                    Button btnCancel = deleteDialog.findViewById(R.id.btn_cancel);
                    Button btnDelete = deleteDialog.findViewById(R.id.btn_delete);

                    btnDelete.setText(getString(R.string.delete));

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.removePhone(phone.number().prefix(), phone.number().number());
                        }
                    });
                    deleteDialog.show();
                }
            });
            rvPhones.setAdapter(phonesAdapter);
        } else {
            rvPhones.setVisibility(View.GONE);
            layNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void successEditPhone() {
        dialogEditPhone.dismiss();
        mPresenter.getPhones(false);
    }

    @Override
    public void successDeletePhone() {
        deleteDialog.dismiss();
        mPresenter.getPhones(false);
    }

    @Override
    public void successNewPhone(String prefix, String number,String language) {
        dialogAddPhone.dismiss();
        startActivity(new Intent(getActivity(), ConfirmCodeActivity.class)
                .putExtra("screenType", 5)
                .putExtra("prefix", prefix)
                .putExtra("language",language)
                .putExtra("credential", number));
    }

    @Override
    public void setPresenter(PhonesContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unregisterView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_back, R.id.btn_add_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_add_phone:
                addPhone();
                break;
        }
    }

    private void addPhone() {
        dialogAddPhone = new Dialog(getActivity());
        dialogAddPhone.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddPhone.setCancelable(true);
        dialogAddPhone.setContentView(R.layout.dialog_phone_add);
        dialogAddPhone.show();

        EditText edtPhone = dialogAddPhone.findViewById(R.id.edt_phone_number);
        Spinner spinnerCountryCode = dialogAddPhone.findViewById(R.id.spinner_country_code);
        EditText edtCode = dialogAddPhone.findViewById(R.id.edt_code);

        TextView txtErrorPhone = dialogAddPhone.findViewById(R.id.txt_error_phone);

        Button btnCancel = dialogAddPhone.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogAddPhone.findViewById(R.id.btn_add);

        RadioButton checkArabic = dialogAddPhone.findViewById(R.id.check_arabic);
        RadioButton checkEnglish = dialogAddPhone.findViewById(R.id.check_english);

        setCountryCodes(edtCode, spinnerCountryCode);

        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC)) {
            checkArabic.setChecked(true);
            lang = LanguageType.ARABIC;
        } else {
            checkEnglish.setChecked(true);
            lang = LanguageType.ENGLISH;
        }


        checkEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = LanguageType.ENGLISH;
                }
            }
        });

        checkArabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = LanguageType.ARABIC;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddPhone.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtErrorPhone.setVisibility(View.GONE);

                if (edtPhone.getText().toString().trim().equals("")) {
                    txtErrorPhone.setText(getString(R.string.error_required));
                    txtErrorPhone.setVisibility(View.VISIBLE);
                    return;
                }

                mPresenter.addPhone(countryCode.getCallingCodes().get(0), edtPhone.getText().toString().trim(), lang);
            }
        });

    }


    private void editPhone(GetPhonesQuery.Phone phone) {
        dialogEditPhone = new Dialog(getActivity());
        dialogEditPhone.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditPhone.setCancelable(true);
        dialogEditPhone.setContentView(R.layout.dialog_phone_edit);
        dialogEditPhone.show();

        EditText edtPhone = dialogEditPhone.findViewById(R.id.edt_phone_number);

        TextView txtErrorPhone = dialogEditPhone.findViewById(R.id.txt_error_phone);

        Button btnCancel = dialogEditPhone.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogEditPhone.findViewById(R.id.btn_add);

        RadioButton checkArabic = dialogEditPhone.findViewById(R.id.check_arabic);
        RadioButton checkEnglish = dialogEditPhone.findViewById(R.id.check_english);


        edtPhone.setText(phone.number() + "");

        if (phone.language().equals(LanguageType.ARABIC)) {
            checkArabic.setChecked(true);
            lang = LanguageType.ARABIC;
        } else {
            checkEnglish.setChecked(true);
            lang = LanguageType.ENGLISH;
        }


        checkEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = LanguageType.ENGLISH;
                }
            }
        });

        checkArabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = LanguageType.ARABIC;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditPhone.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editPhone(phone.number().prefix(),phone.number().number(), lang);
            }
        });

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

    private void setCountryCodes(EditText edtCode, Spinner spinnerCountryCode) {

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

        edtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCountryCode.performClick();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getPhones(true);
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event.getMsg().equals("refresh_add_phone")) {
            if (isInternetConnection())
                mPresenter.getPhones(false);
            else
                showNoConnectionAlert();
        }

    }
}
