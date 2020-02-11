package com.soleeklab.nilebot.features.home.settings.profile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.soleeklab.nilebot.ModifyUserMutation;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EditNameFragment extends ParentFragment {


    Handler uiHandler = new Handler(Looper.getMainLooper());

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_error_name)
    TextView txtErrorName;
    @BindView(R.id.btn_edit_name)
    Button btnEditName;
    Unbinder unbinder;

    private String name = "";

    @Inject
    LocalRepo localRepo;

    @Inject
    public EditNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_name, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtToolbarTitle.setText(getString(R.string.edit_name));
        name = getArguments().getString("name");
        edtName.setText(name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_edit_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_edit_name:
                editName(edtName.getText().toString().trim());
                break;
        }
    }

    private void editName(String name) {


        txtErrorName.setVisibility(View.GONE);

        if (name.length() < 3) {
            txtErrorName.setText(getString(R.string.error_name_invalid));
            txtErrorName.setVisibility(View.VISIBLE);
            return;
        }
        showProgress();
        ModifyUserMutation modifyUserMutation = ModifyUserMutation.builder().name(name).build();

        ApolloClientBuilder.getApolloClient(true).mutate(modifyUserMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyUserMutation.Data> response) {
                hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyUser().equals(Status.SUCCESS)) {
                        EventBus.getDefault().post(new MessageEvent("refresh_user"));
                        getActivity().onBackPressed();
                    } else {
                        showAlert(localRepo.getTranslation(response.data().modifyUser().rawValue()));
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
