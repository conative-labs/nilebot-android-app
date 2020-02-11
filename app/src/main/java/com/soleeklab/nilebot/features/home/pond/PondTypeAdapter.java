package com.soleeklab.nilebot.features.home.pond;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.ArrayList;

public class PondTypeAdapter extends ArrayAdapter<String> {


    LayoutInflater inflater;
    ArrayList<String> pondTypesArrayList;
    ViewHolder holder = null;
    LocalRepo localRepo;

    public PondTypeAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.pondTypesArrayList = objects;
        localRepo = new LocalRepoImpl(context);
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
        String farmType = pondTypesArrayList.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.item_farmtype_spinner, parent, false);
            holder.farmTypeName = (TextView) row.findViewById(R.id.txt_farm_type);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        if (position == 0) {

            holder.farmTypeName.setTextColor(getContext().getResources().getColor(R.color.colorGray));
        } else {
            holder.farmTypeName.setTextColor(getContext().getResources().getColor(R.color.colorText));
        }

        holder.farmTypeName.setText(localRepo.getTranslation(farmType));
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0)
            return false;
        else
            return true;
    }

    static class ViewHolder {
        TextView farmTypeName;
    }
}
