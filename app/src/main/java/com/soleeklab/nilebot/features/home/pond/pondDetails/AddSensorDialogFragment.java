package com.soleeklab.nilebot.features.home.pond.pondDetails;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddParameterMutation;
import com.soleeklab.nilebot.GetUserBotsQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.repo.ApplicationContextInjector;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.home.settings.devices.DevicesFragment;
import com.soleeklab.nilebot.type.ISensor;
import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.GeneralUtils;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//import com.soleeklab.nilebot.features.home.settings.devices.DevicesFragment;


public class AddSensorDialogFragment extends DialogFragment {


    Unbinder unbinder;

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.lay_option)
    LinearLayout layOption;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    @BindView(R.id.spinner_bots)
    Spinner spinnerBots;
    @BindView(R.id.spinner_sensors)
    Spinner spinnerSensors;
    @BindView(R.id.edt_sensor_name)
    EditText edtSensorName;
    @BindView(R.id.txt_error_sensor_name)
    TextView txtErrorSensorName;
    @BindView(R.id.lay_name)
    LinearLayout layName;
    @BindView(R.id.edt_sensor_max)
    EditText edtSensorMax;
    @BindView(R.id.txt_error_sensor_max)
    TextView txtErrorSensorMax;
    @BindView(R.id.edt_sensor_min)
    EditText edtSensorMin;
    @BindView(R.id.txt_error_sensor_min)
    TextView txtErrorSensorMin;
    @BindView(R.id.lay_sensor_data)
    RelativeLayout laySensorData;
    @BindView(R.id.btn_add_sensor)
    Button btnAddSensor;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_cancel_add_device)
    Button btnCancelAddDevice;
    @BindView(R.id.btn_go_to_settings)
    Button btnGoToSettings;
    @BindView(R.id.lay_no_sensor)
    LinearLayout layNoSensor;
    @BindView(R.id.lay_sensor_details)
    RelativeLayout laySensorDetails;
    @BindView(R.id.lay_threshold)
    LinearLayout layThreshold;
    @BindView(R.id.txt_error_sensor_threshold)
    TextView txtErrorSensorThreshold;
    @BindView(R.id.txt_error_sensor)
    TextView txtErrorSensor;
    @BindView(R.id.lay_spinners)
    LinearLayout laySpinners;
    @BindView(R.id.txt_error_bot)
    TextView txtErrorBot;

    private LocalRepo localRepo;
    private String pondId;
    private GetUserBotsQuery.Bot selectedBot = null;
    private GetUserBotsQuery.Sensor selectedSensor = null;

    private BotSpinnerAdapter botSpinnerAdapter;
    private SensorSpinnerAdapter sensorSpinnerAdapter;

    public AddSensorDialogFragment() {
        localRepo = new LocalRepoImpl(ApplicationContextInjector.getApplicationContext());
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_sensor_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pondId = getArguments().getString("pondId");
        getBots();

    }

    private void getBots() {
        progress.setVisibility(View.VISIBLE);
        GetUserBotsQuery getUserBotsQuery = GetUserBotsQuery.builder().build();
        ApolloClientBuilder.getApolloClient(true).query(getUserBotsQuery).enqueue(new ApolloCall.Callback<GetUserBotsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserBotsQuery.Data> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);

                        if (response.data() != null) {
                            if (response.data().user().bots() != null) {

                                if (response.data().user().bots().size() > 0) {
                                    laySensorDetails.setVisibility(View.VISIBLE);
                                } else {
                                    layNoSensor.setVisibility(View.VISIBLE);
                                }
                                initBots(new ArrayList<GetUserBotsQuery.Bot>(response.data().user().bots()));

                            } else {
                                DialogUtil.showAlertDialog(getActivity(), getString(R.string.error_signup));
                            }
                        } else
                            DialogUtil.showAlertDialog(getActivity(), getString(R.string.server_error));
                    }
                });


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        layOption.setVisibility(View.GONE);
                        DialogUtil.showAlertDialog(getActivity(), getString(R.string.error_occured));
                    }
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initBots(ArrayList<GetUserBotsQuery.Bot> bots) {

        if (getView() == null)
            return;

        bots.add(0, new GetUserBotsQuery.Bot("", "", getString(R.string.choose_bot), new ArrayList<>()));
        botSpinnerAdapter = new BotSpinnerAdapter(getActivity(), R.layout.item_farmtype_spinner, bots);

        spinnerBots.setAdapter(botSpinnerAdapter);
        spinnerBots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (botSpinnerAdapter != null) {
                    initSensors(new ArrayList<GetUserBotsQuery.Sensor>(new ArrayList<>()));
                    if (position != 0) {
                        selectedBot = bots.get(position);
                        spinnerSensors.setVisibility(View.VISIBLE);

                        selectedSensor = null;
                        initSensors(new ArrayList<GetUserBotsQuery.Sensor>(selectedBot.sensors()));

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initSensors(ArrayList<GetUserBotsQuery.Sensor> sensors) {

        if (getView() == null)
            return;

        sensors.add(0, new GetUserBotsQuery.Sensor("", getString(R.string.choose_sensor), null));

        sensorSpinnerAdapter = new SensorSpinnerAdapter(getActivity(), pondId, R.layout.item_farmtype_spinner, sensors);

        spinnerSensors.setAdapter(sensorSpinnerAdapter);
        spinnerSensors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sensorSpinnerAdapter != null) {
                    if (position != 0) {
                        selectedSensor = sensors.get(position);
                        laySensorData.setVisibility(View.VISIBLE);
                        btnAddSensor.setBackgroundResource(R.drawable.round_corner_button_blue);
                        btnAddSensor.setTextColor(getResources().getColor(R.color.white));
                        btnAddSensor.setEnabled(true);
                    }
                }
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

    @OnClick({R.id.btn_add_sensor, R.id.btn_cancel, R.id.btn_go_to_settings, R.id.btn_cancel_add_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_sensor:
                addSensors();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_cancel_add_device:
                dismiss();
                break;
            case R.id.btn_go_to_settings:
                dismiss();
                GeneralUtils.navigateToFragment(getActivity(), new DevicesFragment(), DevicesFragment.class.getSimpleName(), true, false, false);
                break;
        }
    }

    private void addSensors() {

        txtErrorSensorName.setVisibility(View.GONE);
        txtErrorSensorThreshold.setVisibility(View.GONE);
        txtErrorSensor.setVisibility(View.GONE);
        txtErrorBot.setVisibility(View.GONE);

        Double minThreshold = null;
        Double maxThreshold = null;

        if (selectedBot == null) {
            txtErrorBot.setVisibility(View.VISIBLE);
            return;
        }
        if (selectedSensor == null) {
            txtErrorSensor.setVisibility(View.VISIBLE);
            return;
        }

        if (edtSensorName.getText().toString().trim().equals("")) {
            txtErrorSensorName.setText(getString(R.string.error_required));
            txtErrorSensorName.setVisibility(View.VISIBLE);
            return;
        }

        if (!edtSensorMax.getText().toString().trim().equals("")) {
            maxThreshold = Double.parseDouble(edtSensorMax.getText().toString().trim());
        }
        if (!edtSensorMin.getText().toString().trim().equals("")) {
            minThreshold = Double.parseDouble(edtSensorMin.getText().toString().trim());
        }

        if (minThreshold != null && maxThreshold != null) {
            if (maxThreshold < minThreshold) {
                txtErrorSensorThreshold.setText(getString(R.string.error_max));
                txtErrorSensorThreshold.setVisibility(View.VISIBLE);
                return;
            } else if (maxThreshold > 999999 || minThreshold > 999999) {
                txtErrorSensorThreshold.setText(getString(R.string.error_large));
                txtErrorSensorThreshold.setVisibility(View.VISIBLE);
                return;
            }
        }

        progress.setVisibility(View.VISIBLE);
        AddParameterMutation addParameterMutation = AddParameterMutation.builder()
                .pondID(pondId)
                .sensor(ISensor.builder().nameID(selectedSensor.nameID()).botID(selectedBot.botID()).build())
                .name(edtSensorName.getText().toString().trim())
                .thresholdLow(minThreshold)
                .thresholdHigh(maxThreshold)
                .build();

        ApolloClientBuilder.getApolloClient(true).mutate(addParameterMutation).enqueue(new ApolloCall.Callback<AddParameterMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddParameterMutation.Data> response) {
                if (response.data() != null) {
                    if (response.data().addParameter() != null) {
                        dismiss();
                        EventBus.getDefault().post(new MessageEvent("refresh"));
                    } else {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                            }
                        });
                        DialogUtil.showAlertDialog(getActivity(), getString(R.string.server_error));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                    }
                });
                DialogUtil.showAlertDialog(getActivity(), getString(R.string.error_occured));
            }
        });
    }
}
