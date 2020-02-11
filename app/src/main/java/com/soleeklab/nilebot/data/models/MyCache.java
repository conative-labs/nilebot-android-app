package com.soleeklab.nilebot.data.models;



import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.data.api.AddPondData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyCache {

    private Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map = new TreeMap<>();
    private ArrayList<SensorSummary> sensorSummaryArrayList = new ArrayList<>();

    @Inject
    public MyCache() {
    }

    AddPondData addPondData;

    private double lat = 0;
    private double lng = 0;

    private boolean isFarmFragment = false;


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public AddPondData getAddPondData() {
        return addPondData;
    }

    public void setAddPondData(AddPondData addPondData) {
        this.addPondData = addPondData;
    }

    public Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map) {
        this.map = map;
    }

    public ArrayList<SensorSummary> getSensorSummaryArrayList() {
        return sensorSummaryArrayList;
    }

    public void setSensorSummaryArrayList(ArrayList<SensorSummary> sensorSummaryArrayList) {
        this.sensorSummaryArrayList = sensorSummaryArrayList;
    }

    public boolean isFarmFragment() {
        return isFarmFragment;
    }

    public void setFarmFragment(boolean farmFragment) {
        isFarmFragment = farmFragment;
    }
}
