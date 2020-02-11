package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildSensorAdapter extends ParentRecyclerAdapter<GetSensorsReadingByPondIDQuery.Parameter> {

    private Context context;
    LocalRepo localRepo;

    public ChildSensorAdapter(List<GetSensorsReadingByPondIDQuery.Parameter> data, Context context) {
        super(data);
        this.context = context;
        localRepo = new LocalRepoImpl(context);
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sensor_read, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        GetSensorsReadingByPondIDQuery.Parameter sensor = getData().get(position);


        viewHolder.txtSensorName.setText(localRepo.getTranslation(sensor.name()));


        viewHolder.txtSensorValue.setText(sensor.readings().get(0).value() + " " + sensor.unit());


        if (sensor.readings().get(0).isAlarm())
            viewHolder.txtSensorValue.setTextColor(context.getResources().getColor(R.color.red));

    }

    class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_sensor_name)
        TextView txtSensorName;
        @BindView(R.id.txt_sensor_value)
        TextView txtSensorValue;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }
}
