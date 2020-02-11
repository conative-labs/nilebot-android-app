package com.soleeklab.nilebot.features.home.settings.profile;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.ModifyUserEmailMutation;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;
import com.soleeklab.nilebot.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEmailFragment extends ParentFragment {

    Handler uiHandler = new Handler(Looper.getMainLooper());

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.txt_error_email)
    TextView txtErrorEmail;
    @BindView(R.id.btn_edit_email)
    Button btnEditEmail;
    Unbinder unbinder;

    private String email = "";

    @Inject
    LocalRepo localRepo;

    @Inject
    public EditEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtToolbarTitle.setText(getString(R.string.edit_email));
        email = getArguments().getString("email");
        edtEmail.setText(email);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_edit_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_edit_email:
                editEMail(edtEmail.getText().toString().trim());
                break;
        }
    }

    private void editEMail(String email) {

        txtErrorEmail.setVisibility(View.GONE);

        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtErrorEmail.setText(getString(R.string.error_email_invalid));
            txtErrorEmail.setVisibility(View.VISIBLE);
            return;
        }

        showProgress();
        ModifyUserEmailMutation modifyUserEmailMutation = ModifyUserEmailMutation.builder().email(email).build();

        ApolloClientBuilder.getApolloClient(true).mutate(modifyUserEmailMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyUserEmailMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyUserEmailMutation.Data> response) {
                hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyUserEmail().equals(Status.SUCCESS)) {
                        DialogUtil.showInformationDialog(getActivity(),
                                getString(R.string.hint),
                                getString(R.string.email_verify),
                                getString(R.string.text_ok), new DialogUtil.DialogClick() {
                            @Override
                            public void onDeleteClick() {

                                EventBus.getDefault().post(new MessageEvent("refresh_user"));
                                getActivity().onBackPressed();

                            }

                                    @Override
                                    public void onCancelClick() {

                                    }
                                });
                    } else {
                        showAlert(localRepo.getTranslation(response.data().modifyUserEmail().rawValue()));
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
