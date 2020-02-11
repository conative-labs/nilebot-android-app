package com.soleeklab.nilebot.features.home.settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.LogoutMutation;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.features.SplashActivity;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.features.home.HomeActivity;
import com.soleeklab.nilebot.features.home.settings.changePassword.ChangePasswordFragment;
import com.soleeklab.nilebot.features.home.settings.devices.DevicesFragment;
import com.soleeklab.nilebot.features.home.settings.phones.PhonesFragment;
import com.soleeklab.nilebot.features.home.settings.profile.ProfileFragment;
import com.soleeklab.nilebot.type.Status;
import com.soleeklab.nilebot.utils.GeneralUtils;
import com.soleeklab.nilebot.utils.LocalHelper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingsFragment extends ParentFragment {


    @BindView(R.id.lay_lang_options)
    LinearLayout layLangOptions;

    @BindView(R.id.lay_language)
    RelativeLayout layLanguage;

    @BindView(R.id.lay_devices)
    RelativeLayout layDevices;

    @BindView(R.id.lay_logout)
    RelativeLayout layLogout;

    @BindView(R.id.img_lang_indicator)
    ImageView imgLangIndicator;
    @BindView(R.id.img_sequence_indicator)
    ImageView imgSequenceIndicator;
    Unbinder unbinder;

    @BindView(R.id.check_arabic)
    RadioButton checkArabic;
    @BindView(R.id.check_english)
    RadioButton checkEnglish;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;


    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    LocalRepo localRepo;

    LocalHelper localHelper;
    Dialog dialog;


    @Inject
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (getActivity().getSupportFragmentManager().getFragments().size() == 2) {

                            //todo navigate to Farms
                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            homeActivity.navigateToFarms();

                        } else {
                            getActivity().onBackPressed();
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localHelper = LocalHelper.getInstance(getActivity());
        txtToolbarTitle.setText(getString(R.string.settings));
        btnBack.setVisibility(View.GONE);

        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC))
            checkArabic.setChecked(true);
        else
            checkEnglish.setChecked(true);

        checkEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    localHelper.setLanguage(LocalHelper.LANGUAGE_ENGLISH);
                    LocalHelper.ChangeDesignToLTR(getActivity());
                    getActivity().finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
                }
            }
        });

        checkArabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    localHelper.setLanguage(LocalHelper.LANGUAGE_ARABIC);
                    LocalHelper.ChangeDesignToRTL(getActivity());
                    getActivity().finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.lay_profile, R.id.lay_phones, R.id.lay_change_password, R.id.lay_language, R.id.lay_devices, R.id.lay_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.lay_language:
//                if (layLangOptions.isShown()) {
//                    GeneralUtils.slide_up(getActivity(), layLangOptions);
//                    layLangOptions.setVisibility(View.GONE);
//                    imgLangIndicator.setImageResource(R.drawable.ic_navigate_forward);
//                } else {
//                    layLangOptions.setVisibility(View.VISIBLE);
//                    GeneralUtils.slide_down(getActivity(), layLangOptions);
//                    imgLangIndicator.setImageResource(R.drawable.ic_navigate_down);
//                }
//                break;
            case R.id.lay_devices:
                GeneralUtils.navigateToFragment(getActivity(), new DevicesFragment(), DevicesFragment.class.getSimpleName(), true, false, false);
                break;
            case R.id.lay_profile:
                GeneralUtils.navigateToFragment(getActivity(), new ProfileFragment(), ProfileFragment.class.getSimpleName(), true, false, false);
                break;
            case R.id.lay_change_password:
                GeneralUtils.navigateToFragment(getActivity(), new ChangePasswordFragment(), ChangePasswordFragment.class.getSimpleName(), true, false, false);
                break;
            case R.id.lay_phones:
                GeneralUtils.navigateToFragment(getActivity(), new PhonesFragment(), PhonesFragment.class.getSimpleName(), true, false, false);
                break;
            case R.id.lay_logout:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_sure_to);

                TextView text = dialog.findViewById(R.id.txt_question);
                text.setText(getString(R.string.sure_to_logout));
                TextView txtHeaderTitle = dialog.findViewById(R.id.txt_header_title);
                txtHeaderTitle.setText(getString(R.string.logout));

                Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                Button btnDelete = dialog.findViewById(R.id.btn_delete);

                btnDelete.setText(getString(R.string.logout));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOut();

                    }
                });

                dialog.show();

                break;
        }
    }


    private void logOut() {
        showProgress();
        LogoutMutation logoutMutation = LogoutMutation.builder().build();

        ApolloClientBuilder.getApolloClient(true).mutate(logoutMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<LogoutMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<LogoutMutation.Data> response) {
                hideProgress();
                if (response.data() != null && response.data().logout() != null) {
                    if (response.data().logout().equals(Status.SUCCESS)) {
                        dialog.dismiss();
                        localRepo.logout();
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    } else {
                        showAlert(localRepo.getTranslation(response.data().logout().rawValue()));
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
}
