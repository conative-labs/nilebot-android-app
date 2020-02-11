package com.soleeklab.nilebot.data.models;

import java.util.ArrayList;

public class CountryCode {

    private String name;
    private String alpha2Code;
    private ArrayList<String> callingCodes = new ArrayList<>();

    public CountryCode(String name, String alpha2Code, ArrayList<String> callingCodes) {
        this.name = name;
        this.alpha2Code = alpha2Code;
        this.callingCodes = callingCodes;
    }

    public String getName() {
        return name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public ArrayList<String> getCallingCodes() {
        return callingCodes;
    }
}
