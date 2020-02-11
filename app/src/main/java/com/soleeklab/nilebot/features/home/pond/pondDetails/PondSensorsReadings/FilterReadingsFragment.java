package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.utils.GeneralUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterReadingsFragment extends ParentFragment {


    @BindView(R.id.edt_from_time)
    TextView edtFromTime;
    @BindView(R.id.txt_error_start_time)
    TextView txtErrorStartTime;
    @BindView(R.id.edt_to_time)
    TextView edtToTime;
    @BindView(R.id.txt_error_to_time)
    TextView txtErrorToTime;
    @BindView(R.id.rv_sensors_check)
    RecyclerView rvSensorsCheck;
    @BindView(R.id.txt_error_sensor)
    TextView txtErrorSensor;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_filter)
    Button btnFilter;
    @BindView(R.id.lay_bottom)
    LinearLayout layBottom;

    Unbinder unbinder;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;

    Dialog dateDialog;

    private String fromTime = "";
    private String toTime = "";
    Locale locale = new Locale("en");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", locale);
    Calendar pickerCalendar;


    private GetSensorsReadingByPondIDQuery.Parameter selectedParam;
    ArrayList<GetSensorsReadingByPondIDQuery.Parameter> sensorArrayList = new ArrayList<>();
    ArrayList<String> sensorIdsList = new ArrayList<>();
    ArrayList<GetSensorsReadingByPondIDQuery.Parameter> selectedSensorList = new ArrayList<>();

    private int selectedParamPosition = 0;

    Calendar calendar = Calendar.getInstance();
    Date fromDate, toDate;


    @Inject
    public FilterReadingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_readings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    private void setupSensors() {
        selectedParam = new Gson().fromJson(getArguments().getString("selected_sensor"), GetSensorsReadingByPondIDQuery.Parameter.class);
        sensorArrayList = new Gson().fromJson(getArguments().getString("sensorList"), new TypeToken<List<GetSensorsReadingByPondIDQuery.Parameter>>() {
        }.getType());
        selectedSensorList.add(selectedParam);
//        for (GetSensorsReadingByPondIDQuery.Parameter sensor : sensorArrayList)
        sensorIdsList.add(selectedParam.parameterID());

    }

    private int getSelectedParamPosition() {

        int selectedPos = 0;

        for (int i = 0; i < sensorArrayList.size(); i++) {
            if (sensorArrayList.get(i).parameterID().equals(selectedParam.parameterID())) {
                selectedPos = i;
            }
        }

        return selectedPos;
    }

    private void initTime() {

        toDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -23);
        fromDate = calendar.getTime();


        pickerCalendar = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();
        toTime = df.format(current);

        calendar.add(Calendar.DAY_OF_YEAR, -23);
        Date from = calendar.getTime();
        fromTime = df.format(from);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtToolbarTitle.setText(getArguments().getString("pondname"));
        setupSensors();
        initTime();

        edtFromTime.setText(fromTime);
        edtToTime.setText(toTime);

        edtFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateDialog(edtFromTime, true);
            }
        });
        edtToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateDialog(edtToTime, false);
            }
        });

        SensorFilterAdapter sensorFilterAdapter = new SensorFilterAdapter(getActivity(), sensorArrayList, new SensorFilterAdapter.OnItemCheckListener() {

            @Override
            public void onItemCheck(GetSensorsReadingByPondIDQuery.Parameter item) {

                if (selectedSensorList.size() > 0) {
                    boolean isFound = false;
                    for (int i = 0; i < selectedSensorList.size(); i++) {
                        if (selectedSensorList.get(i).parameterID().equals(item.parameterID())) {
                            isFound = true;
                        }
                    }
                    if (!isFound) {
                        selectedSensorList.add(item);
                    }

                } else {
                    selectedSensorList.add(item);
                }
            }

            @Override
            public void onItemUncheck(GetSensorsReadingByPondIDQuery.Parameter item) {
                for (int i = 0; i < selectedSensorList.size(); i++) {
                    if (selectedSensorList.get(i).parameterID().equals(item.parameterID())) {
                        selectedSensorList.remove(i);
                        break;
                    }
                }
            }
        }, selectedSensorList);
        rvSensorsCheck.setAdapter(sensorFilterAdapter);
        rvSensorsCheck.smoothScrollToPosition(getSelectedParamPosition());
        rvSensorsCheck.getLayoutManager().scrollToPosition(getSelectedParamPosition());
        rvSensorsCheck.scrollToPosition(getSelectedParamPosition());

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                txtErrorStartTime.setVisibility(View.GONE);
                txtErrorToTime.setVisibility(View.GONE);
                txtErrorSensor.setVisibility(View.GONE);

                fromTime = edtFromTime.getText().toString().trim();
                toTime = edtToTime.getText().toString().trim();

                if (isInternetConnection()) {
                    sensorIdsList = new ArrayList<>();
                    for (int i = 0; i < selectedSensorList.size(); i++) {
                        if (!sensorIdsList.contains(selectedSensorList.get(i).parameterID()))
                            sensorIdsList.add(selectedSensorList.get(i).parameterID());
                    }
                    if (TextUtils.isEmpty(fromTime)) {
                        txtErrorStartTime.setVisibility(View.VISIBLE);
                        txtErrorStartTime.setText(getString(R.string.error_required));
                        return;
                    }
                    if (TextUtils.isEmpty(toTime)) {
                        txtErrorToTime.setVisibility(View.VISIBLE);
                        txtErrorToTime.setText(getString(R.string.error_required));
                        return;
                    }
                    try {
                        Date fromDate = df.parse(fromTime);
                        Date toDate = df.parse(toTime);
                        if (fromDate.after(toDate)) {
                            txtErrorToTime.setVisibility(View.VISIBLE);
                            txtErrorToTime.setText(getString(R.string.error_sensors_date));
                            return;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (sensorIdsList.size() == 0) {
                        txtErrorSensor.setVisibility(View.VISIBLE);
                        txtErrorSensor.setText(getString(R.string.error_required));
                        return;
                    }
                    Gson gson = new Gson();
                    Bundle b = new Bundle();
                    b.putString("sensorList", gson.toJson(sensorArrayList));
                    b.putString("sensorIdsList", gson.toJson(sensorIdsList));
                    b.putString("selectedSensorList", gson.toJson(selectedSensorList));
                    b.putBoolean("isFromFilter", true);
                    b.putString("fromTime", fromTime);
                    b.putString("toTime", toTime);
                    PondSensorsReadingsParentFragment pondSensorsReadingsParentFragment = new PondSensorsReadingsParentFragment();
                    pondSensorsReadingsParentFragment.setArguments(b);
                    GeneralUtils.navigateToFragment(getActivity(), pondSensorsReadingsParentFragment, PondSensorsReadingsParentFragment.class.getSimpleName(), true, false, false);
                } else
                    showNoConnectionAlert();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    private void selectDateDialog(TextView textView, boolean isFromDate) {

        dateDialog = new Dialog(getActivity());
        dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dateDialog.setCancelable(true);
        dateDialog.setContentView(R.layout.date_dialog);
        Button btnOk = dateDialog.findViewById(R.id.btn_ok);
        Button btnCancel = dateDialog.findViewById(R.id.btn_cancel);
        SingleDateAndTimePicker datePicker = dateDialog.findViewById(R.id.date_picker);
        if (isFromDate)
            datePicker.setDefaultDate(fromDate);
        else
            datePicker.setDefaultDate(toDate);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(datePicker.getDate()));
                if (isFromDate)
                    fromDate = datePicker.getDate();
                else
                    toDate = datePicker.getDate();
                dateDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });
        dateDialog.show();


//        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
//                new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Calendar newDate = Calendar.getInstance();
//                        newDate.set(year, monthOfYear, dayOfMonth);
//                        pickerCalendar.set(year, monthOfYear, dayOfMonth);
//                        String myFormat = "yyyy-MM-dd";
//                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                        editText.setText(sdf.format(newDate.getTime()));
//                    }
//
//                }, pickerCalendar.get(Calendar.YEAR), pickerCalendar.get(Calendar.MONTH), pickerCalendar.get(Calendar.DAY_OF_MONTH));
//
//        StartTime.show();
//        StartTime.getDatePicker().setMaxDate(System.currentTimeMillis() - 86400000);
//        StartTime.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
//        StartTime.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new MessageEvent("back_filter"));
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
