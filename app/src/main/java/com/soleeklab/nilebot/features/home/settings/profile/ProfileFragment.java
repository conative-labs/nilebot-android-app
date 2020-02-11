package com.soleeklab.nilebot.features.home.settings.profile;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soleeklab.nilebot.GetUserQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.utils.GeneralUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends ParentFragment implements ProfileContract.View,SwipeRefreshLayout.OnRefreshListener {


    Unbinder unbinder;


    @Inject
    ProfileContract.Presenter mPresenter;

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;

    private String country = "";
    @Inject
    LocalRepo localRepo;

    private GetUserQuery.User user = null;

    @Inject
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        swipToRefreshLayout.setOnRefreshListener(this);
        if (isInternetConnection()) {
            mPresenter.getUser();
        } else {
            showNoConnectionAlert();
        }
        txtToolbarTitle.setText(getString(R.string.profile));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserData(GetUserQuery.User user) {
        this.user = user;
        txtEmail.setText(user.email());
        txtName.setText(user.name());
        txtPhone.setText(user.phone().prefix() + "" + user.phone().number());

        txtName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);

    }


    @OnClick({R.id.btn_back, R.id.btn_edit_name, R.id.btn_edit_mail, R.id.btn_edit_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_edit_name:
                Bundle b1 = new Bundle();
                b1.putString("name", user.name());
                EditNameFragment editNameFragment = new EditNameFragment();
                editNameFragment.setArguments(b1);
                GeneralUtils.navigateToFragment(getActivity(), editNameFragment, EditNameFragment.class.getSimpleName(), true, false, false);

                break;
            case R.id.btn_edit_mail:
                Bundle b2 = new Bundle();
                b2.putString("email", user.email());
                EditEmailFragment editEmailFragment = new EditEmailFragment();
                editEmailFragment.setArguments(b2);
                GeneralUtils.navigateToFragment(getActivity(), editEmailFragment, EditEmailFragment.class.getSimpleName(), true, false, false);
                break;
            case R.id.btn_edit_number:
                Bundle b3 = new Bundle();
                b3.putString("phone", user.phone().number());
                b3.putString("prefix", user.phone().prefix());
                EditNumberFragment editNumberFragment = new EditNumberFragment();
                editNumberFragment.setArguments(b3);
                GeneralUtils.navigateToFragment(getActivity(), editNumberFragment, EditNumberFragment.class.getSimpleName(), true, false, false);
                break;
        }
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
        if (event.getMsg().equals("refresh_user")) {
            if (isInternetConnection())
                mPresenter.getUser();
            else
                showNoConnectionAlert();
        }

    }

    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getUser();
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setRefreshing(false);
    }
}
