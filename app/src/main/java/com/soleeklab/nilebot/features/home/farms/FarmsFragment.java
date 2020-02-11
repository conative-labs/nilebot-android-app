package com.soleeklab.nilebot.features.home.farms;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;
import com.soleeklab.nilebot.GetFarmsQuery;
import com.soleeklab.nilebot.LogoutMutation;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.features.home.HomeActivity;
import com.soleeklab.nilebot.features.home.farms.addFarm.AddFarmActivity;
import com.soleeklab.nilebot.features.home.pond.PondsFragment;
import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.GeneralUtils;
import com.soleeklab.nilebot.utils.listeners.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FarmsFragment extends ParentFragment implements FarmsContract.View, SwipeRefreshLayout.OnRefreshListener {


    Unbinder unbinder;

    @Inject
    FarmsContract.Presenter mPresenter;

    @BindView(R.id.rv_farms)
    RecyclerView rvFarms;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_add_farm)
    Button btnAddFarm;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;

    FarmsAdapter farmsAdapter;

    @Inject
    LocalRepo localRepo;


    Dialog dialog;


    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    MyCache myCache;

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;


    @Inject
    public FarmsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farms, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (getActivity().getSupportFragmentManager().getFragments().size() == 2) {
                            DialogUtil.showSureToExitDialog(getActivity(), getString(R.string.sure_to_exit), new DialogUtil.DialogClick() {
                                @Override
                                public void onDeleteClick() {
                                    getActivity().finish();
                                }

                                @Override
                                public void onCancelClick() {

                                }
                            });

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
    public void setPresenter(FarmsContract.Presenter presenter) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        txtToolbarTitle.setText(getString(R.string.farms));
        btnBack.setVisibility(View.GONE);
        swipToRefreshLayout.setOnRefreshListener(this);
        if (isInternetConnection()) {
            mPresenter.getFarms();
            HomeActivity homeActivity = (HomeActivity)getActivity();
            homeActivity.getNotifications(0);
        }
        else
            showNoConnectionAlert();


        initSearch();
    }

    private void initSearch() {
        //change icon color
//        searchView.setIconified(false);
        ImageView searchIcon = searchView.findViewById(R.id.search_button);
        ImageView searchbadge = searchView.findViewById(R.id.search_voice_btn);

        ImageView searchClose = searchView.findViewById(R.id.search_close_btn);


        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_black_24dp));
        searchbadge.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_black_24dp));

        searchClose.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_white_24dp));

        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (farmsAdapter != null)
                    farmsAdapter.getFilter().filter(query.trim());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (farmsAdapter != null)
                    farmsAdapter.getFilter().filter(newText.trim());
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mPresenter.unregisterView();
        unbinder.unbind();

    }

    @OnClick({R.id.btn_add_farm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_farm:
                startActivity(new Intent(getActivity(), AddFarmActivity.class));
                break;
        }
    }

    @Override
    public void initFarms(List<GetFarmsQuery.Farm> farms) {

        if (farms.size() > 0) {
            rvFarms.setVisibility(View.VISIBLE);
            layNoData.setVisibility(View.GONE);
            farmsAdapter = new FarmsAdapter(getActivity(), farms, new FarmsAdapter.FarmsClicks() {
                @Override
                public void onEditFarmClick(GetFarmsQuery.Farm farm) {
                    Gson gson = new Gson();
                    Intent i = new Intent(getActivity(), AddFarmActivity.class);
                    i.putExtra("isEdit", true);
                    i.putExtra("farm", gson.toJson(farm));
                    startActivity(i);
                }

                @Override
                public void onDeleteFarm(GetFarmsQuery.Farm farm) {
                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_sure_to);

                    TextView text = dialog.findViewById(R.id.txt_question);
                    text.setText(getString(R.string.sure_to_delete_farm));

                    Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                    Button btnDelete = dialog.findViewById(R.id.btn_delete);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.removeFarm(farm.farmID());

                        }
                    });

                    dialog.show();


                }
            });
            farmsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Bundle b = new Bundle();
                    b.putString("farmId", farmsAdapter.getData().get(position).farmID());
                    PondsFragment pondsFragment = new PondsFragment();
                    pondsFragment.setArguments(b);

                    GeneralUtils.navigateToFragment(getActivity(), pondsFragment, PondsFragment.class.getSimpleName(), true, false, false);

//                    GeneralUtils.replaceFragmentToActivity(getActivity().getSupportFragmentManager(), pondsFragment,
//                            R.id.frame_home);

                }
            });
            rvFarms.setAdapter(farmsAdapter);
        } else {
            rvFarms.setVisibility(View.GONE);
            layNoData.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }

    @Override
    public void logoutUser() {

        showProgress();
        LogoutMutation logoutMutation = LogoutMutation.builder().build();

        ApolloClientBuilder.getApolloClient(true).mutate(logoutMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<LogoutMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<LogoutMutation.Data> response) {
                hideProgress();
                if (response.data().logout() != null) {
                    localRepo.logout();
                    getActivity().finishAffinity();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                hideProgress();
                showAlert(R.string.error_occured);
            }
        }, uiHandler));
    }


    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getFarms();
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
        if (event.getMsg().equals("refresh_farms")) {
            if (isInternetConnection())
                mPresenter.getFarms();
            else
                showNoConnectionAlert();
        }

    }


}


