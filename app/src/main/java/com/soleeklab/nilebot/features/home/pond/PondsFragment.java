package com.soleeklab.nilebot.features.home.pond;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondActivity;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondDetailsFragment;
import com.soleeklab.nilebot.utils.GeneralUtils;
import com.soleeklab.nilebot.utils.listeners.OnItemClickListener;

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

//import com.soleeklab.nilebot.features.home.farms.pond.addPond.AddPondActivity;
//import com.soleeklab.nilebot.features.home.farms.pond.pondDetails.PondDetailsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PondsFragment extends ParentFragment implements PondsContract.View, SwipeRefreshLayout.OnRefreshListener {


    @Inject
    PondsContract.Presenter mPresenter;

    @BindView(R.id.rv_ponds)
    RecyclerView rvPonds;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;

    Unbinder unbinder;

    PondsAdapter pondsAdapter;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;

    Dialog dialog;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_add_pond)
    Button btnAddPond;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.spinner_filter_pond)
    Spinner spinnerFilterPond;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;

    private String farmId;

    private String pondType = "All";
    private String keyWord = "";
    PondFilterTypeAdapter pondTypeAdapter;

    @Inject
    public PondsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ponds, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void setPresenter(PondsContract.Presenter presenter) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        swipToRefreshLayout.setOnRefreshListener(this);
        txtToolbarTitle.setText(getString(R.string.ponds));
        farmId = getArguments().getString("farmId");
        if (isInternetConnection())
            mPresenter.getPonds(farmId);
        else
            showNoConnectionAlert();

        initSearch();
        mPresenter.getPondTypes();
    }


    private void initSearch() {
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
                if (pondsAdapter != null)
                    pondsAdapter.getFilter().filter(query.trim() + "#" + pondType);
                keyWord = query.trim();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pondsAdapter != null)
                    pondsAdapter.getFilter().filter(newText.trim() + "#" + pondType);
                keyWord = newText.trim();
                return false;
            }
        });
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
        if (event.getMsg().equals("refresh_ponds")) {
            if (isInternetConnection())
                mPresenter.getPonds(farmId);
            else
                showNoConnectionAlert();
        }

    }

    @Override
    public void initPondTypes(ArrayList<String> pondTypesList) {
        pondTypesList.remove(pondTypesList.size() - 1);
        pondTypesList.add(0, getString(R.string.all));
        pondTypeAdapter = new PondFilterTypeAdapter(getActivity(), R.layout.item_farmtype_spinner, pondTypesList);
        spinnerFilterPond.setAdapter(pondTypeAdapter);
        spinnerFilterPond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (pondsAdapter != null) {
                    pondType = pondTypesList.get(position);
                    pondsAdapter.getFilter().filter(keyWord + "#" + pondType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initPonds(List<GetPondsByFarmIDQuery.Pond> ponds) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ponds.size() > 0) {
                    rvPonds.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                    pondsAdapter = new PondsAdapter(getActivity(), ponds, new PondsAdapter.PondsClicks() {
                        @Override
                        public void onEditPondClick(GetPondsByFarmIDQuery.Pond pond) {
                            Gson gson = new Gson();
                            Intent i = new Intent(getActivity(), AddPondActivity.class);
                            i.putExtra("farmId", farmId);
                            i.putExtra("pond", gson.toJson(pond));
                            i.putExtra("isEdit", true);
                            startActivity(i);
                        }

                        @Override
                        public void onDeletePond(GetPondsByFarmIDQuery.Pond pond) {

                            dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_sure_to);

                            TextView text = dialog.findViewById(R.id.txt_question);
                            text.setText(getString(R.string.sure_to_pond));

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
                                    mPresenter.removePond(pond.pondID(), farmId);
                                }
                            });

                            dialog.show();


                        }

                        @Override
                        public void onSearchResult(int size) {

                            if (size == 0) {
                                layNoData.setVisibility(View.VISIBLE);
                                txtNoData.setText(getString(R.string.no_ponds_found));
                                rvPonds.setVisibility(View.GONE);
                            } else {
                                rvPonds.setVisibility(View.VISIBLE);
                                layNoData.setVisibility(View.GONE);
                            }
                        }
                    });
                    pondsAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle b = new Bundle();
                            b.putString("pondId", pondsAdapter.getData().get(position).pondID());
                            b.putString("pondname", pondsAdapter.getData().get(position).name());
                            PondDetailsFragment pondDetailsFragment = new PondDetailsFragment();
                            pondDetailsFragment.setArguments(b);
                            GeneralUtils.navigateToFragment(getActivity(), pondDetailsFragment, PondDetailsFragment.class.getSimpleName(), true, false, false);

                        }
                    });
                    rvPonds.setAdapter(pondsAdapter);
                } else {
                    rvPonds.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unregisterView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_add_pond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_add_pond:
                startActivity(new Intent(getActivity(), AddPondActivity.class).putExtra("farmId", farmId));
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getPonds(farmId);
        else
            showNoConnectionAlert();
        swipToRefreshLayout.setRefreshing(false);
    }
}
