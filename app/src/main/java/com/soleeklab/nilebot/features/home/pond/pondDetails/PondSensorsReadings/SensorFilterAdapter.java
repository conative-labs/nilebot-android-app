package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.content.Context;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SensorFilterAdapter extends ParentRecyclerAdapter<GetSensorsReadingByPondIDQuery.Parameter> {


    @NonNull
    private OnItemCheckListener onItemCheckListener;


    private List<GetSensorsReadingByPondIDQuery.Parameter> selectedSensorArrayList = new ArrayList<>();

    LocalRepo localRepo;

    public SensorFilterAdapter(Context context, List<GetSensorsReadingByPondIDQuery.Parameter> sensorList, @NonNull OnItemCheckListener onItemCheckListener, ArrayList<GetSensorsReadingByPondIDQuery.Parameter> selectedSensors) {
        super(sensorList);
        this.selectedSensorArrayList = selectedSensors;
        this.onItemCheckListener = onItemCheckListener;
        localRepo = new LocalRepoImpl(context);
    }


    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_multi_select, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;

        final GetSensorsReadingByPondIDQuery.Parameter sensor = getData().get(position);


        viewHolder.txtChoiseName.setText(localRepo.getTranslation(sensor.name()));


        for (GetSensorsReadingByPondIDQuery.Parameter equalSensor : selectedSensorArrayList) {
            if (equalSensor.parameterID().equals(sensor.parameterID())) {
                viewHolder.checkBox.setChecked(true);
                onItemCheckListener.onItemCheck(sensor);

            }
        }


        viewHolder.layChoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(sensor);
                } else {
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(sensor);
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(sensor);

                } else {
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(sensor);
                }
            }
        });

    }

    class ViewHolder extends ParentRecyclerViewHolder {


        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.lay_choise)
        LinearLayout layChoise;

        @BindView(R.id.txt_brand_name)
        TextView txtChoiseName;

        ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            ButterKnife.bind(this, itemView);

        }
    }

    interface OnItemCheckListener {

        void onItemCheck(GetSensorsReadingByPondIDQuery.Parameter item);

        void onItemUncheck(GetSensorsReadingByPondIDQuery.Parameter item);
    }
}
