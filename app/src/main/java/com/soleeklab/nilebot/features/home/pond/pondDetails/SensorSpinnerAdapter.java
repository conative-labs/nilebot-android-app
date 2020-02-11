package com.soleeklab.nilebot.features.home.pond.pondDetails;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soleeklab.nilebot.GetUserBotsQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.List;

public class SensorSpinnerAdapter extends ArrayAdapter<GetUserBotsQuery.Sensor> {


    LayoutInflater inflater;
    List<GetUserBotsQuery.Sensor> sensors;
    ViewHolder holder = null;
    LocalRepo localRepo;
    private String pondId;

    public SensorSpinnerAdapter(Context context, String pondId, int textViewResourceId, List<GetUserBotsQuery.Sensor> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.sensors = objects;
        localRepo = new LocalRepoImpl(context);
        this.pondId = pondId;
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
        GetUserBotsQuery.Sensor sensor = sensors.get(position);
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

            if (sensor.parameter() != null) {
                if (sensor.parameter().pondID().equals(pondId)) {
                    holder.farmTypeName.setTextColor(getContext().getResources().getColor(R.color.text_color_dimmed));
                } else {
                    holder.farmTypeName.setTextColor(getContext().getResources().getColor(R.color.green));

                }
            } else {
                holder.farmTypeName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

            }
        }
        holder.farmTypeName.setText(localRepo.getTranslation(sensor.nameID()));
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0)
            return false;
        else {
            if (sensors.get(position).parameter() != null) {
                if (sensors.get(position).parameter().pondID().equals(pondId)) {
                    return false;
                }
            }
        }
        return true;

    }

    static class ViewHolder {
        TextView farmTypeName;
    }
}
