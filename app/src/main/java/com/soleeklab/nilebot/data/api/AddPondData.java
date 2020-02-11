package com.soleeklab.nilebot.data.api;

public class AddPondData {

    private String farmId;
    private String pondName;
    private String pondType;
    private Float length;
    private Float width;
    private Float Height;
    private Float diameter;
    private boolean equipment;
    private int pondsCount;
    private boolean isRectangle;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPondName() {
        return pondName;
    }

    public String getPondType() {
        return pondType;
    }

    public Float getLength() {
        return length;
    }

    public Float getWidth() {
        return width;
    }

    public Float getHeight() {
        return Height;
    }

    public Float getDiameter() {
        return diameter;
    }

    public boolean isEquipment() {
        return equipment;
    }

    public int getPondsCount() {
        return pondsCount;
    }

    public void setPondName(String pondName) {
        this.pondName = pondName;
    }

    public void setPondType(String pondType) {
        this.pondType = pondType;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public void setHeight(Float height) {
        Height = height;
    }

    public void setDiameter(Float diameter) {
        this.diameter = diameter;
    }

    public void setEquipment(boolean equipment) {
        this.equipment = equipment;
    }

    public void setPondsCount(int pondsCount) {
        this.pondsCount = pondsCount;
    }

    public boolean isRectangle() {
        return isRectangle;
    }

    public void setRectangle(boolean rectangle) {
        isRectangle = rectangle;
    }
}
