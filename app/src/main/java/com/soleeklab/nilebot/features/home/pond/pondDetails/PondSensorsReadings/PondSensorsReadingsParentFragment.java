package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.models.SensorSummary;
import com.soleeklab.nilebot.data.repo.ApplicationContextInjector;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.utils.DialogUtil;
import com.soleeklab.nilebot.utils.LocalHelper;
import com.soleeklab.nilebot.utils.NonSwipeableViewPager;
import com.soleeklab.nilebot.utils.PermissionsUtils;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.DOWNLOAD_SERVICE;


public class PondSensorsReadingsParentFragment extends ParentFragment implements PondSensorsReadingsParentContract.View {


    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    NonSwipeableViewPager pager;
    Unbinder unbinder;

    @BindView(R.id.btn_export)
    ImageView btnExport;
    @BindView(R.id.tab_no_data)
    LinearLayout tabNoData;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;

    LocalHelper localHelper;

    private String fromTime = "";
    private String toTime = "";
    Locale locale = new Locale("en");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", locale);
    Dialog dialog;
    Dialog dateDialog;
    Calendar pickerCalendar;

    @Inject
    LocalRepo localRepo;

    @Inject
    PondSensorsReadingsParentContract.Presenter mPresenter;

    ArrayList<GetSensorsReadingByPondIDQuery.Parameter> sensorArrayList = new ArrayList<>();
    ArrayList<String> sensorIdsList = new ArrayList<>();
    ArrayList<GetSensorsReadingByPondIDQuery.Parameter> selectedSensorList = new ArrayList<>();
    int Xcount;
    Map<String, List<Entry>> sensorListMap;
    @Inject
    MyCache myCache;

    Date fromDate, toDate;

    @Inject
    public PondSensorsReadingsParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pond_sensors_readings_parent, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().onBackPressed();
                        getActivity().onBackPressed();
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
        mPresenter.registerView(this);
        localHelper = LocalHelper.getInstance(getActivity());
        txtToolbarTitle.setText(getString(R.string.ponds));

        setupSensors();
        initTime();
        if (isInternetConnection()) {
            mPresenter.getSensorsByPondId(fromTime, toTime, sensorIdsList, false);
        } else
            showNoConnectionAlert();


    }

    private void setupSensors() {

        sensorArrayList = new Gson().fromJson(getArguments().getString("sensorList"), new TypeToken<List<GetSensorsReadingByPondIDQuery.Parameter>>() {
        }.getType());


        selectedSensorList = new Gson().fromJson(getArguments().getString("selectedSensorList"), new TypeToken<ArrayList<GetSensorsReadingByPondIDQuery.Parameter>>() {
        }.getType());


        for (GetSensorsReadingByPondIDQuery.Parameter sensor : selectedSensorList)
            sensorIdsList.add(sensor.parameterID());

    }

    private void initTime() {

        fromTime = getArguments().getString("fromTime");
        toTime = getArguments().getString("toTime");

        fromDate = getDateFromString(getArguments().getString("fromTime"));
        toDate = getDateFromString(getArguments().getString("toTime"));

    }

    private Date getDateFromString(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateInString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initTabs() {
        tabLayout.removeAllTabs();
        if (tabLayout.getTabCount() == 0) {
            tabLayout.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.readings)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.chart)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.summary)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
            pager.setAdapter(adapter);
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            pager.setOffscreenPageLimit(2);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
    }

    @OnClick({R.id.btn_filter, R.id.btn_export, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:
                setUpFilter();
                break;
            case R.id.btn_export:
                if (myCache.getMap().size() > 0) {
                    exportAction();
                } else
                    showAlert(getString(R.string.no_data_to_export));
                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
                getActivity().onBackPressed();
                break;
        }
    }


    private void setUpFilter() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_filter_sensorss);

        TextView edtToTime = dialog.findViewById(R.id.edt_to_time);
        TextView edtFromTime = dialog.findViewById(R.id.edt_from_time);
        RecyclerView rvSensors = dialog.findViewById(R.id.rv_sensors_check);
        Button btnFilter = dialog.findViewById(R.id.btn_filter);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView txtErrorToTime = dialog.findViewById(R.id.txt_error_to_time);
        TextView txtErrorFromTime = dialog.findViewById(R.id.txt_error_start_time);
        TextView txtErrorSensor = dialog.findViewById(R.id.txt_error_sensor);

        edtFromTime.setText(fromTime);
        edtToTime.setText(toTime);
        edtFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleceDateDialog(edtFromTime, true);
            }
        });
        edtToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleceDateDialog(edtToTime, false);
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
        rvSensors.setAdapter(sensorFilterAdapter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtErrorFromTime.setVisibility(View.GONE);
                txtErrorToTime.setVisibility(View.GONE);
                txtErrorSensor.setVisibility(View.GONE);


                fromTime = edtFromTime.getText().toString().trim();
                toTime = edtToTime.getText().toString().trim();

                if (isInternetConnection()) {


                    sensorIdsList = new ArrayList<>();
                    for (GetSensorsReadingByPondIDQuery.Parameter sensor : selectedSensorList) {
                        if (!sensorIdsList.contains(sensor.parameterID()))
                            sensorIdsList.add(sensor.parameterID());

                    }


                    if (TextUtils.isEmpty(fromTime)) {
                        txtErrorFromTime.setVisibility(View.VISIBLE);
                        txtErrorFromTime.setText(getString(R.string.error_required));
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


                    mPresenter.getSensorsByPondId(
                            fromTime,
                            toTime,
                            sensorIdsList, true);
                } else
                    showNoConnectionAlert();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void seleceDateDialog(TextView textView, boolean isFromDate) {

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
    }

    @Override
    public void initList(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map, ArrayList<SensorSummary> sensorSummaryArrayList) {
        myCache.setMap(new HashMap<>());
        myCache.setSensorSummaryArrayList(new ArrayList<>());

        if (map.size() > 0) {
            myCache.setSensorSummaryArrayList(sensorSummaryArrayList);
            myCache.setMap(map);
            layNoData.setVisibility(View.GONE);
            tabNoData.setVisibility(View.VISIBLE);
            initTabs();
        } else {
            layNoData.setVisibility(View.VISIBLE);
            tabNoData.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
        }

    }

    @Override
    public void hideDialog() {
        dialog.dismiss();

    }

    @Override
    public void setPresenter(PondSensorsReadingsParentContract.Presenter presenter) {

    }

    private void exportAction() {
        if (!PermissionsUtils.hasPermissions(getActivity(), PermissionsUtils.WRITE_PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PermissionsUtils.WRITE_PERMISSIONS, 1000);
            }
        } else {
            //open camera
            exportCSV(myCache.getMap());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportAction();
                } else {
                    showAlert(getString(R.string.error_permission));
                }
                break;
        }
    }


    public void exportCSV(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map) {
        sensorListMap = new TreeMap<>();
        Xcount = 0;

        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (String time : map.keySet()) {
                        for (GetSensorsReadingByPondIDQuery.Parameter sensor : map.get(time)) {
                            for (GetSensorsReadingByPondIDQuery.Reading reading : sensor.readings()) {

                                if (sensorListMap.containsKey(sensor.name())) {
                                    List<Entry> list = sensorListMap.get(sensor.name());
                                    list.add(new Entry(Xcount, (float) reading.value()));
                                    sensorListMap.put(sensor.name(), list);
                                } else {
                                    List<Entry> list = new ArrayList<>();
                                    list.add(new Entry(Xcount, (float) reading.value()));
                                    sensorListMap.put(sensor.name(), list);
                                }
                            }
                        }
                        Xcount++;
                    }

                    File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download");
                    if (!root.exists()) {
                        root.mkdirs();
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                    String currentDate = dateFormat.format(Calendar.getInstance().getTime());

                    String exportFileName = getString(R.string.app_name) + " " + currentDate + ".csv";
                    File filename = new File(root, getString(R.string.app_name) + " " + currentDate + ".csv");
                    FileWriter fw = new FileWriter(filename);
                    // StringBuffer fw = new StringBuffer();
                    fw.append("Date/Sensor");
                    fw.append(',');

                    int sensorIterator = 0;
                    for (String sensor : sensorListMap.keySet()) {
                        sensorIterator++;
                        fw.append(localRepo.getTranslation(sensor));
                        if (sensorIterator != sensorListMap.size()) {
                            fw.append(',');
                        }
                    }

                    fw.append('\n');
                    Date date;
                    String key_date = "";
                    Xcount = 0;
                    for (String time : map.keySet()) {
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                            key_date = new SimpleDateFormat("dd MMM yyy h:mm a").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fw.append(key_date);
                        fw.append(',');


                        int sensorValueIterator = 0;
                        for (String sensor : sensorListMap.keySet()) {
                            sensorValueIterator++;
                            for (Entry yAxesvalues : sensorListMap.get(sensor)) {
                                if (yAxesvalues.getX() == Xcount) {
                                    fw.append(yAxesvalues.getY() + "");
                                    break;
                                }

                            }
                            if (sensorValueIterator != sensorListMap.size()) {
                                fw.append(',');
                            }

                        }
                        if (Xcount < map.keySet().size() - 1) fw.append('\n');
                        Xcount++;


                    }

                    fw.flush();
                    fw.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            hideProgress();

                            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                            downloadManager.addCompletedDownload(exportFileName, getString(R.string.excel_exported),
                                    true,
                                    "text/csv",
                                    filename.getAbsolutePath(),
                                    filename.length(),
                                    true);

//                            createExportNotification(getString(R.string.excel_exported), exportFileName, "");
                            Toast.makeText(getActivity(), getString(R.string.excel_exported), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            hideProgress();
                            showAlert("error" + e.getLocalizedMessage());
                        }
                    });
                    e.printStackTrace();

                }
            }
        }).start();

    }


    private void createExportNotification(String title, String desc, String filename) {

        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//        Log.e("FILEPATH",Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename); //filename is string with value 46_1244625499.gif
//        intent.setDataAndType(uri, "file/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Intent j = Intent.createChooser(intent, "Choose an application to open with:");


        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), channelId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.logo_box);
            notificationBuilder.setColor(this.getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.logo_box);
        }
        notificationBuilder.setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(desc))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        int unique = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(unique, notificationBuilder.build());


    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC)) {
            localHelper.setLanguage(LocalHelper.LANGUAGE_ARABIC);
            LocalHelper.ChangeDesignToRTL(getActivity());
        } else {
            localHelper.setLanguage(LocalHelper.LANGUAGE_ENGLISH);
            LocalHelper.ChangeDesignToLTR(getActivity());
        }

    }
}
