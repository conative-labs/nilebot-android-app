package com.soleeklab.nilebot.data.models;

public class SensorSummary {

    private String sensorName;
    private double min;
    private double max;
    private double average;
    private String unit;


    public SensorSummary(String unit,String sensorName, double min, double max, double average) {
        this.unit = unit;
        this.sensorName = sensorName;
        this.min = min;
        this.max = max;
        this.average = average;
    }

    public String getUnit() {
        return unit;
    }

    public String getSensorName() {
        return sensorName;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getAverage() {
        return average;
    }


}
