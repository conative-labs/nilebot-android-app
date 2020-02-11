package com.soleeklab.nilebot.features.auth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.CountryCode;

import java.util.ArrayList;

public class CountryCodeAdapter extends ArrayAdapter<CountryCode> {


    LayoutInflater inflater;
    ArrayList<CountryCode> countryCodeArrayList;
    ViewHolder holder = null;

    public CountryCodeAdapter(Context context, int textViewResourceId, ArrayList<CountryCode> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.countryCodeArrayList = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        CountryCode countryCode = countryCodeArrayList.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.item_spinner_img_row, parent, false);
            holder.emoji = (TextView) row.findViewById(R.id.txt_country_emoji);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.emoji.setText(localeToEmoji(countryCode.getAlpha2Code()));
        if (!(countryCode.getCallingCodes().get(0).equals("") || countryCode.getAlpha2Code().equals(""))) {
            holder.emoji.setText(localeToEmoji(countryCode.getAlpha2Code()) + " " + countryCode.getName());
//            holder.name.setText(countryCode.getName());
        }
        return row;
    }

    private String localeToEmoji(String alpha) {

        int firstLetter = Character.codePointAt(alpha, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(alpha, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));

    }


    static class ViewHolder {
        public TextView emoji;
    }
}
