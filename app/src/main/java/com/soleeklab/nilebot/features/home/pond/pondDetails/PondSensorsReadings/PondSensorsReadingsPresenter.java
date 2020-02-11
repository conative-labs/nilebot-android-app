package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.SensorSummary;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

public class PondSensorsReadingsPresenter implements PondSensorsReadingsParentContract.Presenter {

    private PondSensorsReadingsParentContract.View mView;


    Handler uiHandler = new Handler(Looper.getMainLooper());

    public static Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map;
    public static Map<String, List<Double>> mapValues = new HashMap<>();
    public static Map<String, String> mapSesnorUnits = new HashMap<>();
    private ArrayList<String> sensorUnitsList = new ArrayList<>();


    @Inject
    public PondSensorsReadingsPresenter() {
    }

    @Override
    public void registerView(PondSensorsReadingsParentContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {
        this.mView = DUMMY_VIEW;
    }

    @Override
    public void start() {

    }

    @Override
    public void getSensorsByPondId(String fromTime, String toTime, ArrayList<String> sensorsIds, boolean isDialog) {
        map = new TreeMap<>();
        mapValues = new TreeMap<>();
        mView.showProgress();
        GetSensorsReadingByPondIDQuery getSensorsReadingByPondIDQuery = GetSensorsReadingByPondIDQuery.builder()
                .id(sensorsIds)
                .from(fromTime)
                .to(toTime)
                .build();

        ApolloClientBuilder.getApolloClient(true).query(getSensorsReadingByPondIDQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetSensorsReadingByPondIDQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetSensorsReadingByPondIDQuery.Data> response) {
                if (response.data() != null) {
                    if (response.data().ponds() != null) {
                        final StringBuffer buffer = new StringBuffer();
                        if (response.data().ponds() != null) {
                            for (GetSensorsReadingByPondIDQuery.Pond pond : response.data().ponds()) {
                                for (GetSensorsReadingByPondIDQuery.Parameter sensor : pond.parameters()) {
                                    List<Double> values = new ArrayList<>();
                                    for (GetSensorsReadingByPondIDQuery.Reading reading : sensor.readings()) {
                                        String key = reading.time();
                                        values.add(reading.value());
                                        try {
                                            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(key);
                                            // String newstring = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a").format(date);
                                            String newstring = new SimpleDateFormat("yyyy-MM-dd h:mm:ss").format(date);
//                                            System.out.println(newstring);
                                            key = newstring;
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (map.containsKey(key)) {
                                            List<GetSensorsReadingByPondIDQuery.Parameter> list = map.get(key);
                                            List<GetSensorsReadingByPondIDQuery.Reading> readings = new LinkedList<>();
                                            readings.add(reading);
                                            GetSensorsReadingByPondIDQuery.Parameter mySensor = new GetSensorsReadingByPondIDQuery.Parameter(sensor.__typename(), sensor.parameterID(), null, sensor.name(), sensor.unit(), readings);
                                            list.add(mySensor);

                                        } else {
                                            List<GetSensorsReadingByPondIDQuery.Parameter> list = new ArrayList<>();
                                            List<GetSensorsReadingByPondIDQuery.Reading> readings = new LinkedList<>();
                                            readings.add(reading);
                                            GetSensorsReadingByPondIDQuery.Parameter mySensor = new GetSensorsReadingByPondIDQuery.Parameter(sensor.__typename(), sensor.parameterID(), null, sensor.name(), sensor.unit(), readings);
                                            list.add(mySensor);
                                            map.put(key, list);
                                        }
                                    }
                                    mapValues.put(sensor.name(), values);
                                    mapSesnorUnits.put(sensor.name(), sensor.unit());
                                    sensorUnitsList.add(sensor.unit());
                                }
                            }

                            ArrayList<SensorSummary> sensorSummaryArrayList = new ArrayList<>();
                            for (String sensorName : mapValues.keySet()) {
                                try {
                                    double sum = 0;
                                    for (Double d : mapValues.get(sensorName))
                                        sum += d;

//                                    System.out.println("Sensor: " + sensor + ",Min: " + Collections.min(mapValues.get(sensor)) + ",Max: " + Collections.max(mapValues.get(sensor)) + ",Avg: " + sum / mapValues.get(sensor).size());
                                    sensorSummaryArrayList.add(new SensorSummary(mapSesnorUnits.get(sensorName), sensorName,
                                            Collections.min(mapValues.get(sensorName)),
                                            Collections.max(mapValues.get(sensorName)), sum / mapValues.get(sensorName).size()));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            mView.hideProgress();
                            if (isDialog)
                                mView.hideDialog();
                            mView.initList(map, sensorSummaryArrayList);

                        } else {
                            mView.hideProgress();
                            mView.showAlert(R.string.error_signup);
                        }
                    } else {
                        mView.hideProgress();
                        mView.showAlert(R.string.server_error);
                    }

                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        }, uiHandler));

    }


    PondSensorsReadingsParentContract.View DUMMY_VIEW = new PondSensorsReadingsParentContract.View() {


        @Override
        public void initList(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map, ArrayList<SensorSummary> sensorSummaryArrayList) {

        }

        @Override
        public void hideDialog() {

        }

        @Override
        public void setPresenter(PondSensorsReadingsParentContract.Presenter presenter) {

        }

        @Override
        public void showProgress() {

        }

        @Override
        public void hideProgress() {

        }

        @Override
        public void showError(String error) {

        }

        @Override
        public void showAlert(String message) {

        }

        @Override
        public void showAlert(int message) {

        }

        @Override
        public void showNoConnectionAlert() {

        }

        @Override
        public void showError() {

        }

        @Override
        public void showNoConnection() {

        }

        @Override
        public void showToast(String message) {

        }
    };
}
