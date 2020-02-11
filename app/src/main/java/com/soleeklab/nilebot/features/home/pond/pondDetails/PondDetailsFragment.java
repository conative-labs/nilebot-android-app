package com.soleeklab.nilebot.features.home.pond.pondDetails;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.soleeklab.nilebot.GetSensorsByPondIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.FilterReadingsFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.PondSensorsReadingsParentFragment;
import com.soleeklab.nilebot.utils.GeneralUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PondDetailsFragment extends ParentFragment implements PondDetailsContract.View, SwipeRefreshLayout.OnRefreshListener {


    Unbinder unbinder;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_readings)
    TextView btnReadings;
    @BindView(R.id.rv_sensors)
    RecyclerView rvSensors;

    @Inject
    PondDetailsContract.Presenter mPresenter;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;
    @BindView(R.id.frame_readings)
    FrameLayout frameReadings;
    @BindView(R.id.btn_add_sensor)
    Button btnAddSensor;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.frame_filter)
    FrameLayout frameFilter;

    private String pondId;
    private String pondName;
    Dialog dialog;
    SensorsAdapter sensorsAdapter;

    @Inject
    public PondDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pond_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        swipToRefreshLayout.setOnRefreshListener(this);
        pondId = getArguments().getString("pondId");
        txtToolbarTitle.setText(getArguments().getString("pondname"));
        refresh();


    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event.getMsg().equals("refresh")) {
            refresh();
        } else if (event.getMsg().equals("back_filter")) {
            rvSensors.setVisibility(View.VISIBLE);
            frameFilter.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unregisterView();
        unbinder.unbind();
    }

    @Override
    public void initSensors(List<GetSensorsByPondIDQuery.Parameter> sensors) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sensors.size() > 0) {
//                    frameReadings.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                    rvSensors.setVisibility(View.VISIBLE);
                    sensorsAdapter = new SensorsAdapter(getActivity(), sensors, new SensorsAdapter.SensorClicks() {
                        @Override
                        public void onEditClick(GetSensorsByPondIDQuery.Parameter sensor) {
                            //todo edit parameter
                            FragmentManager fm;
                            EditSensorDialogFragment editSensorDialogFragment;
                            fm = getChildFragmentManager();
                            Bundle bundle = new Bundle();
                            bundle.putString("pondId", pondId);
                            bundle.putString("sensor", new Gson().toJson(sensor));
                            editSensorDialogFragment = new EditSensorDialogFragment();
                            editSensorDialogFragment.setArguments(bundle);
                            editSensorDialogFragment.show(fm, "fragment_add_sensor");

                        }

                        @Override
                        public void onShowReadsClick(GetSensorsByPondIDQuery.Parameter sensor) {
//                            rvSensors.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.GONE);
//                            frameFilter.setVisibility(View.VISIBLE);


                            sensorsAdapter.getData().get(0).parameterID();
                            Gson gson = new Gson();
                            Bundle b = new Bundle();
                            b.putString("sensorList", gson.toJson(sensorsAdapter.getData()));
                            b.putString("selected_sensor", gson.toJson(sensor));
                            b.putString("pondname", getArguments().getString("pondname"));
                            FilterReadingsFragment filterReadingsFragment = new FilterReadingsFragment();
                            filterReadingsFragment.setArguments(b);
                            GeneralUtils.navigateToFragment(getActivity(), filterReadingsFragment, FilterReadingsFragment.class.getSimpleName(), true, false, false);

                        }
                    });
                    rvSensors.setAdapter(sensorsAdapter);
                } else {
                    layNoData.setVisibility(View.VISIBLE);
                    frameReadings.setVisibility(View.GONE);
                    rvSensors.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void refresh() {
        if (isInternetConnection())
            mPresenter.getSensors(pondId, true);
        else
            showNoConnectionAlert();
    }

    @Override
    public void successDeleteSensor() {

        if (isInternetConnection())
            mPresenter.getSensors(pondId, false);
        else
            showNoConnectionAlert();

        dialog.dismiss();
    }

    @Override
    public void setPresenter(PondDetailsContract.Presenter presenter) {

    }

    @OnClick({R.id.btn_add, R.id.btn_back, R.id.btn_readings, R.id.btn_add_sensor})
    public void onViewClicked(View view) {
        FragmentManager fm;
        AddSensorDialogFragment addSensorDialogFragment;
        switch (view.getId()) {

            case R.id.btn_add:
                fm = getChildFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("pondId", pondId);
                addSensorDialogFragment = new AddSensorDialogFragment();
                addSensorDialogFragment.setArguments(bundle);
                addSensorDialogFragment.show(fm, "fragment_add_sensor");

                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_readings:

                sensorsAdapter.getData().get(0).parameterID();
                Gson gson = new Gson();
                Bundle b = new Bundle();
                b.putBoolean("isFromFilter", false);
                b.putString("sensorList", gson.toJson(sensorsAdapter.getData()));
                PondSensorsReadingsParentFragment pondSensorsReadingsParentFragment = new PondSensorsReadingsParentFragment();
                pondSensorsReadingsParentFragment.setArguments(b);
                GeneralUtils.navigateToFragment(getActivity(), pondSensorsReadingsParentFragment, PondSensorsReadingsParentFragment.class.getSimpleName(), true, false, false);

                break;
            case R.id.btn_add_sensor:
                fm = getChildFragmentManager();
                Bundle bundlee = new Bundle();
                bundlee.putString("pondId", pondId);
                addSensorDialogFragment = new AddSensorDialogFragment();
                addSensorDialogFragment.setArguments(bundlee);
                addSensorDialogFragment.show(fm, "fragment_add_sensor");

                break;

        }
    }

    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getSensors(pondId, true);
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setRefreshing(false);
    }
}
